package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.User;
import parking.project.model.ParkingSpot;
import parking.project.model.Payment;
import parking.project.repository.UserRepository;
import parking.project.repository.ParkingSpotRepository;
import parking.project.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [GRASP: Information Expert]
 * This service is the Information Expert for system-wide oversight. It coordinates 
 * data from User, ParkingSpot, and Payment repositories to provide a 
 * comprehensive view of the platform's health.
 * * [GRASP: High Cohesion]
 * Centralizes all administrative business logic, including user management, 
 * listing oversight, and report generation.
 * * [Design Pattern: Proxy - Target]
 * The methods in this service are the intended targets for the 'VerifyProxy'. 
 * The Proxy ensures that only users with the ADMIN role can execute these 
 * sensitive operations.
 */
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public AdminService(UserRepository userRepository, 
                        ParkingSpotRepository parkingSpotRepository, 
                        PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.parkingSpotRepository = parkingSpotRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * [Goal Alignment: User Management]
     * Provides a full registry of all users (Drivers, Owners, Admins) for 
     * administrative review and account control.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * [Goal Alignment: Smooth Operation]
     * Allows Administrators to deactivate or remove accounts that violate 
     * system terms of service.
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * [Goal Alignment: Listing Oversight]
     * Allows the Administrator to monitor all urban parking resources 
     * registered in the system.
     */
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    /**
     * [Goal Alignment: Usage Reports]
     * Aggregates financial data within a specific timeframe to generate 
     * platform-wide usage and revenue reports.
     */
    public List<Payment> generateUsageReport(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    /**
     * [Goal Alignment: Efficient Management]
     * Provides analytics on the total number of transactions and total 
     * system revenue.
     */
    public double calculateTotalSystemRevenue() {
        List<Payment> allPayments = paymentRepository.findAll();
        double total = 0;
        for (Payment payment : allPayments) {
            total += payment.getAmount();
        }
        return total;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public long getTotalSpots() {
        return parkingSpotRepository.count();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }
}