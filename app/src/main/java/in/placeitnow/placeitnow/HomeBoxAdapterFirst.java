package in.placeitnow.placeitnow;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Pranav Gupta on 1/7/2017.
 */

public class HomeBoxAdapterFirst extends RecyclerView.Adapter<HomeBoxAdapterFirst.ViewHolder>{

    private Activity activity;
    private List<HomeBoxFirst> boxFirst;

    public HomeBoxAdapterFirst(Activity activity,List<HomeBoxFirst> boxFirst){
        this.activity=activity;
        this.boxFirst=boxFirst;
    }

    @Override
    public HomeBoxAdapterFirst.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.home_item_first,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeBoxAdapterFirst.ViewHolder holder, int position) {
        holder.name.setText(boxFirst.get(position).getName());
        holder.description.setText(boxFirst.get(position).getDes());
        Picasso.with(activity).load(boxFirst.get(position).getThumbnail()).fit().into(holder.imgThumbnail);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,VendorProfile.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!=boxFirst ? boxFirst.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView name;
        TextView description;
        CardView container;
        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            name = (TextView)itemView.findViewById(R.id.tv_nature);
            description = (TextView)itemView.findViewById(R.id.tv_des_nature);
            container =(CardView) itemView.findViewById(R.id.vendor_profile);
        }
    }
}
