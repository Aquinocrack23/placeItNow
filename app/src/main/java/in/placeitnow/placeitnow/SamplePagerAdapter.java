package in.placeitnow.placeitnow;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class SamplePagerAdapter extends PagerAdapter {

    private final Random random = new Random();
    private int mSize;

    public SamplePagerAdapter() {
        mSize = 5;
    }

    public SamplePagerAdapter(int count) {
        mSize = count;
    }

    @Override public int getCount() {
        return mSize;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
        ImageView imageView = new ImageView(view.getContext());

        TextView textView = new TextView(view.getContext());

        switch (random.nextInt(8)){
            case 0:
                imageView.setBackgroundResource(R.drawable.food);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.burger);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.noodles);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.dosa);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.latte);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.roll);
                break;
            case 6:
                imageView.setBackgroundResource(R.drawable.sundae);
                break;
            case 7:
                imageView.setBackgroundResource(R.drawable.sushi);
                break;
        }
        textView.setText(String.valueOf(position + 1));
        //textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(48);
        view.addView(imageView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
    }

    public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }

    public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }
}