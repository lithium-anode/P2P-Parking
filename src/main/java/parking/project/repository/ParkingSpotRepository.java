package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.model.enums.SpotType;
import java.util.List;

/*
    [GRASP: Information Expert]
    The expert for querying parking listings based on the criteria provided 
    by Drivers (location, price, and spot type).
*/
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    // Finds spots in a specific location as requested by Drivers.
    List<ParkingSpot> findByLocationContainingIgnoreCase(String location);

    // Finds spots that are within a specific budget (hourly rate).
    List<ParkingSpot> findByHourlyRateLessThanEqual(double maxRate);

    // Finds spots that match a specific type
    List<ParkingSpot> findBySpotType(SpotType spotType);

    // Finds all spots owned by a specific Space Owner.
    List<ParkingSpot> findByOwner(SpaceOwner owner);

    // Finds all *active* spots owned by a specific Space Owner.
    List<ParkingSpot> findByOwnerAndActiveTrue(SpaceOwner owner);

    // Finds all *active* spots in a specific location as requested by Drivers.
    List<ParkingSpot> findByLocationContainingIgnoreCaseAndActiveTrue(String location);

    // Finds all *active* spots that match a specific type.
    List<ParkingSpot> findBySpotTypeAndActiveTrue(SpotType spotType);
}