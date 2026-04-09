package parking.project.model;

import jakarta.persistence.*;
import parking.project.model.enums.UserRole;

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