package parking.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED
}

@Entity
@Table(name = "bookings")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver; 

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot spot; 

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
