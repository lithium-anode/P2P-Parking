package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import parking.project.model.ParkingSpot;
import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    // Supports Driver search functionality
    List<ParkingSpot> findByLocationContainingAndIsAvailableTrue(String location);
    List<ParkingSpot> findByPricePerHourLessThanEqual(double maxPrice);

    // Owner management helpers
    List<ParkingSpot> findByOwnerId(Long ownerId);
}
