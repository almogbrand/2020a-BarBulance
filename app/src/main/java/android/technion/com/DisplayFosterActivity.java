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

public class DisplayFosterActivity extends AppCompatActivity {
    private ImageView displayFosterImageView;
    private TextInputEditText displayFosterNameText;
    private TextInputEditText displayFosterPhoneText;
    private TextInputEditText displayFosterLocationText;
    private TextInputEditText displayFosterFromDateText;
    private TextInputEditText displayFosterFromTimeText;
    private TextInputEditText displayFosterUntilDateText;
    private TextInputEditText displayFosterUntilTimeText;
    private Button displayFosterChatButton;
    private Button displayFosterCallButton;
    private Toolbar toolbar;
    private Database db;
    private FirebaseAuth mAuth;
    private Foster foster;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foster);

        event = (Event) getIntent().getSerializableExtra("event");
        foster = (Foster) getIntent().getSerializableExtra("foster");

        displayFosterImageView = findViewById(R.id.displayFosterImageView);
        Picasso.with(this).load(Uri.parse(foster.getFosterProfilePicUri())).into(displayFosterImageView);

        displayFosterNameText = findViewById(R.id.displayFosterNameText);
        displayFosterNameText.setText(foster.getFosterFullName());
        displayFosterNameText.setKeyListener(null);

        displayFosterPhoneText = findViewById(R.id.displayFosterPhoneText);
        displayFosterPhoneText.setText(foster.getFosterPhoneNumber());
        displayFosterPhoneText.setKeyListener(null);

        displayFosterLocationText = findViewById(R.id.displayFosterLocationText);
        displayFosterLocationText.setText(foster.getLocation());
        displayFosterLocationText.setKeyListener(null);

        displayFosterFromDateText = findViewById(R.id.displayFosterFromDateText);
        displayFosterFromDateText.setText(foster.getFromDate());
        displayFosterFromDateText.setKeyListener(null);

        displayFosterFromTimeText = findViewById(R.id.displayFosterFromTimeText);
        displayFosterFromTimeText.setText(foster.getFromTime());
        displayFosterFromTimeText.setKeyListener(null);

        displayFosterUntilDateText = findViewById(R.id.displayFosterUntilDateText);
        displayFosterUntilDateText.setText(foster.getUntilDate());
        displayFosterUntilDateText.setKeyListener(null);

        displayFosterUntilTimeText = findViewById(R.id.displayFosterUntilTimeText);
        displayFosterUntilTimeText.setText(foster.getUntilTime());
        displayFosterUntilTimeText.setKeyListener(null);

        displayFosterCallButton = findViewById(R.id.displayFosterCallButton);
        displayFosterCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + foster.getFosterPhoneNumber()));
                startActivity(intent);
            }
        });

        displayFosterChatButton = findViewById(R.id.displayFosterChatButton);
        displayFosterChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storedPhoneNumber = foster.getFosterPhoneNumber();
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
        if(currentUser.getUid().equals(foster.getFosterId())){
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
                        intent = new Intent(DisplayFosterActivity.this, AddFosterActivity.class);
                        intent.putExtra("event", event);
                        intent.putExtra("foster", foster);
                        startActivity(intent);
                        return true;
                    case R.id.action_delete:
                        db.removeFosterFromDataBase(foster.getDatabaseID());
                        Toast toast = Toast.makeText(getApplicationContext(), "Foster Removed!", Toast.LENGTH_SHORT);
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
