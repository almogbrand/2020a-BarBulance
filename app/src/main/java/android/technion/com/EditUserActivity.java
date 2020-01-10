package android.technion.com;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.AccessToken;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText editUserName;
    private TextInputEditText editUserEmail;
    private TextInputEditText editUserPhone;
    private ImageView imgView;
    private User user;
    private FirebaseAuth mAuth;
    private Database db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new Database();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            uid = currentUser.getUid();

            editUserName = findViewById(R.id.editUserName);
            editUserName.setText(currentUser.getDisplayName());

            editUserEmail = findViewById(R.id.editUserEmail);
            editUserEmail.setText(currentUser.getEmail());

            editUserPhone = findViewById(R.id.editUserPhone);
            TextView textView = new TextView(getBaseContext());
            db.getUserPhoneNumberToTextView(uid, textView);
            editUserPhone.setText(textView.getText().toString());

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
                return onUpdateProfile(item);
            }
        });

    }

    public boolean onUpdateProfile(MenuItem item) {
        String name = editUserName.getText().toString();
        String email = editUserEmail.getText().toString();
        String phone = editUserPhone.getText().toString();

        Toast toast;
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty()){
            if(name.isEmpty()){
                editUserName.setError("Required");
            }
            if(email.isEmpty()) {
                editUserEmail.setError("Required");
            }
            if(phone.isEmpty()) {
                editUserPhone.setError("Required");
            }

            toast = Toast.makeText(getApplicationContext(),"Required fields are empty!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        if (!isValidPhone(phone)) {
            editUserPhone.setError("Invalid phone number!");
            return true;
        }

        User newUser = new User(name, email, phone, uid);
        db.removeUserFromDataBase(uid);
        db.addUserToDatabase(newUser);
        toast = Toast.makeText(getApplicationContext(), "Profile Updated!", Toast.LENGTH_SHORT);
        toast.show();

        this.finish();
        return true;
    }

    private boolean isValidPhone(String phone) {
        String PHONE_PATTERN = "^\\d{9,10}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
