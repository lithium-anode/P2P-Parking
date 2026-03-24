package parking.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private SpaceOwner owner;

    private double amount;
    private LocalDateTime paymentDate;
    private String paymentStatus; // PENDING, COMPLETED, FAILED

    // Constructor
    public Payment() {}

    public Payment(Booking booking, SpaceOwner owner, double amount) {
        this.booking = booking;
        this.owner = owner;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.paymentStatus = "COMPLETED";
    }
}
