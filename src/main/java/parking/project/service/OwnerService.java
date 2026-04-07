package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.ParkingSpot;
// import parking.project.model.SpaceOwner;
import parking.project.repository.ParkingSpotRepository;
import parking.project.repository.PaymentRepository;
import parking.project.repository.SpaceOwnerRepository;
import java.util.List;

@Service
public class OwnerService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final SpaceOwnerRepository spaceOwnerRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public OwnerService(ParkingSpotRepository parkingSpotRepository, 
                        SpaceOwnerRepository spaceOwnerRepository,
                        PaymentRepository paymentRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.spaceOwnerRepository = spaceOwnerRepository;
        this.paymentRepository = paymentRepository;
    }

    // Requirement: Manage books (Parking Spots) - Add/Update
    public ParkingSpot addNewSpot(ParkingSpot spot) {
        spot.setIsAvailable(true); // Default to available
        return parkingSpotRepository.save(spot);
    }

    public void updateSpotPrice(Long spotId, double newPrice) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
            .orElseThrow(() -> new RuntimeException("Spot not found"));
        spot.setPricePerHour(newPrice);
        parkingSpotRepository.save(spot);
    }

    // Requirement: Monitor transactions and calculate/view earnings
    public Double calculateTotalEarnings(Long ownerId) {
        Double earnings = paymentRepository.getTotalEarningsByOwner(ownerId);
        return (earnings != null) ? earnings : 0.0;
    }

    public List<ParkingSpot> getOwnerSpots(Long ownerId) {
        // This assumes a custom finder method in ParkingSpotRepository
        return parkingSpotRepository.findAll().stream()
            .filter(spot -> spot.getOwner().getId().equals(ownerId))
            .toList();
    }
}