package parking.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String location;
    private double pricePerHour;
    private boolean isAvailable;
    private String spotType;

    // Standard Default Constructor
    public ParkingSpot() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }

    public boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(boolean available) { isAvailable = available; }

    public String getSpotType() { return spotType; }
    public void setSpotType(String spotType) { this.spotType = spotType; }
}
