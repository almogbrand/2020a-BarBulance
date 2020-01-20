package android.technion.com;

import android.content.Intent;
import android.os.Bundle;

import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textGraphicDesigner = findViewById(R.id.textGraphicDesigner);
        textGraphicDesigner.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textTermsAndConditions = findViewById(R.id.textTermsAndConditions);
        textTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textPrivacyPolicy = findViewById(R.id.textPrivacyPolicy);
        textPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textOpenSourceLicences = findViewById(R.id.textOpenSourceLicences);
        textOpenSourceLicences.setMovementMethod(LinkMovementMethod.getInstance());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(R.string.about);
        toolbar.inflateMenu(R.menu.main_without_edit_menu);

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
                        return true;
                    case R.id.action_contact_us:
                        intent = new Intent(AboutActivity.this, ContactActivity.class);
                        finish();
                        startActivity(intent);
                        return true;
                    case R.id.action_logout:
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        intent = new Intent(AboutActivity.this, FacebookActivity.class);
                        finish();
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}