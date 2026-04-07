package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.*;
import parking.project.repository.PaymentRepository;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Process payment for a completed booking.
     * For demo purposes: ANY booking (even 30 seconds) charges for 1 full hour.
     * @param booking The completed booking
     * @return The created Payment record
     */
    public Payment processPaymentForBooking(Booking booking) {
        // DEMO MODE: billing is based on actual elapsed time at checkout.
        // Every 20 seconds counts as 1 hour.
        long durationSeconds = Duration.between(booking.getStartTime(), LocalDateTime.now()).toSeconds();
        if (durationSeconds <= 0) {
            durationSeconds = 1;
        }
        double hours = Math.ceil(durationSeconds / 20.0);

        // Get hourly rate from parking spot
        double hourlyRate = booking.getSpot().getPricePerHour();
        
        // Calculate total amount using demo billing rule
        double totalAmount = hourlyRate * hours;

        // Create and save payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setStatus("PAID");

        return paymentRepository.save(payment);
    }

    /**
     * Get the amount charged for a booking (payment amount)
     * For demo purposes: ANY booking charges for 1 full hour.
     * @param booking The booking
     * @return The calculated charge amount (always 1 hour minimum)
     */
    public double calculateChargeForBooking(Booking booking) {
        long durationSeconds = Duration.between(booking.getStartTime(), LocalDateTime.now()).toSeconds();
        if (durationSeconds <= 0) {
            durationSeconds = 1;
        }
        double hours = Math.ceil(durationSeconds / 20.0);
        return booking.getSpot().getPricePerHour() * hours;
    }
}