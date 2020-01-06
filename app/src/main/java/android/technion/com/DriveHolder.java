package android.technion.com;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class DriveHolder extends RecyclerView.ViewHolder{

    public ImageView driverLogo;
    private TextView driverID;
    private TextView driveFromLocation;
    private TextView driveToLocation;
    private TextView driveTime;
    public View itemView;


    public DriveHolder(View v ) {
        super(v);
        driverLogo = v.findViewById(R.id.rideImageView);
        driverID = v.findViewById(R.id.rideDriver);
        driveFromLocation = v.findViewById(R.id.rideFromLocation);
        driveToLocation = v.findViewById(R.id.rideToLocation);
        driveTime = v.findViewById(R.id.rideTime);
        itemView = v;
    }

    public void setDriveFromLocation(String str) {
        driveFromLocation.setText(str);
    }
    public void setDriveToLocation(String str) {
        driveToLocation.setText(str);
    }
    public void setDriveTime(String str) {
        driveTime.setText(str);
    }
    public void setDriverID(String str) {
        driverID.setText(str);
    }
    public void setDriversProfilePic(Uri uri) {
        Picasso.with(itemView.getContext()).load(uri).into(driverLogo);
    }
}