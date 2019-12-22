package android.technion.com;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {
    String collection="Events";

    String eventID;
    String location;
    String reporterId;
    List<Drive> drives;
    List<Foster> fosters;

}
