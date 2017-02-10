package in.placeitnow.placeitnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<OrderedItemContents> order= new ArrayList<>();
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
    private ArrayList<OrderedItemContents> orderDetails = new ArrayList<>();

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
        orderDetails = (ArrayList<OrderedItemContents>) i.getSerializableExtra("summary");
        vid = i.getStringExtra("vid");
        show(vid);
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
        rootRef = database.getReference("orders");
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
                final ProgressDialog pg = ProgressDialog.show(this,"PlaceItNow","Placing Your Order...");
                            /** always remember that firebase makes asynchronous calls and results are only present in this
                             * onDataChange callback method so if one tries to use value of vendor id out of this callback
                             * function immediately than most probably it will return null as vendor id has may not received its value
                             * but we try to access that so the best way to ensure proper results is that one should put all code which
                             * uses vendorID either inside this callback function or make some market to make sure that it has gor its
                             * value
                             * */
                orderString = vid+uid ;
                orderRef = rootRef.child(orderString).push();
                orderId = orderRef.getKey();
                newOrder = new OrderContents(orderSummary.get("Name"),orderSummary.get("Cont"),orderSummary.get("Address"),orderSummary.get("Time"),orderSummary.get("Date"),orderSummary.get("amount"),orderSummary.get("vendorName"),"5",orderId,"8",orderDetails);
                orderRef.setValue(newOrder, new DatabaseReference.CompletionListener() {
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
                String displayName = orderSummary.get("Name");

                //Creating order items list
                ArrayList<OrderItem> orderItems = new ArrayList<>();
                orderItems.add(new OrderItem("-Kb-3CqIo_FYQ4evMoiT", "Burger", 40.0, 1));
                //Creating order
                OrderLayout order = new OrderLayout(uid,displayName,orderItems);
                //Add order on Vendor side
                DatabaseReference ref = databaseReference.child("vendors").child(vid).child("orders").push().getRef();
                ref.setValue(order, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){
                            pg.dismiss();
                            show("Order Successfully Placed");
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
