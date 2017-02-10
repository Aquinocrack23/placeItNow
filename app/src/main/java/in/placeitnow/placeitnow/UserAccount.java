package in.placeitnow.placeitnow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pranav Gupta on 12/22/2016.
 */
public class UserAccount extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=  getActivity().getLayoutInflater().inflate(R.layout.account,container,false);

        return view;
    }
}
