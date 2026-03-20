package parking.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String email;
    private String role; // DRIVER, OWNER, ADMIN

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.UNVERIFIED; // Default state

    private LocalDateTime createdAt = LocalDateTime.now(); // For timeout check
}