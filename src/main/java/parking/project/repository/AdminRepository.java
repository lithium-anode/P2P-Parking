package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Admin;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Supports the Administrator's role in overseeing system operations
    Optional<Admin> findByUsername(String username);
    
    // Can be extended later for specific admin search criteria (e.g., by Employee ID)
    Optional<Admin> findByEmployeeId(String employeeId);
}