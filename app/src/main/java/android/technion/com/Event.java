package android.technion.com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event implements Serializable {
    final String collection="Events";

    String location;
    String reporterId;
    String reporterPhoneNumber;
    String animalType;
    String description;
    Boolean urgent;
    String photoID;
    List<Drive> drives;
    List<Foster> fosters;

    public Event(String location, String reporterId, String reporterPhoneNumber, String animalType , String description, Boolean urgent) {
        this.location = location;
        this.reporterId = reporterId;
        this.animalType = animalType;
        this.reporterPhoneNumber=reporterPhoneNumber;
        this.description = description;
        this.urgent = urgent;
        this.drives = new ArrayList<>();
        this.fosters = new ArrayList<>();
    }

}
