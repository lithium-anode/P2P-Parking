package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/**
 * [Design Pattern: State - Concrete State]
 * Represents the 'Reserved' status of a spot. 
 * A spot in this state is held for a specific driver to prevent double-booking.
 * * [Goal Alignment: Availability Management]
 * Ensures that before a booking is confirmed, the system can track that the 
 * spot is no longer 'Available' for others.
 */
public class ReservedState implements SpotState {
    @Override
    public void handleReserve(ParkingSpot spot) {
        throw new IllegalStateException("Spot is already reserved.");
    }

    @Override
    public void handleOccupy(ParkingSpot spot) {
        // Transition to Occupied when the driver arrives/checks in
        spot.setCurrentState(new OccupiedState());
    }

    @Override
    public void handleVacate(ParkingSpot spot) {
        // Transition back to Available if the reservation is cancelled
        spot.setCurrentState(new AvailableState());
    }

    @Override
    public String getStatusName() {
        return "RESERVED";
    }
}