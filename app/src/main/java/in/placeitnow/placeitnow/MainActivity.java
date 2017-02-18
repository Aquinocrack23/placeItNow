package in.placeitnow.placeitnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomBar mBottomBar;
    private FirebaseAuth auth;                         //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;
    private RecyclerAdapterOrderDashboard dashboard;
    private ArrayList<OrderLayoutClass> orderContents;
    private ArrayList<OrderLayoutClass> selectedOrderContents = new ArrayList<>();
    private DatabaseReference rootRef;
    private DatabaseReference vendorRef;
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootRef = database.getReference("users");
        vendorRef = database.getReference("vendors");

        //insantiating the objects
        /** always make sure to instantiate the arraylist before linking it to recycler adapter otherwise no data will be shown
         * */
        orderContents = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dashboard = new RecyclerAdapterOrderDashboard(MainActivity.this,orderContents,uid);
        recyclerView.setAdapter(dashboard);
        dashboard.notifyDataSetChanged();

        /** using function recycler view outside data was crashing the app because we use uid to get data set but this
         * getting uid is done asynchronously so uid might not be fetch till we get data so using setRecyclerViewData() inside
         * this completion listener ensures that uid is not null
         * */
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    setRecyclerViewData();
                } else {
                    //User is signed out
                    Toast.makeText(MainActivity.this,"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };



        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //setRecyclerViewData(); //adding data to array list

        // Customize the colors here
        mBottomBar = BottomBar.attach(this, savedInstanceState,
                Color.parseColor("#212121"), // Background Color
                ContextCompat.getColor(this,R.color.cardview_light_background), // Tab Item Color
                0.25f); // Tab Item Alpha
        mBottomBar.setItems(R.menu.bottom_bar_menu);


        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {

            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                showToast(menuItemId, false);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                showToast(menuItemId, true);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                filter(searchQuery.trim());
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.home){
            Intent i = new Intent(MainActivity.this,BaseActivityFragment.class);
            startActivity(i);

        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerViewData() {

        rootRef.child(uid).child("orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 rootRef.child(uid).child("orders").child(dataSnapshot.getKey()).addChildEventListener(new ChildEventListener() {
                     @Override
                     public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                         String key = dataSnapshot.getKey();
                         OrderLayoutClass orderLayoutClass = dataSnapshot.getValue(OrderLayoutClass.class);
                         orderLayoutClass.setOrderKey(key);
                         if(!checkIfPresent(orderLayoutClass)){
                             orderContents.add(0,orderLayoutClass);
                             selectedOrderContents.add(0,orderLayoutClass);
                         }
                         dashboard.notifyDataSetChanged();
                     }

                     @Override
                     public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                         OrderLayoutClass orderLayoutClass = dataSnapshot.getValue(OrderLayoutClass.class);
                         for (int i=0;i<orderContents.size();i++){
                             orderContents.get(i).setPaymentDone(orderLayoutClass.isPaymentDone());
                             orderContents.get(i).setOrderDone(orderLayoutClass.isOrderDone());
                             dashboard.notifyDataSetChanged();
                         }
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

    private boolean checkIfPresent(OrderLayoutClass orderLayoutClass) {
        for(int i =0;i<orderContents.size();i++){
            if(orderContents.get(i).getOrderKey().contentEquals(orderLayoutClass.getOrderKey())){
                return true;
            }
        }
        return false;
    }

    private void showToast(int menuId, boolean isReselected) {
        if (menuId == R.id.like) {
            if (isReselected) {
                show("Tab Reselected!");
            } else {
                show("Find the live items!");
            }
        }
        else if (menuId == R.id.love) {
            if (isReselected) {
                show("Tab Love Reselected!");
            }
            else
                show("Find new addons!");
        }
        else if (menuId == R.id.sad) {
            if (isReselected) {
                show("Tab Sad Reselected");
            }
            else {
                show("Find coupons");
            }
            }
        else if (isReselected) {
            FirebaseAuth.getInstance().signOut();
            //show("Tab Angry Reselected!");
            show("Signed Out");
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }
        else show("Press again to logout");
    }

    private void show(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    public void filter(String charText) {

        charText = charText.toLowerCase();

        orderContents.clear();
        if (charText.length() == 0) {
            orderContents.addAll(selectedOrderContents);

        } else {
            for (OrderLayoutClass order : selectedOrderContents) {
                if (charText.length() != 0 && order.getDisplayName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    orderContents.add(order);
                }
            }
        }
        dashboard.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }


}