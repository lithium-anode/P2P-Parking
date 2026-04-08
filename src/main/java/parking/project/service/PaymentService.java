package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parking.project.model.Booking;
import parking.project.model.Payment;
import parking.project.model.SpaceOwner;
import parking.project.model.enums.PaymentMethod;
import parking.project.repository.PaymentRepository;
import parking.project.repository.SpaceOwnerRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [GRASP: Creator]
 * This service is responsible for creating Payment objects. It has the 
 * necessary information (Booking and PaymentMethod) to initialize a transaction.
 * * [GRASP: High Cohesion]
 * Centralizes all financial logic, ensuring that payment processing and 
 * earning updates are handled in a single location.
 */
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final SpaceOwnerRepository spaceOwnerRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, SpaceOwnerRepository spaceOwnerRepository) {
        this.paymentRepository = paymentRepository;
        this.spaceOwnerRepository = spaceOwnerRepository;
    }

    /**
     * [Goal Alignment: Tracking Earnings]
     * Processes a payment for a completed booking and updates the 
     * Space Owner's total earnings.
     */
    @Transactional
    public Payment processPayment(Booking booking, PaymentMethod method) {
        // [GRASP: Creator]
        // Create the payment record associated with the booking
        Payment payment = new Payment(booking, booking.getTotalCost(), method);
        
        // Update the Space Owner's earnings
        SpaceOwner owner = booking.getParkingSpot().getOwner();
        double updatedEarnings = owner.getTotalEarnings() + booking.getTotalCost();
        owner.setTotalEarnings(updatedEarnings);

        // Persist both the payment and the updated owner data
        spaceOwnerRepository.save(owner);
        return paymentRepository.save(payment);
    }

    /**
     * [Goal Alignment: Usage Reports]
     * Retrieves financial data for Administrators to generate reports 
     * within a specific timeframe.
     */
    public List<Payment> getPaymentsByPeriod(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    /**
     * [Goal Alignment: Space Owner Control]
     * Allows owners to view their specific transaction history and total revenue.
     */
    public List<Payment> getOwnerEarningsReport(Long ownerId) {
        return paymentRepository.findByOwnerId(ownerId);
    }
}