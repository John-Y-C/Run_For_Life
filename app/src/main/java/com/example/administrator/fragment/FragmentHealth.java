package com.example.administrator.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.administrator.healthpage.HealthInfoPage;
import com.example.administrator.healthpage.HealthTrainPage;
import com.example.administrator.runforlife.R;
import com.example.administrator.view.NoScrollViewPage;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class FragmentHealth extends Fragment {

    private static final String TAG = "FragmentHealth";
    private ArrayList<Object> healthPages;
    private NoScrollViewPage vpHealthContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //更换fragment的theme
        getActivity().getTheme().applyStyle(com.viewpagerindicator.R.style.Theme_PageIndicatorDefaults, true);

        View inflate = View.inflate(getActivity(), R.layout.fragment_health, null);

        final RadioGroup rgHealthChoice = (RadioGroup) inflate.findViewById(R.id.rg_health_choice);
        ImageButton imFragmentHealthShare = (ImageButton) inflate.findViewById(R.id.im_fragment_health_share);

        imFragmentHealthShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(getActivity());
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle("标题");
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl("http://sharesdk.cn");
                // text是分享文本，所有平台都需要这个字段
                oks.setText("我是分享文本");
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://sharesdk.cn");
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
                oks.show(getActivity());

            }
        });
        vpHealthContent = (NoScrollViewPage) inflate.findViewById(R.id.vp_health_content);

        healthPages = new ArrayList<>();

        healthPages.add(new HealthTrainPage(getActivity()));
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
                        break;

                    case R.id.btn_health_info:
                        vpHealthContent.setCurrentItem(1,false);
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
                HealthTrainPage page = (HealthTrainPage) healthPages.get(position);
                container.addView(page.trainView);
                return page.trainView;
            } else {
                HealthInfoPage page = (HealthInfoPage) healthPages.get(position);
                container.addView(page.infoView);
                return page.infoView;
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
