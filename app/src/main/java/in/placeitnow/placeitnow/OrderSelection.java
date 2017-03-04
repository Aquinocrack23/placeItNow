package in.placeitnow.placeitnow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Pranav Gupta on 12/24/2016.
 */

public class OrderSelection extends AppCompatActivity implements View.OnClickListener{


    private RecyclerView recyclerView;
    private RecyclerAdapterMenuItem RAMItem,RAMItemforFilter;

    private CardView all,starters,maincourse,beverages,sweets,dinner,snacks;
    private ArrayList<Menu> menuList;
    private ArrayList<OrderItem> orderedList;
    private ArrayList<Menu> filteredMenuList;

    private Toolbar toolbar;
    private TextView vend_name,stat;

    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    private HashMap<String,String> orderDet = new HashMap<>();
    private String uid,vendor;
    private Button finalize;


    //map to store the order contents
    private HashMap<String,String> orderList = new HashMap<String, String>();
    private String quantity;
    private String payment;
    private EditText name,date,time,address,phno,comment;
    private FirebaseDatabase database;
    private DatabaseReference rootRef;
    private DatabaseReference user_ref;
    private String vid,status;
    private String user_name,contact_no,user_address;
    private DatabaseReference menu_list;
    private ChildEventListener menu_list_listener;
    private int amount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_selection);

        setOnClickCards();
        initialiseEditTexts();

        vend_name=(TextView)findViewById(R.id.vendor_name);
        stat=(TextView)findViewById(R.id.status);


        //FireBase References
        database = FirebaseDatabase.getInstance();
        rootRef =database.getReference("vendors");
        user_ref = database.getReference("users");


        //Firebase Auth
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    fetchUserDetails();

                } else {
                    //User is signed out
                    Toast.makeText(OrderSelection.this,"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(OrderSelection.this,LoginActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.inflateMenu(R.menu.order_selection_botton);
        toolbar.setTitle("Proceed");
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_menu);
        recyclerView.setHasFixedSize(true);

        //Required for recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Instantiating arraylists
        menuList = new ArrayList<>();
        filteredMenuList = new ArrayList<>();
        orderedList = new ArrayList<>();

        Intent i = getIntent();
        vendor = i.getStringExtra("vendorname");
        vid = i.getStringExtra("vid");
        status = i.getStringExtra("status");
        vend_name.setText(vendor);
        stat.setText(status);

        //fill the arraylist
        fillMenu();

        RAMItem = new RecyclerAdapterMenuItem(this,menuList,orderedList);
        recyclerView.setAdapter(RAMItem);

    }

    private void fetchUserDetails() {

        user_ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.child("Name").getValue(String.class);
                user_address=dataSnapshot.child("Address").getValue(String.class);
                contact_no=dataSnapshot.child("Contact").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initialiseEditTexts() {
        name=(EditText)findViewById(R.id.name);
    }

    private void setOnClickCards() {
        all = (CardView)findViewById(R.id.all);
        starters = (CardView)findViewById(R.id.starters);
        beverages = (CardView)findViewById(R.id.beverages);
        sweets = (CardView)findViewById(R.id.sweets);
        dinner = (CardView)findViewById(R.id.dinner);
        maincourse= (CardView)findViewById(R.id.maincourse);
        snacks=(CardView)findViewById(R.id.snacks);
        all.setOnClickListener(this);
        snacks.setOnClickListener(this);
        starters.setOnClickListener(this);
        beverages.setOnClickListener(this);
        sweets.setOnClickListener(this);
        dinner.setOnClickListener(this);
        maincourse.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_selection_botton,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.payment:
                if(orderedList.isEmpty()){
                    Toast.makeText(OrderSelection.this,"Sorry please select some items first",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(OrderSelection.this,OrderSummary.class);
                    intent.putExtra("vendorname",vendor);
                    intent.putExtra("amount",amount);
                    intent.putExtra("summary",orderedList);
                    intent.putExtra("payment_mode",payment);
                    intent.putExtra("vid",vid);
                    intent.putExtra("status",status);
                    intent.putExtra("user_name",user_name);
                    intent.putExtra("contact",contact_no);
                    intent.putExtra("address",user_address);
                    startActivity(intent);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /** in case when we directly take snapshot of POJO from firebase database ensure that your class has all getter and setter of the type
     * of which data is present in database
     *
     * or either you create your custom POJO and instantiate cbject by using constructor and fetching data by absolute address (i.e get strings)
     * as described in below example
     * */
    private void fillMenu() {

        menu_list = rootRef.child(vid).child("menuItems");
        menu_list.keepSynced(true);
        menu_list_listener = menu_list.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("status").getValue(Boolean.class)){
                    Menu item =new Menu(dataSnapshot.getKey(),dataSnapshot.child("itemName").getValue(String.class),dataSnapshot.child("itemPrice").getValue(Integer.class),
                            "customizations available",dataSnapshot.child("itemType").getValue(String.class),0);
                    menuList.add(item);
                    filteredMenuList.add(item);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void filter(String charText) {

        charText = charText.toLowerCase();

        menuList.clear();

        if (charText.length() == 0) {

            menuList.addAll(filteredMenuList);

        } else {
            for (Menu MenuDetail : filteredMenuList) {
                if (charText.length() != 0 && MenuDetail.getType().toLowerCase(Locale.getDefault()).contains(charText)) {
                    menuList.add(MenuDetail);
                }

                else if (charText.length() != 0 && MenuDetail.getType().toLowerCase(Locale.getDefault()).contains(charText)) {
                    menuList.add(MenuDetail);
                }
            }
        }
        RAMItem.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all:
                menuList.clear();
                menuList.addAll(filteredMenuList);
                RAMItem.notifyDataSetChanged();
                break;
            case R.id.snacks:
                filter("snacks");
                break;
            case R.id.maincourse:
                filter("maincourse");
                break;
            case R.id.dinner:
                filter("dinner");
                break;
            case R.id.beverages:
                filter("beverages");
                break;
            case R.id.sweets:
                filter("sweets");
                break;
            case R.id.starters:
                filter("starters");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.addAuthStateListener(mAuthListener);
        if(menu_list!=null&&menu_list_listener!=null){
            menu_list.removeEventListener(menu_list_listener);
        }
    }
}
