package parking.project.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import parking.project.model.enums.UserRole;
import parking.project.model.enums.VehicleType;

/**
 * [Design Pattern: Data Transfer Object (DTO)]
 * Acts as a simple container to move data from the registration form to the service layer.
 * This avoids the issue of trying to instantiate the abstract 'User' entity directly.
 */
public class UserRegistrationForm {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be 4-20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Please select a role")
    private UserRole role;

    private List<VehicleType> vehicleTypes;

    
    // Getters and Setters
    public List<VehicleType> getVehicleTypes() { return vehicleTypes; }
    public void setVehicleTypes(List<VehicleType> vehicleTypes) { this.vehicleTypes = vehicleTypes; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}