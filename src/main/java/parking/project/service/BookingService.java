package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.*;
import parking.project.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Save a new booking
     * @param driver The driver making the booking
     * @param spot The parking spot being booked
     * @param start The start time of the booking
     * @param end The end time of the booking
     * @return The saved booking
     */
    public Booking saveBooking(User driver, ParkingSpot spot, LocalDateTime start, LocalDateTime end) {
        // Create new booking with PENDING status
        Booking booking = new Booking(driver, spot, start, end, BookingStatus.PENDING);

        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        // Mark the spot as unavailable
        spot.setIsAvailable(false);

        return savedBooking;
    }

    /**
     * Get all bookings for a driver
     */
    public List<Booking> getBookingsByDriver(User driver) {
        return bookingRepository.findByDriver(driver);
    }

    /**
     * Get all bookings for a parking spot
     */
    public List<Booking> getBookingsBySpot(ParkingSpot spot) {
        return bookingRepository.findBySpot(spot);
    }

    /**
     * Check if a parking spot is available for the given time range
     */
    public boolean isSpotAvailable(ParkingSpot spot, LocalDateTime start, LocalDateTime end) {
        List<Booking> conflictingBookings = bookingRepository
            .findBySpotAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(spot, end, start);

        return conflictingBookings.isEmpty() && spot.getIsAvailable();
    }

    /**
     * Confirm a booking (change status from PENDING to CONFIRMED)
     */
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    /**
     * Cancel a booking
     */
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);

        // Make the spot available again
        booking.getSpot().setIsAvailable(true);

        return bookingRepository.save(booking);
    }
}
