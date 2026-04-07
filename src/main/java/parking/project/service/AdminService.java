package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.Admin;
import parking.project.model.User;
import parking.project.model.UserStatus;
import parking.project.repository.AdminRepository;
import parking.project.repository.UserRepository;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    // Requirement: Oversee user management 
    public void verifyUserAccount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.VERIFIED);
        userRepository.save(user);
    }

    public void suspendUserAccount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);
    }

    // Requirement: Monitor system performance/usage 
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Admin getAdminProfile(String username) {
        return adminRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
    }
}