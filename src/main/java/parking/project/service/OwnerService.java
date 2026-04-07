package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parking.project.model.ParkingSpot;
import parking.project.model.User;
import parking.project.repository.ParkingSpotRepository;
import parking.project.repository.PaymentRepository;
import parking.project.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OwnerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public ParkingSpot addParkingSpot(Long ownerId, ParkingSpot spotRequest) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found."));

        if (!"OWNER".equals(owner.getRole())) {
            throw new IllegalArgumentException("User is not a space owner.");
        }

        ParkingSpot spot = new ParkingSpot();
        spot.setOwner(owner);
        spot.setLocation(spotRequest.getLocation());
        spot.setPricePerHour(spotRequest.getPricePerHour());
        spot.setSpotType(spotRequest.getSpotType());
        spot.setIsAvailable(spotRequest.getIsAvailable());

        return parkingSpotRepository.save(spot);
    }

    public ParkingSpot updateParkingSpot(Long ownerId, Long spotId, ParkingSpot spotRequest) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found."));

        if (spot.getOwner() == null || !ownerId.equals(spot.getOwner().getId())) {
            throw new IllegalArgumentException("Owner does not own this parking spot.");
        }

        if (spotRequest.getLocation() != null) {
            spot.setLocation(spotRequest.getLocation());
        }
        if (spotRequest.getPricePerHour() > 0) {
            spot.setPricePerHour(spotRequest.getPricePerHour());
        }
        if (spotRequest.getSpotType() != null) {
            spot.setSpotType(spotRequest.getSpotType());
        }
        spot.setIsAvailable(spotRequest.getIsAvailable());

        return parkingSpotRepository.save(spot);
    }

    @Transactional
    public void removeParkingSpot(Long ownerId, Long spotId) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found."));

        if (spot.getOwner() == null || !ownerId.equals(spot.getOwner().getId())) {
            throw new IllegalArgumentException("Owner does not own this parking spot.");
        }

        parkingSpotRepository.delete(spot);
    }

    public List<ParkingSpot> getOwnerSpots(Long ownerId) {
        return parkingSpotRepository.findByOwnerId(ownerId);
    }

    public Map<String, Object> viewEarnings(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found."));

        if (!"OWNER".equals(owner.getRole())) {
            throw new IllegalArgumentException("User is not a space owner.");
        }

        Double totalEarnings = paymentRepository.getTotalEarningsByOwner(ownerId);
        if (totalEarnings == null) {
            totalEarnings = 0.0;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ownerId", ownerId);
        response.put("ownerName", owner.getUsername());
        response.put("totalEarnings", totalEarnings);
        response.put("spotCount", getOwnerSpots(ownerId).size());
        return response;
    }

    // Legacy method for compatibility
    public ParkingSpot addNewSpot(ParkingSpot spot) {
        spot.setIsAvailable(true); // Default to available
        return parkingSpotRepository.save(spot);
    }

    public Double calculateTotalEarnings(Long ownerId) {
        Double earnings = paymentRepository.getTotalEarningsByOwner(ownerId);
        return (earnings != null) ? earnings : 0.0;
    }
}