package in.placeitnow.placeitnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Pranav Gupta on 12/22/2016.
 */
public class UserAccount extends Fragment implements View.OnClickListener{


    private TextView username;
    private LinearLayout order_history;
    private View view;

    private FirebaseDatabase database;
    private DatabaseReference rootRef;

    //constant id for a user
    private String uid;
    private String user_name;

    //Firebase authentication
    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=  getActivity().getLayoutInflater().inflate(R.layout.account,container,false);

        /** getSupportActionBar is only present in AppCompatActivity while getActivity returns FragmentActivity so we first
         * need to cast to AppCompatActivity to use that method
         *
         * */
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("User Account");

        setViews();

        order_history.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();

        fetchUserUIDthroughLogin();

        return view;
    }

    private void setViews() {
        username = (TextView)view.findViewById(R.id.user_name);
        order_history=(LinearLayout) view.findViewById(R.id.order_history);
    }

    private void fetchUserUIDthroughLogin() {
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    uid = firebaseAuth.getCurrentUser().getUid();
                    setUpFetchingDataForUserFromDatabase();
                }
                else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
            }
        };
    }

    private void setUpFetchingDataForUserFromDatabase() {

        rootRef.child("users").child(uid).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.getValue(String.class);
                username.setText(user_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        auth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_history:
                startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }
}
