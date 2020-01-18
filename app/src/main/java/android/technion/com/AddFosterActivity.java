package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AddFosterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Foster foster;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_foster);

        foster = (Foster) getIntent().getSerializableExtra("foster");
        event = (Event) getIntent().getSerializableExtra("event");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(foster != null){
            toolbar.setTitle("Edit Foster");
        } else {
            toolbar.setTitle("Add Foster");
        }
        toolbar.inflateMenu(R.menu.send_menu);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                return onSendRide(item);
                return true;
            }
        });
    }
}
