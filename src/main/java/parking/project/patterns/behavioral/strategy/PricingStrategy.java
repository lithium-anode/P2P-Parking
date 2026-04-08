package parking.project.patterns.behavioral.strategy;

import parking.project.model.enums.SpotType;

/**
 * [Design Pattern: Strategy - Strategy Interface]
 * Declares an interface common to all supported pricing algorithms.
 * * [GRASP: Low Coupling]
 * The ParkingSpot class remains decoupled from specific pricing calculations, 
 * allowing new models (like 'Holiday' or 'Early Bird') to be added easily.
 */
public interface PricingStrategy {
    /**
     * [Goal Alignment: Flexible Pricing]
     * Calculates the total cost based on duration, base rate, and the type of spot.
     */
    double calculatePrice(int hours, double baseRate, SpotType spotType);
}