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
    final String collection = "Events";

    String databaseID;
    String location;
    String locationCity;
    String reporterId;
    String phoneNumber;
    String animalType;
    String description;
    Boolean urgent;
    String photoID;
    List<Drive> drives;
    List<Foster> fosters;

    public Event(String location, String locationCity, String reporterId, String phoneNumber, String animalType , String description, Boolean urgent, String photoID) {
        this.databaseID="";
        this.location = location;
        this.locationCity = locationCity;
        this.reporterId = reporterId;
        this.animalType = animalType;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.urgent = urgent;
        this.photoID = photoID;
        this.drives = new ArrayList<>();
        this.fosters = new ArrayList<>();
    }
}
