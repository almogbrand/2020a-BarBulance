package android.technion.com;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
//        TextInputEditText view = findViewById(R.id.text);
//        view.setFocusable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        TextView textView1;
        TextView textView2;
        TextView textView3;

        textView1 = findViewById(R.id.link1);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());

        textView2 = findViewById(R.id.link2);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());

        textView3 = findViewById(R.id.link3);
        textView3.setMovementMethod(LinkMovementMethod.getInstance());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                return true;
            case R.id.action_contact_us:
                // TODO: add contact us activity + SAME IN ABUT PAGE + SAME IN CONTACT US PAGE
                intent = new Intent(About.this, Contact.class);
                this.finish();
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