package android.technion.com;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputEditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event);

        event = (Event) getIntent().getSerializableExtra("event");
        displayEventImage = findViewById(R.id.displayEventImage);

        displayEventNameText = findViewById(R.id.displayEventNameText);
        displayEventNameText.setText(event.getReporterId());

        displayEventPhoneText = findViewById(R.id.displayEventPhoneText);
        displayEventPhoneText.setText(event.getReporterPhoneNumber());

        displayEventLocationText = findViewById(R.id.displayEventLocationText);
        displayEventLocationText.setText(event.getLocation());

        displayEventAnimalTypeText = findViewById(R.id.displayEventAnimalTypeText);
        displayEventAnimalTypeText.setText(event.getAnimalType());

        displayEventDescriptionText = findViewById(R.id.displayEventDescriptionText);
        String description = event.getDescription();
        if(description.equals("")){
            displayEventDescriptionText.setText("No description");
        }  else {
            displayEventDescriptionText.setText(description);
        }

        displayEventUrgentButton = findViewById(R.id.displayEventUrgentButton);
        boolean urgent = event.getUrgent();
        if(urgent){
            displayEventUrgentButton.setText(R.string.urgent);
            displayEventUrgentButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        displayEventPickupButton = findViewById(R.id.displayEventPickupButton);
        displayEventFosterButton = findViewById(R.id.displayEventFosterButton);

        // to start at the bottom of the activity
        scrollview = findViewById(R.id.displayEventScrollView);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });



    }
}
