package android.technion.com;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Event event1 = new Event("Hifa", "Dani Ginsberg","01","Turkey","bla bla bla",false);
        Event event2 = new Event("Beit Shemesh", "Almog Brand","02","Panda","bla bla",false);
        Event event3 = new Event("Kfar Masrik", "Lilit Yenokyan","03","Squirrel","bla",true );
        Database db = new Database();
        db.setUpRecyclerViewEventsList(this, (RecyclerView)findViewById(R.id.events));
        db.addEventToDatabase(event1);
        db.addEventToDatabase(event2);
        db.addEventToDatabase(event3);


//       List<Event> events = db.getEventFromDatabase(event1.getEventID());
//       Event event = events.get(0);
//       event.setLocation("aaaaaaa");
//       db.addEventToDatabase(event);

        Drive drive1 = new Drive("Dani Ginsberg","0542344156", "Hifa","Tel-aviv","25/12/2019");
        Drive drive2 = new Drive("Almog Brand","050000000", "Kiryat Shmona","Kiryat Bialik","26/12/2019");
        Drive drive3 = new Drive("Lilit Yenokyan","0544444444", "Hifa","Naharia","27/12/2019");
        db.setUpRecyclerViewDrivesList(this,(RecyclerView)findViewById(R.id.drives));
        db.addDriveToDatabase(drive1);
        db.addDriveToDatabase(drive2);
        db.addDriveToDatabase(drive3);


        User user = new User("d@g.co.il","054","123");
        db.addUserToDatabase(user);

//       // Drive drive = db.getDriveFromDatabase(drive1.getDriverID());
//        //db.addDriveToDatabase(drive);
//
//
//        Foster foster1 = new Foster("a",2.5,"TestUser1");
//        Foster foster2 = new Foster("b",3.5,"TestUser2");
//        Foster foster3 = new Foster("c",4.5,"TestUser3");
//        db.setUpRecyclerViewFostersList(this,(RecyclerView)findViewById(R.id.fosters));
//        db.addFosterToDatabase(foster1);
//        db.addFosterToDatabase(foster2);
//        db.addFosterToDatabase(foster3);
    }
}
