package parking.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "space_owners")
public class SpaceOwner extends User {
    // private String businessName; // Optional: If the owner operates as a business
    private double totalEarnings; // To track accumulated revenue from all spots
    private String contactNumber;

    // Standard Default Constructor
    public SpaceOwner() {
        super();
        this.setRole("OWNER"); // Identifies this user type in the system
    }

    // Parameterized Constructor
    public SpaceOwner(String username, String password, String email, String businessName, String contactNumber) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setRole("OWNER");
        // this.businessName = businessName;
        this.contactNumber = contactNumber;
        this.totalEarnings = 0.0; // Initialize earnings to zero
    }

    // Manual Getters and Setters
    // public String getBusinessName() { return businessName; }

    // public void setBusinessName(String businessName) { this.businessName = businessName; }

    public double getTotalEarnings() { return totalEarnings; }

    public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }

    public String getContactNumber() { return contactNumber; }

    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}