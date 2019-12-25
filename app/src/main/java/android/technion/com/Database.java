package android.technion.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;


public class Database {

    private FirestoreRecyclerAdapter FBAdapter;
    private RecyclerView recyclerV;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private FirebaseStorage storage;


    public Database() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void addEventToDatabase(Event event) {

        db.collection("Events").add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Event added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Event", e);
                    }
                });
    }
    public void addDriveToDatabase(Drive drive){

        db.collection("Drives").add(drive).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Drive added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Drive", e);
                    }
                });

    }
    public void addFosterToDatabase(Foster foster) {
        db.collection("Fosters").add(foster).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Foster added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Foster", e);
                    }
                });
    }
    public void addUserToDatabase(final User user) {
        DocumentReference mFirestoreUsers = db.collection("Users").document(user.getUID());
       mFirestoreUsers.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Log.d(TAG, "User added");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding User", e);
                    }
                });
    }
    public void setUpRecyclerViewEventsList(Context context, RecyclerView recyclerList) {

        Query query = FirebaseFirestore.getInstance().collection("Events").limit(50);
        FirestoreRecyclerOptions<Event> options =
                new FirestoreRecyclerOptions.Builder<Event>()
                        .setQuery(query,Event.class)
                        .build();
        recyclerV = recyclerList;

        FBAdapter = new FirestoreRecyclerAdapter<Event, EventHolder>(options){

            @Override
            public EventHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.eventholder_row, parent, false);

                return  new EventHolder(v);
            }

            @Override
            public void onBindViewHolder(EventHolder holder, int position,final Event item) {
                holder.mainLogo.setImageResource(R.drawable.account);
                holder.houseLogo.setImageResource(R.drawable.home);
                holder.driveLogo.setImageResource(R.drawable.ambulance);
                holder.setAnimalType(item.getAnimalType());
                holder.setEventLocation(item.getLocation());
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO - add going to next intent

                    }
                });
            }
        };
        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();

    }
    public void setUpRecyclerViewDrivesList(final Context context, RecyclerView recyclerList) {

        Query query = FirebaseFirestore.getInstance().collection("Drives").orderBy("driverID", Query.Direction.ASCENDING).limit(50);
        FirestoreRecyclerOptions<Drive> options =
                new FirestoreRecyclerOptions
                        .Builder<Drive>()
                        .setQuery(query,Drive.class)
                        .build();
        recyclerV = recyclerList;

        FBAdapter = new FirestoreRecyclerAdapter<Drive, DriveHolder>(options){

            @Override
            public DriveHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.driveholder_row, parent, false);

                return  new DriveHolder(v);
            }

            @Override
            public void onBindViewHolder(DriveHolder holder, int position, final Drive item) {
                holder.setDriveFromLocation(item.getFromLocation());
                holder.setDriveTime(item.getDateOfRide());
                holder.setDriveToLocation(item.getToLocation());
                holder.setDriverID(item.getDriverID());
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO - add going to next intent


                    }
                });
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();

    }
    public void setUpRecyclerViewFostersList(Context context, RecyclerView recyclerList) {

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
    public void storeImageInDatabaseStorage(ImageView imageView,String photoID) {

        StorageReference storageRef = storage.getReference();
        StorageReference userImagesRef = storageRef.child("images/" + photoID +".jpg");


        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }

    public void getUserPhoneNumber (String UID) {
        DocumentReference docRef = db.collection("Users").document(UID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                //TODO see if possible to somehow get the phone number out.
            }
        });
    }


    /*
    public List<Event> getEventFromDatabase(String eventID) {
        final Event[] eventToReturn = {new Event()};
        DocumentReference docRef = db.collection("Events").document(eventID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> documentSnapshot) { // change to oncomplete and check for null

                if(documentSnapshot.isSuccessful()) {
                    events.add(documentSnapshot.getResult().toObject(Event.class));
                }
                else {

                }
                // anything we want to do with this event
            }
        });
        return events;
    }
    public Drive getDriveFromDatabase(String driverID) {
        DocumentReference docRef = db.collection("Drives").document(driverID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {//change to oncomplete and check for null
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                drive2 = documentSnapshot.toObject(Drive.class);
                drive2.setFromLocation("aaaaaa");
//                db.collection("")
                //what we want to do with this drive
            }
        });
        return drive2;
    }

    Drive getDrive2(){
        return drive2;
    }
    public void getFosterFromDatabase(String fosterID) {
        DocumentReference docRef = db.collection("Fosters").document(fosterID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {// change to oncomplete and check for null
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Foster foster = documentSnapshot.toObject(Foster.class);
                // what we want to do with this foster class
            }
        });
    }
    //add get for each of the objects
    //add update for all objects
*/
}
