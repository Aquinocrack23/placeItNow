package in.placeitnow.placeitnow;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<Vendor> vendorsArrayList;
    private BottomBar mBottomBar;
    private ArrayList<Vendor> selectedVendorList= new ArrayList<>();
    private FirebaseAuth auth;                         //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference("orders");

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
                    Toast.makeText(MainActivity.this,"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };

        vendorsArrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);

        recyclerView.setHasFixedSize(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setRecyclerViewData(); //adding data to array list
        adapter = new RecyclerAdapter(this, vendorsArrayList);
        recyclerView.setAdapter(adapter);
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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                filter(searchQuery.toString().trim());
                recyclerView.invalidate();
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
        vendorsArrayList.add(new Vendor("Bake Hut",true));
        vendorsArrayList.add(new Vendor("Treat Kamand",true));
        vendorsArrayList.add(new Vendor("Priya Fruit Juice",true));
        vendorsArrayList.add(new Vendor("River Bank",true));
        selectedVendorList.addAll(vendorsArrayList);
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

        vendorsArrayList.clear();
        if (charText.length() == 0) {
            vendorsArrayList.addAll(selectedVendorList);

        } else {
            for (Vendor VendorDetail : selectedVendorList) {
                if (charText.length() != 0 && VendorDetail.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    vendorsArrayList.add(VendorDetail);
                }

                else if (charText.length() != 0) {
                    vendorsArrayList.add(VendorDetail);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
//        System.exit(0);
    }


}