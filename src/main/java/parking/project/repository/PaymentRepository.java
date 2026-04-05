package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parking.project.model.Payment;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Supports finding a specific payment record for a booking 
    Optional<Payment> findByBookingId(Long bookingId);

    // Essential for Member C: Allows Drivers to view their personal payment history [cite: 30, 45]
    List<Payment> findByBookingDriverId(Long driverId);

    // Essential for Member B: Allows Space Owners to view earnings from their specific spots 
    List<Payment> findByBookingSpotOwnerId(Long ownerId);

    // Custom query to calculate total earnings for a specific Space Owner [cite: 22, 30]
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.booking.spot.owner.id = :ownerId AND p.status = 'PAID'")
    Double getTotalEarningsByOwner(@Param("ownerId") Long ownerId);
}