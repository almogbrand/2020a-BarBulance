package android.technion.com.ui.rides;

import android.content.Intent;
import android.os.Bundle;
import android.technion.com.AddRideActivity;
import android.technion.com.Database;
import android.technion.com.Drive;
import android.technion.com.R;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private RidesViewModel ridesViewModel;
    private TextView ridesEmptyText;
    private FloatingActionButton ridesFab;
    private FirebaseAuth mAuth;
    private Database db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ridesViewModel =
                ViewModelProviders.of(this).get(RidesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rides, container, false);
        ridesEmptyText = root.findViewById(R.id.ridesEmptyText);
        ridesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ridesEmptyText.setText(s);
            }
        });

        db = new Database();
        ridesFab = root.findViewById(R.id.ridesFab);
        ridesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddRideActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.ridesList);

        db.setUpRecyclerViewDrivesList(this.getContext(), recyclerView);

        if(db.getFBAdapter().getItemCount() == 0){
            ridesEmptyText.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_edit);
        if(item != null){
            item.setVisible(false);
        }
    }
}