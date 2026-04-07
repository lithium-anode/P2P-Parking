package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
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
    public String processLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User user = userService.loginUser(username, password); // Logic for authentication
        System.out.println(">>> Login attempt for user: " + username);
        
        if (user != null) {
            System.out.println(">>> User authenticated: " + user.getUsername() + " Role: " + user.getRole());
            // Store user in session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            
            // Role-based redirection
            switch (user.getRole()) {
                case "ADMIN": return "redirect:/admin/dashboard";
                case "OWNER": return "redirect:/owner/dashboard";
                case "DRIVER": return  "redirect:/driver/dashboard?username=" + user.getUsername();
                default: return "redirect:/";
            }
        } else {
            System.out.println(">>> Login failed for user: " + username);
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
                                        System.out.println(">>> REGISTER ENDPOINT HIT");
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
            System.out.println(">>> Registration successful for: " + username);
            return "redirect:/login?success=registered";
            
        } catch (Exception e) {
            System.out.println(">>> Registration error: " + e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
        
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/register";
    }
}