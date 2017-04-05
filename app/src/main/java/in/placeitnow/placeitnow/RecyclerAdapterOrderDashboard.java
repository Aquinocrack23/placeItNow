package in.placeitnow.placeitnow;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Pranav Gupta on 2/18/2017.
 */

public class RecyclerAdapterOrderDashboard extends RecyclerView.Adapter<RecyclerAdapterOrderDashboard.ViewHolder> {

    private Activity activity;
    private ArrayList<OrderLayoutClass> orders;
    private String uid;
    private String order_description;

    public RecyclerAdapterOrderDashboard(Activity activity,ArrayList<OrderLayoutClass> orderContentses,String uid){
        this.activity =activity;
        this.orders = orderContentses;
        this.uid = uid;
        this.order_description="";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.order_dashboard_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        order_description = "";
        if(orders.get(position).getVendor_name()!=null){
            holder.vendor_name.setText(orders.get(position).getVendor_name());
        }

        if(orders.get(position).getOrderID()!=null){
            holder.order_id.setText("ID : "+ orders.get(position).getOrderID());
        }

        if(orders.get(position).getDisplayName()!=null){
            holder.user_name.setText(orders.get(position).getDisplayName());
        }

        long timestamp = Long.parseLong(String.valueOf(orders.get(position).getTime())) / 1000;
        String date_format = GetHumanReadableDate(timestamp, "dd-MM-yyyy HH:mm:ss aa");
        holder.date_time.setText(date_format);
        holder.imageView.setImageResource(R.drawable.burger);
        if(orders.get(position).getAmount()!=null){
            holder.amount.setText("Total : "+orders.get(position).getAmount()+"");
        }

        if(orders.get(holder.getAdapterPosition()).getOrders_before_yours()==0){
            holder.orders_before.setTextSize(20);
            holder.orders_before.setText("Making");
        }else {
            holder.orders_before.setText((orders.get(position).getOrders_before_yours())+"");
        }
        if(orders.get(position).isOrderDone()){
            holder.orders_before.setText("Done");
        }
        holder.progess_order_number.setText("Your Order Number : "+orders.get(position).getProgress_order_number()+"");
        for(int i =0;i<orders.get(position).getItems().size();i++){
            order_description+= orders.get(position).getItems().get(i).getItemName()+" ("+orders.get(position).getItems().get(i).getItemQuantity() +") : "
                    +orders.get(position).getItems().get(i).getItemPrice()+"\n";
        }
        holder.contents.setText(order_description);
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
        TextView amount;
        TextView contents;
        TextView orders_before;
        TextView date_time;
        ViewHolder(View itemView) {
            super(itemView);
            vendor_name = (TextView)itemView.findViewById(R.id.vendor_name);
            order_id = (TextView)itemView.findViewById(R.id.order_id);
            order_payment = (TextView)itemView.findViewById(R.id.order_payment);
            status = (Button)itemView.findViewById(R.id.status);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            progess_order_number=(TextView)itemView.findViewById(R.id.progress_order_number);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            amount = (TextView)itemView.findViewById(R.id.amount);
            contents = (TextView)itemView.findViewById(R.id.order_contents);
            orders_before=(TextView)itemView.findViewById(R.id.orders_before);
            date_time = (TextView)itemView.findViewById(R.id.date_and_time);
        }
    }

    public static String GetHumanReadableDate(long epochSec, String dateFormatStr) {
        Date date = new Date(epochSec * 1000);
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStr,
                Locale.getDefault());
        return format.format(date);
    }

}
