package android.technion.com;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    String userEmail;
    String userPhoneNumber;
    String UID;
}
