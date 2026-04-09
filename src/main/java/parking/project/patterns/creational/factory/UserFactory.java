package parking.project.patterns.creational.factory;

import parking.project.model.*;
import parking.project.model.enums.UserRole;

/*
    [Design Pattern: Creational - Factory Method]
    Creator of User objects based on UserRole
    [GRASP: Pure Fabrication]
    This factory centralizes user creation logic, decoupling it from controllers
    allowing addition of new user types in the future without modifying existing code.
*/
public class UserFactory {
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