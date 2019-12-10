package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignInActivity extends AppCompatActivity {
    private EditText signInUser;
    private EditText signInPassword;
    private Button signInButton;
    private FloatingActionButton signInFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInUser = findViewById(R.id.signInUser);
        signInPassword = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);
        signInFab = findViewById(R.id.signInFab);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO DO: add permissions here
                Intent intent = new Intent(SignInActivity.this, EventsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        signInFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });

    }
}
