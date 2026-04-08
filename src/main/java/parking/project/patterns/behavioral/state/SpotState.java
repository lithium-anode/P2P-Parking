package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/**
 * [Design Pattern: State - State Interface]
 * Defines the interface for encapsulating the behavior associated with a 
 * particular state of the ParkingSpot.
 * * [GRASP: Polymorphism]
 * By using an interface, we can change the spot's behavior at runtime 
 * without using complex conditional (if/else) logic.
 */
public interface SpotState {
    void handleReserve(ParkingSpot spot);
    void handleOccupy(ParkingSpot spot);
    void handleVacate(ParkingSpot spot);
    String getStatusName();
}