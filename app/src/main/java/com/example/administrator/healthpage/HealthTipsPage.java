package com.example.administrator.healthpage;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.runforlife.R;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by GTR on 2016/9/3.
 */
public class HealthTipsPage {


    protected Activity mActivity;
    public View tipsView;

    public HealthTipsPage(Activity activity){
        this.mActivity = activity;
        initView();
    }

    private void initView() {

        tipsView = View.inflate(mActivity, R.layout.content_tips,null);

        TabPageIndicator indicatorHealthpageTitle = (TabPageIndicator) tipsView.findViewById(R.id.indicator_healthpage_title);
        ViewPager vpHealthpageContent = (ViewPager) tipsView.findViewById(R.id.vp_healthpage_content);

        vpHealthpageContent.setAdapter(new MyHealthpageAdapter());

        indicatorHealthpageTitle.setViewPager(vpHealthpageContent);
    }



    String[] healthpageTitles = new String[]{"title1","title2"};

    class MyHealthpageAdapter extends PagerAdapter {

        //为indicator提供Title
        @Override
        public CharSequence getPageTitle(int position) {
            return healthpageTitles[position];
        }

        @Override
        public int getCount() {
            return healthpageTitles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            TextView textView = new TextView(mActivity);
            textView.setText(healthpageTitles[position]);
            container.addView(textView);

            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
