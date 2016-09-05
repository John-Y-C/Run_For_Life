package com.example.administrator.healthpage;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bean.HealthInfoCategories;
import com.example.administrator.runforlife.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.InjectView;


public class HealthInfoPage {


    protected Activity mActivity;
    public View tipsView;
    @InjectView(R.id.indicator_healthinfo_title)
    TabPageIndicator indicatorHealthinfoTitle;
    @InjectView(R.id.vp_healthinfo_content)
    ViewPager vpHealthinfoContent;
    private ArrayList<HealthInfoCategories> infoCategoriesList;

    public HealthInfoPage(Activity activity) {
        this.mActivity = activity;
        initView();
    }

    private void initView() {

        tipsView = View.inflate(mActivity, R.layout.content_health_info, null);

        TabPageIndicator indicatorHealthinfoTitle = (TabPageIndicator) tipsView.findViewById(R.id.indicator_healthinfo_title);
        ViewPager vpHealthinfoContent = (ViewPager) tipsView.findViewById(R.id.vp_healthinfo_content);

        initdata();

        vpHealthinfoContent.setAdapter(new MyHealthpageAdapter());

        indicatorHealthinfoTitle.setViewPager(vpHealthinfoContent);
    }

    private void initdata() {

        infoCategoriesList = new ArrayList<>();

        infoCategoriesList.add(new HealthInfoCategories(11,"减肥瘦身"));
        infoCategoriesList.add(new HealthInfoCategories(3,"健康饮食"));
        infoCategoriesList.add(new HealthInfoCategories(12,"医疗护理"));
        infoCategoriesList.add(new HealthInfoCategories(10,"四季养生"));
    }


    class MyHealthpageAdapter extends PagerAdapter {

        //为indicator提供Title
        @Override
        public CharSequence getPageTitle(int position) {
            return infoCategoriesList.get(position).getName();
        }

        @Override
        public int getCount() {
            return infoCategoriesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            HealthInfoDetailPage healthInfoDetailPage = new HealthInfoDetailPage(mActivity, infoCategoriesList.get(position).getId());

            container.addView(healthInfoDetailPage.infoDetailView);

            return healthInfoDetailPage.infoDetailView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
