package parking.project.model;

import jakarta.persistence.Entity;
import java.util.Objects;

@Entity
public class Driver extends User {
    private String vehicleNumber; // Specific to the Driver role
    private String licenseNumber;
    private String vehicleModel;

    public String getVehicleNumber() { return vehicleNumber; }

    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getLicenseNumber() { return licenseNumber; }

    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getVehicleModel() { return vehicleModel; }

    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        if (!super.equals(o)) return false;
        Driver driver = (Driver) o;
        return Objects.equals(vehicleNumber, driver.vehicleNumber)
            && Objects.equals(licenseNumber, driver.licenseNumber)
            && Objects.equals(vehicleModel, driver.vehicleModel);
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof Driver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vehicleNumber, licenseNumber, vehicleModel);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "vehicleNumber='" + vehicleNumber + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", " + super.toString() +
                '}';
    }
}