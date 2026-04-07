package parking.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    private String employeeId; // Specific to the Admin role
    private String department;
    private int accessLevel; // e.g., 1 for SuperAdmin, 2 for Moderator

    // Standard Default Constructor
    public Admin() {
        super();
        this.setRole("ADMIN"); // Ensures the user type is set correctly 
    }

    // Parameterized Constructor
    public Admin(String username, String password, String email, String employeeId, String department) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setRole("ADMIN");
        this.employeeId = employeeId;
        this.department = department;
    }

    // Manual Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}