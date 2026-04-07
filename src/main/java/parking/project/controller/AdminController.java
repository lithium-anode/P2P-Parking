package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.model.User;
import parking.project.service.AdminService;
import parking.project.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService; // Business logic for Admin tasks

    @Autowired
    private UserService userService; // Shared logic for user states

    // 1. Admin Dashboard: Overview of system users
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "admin/dashboard"; // Points to templates/admin/dashboard.html
    }

    // 2. Verify User: Transition status from UNVERIFIED to ACTIVE
    @PostMapping("/user/verify/{id}")
    public String verifyUser(@PathVariable Long id) {
        userService.verifyUser(id);
        return "redirect:/admin/dashboard?success=verified";
    }

    // 3. Suspend/Activate User: Manage access based on policy
    @PostMapping("/user/suspend/{id}")
    public String toggleSuspension(@PathVariable Long id, @RequestParam boolean suspend) {
        userService.updateSuspensionStatus(id, suspend);
        return "redirect:/admin/dashboard?success=statusUpdated";
    }

    // 4. Reports: Monitor system performance and usage
    @GetMapping("/reports")
    public String showReports(Model model) {
        // Logic for generating usage reports would go here
        return "admin/reports";
    }
}