package in.placeitnow.placeitnow;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Pranav Gupta on 12/23/2016.
 */

public class RecyclerAdapterMenuItem extends RecyclerView.Adapter<RecyclerAdapterMenuItem.ViewHolder>{

    private List<Menu> menu;
    private List<OrderItem> ordered;
    private Activity activity;
    private int val;
    Integer[] items = new Integer[]{0,1,2,3,4,5,6,7,8,9,10};

    public RecyclerAdapterMenuItem(Activity activity, List<Menu> menu,List<OrderItem> ordered) {

        this.menu = menu;
        this.ordered=ordered;
        this.activity =activity;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.menu_item_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterMenuItem.ViewHolder viewHolder, final int position) {
        viewHolder.name.setText(menu.get(position).getMenu_item());
        viewHolder.price.setText(""+menu.get(position).getPrice()+"");
        viewHolder.message.setText(menu.get(position).getMessage());
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val ==0){
                    Toast.makeText(activity,"Please Select Some Quantity first",Toast.LENGTH_SHORT).show();
                }
                else {
                    menu.get(viewHolder.getAdapterPosition()).setQuantity(val);
                    ordered.add(new OrderItem(menu.get(viewHolder.getAdapterPosition()).getItem_key(),
                            menu.get(viewHolder.getAdapterPosition()).getMenu_item(), (double) menu.get(position).getPrice(),
                            menu.get(position).getQuantity()));
                    Toast.makeText(activity.getBaseContext(),val+" "+menu.get(viewHolder.getAdapterPosition()).getMenu_item()+" added",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(activity.getBaseContext(),android.R.layout.simple_spinner_item, items);
        viewHolder.sp.setAdapter(adapter);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //
        viewHolder.sp.setOnItemSelectedListener(onItemSelectedListener(position,viewHolder));
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener(final int position, final RecyclerAdapterMenuItem.ViewHolder viewHolder) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                val = (int)parent.getItemAtPosition(i);
                menu.get(viewHolder.getAdapterPosition()).setQuantity(val);
                viewHolder.sp.setSelection(val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    @Override
    public int getItemCount() {

        return (null != menu ? menu.size() : 0);
    }




    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private TextView message;
        private Spinner sp;
        private Button add;
        private View container;

        public ViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.item_price);
            message = (TextView) view.findViewById(R.id.item_message);
            sp= (Spinner)view.findViewById(R.id.quantity);
            add=(Button)view.findViewById(R.id.add);
            container = view.findViewById(R.id.card_view);
        }
    }
}
