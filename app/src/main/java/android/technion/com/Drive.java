package android.technion.com;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Drive {
   final String collection="Drives";

    String driveDbId;
    String driverID;
    String driverPhoneNumber;
    String fromLocation;
    String toLocation;
    String dateOfRide;

}
