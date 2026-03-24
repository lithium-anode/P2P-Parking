package parking.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parking_spots")
@Data
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private String address;
    private double hourlyRate;
    private boolean isAvailable = true; // Default to available

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private SpaceOwner owner;

    // Constructor
    public ParkingSpot() {}

    public ParkingSpot(String location, String address, double hourlyRate, SpaceOwner owner) {
        this.location = location;
        this.address = address;
        this.hourlyRate = hourlyRate;
        this.owner = owner;
        this.isAvailable = true;
    }

    // Getter for isAvailable (required by BookingFacade)
    public boolean getIsAvailable() {
        return isAvailable;
    }

    // Setter for isAvailable
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
