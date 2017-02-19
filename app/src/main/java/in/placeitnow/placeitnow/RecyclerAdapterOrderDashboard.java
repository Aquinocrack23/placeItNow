package in.placeitnow.placeitnow;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pranav Gupta on 2/18/2017.
 */

public class RecyclerAdapterOrderDashboard extends RecyclerView.Adapter<RecyclerAdapterOrderDashboard.ViewHolder>{

    private Activity activity;
    private ArrayList<OrderLayoutClass> orders;
    private String uid;

    public RecyclerAdapterOrderDashboard(Activity activity,ArrayList<OrderLayoutClass> orderContentses,String uid){
        this.activity =activity;
        this.orders = orderContentses;
        this.uid = uid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.order_dashboard_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vendor_name.setText(orders.get(position).getVendor_name());
        holder.order_id.setText(orders.get(position).getOrderID());
        holder.user_name.setText(orders.get(position).getDisplayName());
        Picasso.with(activity).load(R.drawable.ic_launcher).fit().into(holder.imageView);
        holder.progess_order_number.setText(orders.get(position).getProgress_order_number()+"");
        if(orders.get(position).isPaymentDone()){
            holder.order_payment.setText("Payment Done");
        }
        else {
            holder.order_payment.setText("Payment Pending");
        }
        if(orders.get(position).isOrderDone()){
            holder.status.setText("DONE");
        } else {
            holder.status.setText("PENDING");
        }

    }

    @Override
    public int getItemCount() {
        return ( null!=orders ? orders.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
         TextView vendor_name;
        TextView order_id;
        TextView order_payment;
        Button status;
        TextView user_name;
        TextView progess_order_number;
        ImageView imageView ;
        ViewHolder(View itemView) {
            super(itemView);
            vendor_name = (TextView)itemView.findViewById(R.id.vendor_name);
            order_id = (TextView)itemView.findViewById(R.id.order_id);
            order_payment = (TextView)itemView.findViewById(R.id.order_payment);
            status = (Button)itemView.findViewById(R.id.status);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            progess_order_number=(TextView)itemView.findViewById(R.id.progress_order_number);
            imageView = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}
