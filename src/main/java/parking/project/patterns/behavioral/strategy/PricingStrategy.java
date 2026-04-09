package parking.project.patterns.behavioral.strategy;

import parking.project.model.enums.SpotType;

/*
    [Design Pattern: Behavioral - Strategy]
    Declares an interface common to all supported pricing algorithms.
    [GRASP: Low Coupling]
    The ParkingSpot class remains decoupled from specific pricing calculations, 
    allowing new models to be added easily.
*/
public interface PricingStrategy {
    double calculatePrice(int hours, double baseRate, SpotType spotType);
}