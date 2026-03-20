package parking.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import parking.project.service.DriverService;

@Controller
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Implementation for the Driver's dashboard view
        return "driver/dashboard"; // Points to templates/driver/dashboard.html
    }

    @GetMapping("/search")
    public String searchParking(Model model) {
        // Implementation for searching available spots
        return "driver/search"; 
    }
}