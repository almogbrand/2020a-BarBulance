package android.technion.com.ui.rides;

import android.content.Intent;
import android.os.Bundle;
import android.technion.com.AddEventActivity;
import android.technion.com.Database;
import android.technion.com.Drive;
import android.technion.com.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RidesFragment extends Fragment {

    private RidesViewModel dashboardViewModel;
    private FloatingActionButton eventsFab;
    private FirebaseAuth mAuth;
Database db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(RidesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rides, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        db= new Database();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        eventsFab = root.findViewById(R.id.ridesFab);
        final Drive drive = new Drive(currentUser.getUid(),currentUser.getPhotoUrl().toString(), "Dani Ginsberg", "0542344156", "Hifa", "Shlomi", "Today");
        eventsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.addDriveToDatabase(drive);
                Toast.makeText(getApplicationContext(), "in onclick", Toast.LENGTH_SHORT).show();

            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.drivesList);

        db.setUpRecyclerViewDrivesList(this.getContext(), recyclerView);

//        if(db.getFBAdapter().getItemCount() == 0){
//            text_empty.setVisibility(View.GONE);
//        }

//        eventsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                text_empty.setText(s);
//            }
//        });
        return root;
    }
}