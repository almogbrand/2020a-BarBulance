package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.content.FileProvider.getUriForFile;

public class AddEventActivity extends AppCompatActivity {
    private TextInputEditText addEventNameText;
    private TextInputEditText addEventPhoneText;
    private TextInputEditText addEventLocationText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.animals_array, R.layout.add_event_dropdown_item);
        AutoCompleteTextView acTextView = findViewById(R.id.filled_exposed_dropdown);
        acTextView.setAdapter(adapter);

        addEventNameText = findViewById(R.id.addEventNameText);
        addEventPhoneText = findViewById(R.id.addEventPhoneText);
        addEventLocationText = findViewById(R.id.addEventLocationText);
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

        addEventFabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });

        // in case of EDITING an existing event
        Database db = new Database();
        event = (Event) getIntent().getSerializableExtra("event");
        if(event != null){
            addEventNameText.setText(event.getReporterId());
            addEventPhoneText.setText(event.getPhoneNumber());
            addEventLocationText.setText(event.getLocation());
            addEventAnimalType.getEditText().setText(event.getAnimalType());
            addEventDescriptionText.setText(event.getDescription());
            addEventUrgentSwitch.setChecked(event.getUrgent());
            db.getImageFromDatabaseToImageView(addEventImage, event.getPhotoID());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_event_send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = addEventNameText.getText().toString();
        String phone = addEventPhoneText.getText().toString();
        String location = addEventLocationText.getText().toString();
        String animalType = addEventAnimalType.getEditText().getText().toString();
        String description = addEventDescriptionText.getText().toString();
        Toast toast;
        if(name.equals("") || phone.equals("") || location.equals("") || animalType.equals("")){
            if(name.equals("")){
                addEventNameText.setError("Required");
            }
            if(phone.equals("")) {
                addEventPhoneText.setError("Required");
            }
            if(location.equals("")) {
                addEventLocationText.setError("Required");
            }
            if(animalType.equals("")){
                addEventAnimalType.setError("Required");
            }

            toast = Toast.makeText(getApplicationContext(),"Required fields are empty!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        Event event = new Event(location, name, phone, animalType, description, urgent, imageName);
        Database db = new Database();
        db.addEventToDatabase(event);
        if(!(imageName.equals(""))){
            db.storeImageInDatabaseStorage(addEventImage, imageName);
        }
        toast = Toast.makeText(getApplicationContext(),"Event Sent!", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
        this.finish();
        startActivity(intent);

        return true;
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
