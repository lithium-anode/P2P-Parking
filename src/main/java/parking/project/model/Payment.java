package parking.project.model;

import jakarta.persistence.*;
import parking.project.model.enums.PaymentMethod;

import java.time.LocalDateTime;

/**
 * [GRASP: Information Expert]
 * This class is the Information Expert for financial transactions. It knows 
 * the amount paid, the payment timestamp, and the associated booking.
 *
 * [Goal Alignment: Earnings Tracking]
 * Directly supports Space Owners in tracking their earnings  
 * and provides data for Administrators to generate usage reports.
 */
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    public Payment() { }

    public Payment(Booking booking, double amount, PaymentMethod paymentMethod) {
        this.booking = booking;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}