package android.technion.com;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DriveHolder extends RecyclerView.ViewHolder{

    public ImageView driverLogo;
    private TextView driverID;
    private TextView driveFromLocation;
    private TextView driveToLocation;
    private TextView driveTime;
    public View itemView;


    public DriveHolder(View v ) {
        super(v);
        driverLogo = v.findViewById(R.id.driverlogo);
        driverID = v.findViewById(R.id.driverID);
        driveFromLocation = v.findViewById(R.id.drivefromlocation);
        driveToLocation = v.findViewById(R.id.drivetolocation);
        driveTime = v.findViewById(R.id.drivetime);
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
}