package android.technion.com;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class FosterHolder extends RecyclerView.ViewHolder {
    public ImageView fosterLogo;
    private TextView fosterPerson;
    private TextView fosterLocation;
    private TextView fosterTime;
    private TextView fosterDate;
    public View itemView;

    public FosterHolder(View v ) {
        super(v);
        fosterLogo = v.findViewById(R.id.fosterImageView);
        fosterPerson = v.findViewById(R.id.fosterPerson);
        fosterLocation = v.findViewById(R.id.fosterLocation);
        fosterTime = v.findViewById(R.id.fosterTime);
        fosterDate = v.findViewById(R.id.fosterDate);
        itemView = v;
    }

    public void setFosterLocation(String str) {
        fosterLocation.setText(str);
    }
    public void setFosterTime(String str) {
        fosterTime.setText(str);
    }
    public void setFosterDate(String str) {
        fosterDate.setText(str);
    }
    public void setFosterPerson(String str) {
        fosterPerson.setText(str);
    }
    public void setFosterProfilePic(Uri uri) {
        Picasso.with(itemView.getContext()).load(uri).into(fosterLogo);
    }
}