package android.technion.com.ui.rides;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RidesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RidesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No rides");
    }

    public LiveData<String> getText() {
        return mText;
    }
}