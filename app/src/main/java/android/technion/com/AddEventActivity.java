package android.technion.com;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.os.Bundle;

public class AddEventActivity extends AppCompatActivity {
    String[] animals = {"Bird", "Hedgehog", "Fox", "Eagle"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

//        //Create Array Adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.add_event_dropdown_item, animals);
//        //Find TextView control
//        AutoCompleteTextView acTextView = findViewById(R.id.filled_exposed_dropdown);
//        //Set the number of characters the user must type before the drop down list is shown
//        acTextView.setThreshold(1);
//        //Set the adapter
//        acTextView.setAdapter(adapter);
    }
}
