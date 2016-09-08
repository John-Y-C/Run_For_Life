package com.example.administrator.healthpage;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bean.HealthInfoCategories;
import com.example.administrator.constants.Const;
import com.example.administrator.runforlife.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.InjectView;


public class HealthInfoPage {


    private static final String TAG ="HealthInfoPage" ;
    protected Activity mActivity;
    public View infoView;
    private ArrayList<HealthInfoCategories> infoCategoriesList;
    private TabPageIndicator indicatorHealthinfoTitle;
    private ViewPager vpHealthinfoContent;

    public HealthInfoPage(Activity activity) {
        this.mActivity = activity;

        infoCategoriesList = new ArrayList<>();

        infoCategoriesList.add(new HealthInfoCategories(3,"健康饮食"));
        infoCategoriesList.add(new HealthInfoCategories(11,"减肥瘦身"));
        infoCategoriesList.add(new HealthInfoCategories(12,"医疗护理"));
        infoCategoriesList.add(new HealthInfoCategories(10,"四季养生"));

        Log.i(TAG,"infoCategoriesList:"+infoCategoriesList.toString());

        infoView=initView();

        initdata();
    }

    private View initView() {

        View inflate = View.inflate(mActivity, R.layout.content_health_info, null);

        indicatorHealthinfoTitle = (TabPageIndicator) inflate.findViewById(R.id.indicator_healthinfo_title);
        vpHealthinfoContent = (ViewPager) inflate.findViewById(R.id.vp_healthinfo_content);

        return inflate;
    }

    private void initdata() {


        vpHealthinfoContent.setAdapter(new MyHealthpageAdapter());
        //关联indicator和viewpager  (必须在viewpager设置Adapter之后)
        indicatorHealthinfoTitle.setViewPager(vpHealthinfoContent);
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

            Log.i(TAG,"instantiateItem--position:"+position);

            int infoId = infoCategoriesList.get(position).getId();
            //生成url  ,筛选关键字为 跑步 锻炼 训练 运动 或 健康 的内容
            String url = Const.healthListAddr +"?key="+ Const.apiKey+"&keyword=跑步u007C锻炼u007C训练u007C运动u007C健康&classify="+infoId+"&rows=8&page=";

            Log.i(TAG,"url:"+url);
            HealthInfoDetailPage healthInfoDetailPage = new HealthInfoDetailPage(mActivity, url);

            container.addView(healthInfoDetailPage.infoDetailView);

            return healthInfoDetailPage.infoDetailView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
