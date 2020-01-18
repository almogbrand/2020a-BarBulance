package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FosterActivity extends AppCompatActivity {
    private FloatingActionButton eventFosterFab;
    private Toolbar toolbar;
    private Event event;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster);

        db = new Database();
        event = (Event) getIntent().getSerializableExtra("event");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Foster " + event.getAnimalType());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FosterActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        eventFosterFab = findViewById(R.id.eventFosterFab);
        eventFosterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FosterActivity.this, AddFosterActivity.class);
                intent.putExtra("event", event);
                finish();
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.eventFosterList);
        db.setUpRecyclerViewFosterListFromCertainEvent(FosterActivity.this, recyclerView, event);
    }
}
