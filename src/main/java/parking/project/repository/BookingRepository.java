package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Booking;
import parking.project.model.enums.BookingStatus;
import java.util.List;

/*
    [GRASP: Information Expert]
    The expert for managing the persistence and retrieval of booking records 
    and their associated status (PENDING, CONFIRMED, etc.).
*/
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Retrieves all bookings for a specific Driver to show their reservation history.
    List<Booking> findByDriverId(Long driverId);

    // Helps identify if there are active bookings for a spot to prevent double-bookings.
    List<Booking> findByParkingSpotIdAndStatus(Long spotId, BookingStatus status);
}