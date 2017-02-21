package in.placeitnow.placeitnow;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MySimpleService extends IntentService {
	public static final int NOTIF_ID = 56;
	long timestamp;
	DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
	private String uid;
	
	// Must create a default constructor
	public MySimpleService() {
		super("simple-service");
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		setRecyclerViewData();
        return super.onStartCommand(intent, flags, startId);
    }

    // This describes what will happen when service is triggered
	@Override
	protected void onHandleIntent(Intent intent) {

		timestamp =  System.currentTimeMillis();
	    // Extract additional values from the bundle
	    uid = intent.getStringExtra("uid");
        Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_SHORT).show();

		// Extract the receiver passed into the service
	    ResultReceiver rec = intent.getParcelableExtra("receiver");
	    // Sleep a bit first
	    //sleep(3000);
	    // Send result to activity
	    sendResultValue(rec, uid);
        //setRecyclerViewData();
        //createNotification(uid);
	}

	// Send result to activity using ResultReceiver
	private void sendResultValue(ResultReceiver rec, String val) {
		// To send a message to the Activity, create a pass a Bundle
	    Bundle bundle = new Bundle();
	    bundle.putString("resultValue", "My Result Value. You Passed in: " + val + " with timestamp: " + timestamp);
	    // Here we call send passing a resultCode and the bundle of extras
	    rec.send(Activity.RESULT_OK, bundle);		
	}
	
	// Construct compatible notification
	private void createNotification(String val) {
		// Construct pending intent to serve as action for notification item
		Intent intent = new Intent(this, BaseActivityFragment.class);
		intent.putExtra("message", "Order Updates");
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// Create notification
		String longText = "Order Updates";
		Notification noti =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_home_white_24dp)
		        .setContentTitle("PlaceItNow")
		        .setContentText("You have Order Updates")
		        .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
		        .setContentIntent(pIntent)
		        .build();
		
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIF_ID, noti);
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setRecyclerViewData() {

		rootRef.child(uid).child("orders").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				rootRef.child(uid).child("orders").child(dataSnapshot.getKey()).addChildEventListener(new ChildEventListener() {
					@Override
					public void onChildAdded(DataSnapshot dataSnapshot, String s) {

					}

					@Override
					public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                       createNotification(uid);
					}
					@Override
					public void onChildRemoved(DataSnapshot dataSnapshot) {

					}

					@Override
					public void onChildMoved(DataSnapshot dataSnapshot, String s) {

					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                createNotification(uid);
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

}