package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parking.project.model.Booking;
import parking.project.model.BookingStatus;
import parking.project.model.ParkingSpot;
import parking.project.model.User;
import parking.project.repository.BookingRepository;
import parking.project.repository.ParkingSpotRepository;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private PaymentService paymentService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ParkingSpotRepository parkingSpotRepository) {
        this.bookingRepository = bookingRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    // Requirement: Verify availability before confirmation
    public boolean isSpotAvailable(ParkingSpot spot) {
        return spot.getIsAvailable();
    }

    @Transactional
    public Booking createBooking(User driver, ParkingSpot spot, LocalDateTime start, LocalDateTime end) {
        // Double-check availability logic [cite: 8]
        if (!isSpotAvailable(spot)) {
            throw new IllegalStateException("The selected parking spot is no longer available.");
        }

        // Initialize the booking record [cite: 2]
        Booking booking = new Booking();
        booking.setDriver(driver);
        booking.setSpot(spot);
        booking.setStartTime(start);
        booking.setEndTime(end);
        booking.setStatus(BookingStatus.CONFIRMED);

        // Update spot status to unavailable [cite: 9]
        spot.setIsAvailable(false);
        parkingSpotRepository.save(spot);

        return bookingRepository.save(booking);
    }

    @Transactional
    public void completeCheckout(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Calculate and process payment
        paymentService.processPaymentForBooking(booking);

        // Requirement: Update availability status on return/checkout
        booking.setStatus(BookingStatus.COMPLETED);
        ParkingSpot spot = booking.getSpot();
        spot.setIsAvailable(true);

        parkingSpotRepository.save(spot);
        bookingRepository.save(booking);
    }
}