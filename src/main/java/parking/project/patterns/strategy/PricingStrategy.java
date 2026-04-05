package parking.project.patterns.strategy;

// The Strategy Interface
public interface PricingStrategy {
    double calculatePrice(double baseRate, long durationInMinutes);
}

// Concrete Strategy: Standard Hourly Pricing
class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseRate, long durationInMinutes) {
        double hours = Math.ceil(durationInMinutes / 60.0);
        return baseRate * hours;
    }
}

// Concrete Strategy: Flat Rate (e.g., for short stays)
class FlatRateStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseRate, long durationInMinutes) {
        return baseRate; // Minimum one-hour charge
    }
}