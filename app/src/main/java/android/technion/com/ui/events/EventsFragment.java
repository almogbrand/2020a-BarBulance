package android.technion.com.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.technion.com.AddEventActivity;
import android.technion.com.Database;
import android.technion.com.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventsFragment extends Fragment {
    private FloatingActionButton eventsFab;
    private EventsViewModel eventsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventsViewModel =
                ViewModelProviders.of(this).get(EventsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_events, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        eventsFab = root.findViewById(R.id.eventsFab);
        eventsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.eventsList);
        Database db = new Database();
        db.setUpRecyclerViewEventsList(this.getContext(), recyclerView);

        eventsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}