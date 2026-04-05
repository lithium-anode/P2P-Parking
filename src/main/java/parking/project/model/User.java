package parking.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
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

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public UserStatus getStatus() { return status; }

    public void setStatus(UserStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (!user.canEqual(this)) return false;
        return Objects.equals(id, user.id)
            && Objects.equals(username, user.username)
            && Objects.equals(password, user.password)
            && Objects.equals(email, user.email)
            && Objects.equals(role, user.role)
            && status == user.status
            && Objects.equals(createdAt, user.createdAt);
    }

    protected boolean canEqual(Object other) {
        return other instanceof User;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, role, status, createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}