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

    // Transition: Unverified -> Active
    public void verifyUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getStatus() == UserStatus.UNVERIFIED) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    // Transition: Active <-> Suspended
    public void updateSuspensionStatus(Long userId, boolean isFlagged) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(isFlagged ? UserStatus.SUSPENDED : UserStatus.ACTIVE);
        userRepository.save(user);
    }

    // Transition: Active -> Deactivated
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

    // Logic for [verification timeout > 5 mins]
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