package android.technion.com;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Foster implements Serializable {
    final String collection="Fosters";

    String databaseID;
    String fosterId;
    String fosterProfilePicUri;
    String fosterFullName;
    String fosterPhoneNumber;
    String location;
    String fromDate;
    String fromTime;
    String untilDate;
    String untilTime;
    String eventID;

    public Foster(String fosterId, String fosterProfilePicUri, String fosterFullName,
                  String fosterPhoneNumber, String location,  String fromDate, String fromTime,
                  String untilDate, String untilTime, String eventID){

        this.databaseID = "";
        this.fosterId = fosterId;
        this.fosterProfilePicUri = fosterProfilePicUri;
        this.fosterFullName = fosterFullName;
        this.fosterPhoneNumber = fosterPhoneNumber;
        this.location = location;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.untilDate = untilDate;
        this.untilTime = untilTime;
        this.eventID = eventID;
    }
}
