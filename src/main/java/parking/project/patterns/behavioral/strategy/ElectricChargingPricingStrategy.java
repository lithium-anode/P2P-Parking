package parking.project.patterns.behavioral.strategy;

import parking.project.model.enums.SpotType;

/**
 * [Design Pattern: Strategy - Concrete Strategy]
 * [Goal Alignment: Specific Vehicle Requirements]
 * Handles specialized pricing for ELECTRIC_CHARGING spots, which may 
 * include infrastructure fees.
 */
public class ElectricChargingPricingStrategy implements PricingStrategy {
    private static final double CHARGING_FEE = 5.00;

    @Override
    public double calculatePrice(int hours, double baseRate, SpotType spotType) {
        double base = hours * baseRate;
        return (spotType == SpotType.ELECTRIC_CHARGING) ? base + CHARGING_FEE : base;
    }
}