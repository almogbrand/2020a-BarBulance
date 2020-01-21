package android.technion.com;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class EventHolder extends RecyclerView.ViewHolder{
    public ImageView mainLogo;
    private TextView animalType;
    private TextView eventLocation;
    public ImageView houseLogo;
    public ImageView driveLogo;
    public View itemView;

    public EventHolder(View v) {
        super(v);
        mainLogo = v.findViewById(R.id.eventImageView);
        animalType = v.findViewById(R.id.eventAnimalType);
        eventLocation = v.findViewById(R.id.eventLocation);
        houseLogo = v.findViewById(R.id.eventHouseLogo);
        driveLogo = v.findViewById(R.id.eventDriveLogo);
        itemView = v;
    }


    public void setAnimalType(String str) {
        animalType.setText(str);
    }

    public void setEventLocation(String str) {
        eventLocation.setText(str);
    }
}
