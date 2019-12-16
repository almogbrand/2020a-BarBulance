package android.technion.com;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Foster {
    String collection="Fosters";

    //remove event id
    String eventID;
    //remove species as well
    String animalSpecies;


    String location;
    double timeThatCanFosterHours;
    String userID;

    public Foster(String EventId, String animalSpecies, String location, double timeThatCanFosterHours, String userID ) {
        this.eventID=EventId;
        this.animalSpecies=animalSpecies;
        this.location=location;
        this.timeThatCanFosterHours=timeThatCanFosterHours;
        this.userID=userID;
    }
}
