package com.example.administrator.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.administrator.healthpage.HealthInfoPage;
import com.example.administrator.healthpage.HealthPlanPage;
import com.example.administrator.runforlife.R;

import java.util.ArrayList;


public class FragmentHealth extends Fragment {

    private static final String TAG = "FragmentHealth";
    private ArrayList<Object> healthPages;
    private ViewPager vpHealthContent;
    private ImageButton ibHealthMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //更换fragment的theme
        getActivity().getTheme().applyStyle(com.viewpagerindicator.R.style.Theme_PageIndicatorDefaults, true);

        View inflate = View.inflate(getActivity(), R.layout.fragment_health, null);

        final RadioGroup rgHealthChoice = (RadioGroup) inflate.findViewById(R.id.rg_health_choice);
        vpHealthContent = (ViewPager) inflate.findViewById(R.id.vp_health_content);
        ibHealthMode = (ImageButton) inflate.findViewById(R.id.ib_health_mode);

        healthPages = new ArrayList<>();

        healthPages.add(new HealthPlanPage(getActivity()));
        healthPages.add(new HealthInfoPage(getActivity()));

        vpHealthContent.setAdapter(new MyContentAdapter());


        //进入时默认选中第一个按钮
        rgHealthChoice.check(R.id.btn_health_plan);



        rgHealthChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btn_health_plan:
                        vpHealthContent.setCurrentItem(0,false);   //false表示取消平滑切换页面的效果
                        ibHealthMode.setVisibility(View.GONE);
                        break;

                    case R.id.btn_health_info:
                        vpHealthContent.setCurrentItem(1,false);
//                        ibHealthMode.setVisibility(View.VISIBLE);
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
                HealthPlanPage page = (HealthPlanPage) healthPages.get(position);
                container.addView(page.habitView);
                return page.habitView;
            } else {
                HealthInfoPage page = (HealthInfoPage) healthPages.get(position);
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
