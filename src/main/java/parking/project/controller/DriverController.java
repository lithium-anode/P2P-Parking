package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import parking.project.model.*;
import parking.project.patterns.facade.BookingFacade;
import parking.project.repository.BookingRepository;
import parking.project.repository.ParkingSpotRepository;
import parking.project.repository.UserRepository;
import parking.project.repository.PaymentRepository;
import parking.project.service.BookingService;
import parking.project.service.DriverService;
import parking.project.service.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/driver")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingFacade bookingFacade; // Uses Facade pattern

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSpotRepository spotRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    // 1. Dashboard: View profile and active bookings
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !(user instanceof Driver)) {
            return "redirect:/login";
        }

        Driver driver = (Driver) user;
        model.addAttribute("driver", driver);

        // Fetch driver's active bookings
        List<Booking> activeBookings = bookingRepository.findByDriverAndStatus(driver, BookingStatus.CONFIRMED);
        model.addAttribute("activeBookings", activeBookings);

        // Fetch all bookings for history
        List<Booking> allBookings = bookingRepository.findByDriver(driver);
        model.addAttribute("allBookings", allBookings);

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
    public String reserveSpot(@RequestParam Long spotId, Model model, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/login";
            }

            User user = userRepository.findById(userId).orElse(null);
            if (!(user instanceof Driver)) {
                return "redirect:/login";
            }

            Driver driver = (Driver) user;
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusHours(1);

            ParkingSpot spot = spotRepository.findById(spotId).orElseThrow();

            bookingFacade.createReservation(driver, spot, start, end);

            return "redirect:/driver/dashboard?success=booked";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "driver/search";
        }
    }

    // 5. Update vehicle details
    @PostMapping("/update-vehicle")
    public String updateVehicleDetails(@RequestParam String licenseNumber,
                                       @RequestParam String vehicleModel,
                                       @RequestParam String vehicleNumber,
                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        driverService.updateAllVehicleDetails(userId, licenseNumber, vehicleModel, vehicleNumber);
        return "redirect:/driver/dashboard?success=vehicle-updated";
    }

    // 6. Checkout: Complete booking and process payment
    @PostMapping("/checkout/{bookingId}")
    public String checkoutBooking(@PathVariable Long bookingId, Model model, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return "redirect:/login";
            }

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Verify this booking belongs to the driver
            if (!booking.getDriver().getId().equals(userId)) {
                return "redirect:/driver/dashboard?error=unauthorized";
            }

            // Complete the checkout - this processes payment automatically
            bookingService.completeCheckout(bookingId);

            // Fetch the created payment
            Payment payment = paymentRepository.findByBookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // Refresh booking to get updated status
            booking = bookingRepository.findById(bookingId).orElseThrow();

            model.addAttribute("booking", booking);
            model.addAttribute("payment", payment);

            return "driver/payment-receipt";
        } catch (Exception e) {
            return "redirect:/driver/dashboard?error=" + e.getMessage();
        }
    }
}
