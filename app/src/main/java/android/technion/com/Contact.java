package android.technion.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact extends AppCompatActivity {
    private EditText your_name;
    private EditText your_email;
    private EditText your_subject;
    private EditText your_message;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        your_name        = (EditText) findViewById(R.id.contactName);
        your_email       = (EditText) findViewById(R.id.contactEmail);
        your_subject     = (EditText) findViewById(R.id.contactSubject);
        your_message     = (EditText) findViewById(R.id.contactMessage);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(R.string.contact_us);
        toolbar.inflateMenu(R.menu.main_menu);
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
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_about:
                        intent = new Intent(Contact.this, About.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.action_contact_us:
                        return true;
                    case R.id.action_logout:
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        intent = new Intent(Contact.this, FacebookActivity.class);
                        finish();
                        startActivity(intent);
                        return true;
                    case R.id.action_send:
                        sendEmail();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // validating email
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void sendEmail() {
        String name      = your_name.getText().toString();
        String email     = your_email.getText().toString();
        String subject   = your_subject.getText().toString();
        String message   = your_message.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(subject)
                || TextUtils.isEmpty(message)){
            if (TextUtils.isEmpty(name)){
                your_name.setError("Enter Your Name");
            }

            if (TextUtils.isEmpty(email)) {
                your_email.setError("Enter Your Email");
            }

            if (TextUtils.isEmpty(subject)){
                your_subject.setError("Enter Subject");
            }

            if (TextUtils.isEmpty(message)){
                your_message.setError("Enter Message");
            }
            Toast toast = Toast.makeText(getApplicationContext(),"Required fields are empty!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (!isValidEmail(email)) {
            your_email.setError("Invalid Email");
            return;
        }

        Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        sendEmail.setType("plain/text");
        sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"barbulance@gmail.com"});
        sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                "Name: "+name+'\n'+'\n'+"Email: "+email+'\n'+'\n'+"Message: "+'\n'+message);

        /* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(sendEmail, "Send mail..."));
    }
}
