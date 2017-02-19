package in.placeitnow.placeitnow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav Gupta on 12/22/2016.
 */
public class Feed extends Fragment {

    private RecyclerView recyclerView;
    private List<FeedItem> itemList;
    private FeedAdapter feedAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.feed,container,false);

        /** getSupportActionBar is only present in AppCompatActivity while getActivity returns FragmentActivity so we first
         * need to cast to AppCompatActivity to use that method
         *
         * */
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Feed");


        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        feedAdapter=new FeedAdapter(getActivity(),itemList);
        recyclerView.setAdapter(feedAdapter);
        setDatatoView();
        return view;
    }

    private void setDatatoView() {
        itemList.add(new FeedItem(234,"Pranav Gupta","r","This app is just too good",
                "e","12/12/2019","www.placeitnow.com"));
        itemList.add(new FeedItem(234,"Pranav Gupta","r","This is a great app for browsing your local canteens",
                "e","12/12/2019","nowplaceit@gmail.com"));
        itemList.add(new FeedItem(234,"Pranav Gupta","null","This is a great app for browsing your local canteens",
                "e","12/12/2019","www.nowplaceit@gmail.com"));
        itemList.add(new FeedItem(234,"Pranav Gupta","r","Enjoy delicious food in just one click",
                "e","12/12/2019","www.placeitnow.com"));
        feedAdapter.notifyDataSetChanged();
    }
}
