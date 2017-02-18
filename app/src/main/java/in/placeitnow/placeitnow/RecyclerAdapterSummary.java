package in.placeitnow.placeitnow;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

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
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.name.setText(menu.get(position).getItemName());
        viewHolder.price.setText(String.valueOf(menu.get(position).getItemPrice()));
        viewHolder.quantity.setText(menu.get(position).getItemQuantity());
    }

    @Override
    public int getItemCount() {

        return (null != menu ? menu.size() : 0);
    }
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private TextView quantity;
        private View container;

        public ViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.item_price);
            quantity = (TextView) view.findViewById(R.id.no_of_items);
            container = view.findViewById(R.id.card_view);
        }
    }
}
