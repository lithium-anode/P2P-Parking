package parking.project.model;

import jakarta.persistence.*;
import java.util.List;
import parking.project.model.enums.UserRole;

@Entity
@Table(name = "space_owners")
@PrimaryKeyJoinColumn(name = "user_id")
public class SpaceOwner extends User {
    private double totalEarnings;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ParkingSpot> parkingSpots;

    public SpaceOwner() {
        super();
    }

    public SpaceOwner(String username, String password, String email) {
        super(username, password, email, UserRole.OWNER);
    }

    // Getters and Setters
    public double getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(double totalEarnings) {this.totalEarnings = totalEarnings; }

    public List<ParkingSpot> getParkingSpots() { return parkingSpots; }
    public void setParkingSpots(List<ParkingSpot> parkingSpots) { this.parkingSpots = parkingSpots; }
}