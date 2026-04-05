package parking.project.service;

import org.springframework.stereotype.Service;
import parking.project.model.*;
import parking.project.repository.PaymentRepository;
import parking.project.patterns.strategy.PricingStrategy;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(Booking booking, PricingStrategy strategy) {
        // Calculate duration in minutes
        long minutes = Duration.between(booking.getStartTime(), booking.getEndTime()).toMinutes();
        
        // Use strategy to calculate amount based on owner's rate 
        double totalAmount = strategy.calculatePrice(booking.getSpot().getPricePerHour(), minutes);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setStatus("PAID");

        return paymentRepository.save(payment); // Persist data 
    }
}