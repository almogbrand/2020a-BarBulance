package android.technion.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
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
    private boolean urgent;
    private FloatingActionButton addEventFab;
    private FloatingActionButton addEventFabGallery;
    private FloatingActionButton addEventFabCamera;
    private ImageView addEventImage;
    private Button addEventButton;
    private static final int GALLERY = 1, CAMERA = 2;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.animals_array, R.layout.add_event_dropdown_item);
        AutoCompleteTextView acTextView = findViewById(R.id.filled_exposed_dropdown);
        acTextView.setAdapter(adapter);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_event_send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextInputEditText addEventNameText = findViewById(R.id.addEventNameText);
        String name = addEventNameText.getText().toString();
        TextInputEditText addEventPhoneText = findViewById(R.id.addEventPhoneText);
        String phone = addEventPhoneText.getText().toString();
        TextInputEditText addEventLocationText = findViewById(R.id.addEventLocationText);
        String location = addEventLocationText.getText().toString();

        TextInputLayout addEventAnimalType = findViewById(R.id.addEventAnimalType);
        String animalType = addEventAnimalType.getEditText().getText().toString();
//        TextInputEditText addEventAnimalType = findViewById(R.id.addEventAnimalType);
//        String animalType = addEventAnimalType.getText().toString();
        TextInputEditText addEventDescriptionText = findViewById(R.id.addEventDescriptionText);
        String description = addEventDescriptionText.getText().toString();
        Switch addEventUrgentSwitch = findViewById(R.id.addEventUrgentSwitch);

        addEventUrgentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    urgent = true;
                } else {
                    urgent = false;
                }
            }
        });

        Event event = new Event(location, name, phone, animalType, description, urgent);
        Database db = new Database();
        db.addEventToDatabase(event);
        Toast toast = Toast.makeText(getApplicationContext(),"Event Sent!",Toast.LENGTH_SHORT);
        toast.show();
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
                Log.d("Create File", "error occurred");
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
