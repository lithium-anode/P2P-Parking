package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import parking.project.dto.UserRegistrationForm;
import parking.project.model.User;
import parking.project.model.Driver;
import parking.project.patterns.creational.factory.UserFactory;
import parking.project.repository.UserRepository;

import java.util.Optional;

/**
 * [GRASP: Controller]
 * Coordinates user-related system events between the web layer and the data layer.
 *
 * [Design Pattern: Factory Method - Client]
 * Uses the UserFactory to instantiate role-specific User objects without 
 * coupling the service to concrete subclasses like Driver or SpaceOwner.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * [Goal Alignment: Authentication - Registration]
     * Creates a new user using the Factory pattern and persists them to the database.
     */
    public User registerUser(UserRegistrationForm form) {
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        User newUser = UserFactory.createUser(form.getUsername(), encodedPassword, form.getEmail(), form.getRole());

        if (newUser instanceof Driver) {
            ((Driver) newUser).setVehicleTypes(form.getVehicleTypes());
        }

        return userRepository.save(newUser);
    }

    /**
     * [Goal Alignment: Authentication - Login]
     * Retrieves a user by their unique username to facilitate secure login.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}