package parking.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.model.SpaceOwner;
import parking.project.model.ParkingSpot;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    // Mock data for demonstration
    private List<MockSpot> mockSpots = new ArrayList<>();
    private SpaceOwner currentOwner;

    public OwnerController() {
        // Initialize mock owner
        currentOwner = new SpaceOwner("jane_owner", "jane@example.com");
        currentOwner.setId(2L);
        currentOwner.setTotalEarnings(5250.75);

        // Initialize with some mock parking spots
        mockSpots.add(new MockSpot(1L, "Downtown Garage", "123 Main St", 5.50, true, 2L));
        mockSpots.add(new MockSpot(2L, "Mall Parking", "456 Shopping Ave", 4.00, true, 2L));
        mockSpots.add(new MockSpot(3L, "Airport Parking", "789 Sky Ln", 8.50, false, 2L));
    }

    /**
     * Owner dashboard
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("owner", currentOwner);
        model.addAttribute("spotCount", mockSpots.size());
        model.addAttribute("totalEarnings", currentOwner.getTotalEarnings());
        model.addAttribute("earnings", currentOwner.viewEarnings());
        return "owner/dashboard";
    }

    /**
     * View all parking spots managed by owner
     */
    @GetMapping("/spots")
    public String viewSpots(Model model) {
        model.addAttribute("owner", currentOwner);
        model.addAttribute("spots", mockSpots);
        return "owner/spots";
    }

    /**
     * Show form to add new parking spot
     */
    @GetMapping("/spots/add")
    public String showAddSpotForm(Model model) {
        model.addAttribute("owner", currentOwner);
        return "owner/add-spot";
    }

    /**
     * Add a new parking spot
     */
    @PostMapping("/spots/add")
    public String addSpot(@RequestParam String location,
                          @RequestParam String address,
                          @RequestParam double hourlyRate) {
        long newId = mockSpots.stream().mapToLong(MockSpot::getId).max().orElse(0) + 1;
        MockSpot newSpot = new MockSpot(newId, location, address, hourlyRate, true, currentOwner.getId());
        mockSpots.add(newSpot);
        return "redirect:/owner/spots";
    }

    /**
     * Show form to edit parking spot
     */
    @GetMapping("/spots/{id}/edit")
    public String showEditSpotForm(@PathVariable Long id, Model model) {
        MockSpot spot = findSpotById(id);
        model.addAttribute("owner", currentOwner);
        model.addAttribute("spot", spot);
        return "owner/edit-spot";
    }

    /**
     * Update an existing parking spot
     */
    @PostMapping("/spots/{id}/update")
    public String updateSpot(@PathVariable Long id,
                            @RequestParam String location,
                            @RequestParam String address,
                            @RequestParam double hourlyRate) {
        MockSpot spot = findSpotById(id);
        if (spot != null) {
            spot.setLocation(location);
            spot.setAddress(address);
            spot.setHourlyRate(hourlyRate);
        }
        return "redirect:/owner/spots";
    }

    /**
     * Remove a parking spot
     */
    @PostMapping("/spots/{id}/remove")
    public String removeSpot(@PathVariable Long id) {
        mockSpots.removeIf(spot -> spot.getId().equals(id));
        return "redirect:/owner/spots";
    }

    /**
     * View earnings
     */
    @GetMapping("/earnings")
    public String viewEarnings(Model model) {
        model.addAttribute("owner", currentOwner);
        model.addAttribute("totalEarnings", currentOwner.getTotalEarnings());
        model.addAttribute("earningsReport", currentOwner.viewEarnings());
        model.addAttribute("spotCount", mockSpots.size());
        return "owner/earnings";
    }

    private MockSpot findSpotById(Long id) {
        return mockSpots.stream()
            .filter(spot -> spot.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    /**
     * Mock ParkingSpot class for demonstration
     */
    public static class MockSpot {
        private Long id;
        private String location;
        private String address;
        private double hourlyRate;
        private boolean available;
        private Long ownerId;

        public MockSpot(Long id, String location, String address, double hourlyRate, boolean available, Long ownerId) {
            this.id = id;
            this.location = location;
            this.address = address;
            this.hourlyRate = hourlyRate;
            this.available = available;
            this.ownerId = ownerId;
        }

        // Getters
        public Long getId() { return id; }
        public String getLocation() { return location; }
        public String getAddress() { return address; }
        public double getHourlyRate() { return hourlyRate; }
        public boolean isAvailable() { return available; }
        public Long getOwnerId() { return ownerId; }

        // Setters
        public void setLocation(String location) { this.location = location; }
        public void setAddress(String address) { this.address = address; }
        public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
        public void setAvailable(boolean available) { this.available = available; }
    }
}
