package in.placeitnow.placeitnow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import me.relex.circleindicator.CircleIndicator;

import static android.app.Activity.RESULT_OK;

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
    public MySimpleReceiver receiverForSimple;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        view=  getActivity().getLayoutInflater().inflate(R.layout.home,container,false);

        /** getSupportActionBar is only present in AppCompatActivity while getActivity returns FragmentActivity so we first
         * need to cast to AppCompatActivity to use that method
         *
         * */
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        setupServiceReceiver();
        checkForMessage();

        //Firebase Auth
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    onSimpleService(view);

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
        LoopViewPager viewpager = (LoopViewPager) view.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new SamplePagerAdapter());
        indicator.setViewPager(viewpager);
        onSimpleService(view);
    }

    private void setData() {
        boxItems.add(new HomeBoxFirst("Kabab","River Bank",R.drawable.food));
        boxItems.add(new HomeBoxFirst("Fresh Juices","Latest Juices",R.drawable.juice));
        boxItems.add(new HomeBoxFirst("Omlette","Bread Omlette",R.drawable.bread_omlette));
        boxItems.add(new HomeBoxFirst("Tea","AFC",R.drawable.chai));
        boxItems2.add(new HomeBoxFirst("Burger","Find all varieties of burgers at BakeHut",R.drawable.burger1));
        boxItems2.add(new HomeBoxFirst("Biryani","Find all biryanis at best price at RiverBank",R.drawable.fried_rice));
        boxItems2.add(new HomeBoxFirst("Parantha","All varieties of parantha are available at AFC",R.drawable.paratha));
    }
    public void onSimpleService(View v) {

        // Construct our Intent specifying the Service
        Intent i = new Intent(getActivity(), MySimpleService.class);
        // Add extras to the bundle
        i.putExtra("uid",uid);
        i.putExtra("receiver", receiverForSimple);
        // Start the service
        getActivity().startService(i);
        //Toast.makeText(getActivity(),"onSimpleService",Toast.LENGTH_SHORT).show();
    }

    // Setup the callback for when data is received from the service
    public void setupServiceReceiver() {
        //Toast.makeText(getActivity(),"setupServiceReceiver",Toast.LENGTH_SHORT).show();
        receiverForSimple = new MySimpleReceiver(new Handler());
        // This is where we specify what happens when data is received from the
        // service
        receiverForSimple.setReceiver(new MySimpleReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    String resultValue = resultData.getString("resultValue");
                    Toast.makeText(getActivity(), resultValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Checks to see if service passed in a message
    private void checkForMessage() {
        //Toast.makeText(getActivity(),"checkForMessage",Toast.LENGTH_SHORT).show();
        String message = getActivity().getIntent().getStringExtra("message");
        if (message != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }



    //to change content of a viewpager inside fragment class
    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(view);
    }
}
