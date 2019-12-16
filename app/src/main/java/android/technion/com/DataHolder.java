package android.technion.com;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DataHolder extends RecyclerView.ViewHolder{

    public TextView value;
    public TextView letter;

    public DataHolder(View v ) {
        super(v);
        value = v.findViewById(R.id.comment_content);
        letter = v.findViewById(R.id.comment_index);
    }

    public void setValue(String str) {
        value.setText(str);
    }

    public void setLetter(String str) {
        letter.setText(str);
    }
}
