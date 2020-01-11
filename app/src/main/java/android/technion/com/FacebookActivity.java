package android.technion.com;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity {
    private Button signInButton;
    private Button signInAddEventButton;
    private LoginButton loginButton;

    private static final String TAG = "FacebookLogin";
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private Database db;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    /////////////////////////////////////////////////
    //          handler FB Access Tokens           //
    /////////////////////////////////////////////////

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            User barbulanceUser = new User(user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), user.getUid());
//                            db.addUserToDatabase(barbulanceUser);

                            Intent next = new Intent(FacebookActivity.this, MainActivity.class);
                            finish();
                            startActivity(next);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FacebookActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //////////////////////////////////
    //          on Create           //
    //////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        db = new Database();
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        //check if user is already signed in
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        FirebaseUser current = mAuth.getCurrentUser();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired() && current != null;
        if(isLoggedIn) {
            Intent intent = new Intent(FacebookActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        //singing in
        loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        // Add an event - unregistered user
        signInAddEventButton = findViewById(R.id.signInAddEventButton);
        signInAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacebookActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        /* Temporary bypassing facebook login *////////////////////////////////////////
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacebookActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        //////////////////////////////////////////////////////////////////////////////

    }

    ////////////////////////////////////////////
    //          on Activity Result            //
    ////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //////////////////////////////////
    //          on Start            //
    //////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();

        //check if user is already signed in
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        FirebaseUser current = mAuth.getCurrentUser();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired() && current!=null;
        if(isLoggedIn) {
            Intent intent = new Intent(FacebookActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
