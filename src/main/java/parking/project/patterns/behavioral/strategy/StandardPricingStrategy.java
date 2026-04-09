package parking.project.patterns.behavioral.strategy;

import parking.project.model.enums.SpotType;

/*
    [Design Pattern: Behavioral - Strategy]
    Goal: Specific Vehicle Requirements
    Implements a basic linear pricing model.
*/
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(int hours, double baseRate, SpotType spotType) {
        return hours * baseRate;
    }
}