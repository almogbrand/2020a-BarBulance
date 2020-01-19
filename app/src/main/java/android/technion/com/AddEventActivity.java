package android.technion.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.technion.com.ui.events.EventsFragment;
import android.technion.com.ui.user.UserFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static androidx.core.content.FileProvider.getUriForFile;

public class AddEventActivity extends AppCompatActivity {
    private TextInputEditText addEventNameText;
    private TextInputEditText addEventPhoneText;
    private TextInputEditText addEventLocationText;
    private TextInputEditText addEventLocationCity;

    private Location userLastKnownLocation;
    private FusedLocationProviderClient fusedLocationClient;

    private TextInputLayout addEventAnimalType;
    private TextInputEditText addEventDescriptionText;
    private Switch addEventUrgentSwitch;
    private boolean urgent;
    private FloatingActionButton addEventFab;
    private FloatingActionButton addEventFabGallery;
    private FloatingActionButton addEventFabCamera;
    private ImageView addEventImage;
    private static final int GALLERY = 1, CAMERA = 2;
    private String currentPhotoPath;
    private String imageName = "";
    private Event event;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Database db = new Database();
        event = (Event) getIntent().getSerializableExtra("event");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        if(event != null){
            toolbar.setTitle(R.string.edit_event);
        } else {
            toolbar.setTitle(R.string.add_event);
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
                return onSendEvent(item);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.animals_array, R.layout.add_event_dropdown_item);
        AutoCompleteTextView acTextView = findViewById(R.id.filled_exposed_dropdown);
        acTextView.setAdapter(adapter);

        addEventNameText = findViewById(R.id.addEventNameText);
        addEventPhoneText = findViewById(R.id.addEventPhoneText);
        addEventLocationText = findViewById(R.id.addEventLocationText);
        addEventLocationCity = new TextInputEditText(AddEventActivity.this);
        addEventAnimalType = findViewById(R.id.addEventAnimalType);
        addEventDescriptionText = findViewById(R.id.addEventDescriptionText);
        addEventUrgentSwitch = findViewById(R.id.addEventUrgentSwitch);
        addEventUrgentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    urgent = true;
                } else {
                    urgent = false;
                }
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            userLastKnownLocation = location;
                        }
                    }
                });

        addEventImage = findViewById(R.id.addEventImage);
        addEventFabGallery = findViewById(R.id.addEventFabGallery);
        addEventFabCamera = findViewById(R.id.addEventFabCamera);
        addEventFab = findViewById(R.id.addEventFab);
        addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addEventFabGallery.getVisibility() == View.VISIBLE){
                    addEventFabGallery.hide();
                    addEventFabCamera.hide();
                } else {
                    addEventFabGallery.show();
                    addEventFabCamera.show();
                }
            }
        });

        addEventFabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallary();
            }
        });

        addEventLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude=32.109333;
                double longitude=34.855499;
                if(userLastKnownLocation!=null) {
                    latitude=userLastKnownLocation.getLatitude();
                    longitude=userLastKnownLocation.getLongitude();
                }
                    Intent intent = new PlacePicker.IntentBuilder()
                            .setLatLong(latitude, longitude)  // Initial Latitude and Longitude the Map will load into
                            .showLatLong(true)  // Show Coordinates in the Activity
                            .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
                            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                            .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                            .setMarkerDrawable(R.drawable.map_marker) // Change the default Marker Image
                            .setMarkerImageImageColor(R.color.colorPrimary)
                            .setFabColor(R.color.colorAccent)
                            .setPrimaryTextColor(R.color.colorPrimaryText) // Change text color of Shortened Address
                            .setSecondaryTextColor(R.color.colorSecondaryText) // Change text color of full Address
                            .setMapRawResourceStyle(R.raw.style_json)  //Set Map Style (https://mapstyle.withgoogle.com/)
                            .setMapType(MapType.NORMAL)
                            .build(AddEventActivity.this);

                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });

        addEventFabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });

        // in case of EDITING an existing event
        if(event != null){
            addEventNameText.setText(event.getReporterId());
            addEventPhoneText.setText(event.getPhoneNumber());
            addEventLocationText.setText(event.getLocation());
            addEventLocationCity.setText(event.getLocationCity());
            addEventAnimalType.getEditText().setText(event.getAnimalType());
            addEventDescriptionText.setText(event.getDescription());
            addEventUrgentSwitch.setChecked(event.getUrgent());
            db.getImageFromDatabaseToImageView(addEventImage, event.getPhotoID());
        }
    }

    public boolean onSendEvent(MenuItem item) {
        String name = addEventNameText.getText().toString();
        String phone = addEventPhoneText.getText().toString();
        String location = addEventLocationText.getText().toString();
        String locationCity = addEventLocationCity.getText().toString();
        String animalType = addEventAnimalType.getEditText().getText().toString();
        String description = addEventDescriptionText.getText().toString();
        Toast toast;
        if(name.isEmpty() || phone.isEmpty() || location.isEmpty() || animalType.isEmpty()){
            if(name.isEmpty()){
                addEventNameText.setError("Required");
            }
            if(phone.isEmpty()) {
                addEventPhoneText.setError("Required");
            }
            if(location.isEmpty()) {
                addEventLocationText.setError("Required");
            }
            if(animalType.isEmpty()){
                addEventAnimalType.setError("Required");
            }

            toast = Toast.makeText(getApplicationContext(),"Required fields are empty!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        if (!isValidPhone(phone)) {
            addEventPhoneText.setError("Invalid phone number!");
            return true;
        }

        Event newEvent = new Event(location, locationCity, name, phone, animalType, description, urgent, imageName);
        Database db = new Database();
        db.addEventToDatabase(newEvent);
        if(!(imageName.isEmpty())){
            db.storeImageInDatabaseStorage(addEventImage, imageName);
        }
        if(event != null) {
            db.removeEventFromDataBase(event);
            toast = Toast.makeText(getApplicationContext(), "Event Updated!", Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(getApplicationContext(), "Event Sent!", Toast.LENGTH_SHORT);
        }
        toast.show();


        // check if user is already signed in
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired() && current != null;

        if(isLoggedIn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            this.finish();
        }

        return true;
    }

    private boolean isValidPhone(String phone) {
        String PHONE_PATTERN = "^\\d{9,10}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addEventFabGallery.hide();
        addEventFabCamera.hide();

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
            Uri contentURI = data.getData();
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageName = "JPEG_" + timeStamp;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                addEventImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            addEventImage.setImageBitmap(imageBitmap);
        } else if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                if(!addressData.getAddressList().isEmpty()) {
                    addEventLocationText.setText(addressData.getAddressList().get(0).getAddressLine(0));
                    addEventLocationCity.setText(addressData.getAddressList().get(0).getLocality());
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageName = image.getName();
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("Create File", "Failed");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = getUriForFile(this,
                        this.getApplicationContext().getPackageName() +".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }
}
