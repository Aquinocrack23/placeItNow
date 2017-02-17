package in.placeitnow.placeitnow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pranav Gupta on 12/26/2016.
 */

public class OrderLocation extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup pay;
    private String payment;
    private EditText name,address,contactno,comment;
    private TextView date,time;
    private HashMap<String,String> orderDet = new HashMap<>();
    private Intent i;
    //private HashMap<String,String> summary = new HashMap<>();
    private ArrayList<OrderedItemContents> summary = new ArrayList<>();
    private TextView name_vendor;
    private String vid;
    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        pay=(RadioGroup)findViewById(R.id.select_payment_method);
        name_vendor = (TextView)findViewById(R.id.name);
        initialise();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.inflateMenu(R.menu.order_selection_botton);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        i = getIntent();
        name_vendor.setText(i.getStringExtra("vendorname"));
        summary = (ArrayList<OrderedItemContents>) i.getSerializableExtra("summary");
        vid = i.getStringExtra("vid");
        status = i.getStringExtra("status");
        pay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.paytm:
                        payment = "PayTM";
                        break;
                    case R.id.credit_debit:
                        payment = "Credit Or Debit Card";
                        break;
                    case R.id.cod:
                        payment = "Cash On Delivery";
                        break;
                }
            }
        });


    }

    private void initialise() {

        name=(EditText)findViewById(R.id.name_edit);
        address=(EditText)findViewById(R.id.add_edit);
        contactno=(EditText)findViewById(R.id.no_edit);
        comment=(EditText)findViewById(R.id.comment_edit);
        date= (TextView)findViewById(R.id.btn1);
        time=(TextView)findViewById(R.id.btn2);

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

                orderDet.put("Name",name.getText().toString());
                orderDet.put("Date",date.getText().toString());
                orderDet.put("Time",time.getText().toString());
                orderDet.put("Address",address.getText().toString());
                orderDet.put("Cont",contactno.getText().toString());
                if(!comment.getText().toString().contentEquals("")){
                    orderDet.put("Comment",comment.getText().toString());
                }
                orderDet.put("vendorName",i.getStringExtra("vendorname"));
                orderDet.put("amount",i.getStringExtra("amount"));
                Intent i = new Intent(OrderLocation.this,OrderSummary.class);
                i.putExtra("details",orderDet);
                i.putExtra("summary",summary);
                i.putExtra("vid",vid);
                i.putExtra("status",status);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}
