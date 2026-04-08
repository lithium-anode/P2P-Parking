package parking.project.patterns.creational.factory;

import org.springframework.stereotype.Component;
import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.model.enums.SpotType;
import parking.project.patterns.behavioral.state.AvailableState;

/**
 * [Design Pattern: Factory Method - Creator]
 * This class acts as a 'Creator' for ParkingSpot objects. It encapsulates the 
 * instantiation logic to ensure that spots are created consistently across the system.
 * * [GRASP: Pure Fabrication]
 * The SpotFactory is a pure fabrication created to achieve High Cohesion and 
 * Low Coupling. By moving the creation logic here, we prevent the OwnerController 
 * from being directly coupled to the constructor of the ParkingSpot entity.
 */
@Component
public class SpotFactory {
    /**
     * [Design Pattern: Factory Method]
     * Creates a ParkingSpot instance. While the current implementation returns a 
     * standard ParkingSpot entity, this factory allows the system to easily 
     * introduce specialized subclasses in the future without changing the 
     * controller logic.
     * * [Goal Alignment: Space Owners can add new spots]
     * Supports the requirement for Owners to maintain their parking listings by 
     * providing a standardized way to generate new entries.
     */
    public ParkingSpot createSpot(String location, double hourlyRate, SpaceOwner owner, SpotType type) {
        // [Goal Alignment: Accurate Records]
        // Ensures that all essential data (location, rate, owner, and type) 
        // is present at the moment of instantiation.
        ParkingSpot spot = new ParkingSpot(location, hourlyRate, owner, type);
        spot.setCurrentState(new AvailableState());
        return spot;
    }
}