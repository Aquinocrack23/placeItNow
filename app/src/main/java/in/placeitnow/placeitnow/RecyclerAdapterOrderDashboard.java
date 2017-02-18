package in.placeitnow.placeitnow;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pranav Gupta on 2/18/2017.
 */

public class RecyclerAdapterOrderDashboard extends RecyclerView.Adapter<RecyclerAdapterOrderDashboard.ViewHolder>{

    Activity activity;
    ArrayList<OrderContents> orders;

    public RecyclerAdapterOrderDashboard(Activity activity,ArrayList<OrderContents> orderContentses){
        this.activity =activity;
        this.orders = orderContentses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.order_dashboard_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vendor_name.setText(orders.get(position).getVendor());
        holder.order_id.setText(orders.get(position).getOrderId());
        holder.order_payment.setText(orders.get(position).getOrder_payment_status());


    }

    @Override
    public int getItemCount() {
        return ( null!=orders ? orders.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
         TextView vendor_name;
        TextView order_id;
        TextView order_payment;
        public ViewHolder(View itemView) {
            super(itemView);
            vendor_name = (TextView)itemView.findViewById(R.id.vendor_name);
            order_id = (TextView)itemView.findViewById(R.id.order_id);
            order_payment = (TextView)itemView.findViewById(R.id.order_payment);
        }
    }
}
