package parking.project.model;

import jakarta.persistence.*;
import parking.project.model.enums.UserRole;
import parking.project.model.enums.VehicleType;

import java.util.List;

/**
 * [Design Pattern: Factory Method - Concrete Product]
 * This is a concrete implementation of the User product specifically for the 'Driver' role.
 * * [GRASP: Information Expert]
 * This class is the expert for a Driver's reservation history. It holds the collection 
 * of Bookings associated with the driver's account.
 */
@Entity
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Driver extends User {
    /**
     * [Goal Alignment: Booking Records]
     * Maintains accurate records of all reservations made by the driver to 
     * support transaction history and checkout processes.
     */
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @ElementCollection(targetClass = VehicleType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "driver_vehicle_types", joinColumns = @JoinColumn(name = "driver_id"))
    @Column(name = "vehicle_type")
    private List<VehicleType> vehicleTypes;

    public Driver() {
        super();
    }

    public Driver(String username, String password, String email) {
        super(username, password, email, UserRole.DRIVER);
    }

    // Getters and Setters
    public List<VehicleType> getVehicleTypes() { return vehicleTypes; }
    public void setVehicleTypes(List<VehicleType> vehicleTypes) { this.vehicleTypes = vehicleTypes; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}