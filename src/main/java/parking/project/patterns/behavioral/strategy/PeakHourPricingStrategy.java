package parking.project.patterns.behavioral.strategy;

import parking.project.model.enums.SpotType;

/**
 * [Design Pattern: Strategy - Concrete Strategy]
 * Implements a premium pricing model for high-demand periods.
 */
public class PeakHourPricingStrategy implements PricingStrategy {
    private static final double PEAK_SURCHARGE = 1.5; // 50% increase

    @Override
    public double calculatePrice(int hours, double baseRate, SpotType spotType) {
        return hours * baseRate * PEAK_SURCHARGE;
    }
}