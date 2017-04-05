package in.placeitnow.placeitnow.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.placeitnow.placeitnow.fragments.Feed;
import in.placeitnow.placeitnow.fragments.Home;
import in.placeitnow.placeitnow.R;
import in.placeitnow.placeitnow.fragments.UserAccount;
import in.placeitnow.placeitnow.fragments.Order;


/**
 * Created by Pranav Gupta on 12/22/2016.
 */

public class BaseActivityFragment extends AppCompatActivity {
    private TabLayout tabLayout;
    private Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth auth;             //FirebaseAuthentication
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);
        //Toast.makeText(BaseActivityFragment.this,"Hii",Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PlaceItNow");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_home_white_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.inflateMenu(R.menu.popup_menu);

        /** What method should I call to know if an Activity has its contentView (once the method setContentView() has been called)?
         this.getWindow().getDecorView().findViewById(android.R.id.content)
         or
         this.findViewById(android.R.id.content)
         or
         this.findViewById(android.R.id.content).getRootView()
         * */

        //Firebase Auth
        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    //setupServiceReceiver();
                    //checkForMessage();
                    //onSimpleService(BaseActivityFragment.this.findViewById(android.R.id.content));

                } else {
                    //User is signed out
                    Toast.makeText(BaseActivityFragment.this,"Please Sign In First",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(BaseActivityFragment.this,LoginActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };

        //Viewpager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(Color.parseColor("#212121"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //tabLayout.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                // tabLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                // tabLayout.setVisibility(View.VISIBLE);
            }
        });
        createTabIcons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_1:
                FirebaseAuth.getInstance().signOut();
                //show("Tab Angry Reselected!");
                Toast.makeText(getBaseContext(),"Signed Out",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(i);
                break;
            case R.id.order_updates:
                Intent intent =new Intent(BaseActivityFragment.this,MainActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments[] = {"Home", "Order", "Feed", "Account"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Home();
                case 1:
                    return new Order();
                case 2:
                    return new Feed();
                case 3:
                    return new UserAccount();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }
    //to generate text inside tabs of tablayout and set icons
    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Home");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_white_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Order");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_shopping_basket_white_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Feed");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_receipt_white_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Account");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_account_circle_white_24dp, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
