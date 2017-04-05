package in.placeitnow.placeitnow.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.placeitnow.placeitnow.pojo.OrderLayoutClass;
import in.placeitnow.placeitnow.R;
import in.placeitnow.placeitnow.recycleradapters.RecyclerAdapterSummary;
import in.placeitnow.placeitnow.pojo.OrderContents;
import in.placeitnow.placeitnow.pojo.OrderItem;

/**
 * Created by Pranav Gupta on 12/24/2016.
 */

public class OrderSummary extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView ven_name,customer_name,delivery_add,cn,date,time,amount,order_id;

    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase database;
    private DatabaseReference rootRef,orderRef;
    private DatabaseReference vendorRef;

    private String[] month = new String[]{"","January","February","March","April","May","June","July","August","September","October","November","December"};
    private String ampm = "AM";

    //user id
    private String uid;

    //recyclerview to get ordered items
    private RecyclerView recycler_view;
    private RecyclerAdapterSummary RASummary;
    //keep track of user and vendor order id (it contain three parts)
    private String orderString;
    private String vendorId;
    private String orderId;
    //object of OrderContent Class
    private OrderContents newOrder;
    private Intent i;
    private String vid,status;
    private DatabaseReference databaseReference;

    private HashMap<String,String> orderSummary = new HashMap<>();

    private ArrayList<OrderItem> orderDetails = new ArrayList<>();
    private Integer order_num;
    private DatabaseReference orderNumber;
    private String orderID;
    private long epoch;
    private String displayName;
    private String vendor_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary);

        setAllViews();

        getRevelantData();

        epoch = System.currentTimeMillis();
        orderID = vid.substring(21).toUpperCase()+String.valueOf(epoch).substring(6);

        recycler_view.setHasFixedSize(true);

        fillDataInOrderSummary();

        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderSummary.this);
        recycler_view.setLayoutManager(layoutManager);

        RASummary = new RecyclerAdapterSummary(OrderSummary.this,orderDetails);
        recycler_view.setAdapter(RASummary);


        //getting database references
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        rootRef = database.getReference("users");
        vendorRef = database.getReference("vendors");


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
                    Toast.makeText(OrderSummary.this,"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(OrderSummary.this,LoginActivity.class);
                    startActivity(i);
                }
            }
        };

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.inflateMenu(R.menu.order_selection_botton);

        //fetching the current order number
        orderNumber = databaseReference.child("vendors").child(vid).child("orderNumber");
        orderNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order_num = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fillDataInOrderSummary(){
        date.setText(generateDateFromSystem());
        time.setText(generateCurrentTime());
        order_id.setText("OrderID : "+orderID);
    }

    private void setAllViews() {
        ven_name=(TextView)findViewById(R.id.name);
        recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
        customer_name=(TextView)findViewById(R.id.cust_name);
        delivery_add=(TextView)findViewById(R.id.delivery_add);
        cn=(TextView)findViewById(R.id.cn);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        amount=(TextView)findViewById(R.id.amount);
        order_id=(TextView)findViewById(R.id.order_id);
    }

    private void getRevelantData() {
        i =getIntent();
        orderDetails = (ArrayList<OrderItem>) i.getSerializableExtra("summary");
        displayName = i.getStringExtra("user_name");
        vendor_name = i.getStringExtra("vendorname");
        vid = i.getStringExtra("vid");
        status = i.getStringExtra("status");
        customer_name.setText(i.getStringExtra("user_name"));
        delivery_add.setText(i.getStringExtra("address"));
        cn.setText(i.getStringExtra("contact"));
        amount.setText(i.getStringExtra("amount"));
        ven_name.setText(i.getStringExtra("vendorname"));
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
                new AlertDialog.Builder(this)
                        .setTitle("PlaceItNow")
                        .setMessage("Do you want to finalize the order?")
                        .setIcon(android.R.drawable.ic_menu_upload)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                placeOrder();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void placeOrder() {
        final ProgressDialog pg = ProgressDialog.show(this,"PlaceItNow","Placing Your Order...");
        /** always remember that firebase makes asynchronous calls and results are only present in this
         * onDataChange callback method so if one tries to use value of vendor id out of this callback
         * function immediately than most probably it will return null as vendor id has may not received its value
         * but we try to access that so the best way to ensure proper results is that one should put all code which
         * uses vendorID either inside this callback function or make some market to make sure that it has gor its
         * value
         * */
        //Creating order
        final OrderLayoutClass order = new OrderLayoutClass(orderID,
                displayName,uid,orderDetails,vendor_name,order_num+1,epoch);

        //Add order on Vendor side
        DatabaseReference ref = databaseReference.child("vendors").child(vid).child("orders").push().getRef();
        String key = ref.getKey();
        orderRef = rootRef.child(uid).child("orders").child(vid).child(key);
        ref.setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    pg.setMessage("Sending Order to Vendor...");
                    show("Order Successfully Placed");
                }
            }
        });

        orderRef.setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    pg.setMessage("Completing Your Order...");
                }
                else {
                    pg.dismiss();
                    show("Some Error Occurred Please try again");
                }
            }
        });
        orderNumber.setValue(order_num + 1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    pg.dismiss();
                    Toast.makeText(OrderSummary.this,"Your order number is "+String.valueOf(order_num),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(OrderSummary.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

            }
        });
    }

    private String generateCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        if (Integer.valueOf(strDate.substring(11, 13)) > 12) {
            ampm = "PM";
        }
        return strDate.substring(10, strDate.length()) + " " + ampm;
    }

    private String generateDateFromSystem() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate.substring(0, 2) + ", " + month[Integer.valueOf(strDate.substring(3, 5))] + " " + strDate.substring(6, 10);
    }

    public void show(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /** its important to attach auth state listener on activity start to get uid and cross check whether user is logged in or not
     * and also remember that auth listener is only fired if this is attached
     * */
    @Override
    public void onStart() {
        super.onStart();
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
