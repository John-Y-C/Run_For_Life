package com.example.administrator.healthpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.activity.TrainplanContentActivity;
import com.example.administrator.runforlife.R;


public class HealthTrainPage {

    protected Activity mActivity;
    public View trainView;

    String[] url = new String[]{
            "http://mp.weixin.qq.com/s?__biz=MzA4ODY0NjMyMw==&mid=2651711105&idx=2&sn=ebee4a9e2fb5bc375edf3d3e5f79db7c&scene=0#wechat_redirect",
            "http://mp.weixin.qq.com/s?__biz=MzA3MTEwNjUyNA==&mid=400565120&idx=1&sn=5517d7014b0934042971b17bb7f07a20&scene=18#wechat_redirect",
            "http://mp.weixin.qq.com/s?__biz=MzA4NjA3MDkyOA==&mid=204145088&idx=1&sn=8d1579b709cf66f34ca270d76a44a568&scene=19#wechat_redirect",
            "http://mp.weixin.qq.com/s?__biz=MzA4ODY0NjMyMw==&mid=2651709582&idx=1&sn=b9d5cd2f978c0c5abafc15ff32d9cd63&scene=0#wechat_redirect",
            "http://mp.weixin.qq.com/s?__biz=MzIyNzA2OTQ3NA==&mid=2652297670&idx=3&sn=95d37378aab3eedc50d7ff4af7c62e9d&scene=0#wechat_redirect",
            "http://mp.weixin.qq.com/s?__biz=MzA4ODY0NjMyMw==&mid=2651711068&idx=4&sn=6d01554311503f76d46c655af14b7f0d&scene=0#wechat_redirect"
    };

    String[] title = {"健身计划", "跑步减肥", "送给跑步的你"};
    int[] imgs = {R.drawable.health_train_page_two_, R.drawable.health_train_page_three, R.drawable.health_train_page_one};

    public HealthTrainPage(Activity activity) {
        this.mActivity = activity;
        trainView=initView();
    }

    private View initView() {
        View inflate = View.inflate(mActivity, R.layout.content_health_train, null);
        ListView lvHealthlistviewTrainplan = (ListView) inflate.findViewById(R.id.lv_healthlistview_trainplan);

        lvHealthlistviewTrainplan.setAdapter(new MyAdapeter());

        View footer = View.inflate(mActivity, R.layout.healthtrain_footer, null);

        RadioButton rb_trainitem_fuji = (RadioButton) footer.findViewById(R.id.rb_trainitem_fuji);
        rb_trainitem_fuji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
                intent.putExtra("url",url[3]);
                mActivity.startActivity(intent);
            }
        });
        RadioButton rb_trainitem_shendun = (RadioButton) footer.findViewById(R.id.rb_trainitem_shendun);
        rb_trainitem_shendun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
                intent.putExtra("url",url[4]);
                mActivity.startActivity(intent);
            }
        });
        RadioButton rb_trainitem_yintixiangshang = (RadioButton) footer.findViewById(R.id.rb_trainitem_yintixiangshang);
        rb_trainitem_yintixiangshang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
                intent.putExtra("url",url[5]);
                mActivity.startActivity(intent);
            }
        });

        lvHealthlistviewTrainplan.addFooterView(footer);

        lvHealthlistviewTrainplan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
                intent.putExtra("url", url[position]);
                mActivity.startActivity(intent);
            }
        });



        return inflate;
    }


    class MyAdapeter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //填充 给一个layout布局文件
            View inflate = View.inflate(mActivity, R.layout.item_healthlistview_trainplan, null);
            TextView tvHealthlistviewTitle = (TextView) inflate.findViewById(R.id.tv_healthlistview_title);
            ImageView ivHealthlistviewImg = (ImageView) inflate.findViewById(R.id.iv_healthlistview_img);

            tvHealthlistviewTitle.setText(title[position]);
            ivHealthlistviewImg.setImageResource(imgs[position]);

            return inflate;
        }

    }
/*

    public void abdominal(View v) {
        Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
        intent.putExtra("url",url[3]);
        mActivity.startActivity(intent);
    }
    public void squat(View v) {
        Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
        intent.putExtra("url",url[4]);
        mActivity.startActivity(intent);
    }
    public void pull_up(View v) {
        Intent intent = new Intent(mActivity, TrainplanContentActivity.class);
        intent.putExtra("url",url[5]);
        mActivity.startActivity(intent);
    }
*/


}