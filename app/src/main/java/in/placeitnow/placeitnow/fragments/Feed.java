package in.placeitnow.placeitnow.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.placeitnow.placeitnow.recycleradapters.FeedAdapter;
import in.placeitnow.placeitnow.pojo.FeedItem;
import in.placeitnow.placeitnow.R;

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
        itemList.add(new FeedItem(234,"PlaceItNow Team","r","Designed and crafted by PlaceItNow",
                "e","12/12/2017","www.placeitnow.com"));
        itemList.add(new FeedItem(234,"PlaceItNow Team","r","This is a great app for browsing your local canteens",
                "e","12/12/2017","nowplaceit@gmail.com"));
        itemList.add(new FeedItem(234,"PlaceItNow Team","null","This is a great app for browsing your local canteens",
                "e","12/12/2017","nowplaceit@gmail.com"));
        itemList.add(new FeedItem(234,"PlaceItNow Team","r","Enjoy delicious food in just one click",
                "e","12/12/2017","www.placeitnow.com"));
        itemList.add(new FeedItem(234,"Pranav Gupta","r","Never stand again in a queue ;p",
                "e","12/12/2017","www.placeitnow.com"));
        feedAdapter.notifyDataSetChanged();
    }
}
