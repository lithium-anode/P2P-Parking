package parking.project.patterns.facade;

import org.springframework.stereotype.Component;
import parking.project.model.*;
import parking.project.service.BookingService;
import parking.project.patterns.observer.BookingObserver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingFacade {
    private final BookingService bookingService;
    private final List<BookingObserver> observers = new ArrayList<>();

    public BookingFacade(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    // High-level method to simplify the booking process for the Driver
    public Booking createReservation(User driver, ParkingSpot spot, LocalDateTime start, LocalDateTime end) {
        // 1. Verify availability (Requirement: Double-booking prevention) 
        if (!spot.getIsAvailable()) {
            throw new IllegalStateException("Spot is already occupied.");
        }

        // 2. Create and save the booking
        Booking booking = bookingService.saveBooking(driver, spot, start, end);

        // 3. Notify Observers (Behavioral Pattern logic) 
        observers.forEach(observer -> observer.onBookingStatusChange(booking));

        return booking;
    }
}