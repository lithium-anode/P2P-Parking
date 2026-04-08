package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/**
 * [Design Pattern: State - Concrete State]
 * Represents the 'Available' status of a spot.
 */
public class AvailableState implements SpotState {
    @Override
    public void handleReserve(ParkingSpot spot) {
        // Transition to Reserved
        spot.setCurrentState(new ReservedState());
    }

    @Override
    public void handleOccupy(ParkingSpot spot) {
        throw new IllegalStateException("Spot must be reserved before it can be occupied.");
    }

    @Override
    public void handleVacate(ParkingSpot spot) {
        System.out.println("Spot is already available.");
    }

    @Override
    public String getStatusName() { return "AVAILABLE"; }
}