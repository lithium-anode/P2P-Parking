package parking.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.model.enums.SpotType;
import parking.project.service.UserService;
import parking.project.patterns.creational.factory.SpotFactory;
import parking.project.patterns.structural.proxy.VerifyProxy;
import parking.project.repository.ParkingSpotRepository;

/**
 * [GRASP: Controller]
 * Manages system events for Space Owners, including listing maintenance and rental management[cite: 2].
 * * [Design Pattern: Proxy - Protection Proxy]
 * Verifies the 'OWNER' role before allowing access to listing management features.
 */
@Controller
@RequestMapping("/owner")
public class OwnerController {
    private final ParkingSpotRepository spotRepository;
    private final VerifyProxy verifyProxy;
    private final SpotFactory spotFactory;
    private final UserService userService;

    /**
     * [GRASP: Low Coupling]
     * Dependencies are injected via the constructor to ensure the controller 
     * is not responsible for instantiating its own logic helpers or factories.
     */
    @Autowired
    public OwnerController(ParkingSpotRepository spotRepository, VerifyProxy verifyProxy, SpotFactory spotFactory, UserService userService) {
        this.spotRepository = spotRepository;
        this.verifyProxy = verifyProxy;
        this.spotFactory = spotFactory;
        this.userService = userService;
    }

    /**
     * [Goal Alignment: Listing Management]
     * Enables owners to view and manage their registered parking spots[cite: 10].
     */
    @GetMapping("/dashboard")
    public String ownerDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        SpaceOwner currentUser = (SpaceOwner) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        model.addAttribute("totalEarnings", currentUser.getTotalEarnings());
        // Rename "spot" to "newSpot" to avoid conflict in the template
        model.addAttribute("newSpot", new ParkingSpot());
        model.addAttribute("mySpots", spotRepository.findByOwnerAndActiveTrue(currentUser));
        return "owner/dashboard";
    }

    @GetMapping("/earnings")
    public String viewEarnings(Model model) {
        return "owner/earnings"; 
    }

    /**
     * [Goal Alignment: Space Owners can add new spots]
     * [Design Pattern: Factory Method]
     * Uses the SpotFactory to create a new ParkingSpot instance. This encapsulates 
     * the creation logic and ensures the spot is initialized correctly with its owner and type.
     */
    @PostMapping("/spots/save")
    public String saveSpot(@RequestParam(required = false) Long id,
                        @RequestParam String location,
                        @RequestParam double hourlyRate,
                        @RequestParam SpotType spotType,
                        Authentication authentication,
                        Model model) {

        String username = authentication.getName();
        SpaceOwner currentUser = (SpaceOwner) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!verifyProxy.isOwner(currentUser)) {
            return "redirect:/login?error=Unauthorized";
        }

        ParkingSpot spot;
        if (id != null) {
            // Edit existing
            spot = spotRepository.findById(id).orElseThrow();
            if (!spot.getOwner().equals(currentUser)) {
                return "redirect:/owner/dashboard?error=Unauthorized";
            }
            spot.setLocation(location);
            spot.setHourlyRate(hourlyRate);
            spot.setSpotType(spotType);
        } else {
            // Add new
            spot = spotFactory.createSpot(location, hourlyRate, currentUser, spotType);
        }

        spotRepository.save(spot);
        return "redirect:/owner/dashboard";
    }

    /**
     * [Goal Alignment: Management of Rentals]
     * [Design Pattern: Proxy - Protection Proxy]
     * Removes a parking spot listing after verifying ownership and roles.
     */
    @PostMapping("/spots/delete/{id}")
    public String deleteSpot(@PathVariable Long id, Authentication authentication) {
        /**
         * [GRASP: Controller]
         * Uses the Authentication object to safely retrieve the username 
         * from the Spring Security context.
         */
        String username = authentication.getName();
        SpaceOwner currentUser = (SpaceOwner) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        /**
         * [Design Pattern: Proxy]
         * Explicitly uses the VerifyProxy to ensure the user has the 'OWNER' role 
         * before proceeding with the deletion of a resource.
         */
        if (!verifyProxy.isOwner(currentUser)) {
            return "redirect:/login?error=Unauthorized";
        }

        // [GRASP: Information Expert]
        ParkingSpot spot = spotRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid spot Id:" + id));

        /**
         * [Goal Alignment: Secure Management]
         * Verifies that the current user is the actual owner of the spot to 
         * prevent unauthorized deletions of other owners' listings.
         */
        if (!spot.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/owner/dashboard?error=Unauthorized";
        }

        spot.setActive(false); // deactivate spot
        spotRepository.save(spot);
        
        return "redirect:/owner/dashboard?deleted";
    }
}