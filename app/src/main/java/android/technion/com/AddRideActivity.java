package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddRideActivity extends AppCompatActivity {
    private TextInputEditText addRideNameText;
    private TextInputEditText addRidePhoneText;
    private TextInputEditText addRideDateText;
    private TextInputEditText addRideTimeText;
    private TextInputEditText addRideFromLocationText;
    private TextInputEditText addRideToLocationText;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private Drive drive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ride);

        drive = (Drive) getIntent().getSerializableExtra("drive");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        if(drive != null){
            toolbar.setTitle("Edit Ride");
        } else {
            toolbar.setTitle(R.string.add_ride);
        }
        toolbar.inflateMenu(R.menu.send_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onSendRide(item);
            }
        });

        addRideNameText = findViewById(R.id.addRideNameText);
        addRidePhoneText = findViewById(R.id.addRidePhoneText);
        addRideDateText = findViewById(R.id.addRideDateText);
        addRideTimeText = findViewById(R.id.addRideTimeText);
        addRideFromLocationText = findViewById(R.id.addRideFromLocationText);
        addRideToLocationText = findViewById(R.id.addRideToLocationText);

        // in case of EDITING an existing ride
        if(drive != null){
            addRideNameText.setText(drive.getDriverFullName());
            addRidePhoneText.setText(drive.getDriverPhoneNumber());
            addRideDateText.setText(drive.getDate());
            addRideTimeText.setText(drive.getTime());
            addRideFromLocationText.setText(drive.getFromLocation());
            addRideToLocationText.setText(drive.getToLocation());
        }
    }

    public boolean onSendRide(MenuItem item) {
        String name = addRideNameText.getText().toString();
        String phone = addRidePhoneText.getText().toString();
        String date = addRideDateText.getText().toString();
        String time = addRideTimeText.getText().toString();
        String fromLocation = addRideFromLocationText.getText().toString();
        String toLocation = addRideToLocationText.getText().toString();
        Toast toast;
        if(name.isEmpty() || phone.isEmpty() || date.isEmpty() || time.isEmpty()
                || fromLocation.isEmpty() || toLocation.isEmpty()){
            if(name.isEmpty()){
                addRideNameText.setError("Required");
            }
            if(phone.isEmpty()) {
                addRidePhoneText.setError("Required");
            }
            if(date.isEmpty()) {
                addRideDateText.setError("Required");
            }
            if(time.isEmpty()){
                addRideTimeText.setError("Required");
            }
            if(fromLocation.isEmpty()){
                addRideFromLocationText.setError("Required");
            }
            if(toLocation.isEmpty()){
                addRideToLocationText.setError("Required");
            }

            toast = Toast.makeText(getApplicationContext(),"Required fields are empty!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        if (!isValidPhone(phone)) {
            addRidePhoneText.setError("Invalid phone number!");
            return true;
        }

        Database db = new Database();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Drive newRide = new Drive(currentUser.getUid(),currentUser.getPhotoUrl().toString(),
                name, phone, fromLocation, toLocation, date, time);
        db.addDriveToDatabase(newRide);

        if(drive != null) {
            db.removeDriveFromDataBase(drive.getDriveDbId());
            toast = Toast.makeText(getApplicationContext(), "Ride Updated!", Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(getApplicationContext(), "Ride Sent!", Toast.LENGTH_SHORT);
        }
        toast.show();

        Intent intent = new Intent(AddRideActivity.this, MainActivity.class);
        this.finish();
        startActivity(intent);

        return true;
    }

    private boolean isValidPhone(String phone) {
        String PHONE_PATTERN = "^\\d{9,10}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
