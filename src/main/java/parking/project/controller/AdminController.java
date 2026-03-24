package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.model.Admin;
import parking.project.model.User;
import parking.project.service.UserService;
import parking.project.repository.UserRepository;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Admin dashboard - main admin page
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Get current admin (in a real app, this would come from security context)
        Admin admin = new Admin();
        admin.setUsername("admin"); // Placeholder

        model.addAttribute("admin", admin);
        model.addAttribute("systemStatus", admin.monitorSystem());
        return "admin/dashboard";
    }

    /**
     * User management page - view all users
     */
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("users", allUsers);
        return "admin/users";
    }

    /**
     * Verify a user account
     */
    @PostMapping("/users/{id}/verify")
    public String verifyUser(@PathVariable Long id) {
        userService.verifyUser(id);
        return "redirect:/admin/users";
    }

    /**
     * Suspend or unsuspend a user
     */
    @PostMapping("/users/{id}/suspend")
    public String suspendUser(@PathVariable Long id, @RequestParam boolean suspend) {
        userService.updateSuspensionStatus(id, suspend);
        return "redirect:/admin/users";
    }

    /**
     * Delete/deactivate a user account
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        // In a real implementation, check for active bookings
        String result = userService.deleteAccount(id, false); // Assuming no active bookings for simplicity
        return "redirect:/admin/users";
    }

    /**
     * Generate system reports
     */
    @GetMapping("/reports")
    public String generateReport(Model model) {
        Admin admin = new Admin();
        admin.setUsername("admin"); // Placeholder

        String report = admin.generateReport();
        model.addAttribute("report", report);
        return "admin/reports";
    }

    /**
     * System monitoring page
     */
    @GetMapping("/monitor")
    public String monitorSystem(Model model) {
        Admin admin = new Admin();
        admin.setUsername("admin"); // Placeholder

        String status = admin.monitorSystem();
        model.addAttribute("systemStatus", status);

        // Additional monitoring data
        long totalUsers = userRepository.count();
        model.addAttribute("totalUsers", totalUsers);

        return "admin/monitor";
    }
}
