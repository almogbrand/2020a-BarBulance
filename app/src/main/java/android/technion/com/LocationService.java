package android.technion.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;
import javax.annotation.Nullable;
import static android.content.ContentValues.TAG;

public class LocationService extends AppCompatActivity {

    Context controller;
    Activity activity;
    AddressData addressData;
    private FirebaseFirestore db;

    public LocationService(Activity activity, Context context) {
        this.activity = activity;
        this.controller = context;
        this.db=FirebaseFirestore.getInstance();
    }

    public void startLocationIntent(){
        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(32.109333, 34.855499)  // Initial Latitude and Longitude the Map will load into
                .showLatLong(true)  // Show Coordinates in the Activity
                .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
                .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                .setMarkerDrawable(R.drawable.map_marker) // Change the default Marker Image
                .setMarkerImageImageColor(R.color.colorPrimary)
                .setFabColor(R.color.colorAccent)
                .setPrimaryTextColor(R.color.colorPrimaryText) // Change text color of Shortened Address
                .setSecondaryTextColor(R.color.colorSecondaryText) // Change text color of full Address
                .setMapRawResourceStyle(R.raw.style_json)  //Set Map Style (https://mapstyle.withgoogle.com/)
                .setMapType(MapType.NORMAL)
                .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                .build(activity);

        activity.startActivityForResult(intent,Constants.PLACE_PICKER_REQUEST);
//        ((Activity) controller).startActivityForResult(intent,Constants.PLACE_PICKER_REQUEST);
//        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                db.collection("Addresses").add(addressData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Address added with ID: " + documentReference.getId());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding Event", e);
                            }
                        });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
