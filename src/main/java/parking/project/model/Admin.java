package parking.project.model;

import jakarta.persistence.*;
import parking.project.model.enums.UserRole;

/**
 * [Design Pattern: Factory Method - Concrete Product]
 * Concrete implementation of the User product for the 'Administrator' role.
 * * [GRASP: Information Expert]
 * As the system overseer, this class represents the administrative authority 
 * required to manage other users and generate system-wide reports.
 */
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {
    public Admin() {
        super();
    }

    public Admin(String username, String password, String email) {
        super(username, password, email, UserRole.ADMIN);
    }
}