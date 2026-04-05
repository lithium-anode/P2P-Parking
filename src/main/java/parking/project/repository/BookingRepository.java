package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Booking;
import parking.project.model.ParkingSpot;
import parking.project.model.BookingStatus;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Finds active bookings for a spot to help verify availability
    List<Booking> findBySpotAndStatus(ParkingSpot spot, BookingStatus status);
}