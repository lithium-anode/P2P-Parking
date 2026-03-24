package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Booking;
import parking.project.model.BookingStatus;
import parking.project.model.User;
import parking.project.model.ParkingSpot;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings by driver
    List<Booking> findByDriver(User driver);

    // Find bookings by parking spot
    List<Booking> findBySpot(ParkingSpot spot);

    // Find active bookings for a spot within a time range
    List<Booking> findBySpotAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
        ParkingSpot spot, LocalDateTime endTime, LocalDateTime startTime);

    // Find bookings by status
    List<Booking> findByStatus(BookingStatus status);
}