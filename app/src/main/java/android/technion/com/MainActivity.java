package android.technion.com;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Event event1 = new Event("TestEvent1","Location1", "TestUser1");
        Event event2 = new Event("TestEvent2","Location2", "TestUser2");
        Event event3 = new Event("TestEvent3","Location3", "TestUser3");
        Database db = new Database();
        db.setUpRecyclerViewEventsList(this, (RecyclerView)findViewById(R.id.events));
        db.addEventToDatabase(event1);
        db.addEventToDatabase(event2);
        db.addEventToDatabase(event3);
//        Drive drive1 = new Drive("TestUser1","1", "a","b");
//        Drive drive2 = new Drive("TestUser2","2", "b","c");
//        Drive drive3 = new Drive("TestUser3","3", "c","d");
//        db.setUpRecyclerViewDrivesList(this,(RecyclerView)findViewById(R.id.drives));
//        db.addDriveToDatabase(drive1);
//        db.addDriveToDatabase(drive2);
//        db.addDriveToDatabase(drive3);
//        Foster foster1 = new Foster("TestEvent1","Species1","a",2.5,"TestUser1");
//        Foster foster2 = new Foster("TestEvent2","Species2","b",3.5,"TestUser2");
//        Foster foster3 = new Foster("TestEvent3","Species3","c",4.5,"TestUser3");
//        db.setUpRecyclerViewFostersList(this,(RecyclerView)findViewById(R.id.fosters));
//        db.addFosterToDatabase(foster1);
//        db.addFosterToDatabase(foster2);
//        db.addFosterToDatabase(foster3);
    }
}
