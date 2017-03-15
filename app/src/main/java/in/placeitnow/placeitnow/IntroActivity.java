package in.placeitnow.placeitnow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends OnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnboarderPage onboarderPage1 = new OnboarderPage("Vendors", "Select from a list of online vendors", R.drawable.vendor_det);
        OnboarderPage onboarderPage2 = new OnboarderPage("Menu Items", "Select the delicious items offered by the vendor", R.drawable.order_item);
        OnboarderPage onboarderPage3 = new OnboarderPage("Order DashBoard", "Stay Updated with all your orders!", R.drawable.order_dash);

        onboarderPage1.setBackgroundColor(R.color.colorPrimary);
        onboarderPage2.setBackgroundColor(R.color.colorPrimary);
        onboarderPage3.setBackgroundColor(R.color.colorPrimary);

        List<OnboarderPage> pages = new ArrayList<>();

        pages.add(onboarderPage1);
        pages.add(onboarderPage2);
        pages.add(onboarderPage3);

        for (OnboarderPage page : pages) {
            page.setTitleColor(R.color.colorPrimaryDark);
            page.setDescriptionColor(R.color.white);
        }

        setSkipButtonTitle("Skip");
        setFinishButtonTitle("Finish");

        setOnboardPagesReady(pages);

    }

    @Override
    public void onSkipButtonPressed() {
        super.onSkipButtonPressed();
        Toast.makeText(this, "Skip button was pressed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(IntroActivity.this,LoginActivity.class));
    }

}
