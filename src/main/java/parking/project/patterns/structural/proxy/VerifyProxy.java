package parking.project.patterns.structural.proxy;

import org.springframework.stereotype.Component;
import parking.project.model.User;
import parking.project.model.enums.UserRole;

/**
 * [Design Pattern: Proxy Pattern - Protection Proxy]
 * This class acts as a Protection Proxy. It intercepts requests to perform sensitive 
 * actions and verifies if the User has the necessary permissions based on their role.
 * * [GRASP: Controller / Gatekeeper]
 * It supports the Controller principle by handling security-related system events 
 * before they reach the core business logic.
 */
@Component
public class VerifyProxy {
    /**
     * [Goal Alignment: User Management & Role Access]
     * Verifies if the user is an Administrator before allowing management operations.
     */
    public boolean isAdmin(User user) {
        return user != null && UserRole.ADMIN.equals(user.getRole());
    }

    /**
     * [Goal Alignment: Space Owner Control]
     * Verifies if the user is a Space Owner before allowing them to add or update listings.
     */
    public boolean isOwner(User user) {
        return user != null && UserRole.OWNER.equals(user.getRole());
    }

    /**
     * [Goal Alignment: Driver Search & Booking]
     * Verifies if the user is a Driver before allowing them to reserve spots.
     */
    public boolean isDriver(User user) {
        return user != null && UserRole.DRIVER.equals(user.getRole());
    }
}