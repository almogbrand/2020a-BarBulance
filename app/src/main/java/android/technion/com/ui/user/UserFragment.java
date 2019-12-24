package android.technion.com.ui.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.technion.com.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.net.URL;



public class UserFragment extends Fragment {

    private UserViewModel notificationsViewModel;
    private FirebaseAuth mAuth;

    //TEMP - TO BE DELETED AFTER CONNECTING TO db
     String user_name = "";
     String user_email = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        notificationsViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            {
                //Update name field from Facebook's data
                user_name = currentUser.getDisplayName();    // CHANGE TO db FIELD
                TextView name_view = root.findViewById(R.id.userName);
                name_view.setText(user_name);
                name_view.setFocusable(false);

                //Update Email field from Facebook's data
                user_email = currentUser.getEmail();    // CHANGE TO db FIELD
                TextView email_view = root.findViewById(R.id.userEmail);
                email_view.setText(user_email);
                email_view.setFocusable(false);

                //Update user profile picture
                ImageView imgView = root.findViewById(R.id.profile_image);
                Uri imgUri = currentUser.getPhotoUrl();
                imgView.setImageURI(imgUri);
                Picasso.with(getActivity()).load(imgUri).into(imgView);
            }
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            user_name = currentUser.getDisplayName();
        }
//        if(!currentUser.getDisplayName().equals(null)){
//            user_name = currentUser.getDisplayName();
//        }



    }
}