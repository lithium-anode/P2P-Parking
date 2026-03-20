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
}