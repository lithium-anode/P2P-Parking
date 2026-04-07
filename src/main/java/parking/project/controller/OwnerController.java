package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.patterns.factory.SpotFactory;
import parking.project.service.OwnerService;

import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    // 1. Owner Dashboard: View earnings and managed spots
    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name="ownerId", required=false, defaultValue="1") Long ownerId, Model model) {
        // Fetch earnings and spots for the owner
        Double totalEarnings = ownerService.calculateTotalEarnings(ownerId);
        List<ParkingSpot> mySpots = ownerService.getOwnerSpots(ownerId);

        model.addAttribute("totalEarnings", totalEarnings);
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
                          @RequestParam Long ownerId) {
        
        // Use a placeholder SpaceOwner for the prototype
        SpaceOwner owner = new SpaceOwner(); 
        owner.setId(ownerId);

        // Logic: Standardize creation via Factory
        ParkingSpot newSpot = SpotFactory.createSpot(type, location, price, owner);
        
        ownerService.addNewSpot(newSpot);
        
        return "redirect:/owner/dashboard?success=added";
    }
}