package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class DisplayRideActivity extends AppCompatActivity {
    private ImageView displayRideImageView;
    private TextInputEditText displayRideNameText;
    private TextInputEditText displayRidePhoneText;
    private TextInputEditText displayRideDateText;
    private TextInputEditText displayRideTimeText;
    private TextInputEditText displayRideFromLocationText;
    private TextInputEditText displayRideToLocationText;
    private Button displayRideCallButton;
    private Button displayRideChatButton;
    private Drive drive;
    private Event event;
    private Toolbar toolbar;
    private Database db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ride);

        event = (Event) getIntent().getSerializableExtra("event");
        drive = (Drive) getIntent().getSerializableExtra("drive");

        displayRideImageView = findViewById(R.id.displayRideImageView);
        Picasso.with(this).load(Uri.parse(drive.getDriverProfilePicUri())).into(displayRideImageView);

        displayRideNameText = findViewById(R.id.displayRideNameText);
        displayRideNameText.setText(drive.getDriverFullName());
        displayRideNameText.setKeyListener(null);

        displayRidePhoneText = findViewById(R.id.displayRidePhoneText);
        displayRidePhoneText.setText(drive.getDriverPhoneNumber());
        displayRidePhoneText.setKeyListener(null);

        displayRideDateText = findViewById(R.id.displayRideDateText);
        displayRideDateText.setText(drive.getDate());
        displayRideDateText.setKeyListener(null);

        displayRideTimeText = findViewById(R.id.displayRideTimeText);
        displayRideTimeText.setText(drive.getTime());
        displayRideTimeText.setKeyListener(null);

        displayRideFromLocationText = findViewById(R.id.displayRideFromLocationText);
        displayRideFromLocationText.setText(drive.getFromLocation());
        displayRideFromLocationText.setKeyListener(null);

        displayRideToLocationText = findViewById(R.id.displayRideToLocationText);
        displayRideToLocationText.setText(drive.getToLocation());
        displayRideToLocationText.setKeyListener(null);

        displayRideCallButton = findViewById(R.id.displayRideCallButton);
        displayRideCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + drive.getDriverPhoneNumber()));
                startActivity(intent);
            }
        });

        displayRideChatButton = findViewById(R.id.displayRideChatButton);
        displayRideChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storedPhoneNumber = drive.getDriverPhoneNumber();
                if(storedPhoneNumber.length() != 10){
                    Toast toast = Toast.makeText(getApplicationContext(), "Can't send message to home number!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                String phone = "972" + storedPhoneNumber.substring(1);
                String url = "https://api.whatsapp.com/send?phone=" + phone;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);

        db = new Database();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser.getUid().equals(drive.getDriverId())){
            toolbar.inflateMenu(R.menu.edit_menu);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        intent = new Intent(DisplayRideActivity.this, AddRideActivity.class);
                        intent.putExtra("event", event);
                        intent.putExtra("drive", drive);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        db.removeDriveFromDataBase(drive.getDatabaseID());
                        Toast toast = Toast.makeText(getApplicationContext(), "Ride Removed!", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
