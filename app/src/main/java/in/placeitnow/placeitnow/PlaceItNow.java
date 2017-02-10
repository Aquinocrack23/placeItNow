package in.placeitnow.placeitnow;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Pranav Gupta on 12/20/2016.
 */

public class PlaceItNow extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
