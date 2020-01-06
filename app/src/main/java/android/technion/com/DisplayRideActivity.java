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
    private Toolbar toolbar;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ride);

        db = new Database();

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
        // TODO: set action onClick call
        displayRideChatButton = findViewById(R.id.displayRideChatButton);
        // TODO: set action onClick chat

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.inflateMenu(R.menu.edit_menu);

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
                        intent.putExtra("drive", drive);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        db.removeDriveFromDataBase(drive.getDriveDbId());
                        Toast toast = Toast.makeText(getApplicationContext(), "Ride Removed!", Toast.LENGTH_SHORT);
                        toast.show();

                        intent = new Intent(DisplayRideActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
