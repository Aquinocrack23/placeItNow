package in.placeitnow.placeitnow.recycleradapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.placeitnow.placeitnow.R;
import in.placeitnow.placeitnow.pojo.OrderItem;

/**
 * Created by Pranav Gupta on 12/23/2016.
 */

public class RecyclerAdapterSummary extends RecyclerView.Adapter<RecyclerAdapterSummary.ViewHolder>{

    private List<OrderItem> menu;
    private Activity activity;
    private int val;

    public RecyclerAdapterSummary(Activity activity, List<OrderItem> menu) {

        this.menu = menu;
        this.activity =activity;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.menu_ordered_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.name.setText(menu.get(position).getItemName());
        viewHolder.price.setText(String.valueOf(menu.get(position).getItemPrice()));
        viewHolder.quantity.setText(String.valueOf(menu.get(position).getItemQuantity()));
        /*viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.remove(menu.get(position-1));
                Toast.makeText(activity,menu.get(position).getItemQuantity()+" "+menu.get(position).getItemName()+" removed",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {

        return (null != menu ? menu.size() : 0);
    }
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private TextView quantity;

        public ViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.item_price);
            quantity = (TextView) view.findViewById(R.id.no_of_items);
        }
    }
}
