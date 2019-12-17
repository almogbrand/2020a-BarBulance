package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.animals_array, R.layout.add_event_dropdown_item);
        AutoCompleteTextView acTextView = findViewById(R.id.filled_exposed_dropdown);
        acTextView.setAdapter(adapter);
    }
}
