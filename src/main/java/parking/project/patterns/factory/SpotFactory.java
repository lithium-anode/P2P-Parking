package parking.project.patterns.factory;

import parking.project.model.ParkingSpot;
import parking.project.model.User;

public class SpotFactory {
    // Standardizes how SpaceOwners add new spots to the system
    public static ParkingSpot createSpot(String type, String location, double price, User owner) {
        ParkingSpot spot = new ParkingSpot();
        spot.setOwner(owner);
        spot.setLocation(location);
        spot.setIsAvailable(true); // New spots are available by default
        
        switch (type.toUpperCase()) {
            case "EV":
                spot.setSpotType("EV_CHARGING");
                spot.setPricePerHour(price + 5.0); // Premium for EV
                break;
            case "PREMIUM":
                spot.setSpotType("PREMIUM");
                spot.setPricePerHour(price * 1.5);
                break;
            default:
                spot.setSpotType("STANDARD");
                spot.setPricePerHour(price);
        }
        return spot;
    }
}