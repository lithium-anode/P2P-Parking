package parking.project.patterns.structural.proxy;

import org.springframework.stereotype.Component;
import parking.project.model.User;
import parking.project.model.enums.UserRole;

/*
    [Design Pattern: Structural - Proxy]
    The Protection Proxy intercepts requests and veerifies if the user has the
    necessary permissions based on their role.
    [GRASP: Controller]
    Handles security-related system events before they reach the core business logic.
*/
@Component
public class VerifyProxy {
    // Goal: Ensure only administrators can perform management operations
    public boolean isAdmin(User user) {
        return user != null && UserRole.ADMIN.equals(user.getRole());
    }

    // Goal: Ensure only space owners can manage their listings
    public boolean isOwner(User user) {
        return user != null && UserRole.OWNER.equals(user.getRole());
    }

    // Goal: Ensure only drivers can search and book parking spots
    public boolean isDriver(User user) {
        return user != null && UserRole.DRIVER.equals(user.getRole());
    }
}