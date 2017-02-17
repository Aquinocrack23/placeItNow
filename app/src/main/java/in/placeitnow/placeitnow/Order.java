package in.placeitnow.placeitnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

/**
 * Created by Pranav Gupta on 12/22/2016.
 */
public class Order extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<Vendor> vendorsArrayList;
    private ArrayList<Vendor> selectedVendorList;

    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view=  getActivity().getLayoutInflater().inflate(R.layout.order,container,false);


        /** getSupportActionBar is only present in AppCompatActivity while getActivity returns FragmentActivity so we first
         * need to cast to AppCompatActivity to use that method
         *
         * */
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order");

        //Firebase Auth
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();

                } else {
                    //User is signed out
                    Toast.makeText(getActivity(),"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(),LoginActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        vendorsArrayList = new ArrayList<>();
        selectedVendorList = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        setRecyclerViewData(); //adding data to array list
        adapter = new RecyclerAdapter(getActivity(), vendorsArrayList);
        recyclerView.setAdapter(adapter);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(getActivity(),MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerViewData() {
        databaseReference.child("vendors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String vid = dataSnapshot.getKey();
                String name = dataSnapshot.child("details").child("displayName").getValue(String.class);
                Boolean status = dataSnapshot.child("details").child("status").getValue(Boolean.class);
                //show(vid +" "+name+" "+ status);
                vendorsArrayList.add(new Vendor(vid,name,status));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    private void show(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
