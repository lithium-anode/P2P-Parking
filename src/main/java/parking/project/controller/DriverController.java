package parking.project.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import parking.project.model.Booking;
import parking.project.model.Driver;
import parking.project.model.ParkingSpot;
import parking.project.model.enums.PaymentMethod;
import parking.project.model.enums.SpotType;
import parking.project.patterns.behavioral.strategy.StandardPricingStrategy;
import parking.project.patterns.behavioral.strategy.PeakHourPricingStrategy;
import parking.project.patterns.behavioral.strategy.ElectricChargingPricingStrategy;
import parking.project.patterns.behavioral.strategy.PricingStrategy;
import parking.project.patterns.structural.proxy.VerifyProxy;
import parking.project.service.BookingService;
import parking.project.service.UserService;
import parking.project.repository.ParkingSpotRepository;
import parking.project.repository.BookingRepository;

/**
 * [GRASP: Controller]
 * Coordinates driver-specific activities such as searching for spots and initiating bookings.
 * * [Design Pattern: Proxy - Protection Proxy]
 * Uses VerifyProxy to ensure only users with the 'DRIVER' role can access these methods.
 */
@Controller
@RequestMapping("/driver")
public class DriverController {
    private final BookingService bookingService;
    private final ParkingSpotRepository spotRepository;
    private final BookingRepository bookingRepository;
    private final VerifyProxy verifyProxy;
    private final UserService userService;

    @Autowired
    public DriverController(BookingService bookingService, 
                            ParkingSpotRepository spotRepository, 
                            BookingRepository bookingRepository,
                            VerifyProxy verifyProxy,
                            UserService userService) {
        this.bookingService = bookingService;
        this.spotRepository = spotRepository;
        this.bookingRepository = bookingRepository;
        this.verifyProxy = verifyProxy;
        this.userService = userService;
    }

    /**
     * [Goal Alignment: Driver Search]
     * Allows drivers to search for parking spots by location or type.
     */
    @GetMapping("/search")
    public String searchSpots(@RequestParam(required = false) String location, 
                              @RequestParam(required = false) SpotType type, 
                              Model model) {
        // In a real implementation, 'currentUser' would be retrieved from the Session
        // if (!verifyProxy.isDriver(currentUser)) return "error/403";

        if (location != null) {
            model.addAttribute("spots", spotRepository.findByLocationContainingIgnoreCaseAndActiveTrue(location));
        } else if (type != null) {
            model.addAttribute("spots", spotRepository.findBySpotTypeAndActiveTrue(type));
        }
        return "driver/search";
    }

    /**
     * [GRASP: Pure Fabrication]
     * Logic to decide which pricing strategy to apply.
     */
    private PricingStrategy determineStrategy(ParkingSpot spot) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. Check for Electric Charging spots
        if (spot.getSpotType() == SpotType.ELECTRIC_CHARGING) {
            return new ElectricChargingPricingStrategy();
        }
        
        // 2. Check for Peak Hours (e.g., 8 AM - 10 AM or 5 PM - 7 PM)
        int hour = now.getHour();
        if ((hour >= 8 && hour <= 10) || (hour >= 17 && hour <= 19)) {
            return new PeakHourPricingStrategy();
        }
        
        // 3. Default to Standard
        return new StandardPricingStrategy();
    }

    /**
     * [GRASP: Controller]
     * Handles the booking event by coordinating between the Driver, the selected Spot, 
     * and the BookingService.
     */
    @PostMapping("/book")
    public String bookSpot(@RequestParam Long spotId, 
                        @RequestParam int duration, // New parameter from search.html
                        Authentication authentication) {
        String username = authentication.getName();
        Driver currentUser = (Driver) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        /**
         * [Design Pattern: Proxy]
         * Protects the booking functionality by verifying the user's role.
         */
        if (!verifyProxy.isDriver(currentUser)) {
            return "redirect:/login?error=Unauthorized";
        }

        ParkingSpot spot = spotRepository.findById(spotId).orElseThrow();
        
        // [Design Pattern: Strategy Selection]
        // Selects the appropriate pricing algorithm based on spot type or time
        PricingStrategy selectedStrategy = determineStrategy(spot);

        try {
            /**
             * [Goal Alignment: Driver Search & Booking]
             * Uses the user-provided duration to set the end time of the booking, 
             * ensuring the total cost is calculated accurately.
             */
            bookingService.createBooking(
                currentUser, 
                spot, 
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(duration), // Use dynamic duration
                selectedStrategy
            );
            return "redirect:/driver/history";
        } catch (IllegalStateException | IllegalArgumentException e) {
            // [Design Pattern: State]
            // Handles cases where the spot is already Reserved or Occupied or if vehicle type is incompatible
            return "redirect:/driver/search?error=" + e.getMessage();
        }
    }

    @GetMapping("/history")
    public String bookingHistory(Model model, Authentication authentication) {
        String username = authentication.getName();
        Driver currentUser = (Driver) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        model.addAttribute("bookings", currentUser.getBookings());
        return "driver/history";
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Driver currentUser = (Driver) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Booking booking = bookingRepository.findById(id).orElseThrow();
        if (!booking.getDriver().equals(currentUser)) {
            return "redirect:/driver/history?error=Unauthorized";
        }

        try {
            bookingService.cancelBooking(booking);
        } catch (IllegalStateException e) {
            return "redirect:/driver/history?error=" + e.getMessage();
        }

        return "redirect:/driver/history";
    }

    @PostMapping("/checkout/{id}")
    public String checkoutBooking(@PathVariable Long id, @RequestParam PaymentMethod paymentMethod, Authentication authentication) {
        String username = authentication.getName();
        Driver currentUser = (Driver) userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Booking booking = bookingRepository.findById(id).orElseThrow();
        if (!booking.getDriver().equals(currentUser)) {
            return "redirect:/driver/history?error=" + "Unauthorized";
        }

        try {
            // [GRASP: Controller]
            // Passes the user's choice to the service layer for processing
            bookingService.processCheckout(booking, paymentMethod);
        } catch (Exception e) {
            return "redirect:/driver/history?error=" + e.getMessage();
        }

        return "redirect:/driver/history";
    }
}