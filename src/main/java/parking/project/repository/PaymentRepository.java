package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parking.project.model.Payment;
import java.time.LocalDateTime;
import java.util.List;

/**
 * [GRASP: Information Expert]
 * The expert for financial transaction records.
 *
 * [Goal Alignment: Usage Reports]
 * Provides the data needed for Administrators to oversee system operations 
 * and generate periodic financial reports.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Finds payments within a specific timeframe for reporting.
     */
    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * [Goal Alignment: Tracking Earnings]
     * Custom query to find all payments associated with a specific Space Owner's 
     * spots to calculate their total revenue.
     */
    @Query("SELECT p FROM Payment p WHERE p.booking.parkingSpot.owner.id = :ownerId")
    List<Payment> findByOwnerId(@Param("ownerId") Long ownerId);
}