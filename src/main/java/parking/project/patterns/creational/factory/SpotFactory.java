package parking.project.patterns.creational.factory;

import org.springframework.stereotype.Component;
import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.model.enums.SpotType;
import parking.project.patterns.behavioral.state.AvailableState;

/*
    [Design Pattern: Creational - Factory]
    Creator of ParkingSpot objects
    [GRASP: Pure Fabrication]
    Moving the creation logic here decouples the OwnerController 
    from the ParkingSpot entity. Specialized subclasses can be added
    in the future without changing the controller logic.
    Goal: Space Owners can add new spots
*/
@Component
public class SpotFactory {
    public ParkingSpot createSpot(String location, double hourlyRate, SpaceOwner owner, SpotType type) {
        ParkingSpot spot = new ParkingSpot(location, hourlyRate, owner, type);
        spot.setCurrentState(new AvailableState());
        return spot;
    }
}