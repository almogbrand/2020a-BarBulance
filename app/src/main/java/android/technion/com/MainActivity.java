package android.technion.com;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private User user;
    private FirebaseAuth mAuth;
    private Database db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_events, R.id.navigation_rides, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //add user to db
        db = new Database();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            user = new User(currentUser.getDisplayName(),currentUser.getEmail(),"",currentUser.getUid(),"");
            db.addUserToDatabase(user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_contact_us:
                intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                intent = new Intent(MainActivity.this, FacebookActivity.class);
                finishAffinity();
                startActivity(intent);
                return true;
            case R.id.action_edit:
                intent = new Intent(MainActivity.this, EditUserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
