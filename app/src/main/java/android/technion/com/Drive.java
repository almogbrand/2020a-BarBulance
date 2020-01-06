package android.technion.com;

import android.net.Uri;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Drive implements Serializable {
    final String collection="Drives";

    String driveDbId;
    String driverProfilePicUri;
    String driverFullName;
    String driverPhoneNumber;
    String fromLocation;
    String toLocation;
    String date;
    String time;
}
