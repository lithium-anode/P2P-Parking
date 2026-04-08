package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Driver;
import parking.project.model.enums.VehicleType;
import java.util.List;

/**
 * [GRASP: Information Expert]
 * Expert for Driver-specific data, including vehicle types and reservation history.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    /**
     * Finds drivers who own a specific vehicle type, useful for targeted 
     * system notifications or analytics.
     */
    List<Driver> findByVehicleTypesContaining(VehicleType vehicleType);
}