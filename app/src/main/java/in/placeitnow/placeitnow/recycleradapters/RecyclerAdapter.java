package in.placeitnow.placeitnow.recycleradapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.placeitnow.placeitnow.R;
import in.placeitnow.placeitnow.pojo.Vendor;
import in.placeitnow.placeitnow.activities.OrderSelection;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;

    private List<Vendor> vendors;
    private Activity activity;
    private String status;

    public RecyclerAdapter(Activity activity, List<Vendor> vendors) {
        this.vendors = vendors;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.vendor_status, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        if(vendors.get(position).getName()!=null){
            //setting data to view holder elements
            viewHolder.name.setText(vendors.get(position).getName());
        }

        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);

        if(vendors.get(position).getOrder_current()!=null){
            viewHolder.order_current.setText(vendors.get(position).getOrder_current()+" orders before you");
        }
        if(vendors.get(position).getStatus()!=null){
            if(vendors.get(position).getStatus()){
                viewHolder.status.setText("Online");
                status = "Online";
            }else {
                viewHolder.status.setText("Offline");
                status = "Offline";
            }
        }
        //setting onClickListener for each container
        viewHolder.container.setOnClickListener(onClickListener(viewHolder));
    }

    @Override
    public int getItemCount() {
        return (null != vendors ? vendors.size() : 0);
    }

    private View.OnClickListener onClickListener(final RecyclerView.ViewHolder viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context,OrderSelection.class);
                if(vendors.get(viewHolder.getAdapterPosition()).getStatus()){
                    intent.putExtra("vendorname",vendors.get(viewHolder.getAdapterPosition()).getName());
                    intent.putExtra("vid",vendors.get(viewHolder.getAdapterPosition()).getVid());
                    intent.putExtra("status",status);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context,"Sorry this vendor is offline and is not ready to take any orders right now",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView status;
        private View container;
        private TextView order_current;
        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            imageView = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            status = (TextView) view.findViewById(R.id.status);
            container = view.findViewById(R.id.card_view);
            order_current = (TextView)view.findViewById(R.id.order_current);
        }

    }

}