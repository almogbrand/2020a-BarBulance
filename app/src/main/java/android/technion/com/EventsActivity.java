package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventsActivity extends AppCompatActivity {
    private FloatingActionButton eventsFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        eventsFab = findViewById(R.id.eventsFab);
        eventsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, FacebookActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
