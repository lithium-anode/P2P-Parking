package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import parking.project.dto.UserRegistrationForm;
import parking.project.model.enums.UserRole;
import parking.project.service.UserService;

/**
 * [GRASP: Controller]
 * Handles system events related to user authentication, such as registration and login routing.
 * * [Goal Alignment: Authentication Mechanisms]
 * Facilitates the entry point for all user types (Driver, Owner, Admin) into the system.
 */
@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login"; // Routes to login.html template
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // [Goal Alignment: Form Binding]
        // Provide a concrete object for Thymeleaf's th:object to prevent null errors.
        model.addAttribute("registrationForm", new UserRegistrationForm());
        model.addAttribute("roles", UserRole.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationForm") UserRegistrationForm form, 
                            BindingResult result, 
                            Model model) {
        // [GRASP: Controller]
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "register";
        }
        
        // [Design Pattern: Factory Method]
        // The Service continues to use the UserFactory to create the specific User subclass.
        userService.registerUser(form);
                                
        return "redirect:/login?success";
    }
}