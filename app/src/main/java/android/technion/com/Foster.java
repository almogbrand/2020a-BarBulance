package android.technion.com;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Foster {
    String collection="Fosters";
    String location;
    double timeThatCanFosterHours;
    String userID;

}
