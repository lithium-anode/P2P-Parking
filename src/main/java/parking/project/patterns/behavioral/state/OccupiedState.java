package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/**
 * [Design Pattern: State - Concrete State]
 * Represents the 'Occupied' status of a spot.
 * * [Goal Alignment: Checkout Logic]
 * Supports the requirement that during every checkout, the availability 
 * status of the spot will be updated.
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
        /**
         * [Goal Alignment: Automatic Status Update]
         * When the driver completes their stay (checkout), the state 
         * transitions back to Available.
         */
        spot.setCurrentState(new AvailableState());
    }

    @Override
    public String getStatusName() {
        return "OCCUPIED";
    }
}