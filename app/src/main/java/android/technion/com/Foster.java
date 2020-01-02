package android.technion.com;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Foster {
    final String collection="Fosters";

    String fosterDbId;
    String location;
    double timeThatCanFosterHours;
    String userID;

}
