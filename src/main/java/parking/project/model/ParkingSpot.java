package parking.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import parking.project.model.enums.SpotType;
import parking.project.model.enums.SpotStatus;
import parking.project.patterns.behavioral.state.ReservedState;
import parking.project.patterns.behavioral.state.OccupiedState;
import parking.project.patterns.behavioral.state.AvailableState;
import parking.project.patterns.behavioral.state.SpotState;
import parking.project.patterns.behavioral.strategy.PricingStrategy;

/**
 * [GRASP: Information Expert]
 * This class is the Information Expert for a parking space's details, including its 
 * location, hourly rate, and current availability status.
 * * [Design Pattern: State - Context]
 * This class acts as the 'Context' in the State Pattern. It maintains a reference 
 * to a SpotState object that defines the current availability (e.g., Available, 
 * Reserved, or Occupied).
 * * [Design Pattern: Strategy - Context]
 * This class also acts as a 'Context' for the Strategy Pattern, allowing different 
 * pricing algorithms (Standard, Peak, etc.) to be applied to the spot.
 */
@Entity
@Table(name = "parking_spots")
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Positive(message = "Hourly rate must be greater than zero")
    @Column(nullable = false)
    private double hourlyRate;

    @NotNull(message = "Spot type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotType spotType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotStatus status = SpotStatus.AVAILABLE;

    /**
     * [Goal Alignment: Availability Management]
     * The availability status is updated during checkouts and verified before 
     * confirming bookings to prevent double-booking.
     */
    @Transient // State logic handled by the State Pattern classes, not just a raw string
    private SpotState currentState;

    /**
     * [Goal Alignment: Space Owner Control]
     * Owners are responsible for maintaining these listings and updating 
     * information like hourly rates.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private SpaceOwner owner;

    private boolean active = true; // Default to true

    // Default constructor required by JPA
    public ParkingSpot() { }

    public ParkingSpot(String location, double hourlyRate, SpaceOwner owner, SpotType spotType) {
        this.location = location;
        this.hourlyRate = hourlyRate;
        this.owner = owner;
        this.spotType = spotType;
    }

    /**
     * [Design Pattern: Strategy - Execution]
     * Delegates the cost calculation to a specific PricingStrategy implementation 
     * based on the owner's choice or system conditions.
     */
    public double calculateCost(int hours, PricingStrategy strategy) {
        return strategy.calculatePrice(hours, this.hourlyRate, spotType);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public SpotType getSpotType() { return spotType; }
    public void setSpotType(SpotType spotType) { this.spotType = spotType; }

    public SpotState getCurrentState() {
        if (currentState == null) {
            switch (status) {
                case AVAILABLE -> currentState = new AvailableState();
                case RESERVED -> currentState = new ReservedState();
                case OCCUPIED -> currentState = new OccupiedState();
            }
        }
        return currentState;
    }
    
    public void setCurrentState(SpotState currentState) {
        this.currentState = currentState;
        // Update status based on state type
        if (currentState instanceof AvailableState) {
            this.status = SpotStatus.AVAILABLE;
        } else if (currentState instanceof ReservedState) {
            this.status = SpotStatus.RESERVED;
        } else if (currentState instanceof OccupiedState) {
            this.status = SpotStatus.OCCUPIED;
        }
    }

    public SpotStatus getStatus() { return status; }
    public void setStatus(SpotStatus status) { this.status = status; }

    public SpaceOwner getOwner() { return owner; }
    public void setOwner(SpaceOwner owner) { this.owner = owner; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}