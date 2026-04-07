package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.model.User;
import parking.project.patterns.factory.SpotFactory;
import parking.project.repository.UserRepository;
import parking.project.service.OwnerService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private UserRepository userRepository;

    // 1. Owner Dashboard: View earnings and managed spots
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !(user instanceof SpaceOwner)) {
            return "redirect:/login";
        }

        SpaceOwner owner = (SpaceOwner) user;

        // Fetch earnings and spots for the owner
        Map<String, Object> earnings = ownerService.viewEarnings(owner.getId());
        List<ParkingSpot> mySpots = ownerService.getOwnerSpots(owner.getId());

        model.addAttribute("owner", owner);
        model.addAttribute("totalEarnings", earnings.get("totalEarnings"));
        model.addAttribute("spots", mySpots);

        return "owner/dashboard"; // Points to templates/owner/dashboard.html
    }

    // 2. Add Spot Form
    @GetMapping("/add-spot")
    public String showAddSpotForm() {
        return "owner/add-spot";
    }

    // 3. Process New Spot using Factory Pattern
    @PostMapping("/add-spot")
    public String addSpot(@RequestParam String type,
                          @RequestParam String location,
                          @RequestParam double price,
                          HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (!(user instanceof SpaceOwner)) {
            return "redirect:/login";
        }

        SpaceOwner owner = (SpaceOwner) user;

        // Logic: Standardize creation via Factory
        ParkingSpot newSpot = SpotFactory.createSpot(type, location, price, owner);

        ownerService.addNewSpot(newSpot);

        return "redirect:/owner/dashboard?success=added";
    }

    // REST API for creating spots (for testing)
    @PostMapping("/api/spots")
    @ResponseBody
    public ResponseEntity<ParkingSpot> createSpot(@RequestBody ParkingSpot spot, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        ParkingSpot createdSpot = ownerService.addParkingSpot(userId, spot);
        return ResponseEntity.ok(createdSpot);
    }
}