package parking.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parking.project.model.Driver;
import parking.project.repository.DriverRepository;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    public Driver getDriverProfile(String username) {
        return driverRepository.findByUsername(username).orElse(null);
    }

    public void updateVehicleDetails(Long id, String vehicleNo) {
        Driver driver = driverRepository.findById(id).orElseThrow();
        driver.setVehicleNumber(vehicleNo);
        driverRepository.save(driver);
    }

    public void updateAllVehicleDetails(Long driverId, String licenseNumber, String vehicleModel, String vehicleNumber) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        
        if (licenseNumber != null && !licenseNumber.isBlank()) {
            driver.setLicenseNumber(licenseNumber);
        }
        if (vehicleModel != null && !vehicleModel.isBlank()) {
            driver.setVehicleModel(vehicleModel);
        }
        if (vehicleNumber != null && !vehicleNumber.isBlank()) {
            driver.setVehicleNumber(vehicleNumber);
        }
        
        driverRepository.save(driver);
    }
}