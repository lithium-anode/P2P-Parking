package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import parking.project.model.Driver;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUsername(String username);
}