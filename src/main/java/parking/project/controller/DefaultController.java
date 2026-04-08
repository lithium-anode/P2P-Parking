package parking.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DefaultController {
    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) return "redirect:/admin/users";
        if (request.isUserInRole("ROLE_OWNER")) return "redirect:/owner/dashboard";
        return "redirect:/driver/search";
    }
}