package in.placeitnow.placeitnow.recycleradapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.placeitnow.placeitnow.pojo.FeedItem;
import in.placeitnow.placeitnow.R;

/**
 * Created by Pranav Gupta on 1/7/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private List<FeedItem> feedItemList;
    private Activity activity;

    public FeedAdapter(Activity activity,List<FeedItem> items){
        this.feedItemList=items;
        this.activity=activity;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.feed_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedAdapter.ViewHolder holder, int position) {
        holder.name.setText(feedItemList.get(position).getName());
        holder.timestamp.setText(feedItemList.get(position).getTimeStamp());
        holder.txtStatusMsg.setText(feedItemList.get(position).getStatus());
        holder.txtUrl.setText(feedItemList.get(position).getUrl());
        if(feedItemList.get(position).getImge().contentEquals("null")){
            holder.feedImage.setVisibility(View.GONE);
        }
        else{
            holder.feedImage.setBackgroundResource(R.drawable.dosa);
        }
        holder.profilePic.setBackgroundResource(R.drawable.burg);

    }

    @Override
    public int getItemCount() {

        return (null != feedItemList ? feedItemList.size() : 0);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic,feedImage;
        TextView name,timestamp;
        TextView txtStatusMsg,txtUrl;
        public ViewHolder(View itemView) {
            super(itemView);
            profilePic = (ImageView)itemView.findViewById(R.id.profilePic);
            name = (TextView)itemView.findViewById(R.id.name);
            txtUrl = (TextView)itemView.findViewById(R.id.txtUrl);
            txtStatusMsg=(TextView)itemView.findViewById(R.id.txtStatusMsg);
            timestamp=(TextView)itemView.findViewById(R.id.timestamp);
            feedImage=(ImageView) itemView.findViewById(R.id.feedImage1);
        }
    }
}
