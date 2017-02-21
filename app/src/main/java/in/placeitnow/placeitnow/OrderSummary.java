package in.placeitnow.placeitnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pranav Gupta on 12/24/2016.
 */

public class OrderSummary extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView ven_name,customer_name,delivery_add,cn,date,time,amount;

    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;
    DatabaseReference rootRef,orderRef;
    DatabaseReference vendorRef;

    //user id
    private String uid;

    //recyclerview to get ordered items
    private RecyclerView recycler_view;
    private RecyclerAdapterSummary RASummary;
    private ArrayList<OrderItem> order= new ArrayList<>();
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
    //private HashMap<String,String> orderDetails = new HashMap<>();
    private ArrayList<OrderItem> orderDetails = new ArrayList<>();
    private Integer order_num;
    private DatabaseReference orderNumber;
    private String orderID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary);

        ven_name=(TextView)findViewById(R.id.name);
        recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
        customer_name=(TextView)findViewById(R.id.cust_name);
        delivery_add=(TextView)findViewById(R.id.delivery_add);
        cn=(TextView)findViewById(R.id.cn);
        date=(TextView)findViewById(R.id.date);
        time=(TextView)findViewById(R.id.time);
        amount=(TextView)findViewById(R.id.amount);

        recycler_view.setHasFixedSize(true);

        i =getIntent();
        orderSummary = (HashMap<String,String>)i.getSerializableExtra("details");
        orderDetails = (ArrayList<OrderItem>) i.getSerializableExtra("summary");
        vid = i.getStringExtra("vid");
        //show(vid);
        status = i.getStringExtra("status");

        customer_name.setText(orderSummary.get("Name"));
        delivery_add.setText(orderSummary.get("Address"));
        cn.setText(orderSummary.get("Cont"));
        date.setText(orderSummary.get("Date"));
        time.setText(orderSummary.get("Time"));
        amount.setText(orderSummary.get("amount"));

        ven_name.setText(orderSummary.get("vendorName"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderSummary.this);
        recycler_view.setLayoutManager(layoutManager);

        setRecyclerViewData();

        RASummary = new RecyclerAdapterSummary(OrderSummary.this,order);
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
                // ...
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

    private void setRecyclerViewData() {

        order.addAll(orderDetails);
        /*for(int j=0;j<orderDetails.size();j++) {
                 if(orderDetails.get("menuitem"+String.valueOf(j))!= null ){
                     if(!orderDetails.get("menuitem" + String.valueOf(j)).contentEquals("")&& !orderDetails.get("quantity"+String.valueOf(j)).contentEquals("") && !orderDetails.get("price"+String.valueOf(j)).contentEquals("") ) {
                       order.add(new OrderedItemContents(orderDetails.get("menuitem" + String.valueOf(j)), orderDetails.get("quantity" + String.valueOf(j)), orderDetails.get("price" + String.valueOf(j))));
                     }
                 }
             }*/

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

                long epoch;
                epoch = System.currentTimeMillis();
                orderID = vid.substring(21).toUpperCase()+String.valueOf(epoch).substring(6);
                final ProgressDialog pg = ProgressDialog.show(this,"PlaceItNow","Placing Your Order...");
                            /** always remember that firebase makes asynchronous calls and results are only present in this
                             * onDataChange callback method so if one tries to use value of vendor id out of this callback
                             * function immediately than most probably it will return null as vendor id has may not received its value
                             * but we try to access that so the best way to ensure proper results is that one should put all code which
                             * uses vendorID either inside this callback function or make some market to make sure that it has gor its
                             * value
                             * */

                String displayName = orderSummary.get("Name");
                String comment = orderSummary.get("Comment");
                String vendor_name = orderSummary.get("vendorName");

                //Creating order
                final OrderLayoutClass order = new OrderLayoutClass(orderID,
                        uid,displayName,orderDetails);
                order.setProgress_order_number(order_num+1);
                order.setComment(comment);
                //Add order on Vendor side
                DatabaseReference ref = databaseReference.child("vendors").child(vid).child("orders").push().getRef();
                String key = ref.getKey();
                order.setTime(epoch);
                order.setVendor_name(vendor_name);
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
                            Toast.makeText(OrderSummary.this,"Your order number is "+String.valueOf(order_num+1),Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(OrderSummary.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }

                    }
                });
        }
        return super.onOptionsItemSelected(item);
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
