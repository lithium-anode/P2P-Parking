package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/*
    [Design Pattern: Behavioral - State]
    Represents the 'Reserved' status of a spot. 
    Goal: Prevent double-booking
*/
public class ReservedState implements SpotState {
    @Override
    public void handleReserve(ParkingSpot spot) {
        throw new IllegalStateException("Spot is already reserved.");
    }

    @Override
    public void handleOccupy(ParkingSpot spot) {
        spot.setCurrentState(new OccupiedState());
    }

    @Override
    public void handleVacate(ParkingSpot spot) {
        spot.setCurrentState(new AvailableState());
    }

    @Override
    public String getStatusName() {
        return "RESERVED";
    }
}