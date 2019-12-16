package android.technion.com;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Event {
    String collection="Events";

    String eventID;
    String location;
    String reporterId;
    List<Drive> drives;
    List<Foster> fosters;

    public Event(String eventID,String location, String reporterId) {
        this.eventID=eventID;
        this.location=location;
        this.reporterId=reporterId;
        this.drives=new ArrayList<>();
        this.fosters=new ArrayList<>();
    }
}
