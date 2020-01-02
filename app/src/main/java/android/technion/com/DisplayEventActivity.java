package android.technion.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class DisplayEventActivity extends AppCompatActivity {
    private NestedScrollView scrollview;
    private ImageView displayEventImage;
    private TextInputEditText displayEventNameText;
    private TextInputEditText displayEventPhoneText;
    private TextInputEditText displayEventLocationText;
    private TextInputEditText displayEventAnimalTypeText;
    private TextInputEditText displayEventDescriptionText;
    private Button displayEventUrgentButton;
    private Button displayEventPickupButton;
    private Button displayEventFosterButton;
    private Event event;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Database db = new Database();

        setContentView(R.layout.activity_display_event);

        event = (Event) getIntent().getSerializableExtra("event");
        displayEventImage = findViewById(R.id.displayEventImage);
        db.getImageFromDatabaseToImageView(displayEventImage,event.getPhotoID());

        displayEventNameText = findViewById(R.id.displayEventNameText);
        displayEventNameText.setText(event.getReporterId());
        displayEventNameText.setKeyListener(null);

        displayEventPhoneText = findViewById(R.id.displayEventPhoneText);
        displayEventPhoneText.setText(event.getPhoneNumber());
        displayEventPhoneText.setKeyListener(null);

        displayEventLocationText = findViewById(R.id.displayEventLocationText);
        displayEventLocationText.setText(event.getLocation());
        displayEventLocationText.setKeyListener(null);

        displayEventAnimalTypeText = findViewById(R.id.displayEventAnimalTypeText);
        displayEventAnimalTypeText.setText(event.getAnimalType());
        displayEventAnimalTypeText.setKeyListener(null);

        displayEventDescriptionText = findViewById(R.id.displayEventDescriptionText);
        String description = event.getDescription();
        if(description.equals("")){
            displayEventDescriptionText.setText("No description");
        }  else {
            displayEventDescriptionText.setText(description);
        }
        displayEventDescriptionText.setKeyListener(null);

        displayEventUrgentButton = findViewById(R.id.displayEventUrgentButton);
        displayEventUrgentButton.setKeyListener(null);
        boolean urgent = event.getUrgent();
        if(urgent){
            displayEventUrgentButton.setText(R.string.urgent);
            displayEventUrgentButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        displayEventPickupButton = findViewById(R.id.displayEventPickupButton);
        // TODO: set action onClick pickup
        displayEventFosterButton = findViewById(R.id.displayEventFosterButton);
        // TODO: set action onClick foster

        // to start at the bottom of the activity
        scrollview = findViewById(R.id.displayEventScrollView);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

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
                        intent = new Intent(DisplayEventActivity.this, AddEventActivity.class);
                        intent.putExtra("event", event);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        // TODO: add here deletion from DB
                        intent = new Intent(DisplayEventActivity.this, MainActivity.class);
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
