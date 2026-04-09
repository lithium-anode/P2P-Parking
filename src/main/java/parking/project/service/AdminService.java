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

/*
    [GRASP: Information Expert]
    Co-ordinates data from User, ParkingSpot, and Payment repositories to provide a
    comprehensive view of the platform's health.
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

    // Goal: Provides a full registry of all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Goal: Allows Administrators to deactivate or remove accounts
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // Goal: Provides a full registry of all parking spots
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    // Goal: Generates usage reports based on payment data
    public List<Payment> generateUsageReport(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    // Goal: Provides analytics on the total number of transactions and total system revenue
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