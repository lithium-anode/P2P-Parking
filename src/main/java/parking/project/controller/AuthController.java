package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import parking.project.model.*;
import parking.project.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Show Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; // Points to templates/auth/login.html
    }

    // 2. Process Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.loginUser(username, password); // Logic for authentication
        
        if (user != null) {
            // Role-based redirection
            switch (user.getRole()) {
                case "ADMIN": return "redirect:/admin/dashboard";
                case "OWNER": return "redirect:/owner/dashboard";
                case "DRIVER": return "redirect:/driver/dashboard";
                default: return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "auth/login";
        }
    }

    // 3. Show Registration Page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "auth/register";
    }

    // 4. Process Registration
    @PostMapping("/register")
    public String processRegistration(@RequestParam String username, 
                                     @RequestParam String password, 
                                     @RequestParam String email,
                                     @RequestParam String role,
                                     Model model) {
        try {
            User newUser;
            // Create specific subclass instance based on role selection
            if ("DRIVER".equals(role)) {
                newUser = new Driver();
            } else if ("OWNER".equals(role)) {
                newUser = new SpaceOwner();
            } else {
                newUser = new Admin();
            }

            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setRole(role);

            userService.registerUser(newUser, UserStatus.UNVERIFIED); // Persistence via Service
            return "redirect:/login?success=registered";
            
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }
}