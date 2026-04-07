package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.model.*;
import parking.project.patterns.facade.BookingFacade;
import parking.project.repository.ParkingSpotRepository;
import parking.project.service.BookingService;
import parking.project.service.DriverService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    private BookingFacade bookingFacade; // Uses Facade pattern

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Autowired
    private BookingService bookingService;

    // 1. Dashboard: View profile and active bookings
    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name="username", required=false, defaultValue="guest") String username, Model model) {
        Driver driver = driverService.getDriverProfile(username);
        model.addAttribute("driver", driver);
        // Requirement: Maintain accurate records
        return "driver/dashboard"; 
    }

    // 2. Search: Find available spots by location
    @GetMapping("/search")
    public String searchParking(@RequestParam(name="location", required=false) String location, Model model) {
        if (location != null && !location.isEmpty()) {
            List<ParkingSpot> availableSpots = spotRepository.findByLocationContainingAndIsAvailableTrue(location);
            model.addAttribute("spots", availableSpots);
        }
        return "driver/search"; 
    }

    // 3. Booking: Create a reservation using the Facade Pattern
    @PostMapping("/reserve")
    public String reserveSpot(@RequestParam Long driverId, @RequestParam Long spotId, Model model) {
        try {
            // Simplified for prototype: reserving for next 1 hour
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusHours(1);
            
            // We fetch objects (normally handled by authentication/service)
            // Using placeholder logic for demonstration
            ParkingSpot spot = spotRepository.findById(spotId).orElseThrow();
            Driver driver = (Driver) driverService.getDriverProfile("example_user"); 

            // Logic: System must verify availability before confirmation
            bookingFacade.createReservation(driver, spot, start, end);
            
            return "redirect:/driver/dashboard?success=booked";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "driver/search";
        }
    }

    // 4. Checkout: Return spot and update availability
    @PostMapping("/checkout/{bookingId}")
    public String checkout(@PathVariable Long bookingId) {
        // Requirement: Availability status must be updated during every return
        bookingService.completeCheckout(bookingId);
        return "redirect:/driver/dashboard?success=checkedout";
    }
}