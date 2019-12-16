package android.technion.com;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Drive {
    String collection="Drives";

    //add hour////
    String driverID;
    String driverPhoneNumber;
    String fromLocation;
    String toLocation;

    public Drive(String driverID, String phone, String from, String to) {
        this.driverID=driverID;
        this.driverPhoneNumber=phone;
        this.fromLocation=from;
        this.toLocation=to;
    }
}
