package parking.project.patterns.creational.factory;

import parking.project.model.*;
import parking.project.model.enums.UserRole;

/**
 * [Design Pattern: Factory Method - Creator]
 * This class acts as the 'Creator'. It provides a static factory method to 
 * encapsulate the instantiation logic for various User types. 
 * * [GRASP: Pure Fabrication]
 * The UserFactory is a pure fabrication; it does not represent a physical domain 
 * concept but is created to achieve Low Coupling and High Cohesion.
 */
public class UserFactory {
    /**
     * [Design Pattern: Factory Method - Factory Method]
     * Creates a specific User subclass based on the provided UserRole.
     * This ensures the calling code (like AuthController) doesn't need to 
     * know the concrete classes (Driver, Admin, etc.).
     */
    public static User createUser(String username, String password, String email, UserRole role) {
        if (role == null) {
            return null;
        }

        switch (role) {
            case DRIVER:
                return new Driver(username, password, email);
            case OWNER:
                return new SpaceOwner(username, password, email);
            case ADMIN:
                return new Admin(username, password, email);
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }
}