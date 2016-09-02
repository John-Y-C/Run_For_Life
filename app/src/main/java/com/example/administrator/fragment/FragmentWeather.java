package com.example.administrator.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.runforlife.R;

/**
 * Created by Administrator on 2016/9/2/002.
 */
public class FragmentWeather extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = View.inflate(getActivity(), R.layout.fragment_weather, null);
        TextView tv_fragment = (TextView) inflate.findViewById(R.id.testView);
        tv_fragment.setText("Weather");
        tv_fragment.setTextColor(Color.RED);
        return inflate;
    }
}
