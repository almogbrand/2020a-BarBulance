package android.technion.com;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class EditUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText editUserName;
    private TextInputEditText editUserEmail;
    private TextInputEditText editUserPhone;
    private ImageView imgView;
    private User user;
    private FirebaseAuth mAuth;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new Database();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String uid = currentUser.getUid();

            editUserName = findViewById(R.id.editUserName);
            editUserName.setText(currentUser.getDisplayName());

            editUserEmail = findViewById(R.id.editUserEmail);
            editUserEmail.setText(currentUser.getEmail());

            editUserPhone = findViewById(R.id.editUserPhone);
            editUserPhone.setText(currentUser.getPhoneNumber());

            ImageView imgView = findViewById(R.id.editUserImage);
            Uri imgUri = currentUser.getPhotoUrl();
            imgView.setImageURI(imgUri);
            Picasso.with(this).load(imgUri).into(imgView);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(R.string.edit_profile);
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
//                return onSendEvent(item);
                return true;
            }
        });

    }


}
