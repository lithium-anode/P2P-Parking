package parking.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.patterns.structural.proxy.VerifyProxy;
import parking.project.service.AdminService;
import parking.project.service.UserService;
import parking.project.model.User;

/**
 * [GRASP: Controller]
 * Coordinates high-level administrative tasks such as generating reports and user oversight.
 * * [Design Pattern: Proxy - Protection Proxy]
 * Strictly enforces that only users with 'ADMIN' privileges can access these operations.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final VerifyProxy verifyProxy;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, VerifyProxy verifyProxy, UserService userService) {
        this.adminService = adminService;
        this.verifyProxy = verifyProxy;
        this.userService = userService;
    }

    /**
     * [Goal Alignment: Usage Reports]
     * Provides access to the administrative reporting dashboard.
     */
    @GetMapping("/reports")
    public String viewReports(Model model) {
        model.addAttribute("totalRevenue", adminService.calculateTotalSystemRevenue());
        model.addAttribute("payments", adminService.getAllPayments());
        model.addAttribute("totalSpots", adminService.getTotalSpots());
        model.addAttribute("totalUsers", adminService.getTotalUsers());
        return "admin/reports";
    }

    /**
     * [Goal Alignment: User Management]
     * Displays a registry of all system users for oversight.
     */
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String removeUser(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!verifyProxy.isAdmin(currentUser)) {
            return "redirect:/login?error=Unauthorized";
        }

        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }
}