package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/*
    [Design Pattern: Behavioral - State]
    Represents the 'Occupied' status of a spot.
*/
public class OccupiedState implements SpotState {
    @Override
    public void handleReserve(ParkingSpot spot) {
        throw new IllegalStateException("Spot is currently occupied and cannot be reserved.");
    }

    @Override
    public void handleOccupy(ParkingSpot spot) {
        throw new IllegalStateException("Spot is already occupied.");
    }

    @Override
    public void handleVacate(ParkingSpot spot) {
        spot.setCurrentState(new AvailableState());
    }

    @Override
    public String getStatusName() {
        return "OCCUPIED";
    }
}