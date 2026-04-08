package parking.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import parking.project.model.enums.UserRole;

/**
 * [GRASP: Information Expert] 
 * This class is the information expert for user identity and authentication credentials. 
 * It holds the core data (username, password, role) required to identify a user in the system.
 *
 * [Design Pattern: Factory Method - Product]
 * In the context of the Factory Method, this is the abstract 'Product'. 
 * The UserFactory will instantiate specific subclasses (Driver, SpaceOwner, Admin) 
 * based on the assigned role during registration.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * [Design Pattern: Proxy - Sensitivity]
     * The role field is critical for the 'VerifyProxy'. The Proxy will check 
     * this value to determine if a user has the authority to access specific 
     * functionalities like 'manage user accounts' or 'add new spots'.
     */
    @NotNull(message = "User role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public User() { }

    // Parameterized constructor for use by the Factory Method
    public User(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}