package parking.project.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("DRIVER")
@Data
@EqualsAndHashCode(callSuper = true)
public class Driver extends User {
    private String vehicleNumber; // Specific to the Driver role
    private String licenseNumber;
    private String vehicleModel;
}