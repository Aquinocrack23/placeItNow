package in.placeitnow.placeitnow;

import android.app.Activity;
import android.app.Dialog;
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
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;

    private List<Vendor> vendors;
    private Activity activity;

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

        if(vendors.get(position).getName()!=null&&vendors.get(position).getStatus()!=null)
        //setting data to view holder elements
        viewHolder.name.setText(vendors.get(position).getName());
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        if(vendors.get(position).getStatus()){
            viewHolder.status.setText("Online");
        }else {
            viewHolder.status.setText("Offline");
        }
        //setting onClickListener for each container
        viewHolder.container.setOnClickListener(onClickListener(position));
    }

    @Override
    public int getItemCount() {
        return (null != vendors ? vendors.size() : 0);
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context,OrderSelection.class);
                intent.putExtra("vendorname",vendors.get(position).getName());
                intent.putExtra("vid",vendors.get(position).getVid());
                context.startActivity(intent);
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
        public ViewHolder(View view) {
            super(view);

            context = view.getContext();
            imageView = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            status = (TextView) view.findViewById(R.id.status);
            container = view.findViewById(R.id.card_view);
        }

    }

}