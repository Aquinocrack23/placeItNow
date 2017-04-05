package in.placeitnow.placeitnow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.imbryk.viewPager.LoopViewPager;

import java.util.ArrayList;
import java.util.List;

import in.placeitnow.placeitnow.recycleradapters.HomeBoxAdapterFirst;
import in.placeitnow.placeitnow.pojo.HomeBoxFirst;
import in.placeitnow.placeitnow.R;
import in.placeitnow.placeitnow.pageradapter.SamplePagerAdapter;
import in.placeitnow.placeitnow.activities.BaseActivityFragment;
import in.placeitnow.placeitnow.activities.LoginActivity;
import in.placeitnow.placeitnow.activities.MainActivity;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Pranav Gupta on 12/22/2016.
 */
public class Home extends Fragment {
    private Button change;
    private View view;
    private ImageButton imgb;

    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;
    private NumberPicker np;
    private Spinner sp;
    private HomeBoxAdapterFirst homeBoxAdapterFirst;
    private HomeBoxAdapterFirst homeBoxAdapterFirst2;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private List<HomeBoxFirst> boxItems,boxItems2;
    private Button dash,make_order;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        view=  inflater.inflate(R.layout.home,container,false);

        dash = (Button)view.findViewById(R.id.order_dashboard);
        make_order = (Button)view.findViewById(R.id.make_order);

        /** getSupportActionBar is only present in AppCompatActivity while getActivity returns FragmentActivity so we first
         * need to cast to AppCompatActivity to use that method
         *
         * */
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        //setupServiceReceiver();
        //checkForMessage();

        //Firebase Auth
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    //onSimpleService(view);

                } else {
                    //User is signed out
                    Toast.makeText(getActivity(),"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(),LoginActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };
        boxItems= new ArrayList<>();
        boxItems2 = new ArrayList<>();
        recyclerView2=(RecyclerView)view.findViewById(R.id.recycler_view2);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        setData();
        homeBoxAdapterFirst = new HomeBoxAdapterFirst(getActivity(),boxItems);
        homeBoxAdapterFirst2 = new HomeBoxAdapterFirst(getActivity(),boxItems2);
        recyclerView.setAdapter(homeBoxAdapterFirst);
        recyclerView2.setAdapter(homeBoxAdapterFirst2);

        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });
        make_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager pager = (ViewPager) ((BaseActivityFragment)getActivity()).findViewById(R.id.viewpager);
                pager.setCurrentItem(1);
            }
        });
        /*imgb = (ImageButton)view.findViewById(R.id.vendor1);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),VendorProfile.class);
                startActivity(i);
            }
        });*/

        return view;
    }
    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /** in case our fragment also wants to contribute to the toolbar for the menu(originally toolbar consists of
         * menu inflated in the BaseFragmentActivity but if we want to additionally add some menu then to show them this
         * must be set to true
         * */
        setHasOptionsMenu(true);
        LoopViewPager viewpager = (LoopViewPager) view.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new SamplePagerAdapter());
        indicator.setViewPager(viewpager);
    }

    private void setData() {
        boxItems.add(new HomeBoxFirst("Kabab","River Bank",R.drawable.food));
        boxItems.add(new HomeBoxFirst("Fresh Juices","Latest Juices",R.drawable.juice));
        boxItems.add(new HomeBoxFirst("Omlette","Bread Omlette",R.drawable.bread_omlette));
        boxItems.add(new HomeBoxFirst("Tea","AFC",R.drawable.chai));
        boxItems2.add(new HomeBoxFirst("Burger","Find all varieties of burgers at BakeHut",R.drawable.burger));
        boxItems2.add(new HomeBoxFirst("Biryani","Find all biryanis at best price at RiverBank",R.drawable.fried_rice));
        boxItems2.add(new HomeBoxFirst("Parantha","All varieties of parantha are available at AFC",R.drawable.paratha));
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(getActivity(),MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
