package com.example.administrator.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.healthpage.HealthHabitPage;
import com.example.administrator.healthpage.HealthTipsPage;
import com.example.administrator.runforlife.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/2/002.
 */
public class FragmentHealth extends Fragment {

    private static final String TAG = "FragmentHealth";
    private ArrayList<Object> healthPages;
    private ViewPager vpHealthContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = View.inflate(getActivity(), R.layout.fragment_health, null);

        RadioGroup rgHealthChoice = (RadioGroup) inflate.findViewById(R.id.rg_health_choice);
        vpHealthContent = (ViewPager) inflate.findViewById(R.id.vp_health_content);

        healthPages = new ArrayList<>();

        healthPages.add(new HealthHabitPage(getActivity()));
        healthPages.add(new HealthTipsPage(getActivity()));

        vpHealthContent.setAdapter(new MyContentAdapter());


        //进入时默认选中第一个按钮
        rgHealthChoice.check(R.id.btnHealthHobit);

        rgHealthChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btnHealthHobit:
                        vpHealthContent.setCurrentItem(0);
                        break;

                    case R.id.btnHealthTips:
                        vpHealthContent.setCurrentItem(1);
                        break;
                }
            }
        });
        return inflate;
    }

    class MyContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return healthPages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Log.i(TAG, "instantiateItem,position:" + position);
            if (position == 0) {
                HealthHabitPage page = (HealthHabitPage) healthPages.get(position);
                container.addView(page.habitView);
                return page.habitView;
            } else {
                HealthTipsPage page = (HealthTipsPage) healthPages.get(position);
                container.addView(page.tipsView);
                return page.tipsView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            Log.i(TAG, "destroyItem,position:" + position);
            container.removeView((View) object);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
