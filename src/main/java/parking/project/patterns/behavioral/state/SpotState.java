package parking.project.patterns.behavioral.state;

import parking.project.model.ParkingSpot;

/*
    [Design Pattern: Behavioral - State]
    The interface encapsulates ParkingSpot state transitions.
    [GRASP: Polymorphism]
    By using an interface, the spot's behavior can be changed at runtime 
    without using complex conditional (if/else) logic.
*/
public interface SpotState {
    void handleReserve(ParkingSpot spot);
    void handleOccupy(ParkingSpot spot);
    void handleVacate(ParkingSpot spot);
    String getStatusName();
}