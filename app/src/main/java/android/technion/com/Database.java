package android.technion.com;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class Database {
    private FirestoreRecyclerAdapter FBAdapter;
    private RecyclerView recyclerV;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public FirestoreRecyclerAdapter getFBAdapter(){
        return FBAdapter;
    }

    public Database() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void addEventToDatabase(final Event event) {
        db.collection("Events").add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "Event added with ID: " + documentReference.getId());
                    db.collection("Events").document(documentReference.getId())
                            .update(
                                    "databaseID", documentReference.getId()
                            );
                }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Event", e);
                    }
                });
    }

    public void updateEventInDatabase(final Event oldEvent, final Event newEvent) {
        final DocumentReference docRef = db.collection("Events").document(oldEvent.getDatabaseID());
        docRef.update("location", newEvent.getLocation());
        docRef.update("locationCity", newEvent.getLocationCity());
        docRef.update("reporterId", newEvent.getReporterId());
        docRef.update("animalType", newEvent.getAnimalType());
        docRef.update("phoneNumber", newEvent.getPhoneNumber());
        docRef.update("description", newEvent.getDescription());
        docRef.update("urgent", newEvent.getUrgent());
        docRef.update("photoID", newEvent.getPhotoID());
    }

    public void addDriveToDatabase(final Drive drive){
        db.collection("Drives").add(drive).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Drive added with ID: " + documentReference.getId());
                db.collection("Drives").document(documentReference.getId())
                        .update(
                                "databaseID", documentReference.getId()
                        );
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Drive", e);
                    }
                });

    }

    public void updateDriveInDatabase(final Drive oldDrive, final Drive newDrive) {
        final DocumentReference docRef = db.collection("Drives").document(oldDrive.getDatabaseID());
        docRef.update("driverId", newDrive.getDriverId());
        docRef.update("driverProfilePicUri", newDrive.getDriverProfilePicUri());
        docRef.update("driverFullName", newDrive.getDriverFullName());
        docRef.update("driverPhoneNumber", newDrive.getDriverPhoneNumber());
        docRef.update("fromLocation", newDrive.getFromLocation());
        docRef.update("fromCity", newDrive.getFromCity());
        docRef.update("toLocation", newDrive.getToLocation());
        docRef.update("toCity", newDrive.getToCity());
        docRef.update("date", newDrive.getDate());
        docRef.update("time", newDrive.getTime());
    }

    public void addFosterToDatabase(Foster foster) {
        db.collection("Fosters").add(foster).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Foster added with ID: " + documentReference.getId());
                db.collection("Fosters").document(documentReference.getId())
                        .update(
                                "databaseID", documentReference.getId()
                        );
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Foster", e);
                    }
                });
    }

    public void updateFosterInDatabase(final Foster oldFoster, final Foster newFoster) {
        final DocumentReference docRef = db.collection("Fosters").document(oldFoster.getDatabaseID());
        docRef.update("fosterId", newFoster.getFosterId());
        docRef.update("fosterProfilePicUri", newFoster.getFosterProfilePicUri());
        docRef.update("fosterFullName", newFoster.getFosterFullName());
        docRef.update("fosterPhoneNumber", newFoster.getFosterPhoneNumber());
        docRef.update("location", newFoster.getLocation());
        docRef.update("locationCity", newFoster.getLocationCity());
        docRef.update("fromDate", newFoster.getFromDate());
        docRef.update("fromTime", newFoster.getFromTime());
        docRef.update("untilDate", newFoster.getUntilDate());
        docRef.update("untilTime", newFoster.getUntilTime());
    }

    public void addUserToDatabase(final User user) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        User NewUser= new User(user.getUserName(),user.getUserEmail(),user.getUserPhoneNumber(),user.getUID(),token);
                        DocumentReference mFirestoreUsers = db.collection("Users").document(NewUser.getUID());
                        mFirestoreUsers.set(NewUser).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                return new EventHolder(v);
            }

            @Override
            public void onBindViewHolder(final EventHolder holder, int position, final Event item) {
                String animal = item.getAnimalType();
                switch (animal){
                    case "Bat":
                        holder.mainLogo.setImageResource(R.drawable.animal_bat);
                        break;
                    case "Bird":
                        holder.mainLogo.setImageResource(R.drawable.animal_bird);
                        break;
                    case "Boar":
                        holder.mainLogo.setImageResource(R.drawable.animal_boar);
                        break;
                    case "Eagle":
                        holder.mainLogo.setImageResource(R.drawable.animal_eagle);
                        break;
                    case "Hedgehog":
                        holder.mainLogo.setImageResource(R.drawable.animal_hedgehog);
                        break;
                    case "Jackal":
                        holder.mainLogo.setImageResource(R.drawable.animal_jackal);
                        break;
                    case "Owl":
                        holder.mainLogo.setImageResource(R.drawable.animal_owl);
                        break;
                    case "Snake":
                        holder.mainLogo.setImageResource(R.drawable.animal_snake);
                        break;
                    case "Turtle":
                        holder.mainLogo.setImageResource(R.drawable.animal_turtle);
                        break;
                    default:
                        holder.mainLogo.setImageResource(R.drawable.animal_other);
                }

                holder.houseLogo.setImageResource(R.drawable.home);
                final CollectionReference fostersRef = FirebaseFirestore.getInstance().collection( "Fosters");
                Query query = fostersRef.whereEqualTo( "eventID", item.getDatabaseID());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                holder.houseLogo.setVisibility(View.VISIBLE);
                            }
                        }  else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                holder.houseLogo.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FosterActivity.class);
                        intent.putExtra("event", item);
                        v.getContext().startActivity(intent);
                    }
                });

                holder.driveLogo.setImageResource(R.drawable.ambulance);
                final CollectionReference ridesRef = FirebaseFirestore.getInstance().collection( "Drives");
                query = ridesRef.whereEqualTo( "eventID", item.getDatabaseID());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                holder.driveLogo.setVisibility(View.VISIBLE);
                            }
                        }  else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                holder.driveLogo.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PickUpActivity.class);
                        intent.putExtra("event", item);
                        v.getContext().startActivity(intent);
                    }
                });

                holder.setAnimalType(item.getAnimalType());
                holder.setEventLocation(item.getLocationCity());
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayEventActivity.class);
                        intent.putExtra("event", item);
                        v.getContext().startActivity(intent);
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
        Query query = FirebaseFirestore.getInstance().collection("Drives").orderBy("driverFullName", Query.Direction.ASCENDING).limit(50);
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
                holder.setDriveFromLocation(item.getFromCity());
                holder.setDriveTime(item.getTime());
                holder.setDriveToLocation(item.getToCity());
                holder.setDriverID(item.getDriverFullName());
                holder.setDriversProfilePic((Uri.parse(item.getDriverProfilePicUri())));
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayRideActivity.class);
                        intent.putExtra("drive", item);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();
    }

//    public void setUpRecyclerViewFostersList(Context context, RecyclerView recyclerList) {
//
//        Query query = FirebaseFirestore.getInstance().collection("Fosters").orderBy("userID", Query.Direction.ASCENDING).limit(50);
//        FirestoreRecyclerOptions<Foster> options = new FirestoreRecyclerOptions.Builder<Foster>().setQuery(query,Foster.class).build();
//        recyclerV = recyclerList;
//
//        FBAdapter = new FirestoreRecyclerAdapter<Foster, DataHolder>(options){
//
//            @Override
//            public DataHolder onCreateViewHolder(ViewGroup parent,
//                                                 int viewType) {
//
//                View v = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.data_holderrow, parent, false);
//
//                return  new DataHolder(v);
//            }
//
//            @Override
//            public void onBindViewHolder(DataHolder holder, int position, Foster item) {
//                holder.setValue(item.getUserID());
//                holder.setLetter(item.getLocation());
//            }
//        };
//
//        layoutManager = new LinearLayoutManager(context);
//        recyclerV.setLayoutManager(layoutManager);
//        recyclerV.setAdapter(FBAdapter);
//        FBAdapter.startListening();
//
//    }


    public void storeImageInDatabaseStorage(ImageView imageView, final String photoID) {
        StorageReference storageRef = storage.getReference();
        StorageReference userImagesRef;
        if(photoID.toLowerCase().contains("Facebook")){
            userImagesRef = storageRef.child("facebookPics/" + photoID);

        } else {
            userImagesRef = storageRef.child("images/" + photoID);
        }
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 33, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "Error loading Event photo");            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Event photo added with ID: " + photoID);
            }
        });
    }

    public void getImageFromDatabaseToImageView(final ImageView imageView, final String photoID) {
        StorageReference islandRef;
        if(photoID.toLowerCase().contains("Facebook")){
            islandRef = storage.getReference("facebookPics/" + photoID );
        }else {
            islandRef = storage.getReference("images/" + photoID);
        }
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "Error downloading Event photo with id:" + photoID);
            }
        });
    }

    public void checkUserExists (String UID, final TextView result){
        DocumentReference docRef = db.collection("Users").document(UID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        result.setText("true");
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        result.setText("false");
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    public void getUserToTextViews (String UID, final TextView name, final TextView email, final TextView phone) {
        DocumentReference docRef = db.collection("Users").document(UID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if(user != null) {
                    name.setText(user.getUserName());
                    email.setText(user.getUserEmail());
                    phone.setText(user.getUserPhoneNumber());
                } else {
                    Log.w(TAG, "Error retrieving user");
                }
            }
        });
    }

    public void removeEventFromDataBase(Event event) {
        db.collection("Events").document(event.getDatabaseID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "event successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting event", e);
                    }
                });
        //Delete the photo from the FireStore Storage platform as well
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child("images/" + event.getPhotoID());
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "event image successfully deleted!");            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "Error deleting event image");
            }
        });
    }

    public void removeDriveFromDataBase(String driveDatabaseId) {
        db.collection("Drives").document(driveDatabaseId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "drive successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting drive", e);
                    }
                });
    }

    public void removeFosterFromDataBase(String fosterDatabaseId) {
        db.collection("Fosters").document(fosterDatabaseId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "foster successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting foster", e);
                    }
                });
    }

    public void removeUserFromDataBase(String userUId) {
        db.collection("Users").document(userUId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "user successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting user", e);
                    }
                });
        //Delete the photo from the FireStore Storage platform as well
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child(userUId + "Facebook");
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "user image successfully deleted!");            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "Error deleting user image");
            }
        });
    }

    public void setUpRecyclerViewDrivesListFromCertainEvent(final Context context, RecyclerView recyclerList, final Event event) {
        Query query = FirebaseFirestore.getInstance().collection("Drives")
                .whereEqualTo("eventID", event.getDatabaseID()).limit(10);
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
                holder.setDriveFromLocation(item.getFromCity());
                holder.setDriveTime(item.getTime());
                holder.setDriveToLocation(item.getToCity());
                holder.setDriverID(item.getDriverFullName());
                holder.setDriversProfilePic((Uri.parse(item.getDriverProfilePicUri())));
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayRideActivity.class);
                        intent.putExtra("event", event);
                        intent.putExtra("drive", item);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();
    }

    public void setUpRecyclerViewFosterListFromCertainEvent(final Context context, RecyclerView recyclerList, final Event event) {
        Query query = FirebaseFirestore.getInstance()
                .collection("Fosters")
                .whereEqualTo("eventID", event.getDatabaseID()).limit(10);
        FirestoreRecyclerOptions<Foster> options =
                new FirestoreRecyclerOptions
                        .Builder<Foster>()
                        .setQuery(query, Foster.class)
                        .build();
        recyclerV = recyclerList;

        FBAdapter = new FirestoreRecyclerAdapter<Foster, FosterHolder>(options){

            @Override
            public FosterHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {

                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fosterholder_row, parent, false);

                return new FosterHolder(v);
            }

            @Override
            public void onBindViewHolder(FosterHolder holder, int position, final Foster item) {
                holder.setFosterLocation(item.getLocationCity());
                holder.setFosterTime(item.getUntilTime());
                holder.setFosterDate(item.getUntilDate());
                holder.setFosterPerson(item.getFosterFullName());
                holder.setFosterProfilePic((Uri.parse(item.getFosterProfilePicUri())));
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DisplayFosterActivity.class);
                        intent.putExtra("event", event);
                        intent.putExtra("foster", item);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };

        layoutManager = new LinearLayoutManager(context);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setAdapter(FBAdapter);
        FBAdapter.startListening();
    }
}
