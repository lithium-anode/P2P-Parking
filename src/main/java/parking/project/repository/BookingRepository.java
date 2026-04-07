package parking.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parking.project.model.Booking;
import parking.project.model.ParkingSpot;
import parking.project.model.BookingStatus;
import parking.project.model.User;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Finds active bookings for a spot to help verify availability
    List<Booking> findBySpotAndStatus(ParkingSpot spot, BookingStatus status);

    // Find bookings by driver
    List<Booking> findByDriver(User driver);

    // Find active bookings by driver
    List<Booking> findByDriverAndStatus(User driver, BookingStatus status);
}