package android.technion.com;

import android.content.Intent;
import android.os.Bundle;
import android.technion.com.ui.Contact;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextInputEditText view = findViewById(R.id.text);
        view.setFocusable(false);
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
                // TODO: add about activity + SAME IN ABUT PAGE + SAME IN CONTACT US PAGE
                intent = new Intent(About.this, About.class);
                this.finish();
                startActivity(intent);
                return true;
            case R.id.action_contact_us:
                // TODO: add contact us activity + SAME IN ABUT PAGE + SAME IN CONTACT US PAGE
                intent = new Intent(About.this, Contact.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                intent = new Intent(About.this, FacebookActivity.class);
                this.finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}