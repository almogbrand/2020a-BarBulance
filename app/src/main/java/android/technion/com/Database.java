package android.technion.com;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Database {

    private FirestoreRecyclerAdapter FBAdapter;
    private RecyclerView recyclerV;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;

    public Database() {
        db = FirebaseFirestore.getInstance();
    }


    public void addEventToDatabase(Event event) {
        db.collection("Events").add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void addDriveToDatabase(Drive drive){

        db.collection("Drives").add(drive);

    }
    public void addFosterToDatabase(Foster foster) {
        db.collection("Fosters").add(foster);
    }

    void setUpRecyclerViewEventsList(Context context, RecyclerView recyclerList) {

        Query query = FirebaseFirestore.getInstance().collection("Events").orderBy("eventID", Query.Direction.ASCENDING).limit(50);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>().setQuery(query,Event.class).build();
        recyclerV = recyclerList;

        FBAdapter = new FirestoreRecyclerAdapter<Event, DataHolder>(options){

            @Override
            public DataHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_holderrow, parent, false);

                return  new DataHolder(v);
            }

            @Override
            public void onBindViewHolder(DataHolder holder, int position, Event item) {
                holder.setValue(item.getEventID());
                holder.setLetter(item.getLocation());
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();

    }

    void setUpRecyclerViewDrivesList(Context context, RecyclerView recyclerList) {

        Query query = FirebaseFirestore.getInstance().collection("Drives").orderBy("driverID", Query.Direction.ASCENDING).limit(50);
        FirestoreRecyclerOptions<Drive> options = new FirestoreRecyclerOptions.Builder<Drive>().setQuery(query,Drive.class).build();
        recyclerV = recyclerList;

        FBAdapter = new FirestoreRecyclerAdapter<Drive, DataHolder>(options){

            @Override
            public DataHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_holderrow, parent, false);

                return  new DataHolder(v);
            }

            @Override
            public void onBindViewHolder(DataHolder holder, int position, Drive item) {
                holder.setValue(item.getDriverID());
                holder.setLetter(item.getFromLocation());
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();

    }
    void setUpRecyclerViewFostersList(Context context, RecyclerView recyclerList) {

        Query query = FirebaseFirestore.getInstance().collection("Fosters").orderBy("userID", Query.Direction.ASCENDING).limit(50);
        FirestoreRecyclerOptions<Foster> options = new FirestoreRecyclerOptions.Builder<Foster>().setQuery(query,Foster.class).build();
        recyclerV = recyclerList;

        FBAdapter = new FirestoreRecyclerAdapter<Foster, DataHolder>(options){

            @Override
            public DataHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.data_holderrow, parent, false);

                return  new DataHolder(v);
            }

            @Override
            public void onBindViewHolder(DataHolder holder, int position, Foster item) {
                holder.setValue(item.getUserID());
                holder.setLetter(item.getLocation());
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();

    }

    private void getAllEvents(final Context context) {
        List<Event> toReturn = new ArrayList<>();

        db.collection("Events").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                          @Override
                                          public void onSuccess(QuerySnapshot documentSnapshots) {
                                              if (documentSnapshots.isEmpty()) {
                                                  Log.d(TAG, "onSuccess: LIST EMPTY");
                                                  return;
                                              } else {
                                                  // Convert the whole Query Snapshot to a list
                                                  // of objects directly! No need to fetch each
                                                  // document.
//                                                  toReturn = documentSnapshots.toObjects(Event.class);
//                                                  // Add all to your list
//                                                  //toReturn.addAll(types);
//                                                  Log.d(TAG, "onSuccess: " + toReturn);
                                              }
                                          }
                                          }).addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Toast.makeText(context, "Error getting data!!!", Toast.LENGTH_LONG).show();
                                              }
                                          });
                                      }
    ////add get for each of the objects
    ////add update for all objects

}
