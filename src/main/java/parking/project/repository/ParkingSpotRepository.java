package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.ParkingSpot;
import parking.project.model.SpaceOwner;
import parking.project.model.enums.SpotType;
import java.util.List;

/**
 * [GRASP: Information Expert]
 * The expert for querying parking listings based on the criteria provided 
 * by Drivers (location, price, and spot type). 
 */
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    /**
     * [Goal Alignment: Driver Search Criteria]
     * Finds spots in a specific location as requested by Drivers. 
     */
    List<ParkingSpot> findByLocationContainingIgnoreCase(String location);

    /**
     * [Goal Alignment: Driver Search Criteria]
     * Finds spots that are within a specific budget (hourly rate). 
     */
    List<ParkingSpot> findByHourlyRateLessThanEqual(double maxRate);

    /**
     * [Goal Alignment: Vehicle Compatibility]
     * Finds spots that match a specific type, such as ELECTRIC_CHARGING 
     * for electric vehicles. 
     */
    List<ParkingSpot> findBySpotType(SpotType spotType);

    List<ParkingSpot> findByOwner(SpaceOwner owner);

    List<ParkingSpot> findByOwnerAndActiveTrue(SpaceOwner owner);

    List<ParkingSpot> findByLocationContainingIgnoreCaseAndActiveTrue(String location);

    List<ParkingSpot> findBySpotTypeAndActiveTrue(SpotType spotType);
}