package in.placeitnow.placeitnow;

import android.app.Application;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pranav Gupta on 12/20/2016.
 */

public class PlaceItNow extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
        /** Calls to setPersistenceEnabled() must be made before any other usage of FirebaseDatabase instance.
         * so it is better to put this inside class which extends Application
         * */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
