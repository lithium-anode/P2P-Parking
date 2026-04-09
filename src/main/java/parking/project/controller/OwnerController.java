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

/*
    [GRASP: Controller]
    Goal: Manages system events for Space Owners, including listing maintenance and rental management.
    [Design Pattern: Structural - Proxy]
    Verifies the 'OWNER' role before allowing access to listing management features.
*/
@Controller
@RequestMapping("/owner")
public class OwnerController {
    private final ParkingSpotRepository spotRepository;
    private final VerifyProxy verifyProxy;
    private final SpotFactory spotFactory;
    private final UserService userService;

    // [GRASP: Low Coupling]
    // Dependencies are injected via the constructor to ensure the controller 
    // is not responsible for instantiating its own logic helpers or factories.
    @Autowired
    public OwnerController(ParkingSpotRepository spotRepository, VerifyProxy verifyProxy, SpotFactory spotFactory, UserService userService) {
        this.spotRepository = spotRepository;
        this.verifyProxy = verifyProxy;
        this.spotFactory = spotFactory;
        this.userService = userService;
    }

    // Goal: Enables owners to view and manage their registered parking spots.
    @GetMapping("/dashboard")
    public String ownerDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        SpaceOwner currentUser = (SpaceOwner) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        model.addAttribute("totalEarnings", currentUser.getTotalEarnings());
        model.addAttribute("newSpot", new ParkingSpot());
        model.addAttribute("mySpots", spotRepository.findByOwnerAndActiveTrue(currentUser));
        return "owner/dashboard";
    }

    @GetMapping("/earnings")
    public String viewEarnings(Model model) {
        return "owner/earnings"; 
    }

    // [Design Pattern: Creational - Factory]
    // Goal: Uses the SpotFactory to create a new ParkingSpot instance.
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

    // [Design Pattern: Structural - Proxy]
    // Ensures that only users with the 'OWNER' role can access the deletion functionality,
    // Goal: Removes a parking spot listing after verifying ownership and roles.
    @PostMapping("/spots/delete/{id}")
    public String deleteSpot(@PathVariable Long id, Authentication authentication) {
        // [GRASP: Information Expert]
        // The UserService is the expert on retrieving user details.
        String username = authentication.getName();
        SpaceOwner currentUser = (SpaceOwner) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // [Design Pattern: Proxy]
        if (!verifyProxy.isOwner(currentUser)) {
            return "redirect:/login?error=Unauthorized";
        }

        // [GRASP: Information Expert]
        // The ParkingSpotRepository is the expert on retrieving spot details.
        ParkingSpot spot = spotRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid spot Id:" + id));

        // Goal: Verifies that the current user is the actual owner of the
        // spot to prevent unauthorized deletions of other owners' listings.
        if (!spot.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/owner/dashboard?error=Unauthorized";
        }

        spot.setActive(false); // deactivate spot
        spotRepository.save(spot);
        
        return "redirect:/owner/dashboard?deleted";
    }
}