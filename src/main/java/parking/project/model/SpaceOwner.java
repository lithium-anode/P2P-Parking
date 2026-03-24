package parking.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Entity
@DiscriminatorValue("OWNER")
@Data
@EqualsAndHashCode(callSuper = true)
public class SpaceOwner extends User {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ParkingSpot> parkingSpots;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Payment> earnings;

    private double totalEarnings = 0.0;

    // Constructor
    public SpaceOwner() {}

    public SpaceOwner(String username, String email) {
        this.setUsername(username);
        this.setEmail(email);
        this.setRole("OWNER");
        this.totalEarnings = 0.0;
    }

    /**
     * Add a parking spot
     */
    public ParkingSpot addParkingSpot(String location, String address, double hourlyRate) {
        ParkingSpot spot = new ParkingSpot(location, address, hourlyRate, this);
        if (this.parkingSpots == null) {
            this.parkingSpots = new java.util.ArrayList<>();
        }
        this.parkingSpots.add(spot);
        return spot;
    }

    /**
     * Update a parking spot
     */
    public void updateParkingSpot(Long spotId, String location, String address, double hourlyRate) {
        if (this.parkingSpots != null) {
            for (ParkingSpot spot : this.parkingSpots) {
                if (spot.getId().equals(spotId)) {
                    spot.setLocation(location);
                    spot.setAddress(address);
                    spot.setHourlyRate(hourlyRate);
                    break;
                }
            }
        }
    }

    /**
     * Remove a parking spot
     */
    public void removeParkingSpot(Long spotId) {
        if (this.parkingSpots != null) {
            this.parkingSpots.removeIf(spot -> spot.getId().equals(spotId));
        }
    }

    /**
     * View earnings
     */
    public String viewEarnings() {
        StringBuilder earningsInfo = new StringBuilder();
        earningsInfo.append("=") .append("=").append("=").append("=").append("=").append("=").append(" EARNINGS REPORT ").append("=").append("=").append("=").append("=").append("=").append("=").append("\\n");
        earningsInfo.append("Owner: ").append(this.getUsername()).append("\\n");
        earningsInfo.append("Total Earnings: $").append(String.format("%.2f", this.totalEarnings)).append("\\n");
        
        if (this.earnings != null && !this.earnings.isEmpty()) {
            earningsInfo.append("\\nRecent Transactions:\\n");
            for (Payment payment : this.earnings) {
                earningsInfo.append("  - $").append(String.format("%.2f", payment.getAmount()))
                    .append(" on ").append(payment.getPaymentDate()).append("\\n");
            }
        } else {
            earningsInfo.append("No transactions yet.\\n");
        }
        
        return earningsInfo.toString();
    }
}
