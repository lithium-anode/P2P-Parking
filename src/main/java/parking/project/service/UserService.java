package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.*;
import parking.project.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 1. New Method: Handle User Registration
    public void registerUser(User user, UserStatus status) {
        System.out.println(">>> SERVICE CALLED");
        user.setStatus(status);
        user.setCreatedAt(LocalDateTime.now()); // For the 5-min timeout check
        userRepository.save(user); // Persist to database
         System.out.println(">>> User saved successfully!");
    }

    // 2. New Method: Handle Login/Authentication
    public User loginUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password)) // Check credentials
                .filter(user -> user.getStatus() != UserStatus.DEACTIVATED) // Ensure account is valid
                .orElse(null); // Returns null if login fails
    }

    // Existing Transition: Unverified -> Active
    public void verifyUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getStatus() == UserStatus.UNVERIFIED) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    // Existing Transition: Active <-> Suspended
    public void updateSuspensionStatus(Long userId, boolean isFlagged) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(isFlagged ? UserStatus.SUSPENDED : UserStatus.ACTIVE);
        userRepository.save(user);
    }

    // Existing Transition: Active -> Deactivated
    public String deleteAccount(Long userId, boolean hasActiveBookings) {
        User user = userRepository.findById(userId).orElseThrow();
        
        // Guard Condition: [no active bookings]
        if (hasActiveBookings) {
            return "Cannot delete account: You have active bookings.";
        }
        
        user.setStatus(UserStatus.DEACTIVATED);
        userRepository.save(user);
        return "Account successfully deactivated.";
    }

    // Existing Logic for [verification timeout > 5 mins]
    public void checkVerificationTimeout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getStatus() == UserStatus.UNVERIFIED) {
            long minutes = Duration.between(user.getCreatedAt(), LocalDateTime.now()).toMinutes();
            if (minutes > 5) {
                user.setStatus(UserStatus.DEACTIVATED);
                userRepository.save(user);
            }
        }
    }
}