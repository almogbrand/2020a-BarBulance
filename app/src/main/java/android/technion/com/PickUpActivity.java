package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PickUpActivity extends AppCompatActivity {
    private FloatingActionButton eventRidesFab;
    private Toolbar toolbar;
    private Event event;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        db = new Database();
        event = (Event) getIntent().getSerializableExtra("event");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pick Up " + event.getAnimalType());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickUpActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        eventRidesFab = findViewById(R.id.eventRidesFab);
        eventRidesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickUpActivity.this, AddRideActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.eventRidesList);
        db.setUpRecyclerViewDrivesListFromCertainEvent(PickUpActivity.this, recyclerView, event);
    }

}
