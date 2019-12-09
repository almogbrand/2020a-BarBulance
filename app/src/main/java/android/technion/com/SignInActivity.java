package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {
    private EditText signInUser;
    private EditText signInPassword;
    private Button signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInUser = findViewById(R.id.signInUser);
        signInPassword = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, EventsActivity.class);
//                finish();
                startActivity(intent);
            }
        });

    }
}
