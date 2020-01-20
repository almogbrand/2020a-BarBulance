package android.technion.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private Database db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database();

        setContentView(R.layout.activity_display_event);

        event = (Event) getIntent().getSerializableExtra("event");
        displayEventImage = findViewById(R.id.displayEventImage);
        if(event.getPhotoID() != ""){
            db.getImageFromDatabaseToImageView(displayEventImage, event.getPhotoID());
        }

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
        displayEventPickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayEventActivity.this, PickUpActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);

//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(DisplayEventActivity.this, R.style.AlertDialogCustom));
//                alertDialogBuilder.setMessage("Are you sure you want to pickup " + event.getAnimalType() + " from " + event.getLocation() + "?");
//                alertDialogBuilder.setPositiveButton("Ok",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(DisplayEventActivity.this, PickUpActivity.class);
//                                intent.putExtra("event", event);
//                                startActivity(intent);
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                okButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                cancelButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        displayEventFosterButton = findViewById(R.id.displayEventFosterButton);
        displayEventFosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayEventActivity.this, FosterActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });

        // to start at the bottom of the activity
        scrollview = findViewById(R.id.displayEventScrollView);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser.getUid().equals(event.getEventReporterDBID())){
            toolbar.inflateMenu(R.menu.edit_menu);
        }
        toolbar.setNavigationIcon(R.drawable.back);

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
                        db.removeEventFromDataBase(event);
                        Toast toast = Toast.makeText(getApplicationContext(), "Event Removed!", Toast.LENGTH_SHORT);
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
