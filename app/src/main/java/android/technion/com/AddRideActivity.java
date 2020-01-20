package android.technion.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.technion.com.ui.rides.RidesFragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddRideActivity extends AppCompatActivity {
    private TextInputEditText addRideNameText;
    private TextInputEditText addRidePhoneText;
    private TextInputEditText addRideDateText;
    private TextInputEditText addRideTimeText;
    private TextInputEditText addRideFromLocationText;
    private TextInputEditText addRideFromCity;
    private TextInputEditText addRideToLocationText;
    private TextInputEditText addRideToCity;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private Drive drive;
    private Event event;
    private TimePickerDialog timePicker;
    private DatePickerDialog datePicker;
    private Location userLastKnownLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private int locationRequestCode = 1000;
    private double ulamShakufLatitude=32.776437;
    private double ulamShakufLongitude=35.022515;
    private double safariLatitude=32.045335;
    private double safariongitude=34.822426;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ride);

        Database db = new Database();
        drive = (Drive) getIntent().getSerializableExtra("drive");
        event = (Event) getIntent().getSerializableExtra("event");

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
        addRideFromCity = new TextInputEditText(AddRideActivity.this);
        addRideToLocationText = findViewById(R.id.addRideToLocationText);
        addRideToCity = new TextInputEditText(AddRideActivity.this);

        addRideTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(AddRideActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String str;
                                if(sHour < 10 && sMinute < 10) {
                                    str = "0" + sHour + ":0" + sMinute;
                                } else if(sHour < 10 && sMinute >= 10){
                                    str = "0" + sHour + ":" + sMinute;
                                } else if(sHour >= 10 && sMinute < 10){
                                    str = sHour + ":0" + sMinute;
                                } else {
                                    str = sHour + ":" + sMinute;
                                }
                                addRideTimeText.setText(str);
                            }
                        }, hour, minutes, true);
                timePicker.show();
            }
        });

        addRideDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(AddRideActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                addRideDateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });


        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            userLastKnownLocation = location;
                        }else {
                            locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationRequest.setInterval(20 * 1000);
                            locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if (locationResult == null) {
                                        return;
                                    }
                                    for (Location location : locationResult.getLocations()) {
                                        if (location != null) {
                                            userLastKnownLocation=location;
                                        }
                                    }
                                }
                            };
                        }
                    }
                });

        if(event != null){
            addRideFromLocationText.setText(event.getLocation());
        }
        addRideFromLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userLastKnownLocation!=null) {
                    ulamShakufLatitude=userLastKnownLocation.getLatitude();
                    ulamShakufLongitude=userLastKnownLocation.getLongitude();
                }
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(ulamShakufLatitude, ulamShakufLongitude)  // Initial Latitude and Longitude the Map will load into
                        .showLatLong(true)  // Show Coordinates in the Activity
                        .setMapZoom(18.0f)  // Map Zoom Level. Default: 14.0
                        .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                        .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                        .setMarkerDrawable(R.drawable.map_marker) // Change the default Marker Image
                        .setMarkerImageImageColor(R.color.colorPrimary)
                        .setFabColor(R.color.colorAccent)
                        .setPrimaryTextColor(R.color.colorPrimaryText) // Change text color of Shortened Address
                        .setSecondaryTextColor(R.color.colorSecondaryText) // Change text color of full Address
                        .setMapRawResourceStyle(R.raw.style_json)  //Set Map Style (https://mapstyle.withgoogle.com/)
                        .setMapType(MapType.NORMAL)
                        .build(AddRideActivity.this);

                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });

        addRideToLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(safariLatitude, safariongitude)  // Initial Latitude and Longitude the Map will load into
                        .showLatLong(true)  // Show Coordinates in the Activity
                        .setMapZoom(18.0f)  // Map Zoom Level. Default: 14.0
                        .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                        .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                        .setMarkerDrawable(R.drawable.map_marker) // Change the default Marker Image
                        .setMarkerImageImageColor(R.color.colorPrimary)
                        .setFabColor(R.color.colorAccent)
                        .setPrimaryTextColor(R.color.colorPrimaryText) // Change text color of Shortened Address
                        .setSecondaryTextColor(R.color.colorSecondaryText) // Change text color of full Address
                        .setMapRawResourceStyle(R.raw.style_json)  //Set Map Style (https://mapstyle.withgoogle.com/)
                        .setMapType(MapType.NORMAL)
                        .build(AddRideActivity.this);

                startActivityForResult(intent, 101);
            }
        });


        // in case of EDITING an existing ride
        if(drive != null){
            addRideNameText.setText(drive.getDriverFullName());
            addRidePhoneText.setText(drive.getDriverPhoneNumber());
            addRideDateText.setText(drive.getDate());
            addRideTimeText.setText(drive.getTime());
            addRideFromLocationText.setText(drive.getFromLocation());
            addRideFromCity.setText(drive.getFromCity());
            addRideToLocationText.setText(drive.getToLocation());
            addRideToCity.setText(drive.getToCity());
        } else {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                db.getUserToTextViews(currentUser.getUid(), addRideNameText, new TextView(AddRideActivity.this), addRidePhoneText);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                if(!addressData.getAddressList().isEmpty()) {
                    addRideFromLocationText.setText(addressData.getAddressList().get(0).getAddressLine(0));
                    addRideFromCity.setText(addressData.getAddressList().get(0).getLocality());
                }
            }
        }
        else if(requestCode == 101){
            if (resultCode == Activity.RESULT_OK && data != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                if(!addressData.getAddressList().isEmpty()) {
                    addRideToLocationText.setText(addressData.getAddressList().get(0).getAddressLine(0));
                    addRideToCity.setText(addressData.getAddressList().get(0).getLocality());
                }
            }
        }
    }

    public boolean onSendRide(MenuItem item) {
        String name = addRideNameText.getText().toString();
        String phone = addRidePhoneText.getText().toString();
        String date = addRideDateText.getText().toString();
        String time = addRideTimeText.getText().toString();
        String fromLocation = addRideFromLocationText.getText().toString();
        String fromCity = addRideFromCity.getText().toString();
        String toLocation = addRideToLocationText.getText().toString();
        String toCity = addRideToCity.getText().toString();
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

        Drive newRide = new Drive(currentUser.getUid(), currentUser.getPhotoUrl().toString(),
                name, phone, fromLocation, fromCity, toLocation, toCity, date, time);

        // in case of adding ride to specific event
        if(event != null){
            newRide.setEventID(event.getDatabaseID());
        }

        if(drive != null) {
            db.updateDriveInDatabase(drive, newRide);
            toast = Toast.makeText(getApplicationContext(), "Ride Updated!", Toast.LENGTH_SHORT);
        } else {
            db.addDriveToDatabase(newRide);
            toast = Toast.makeText(getApplicationContext(), "Ride Sent!", Toast.LENGTH_SHORT);
        }
        toast.show();

        Intent intent;
        if(event != null){
            intent = new Intent(AddRideActivity.this, PickUpActivity.class);
            intent.putExtra("event", event);
        } else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                ulamShakufLatitude = location.getLatitude();
                                ulamShakufLongitude = location.getLongitude();
                                userLastKnownLocation = location;
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private boolean isValidPhone(String phone) {
        String PHONE_PATTERN = "^\\d{9,10}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
