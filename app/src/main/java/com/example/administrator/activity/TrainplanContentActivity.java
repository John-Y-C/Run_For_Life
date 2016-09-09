package com.example.administrator.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.administrator.runforlife.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class TrainplanContentActivity extends AppCompatActivity {

    private WebView wvShowtrainplanContent;
    private ProgressBar pb_trainplancontent_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainplan_content);

        getSupportActionBar().hide();

        ImageButton im_healthtrain_share = (ImageButton)findViewById(R.id.im_healthtrain_share);

        im_healthtrain_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(TrainplanContentActivity.this);
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
                oks.show(TrainplanContentActivity.this);

            }
        });

        pb_trainplancontent_loading = (ProgressBar) findViewById(R.id.pb_trainplancontent_loading);
        wvShowtrainplanContent = (WebView) findViewById(R.id.wv_showtrainplan_content);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        img(url);
    }
    public void img(String url) {
        wvShowtrainplanContent.getSettings().setJavaScriptEnabled(true);
        wvShowtrainplanContent.getSettings().setBlockNetworkImage(false);
        wvShowtrainplanContent.getSettings().setDefaultFixedFontSize(13);
        wvShowtrainplanContent.getSettings().setDefaultTextEncodingName("UTF-8");
        wvShowtrainplanContent.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wvShowtrainplanContent.getSettings().setSupportZoom(true);
        wvShowtrainplanContent.getSettings().setSaveFormData(false);
        wvShowtrainplanContent.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                pb_trainplancontent_loading.setVisibility(View.GONE);
            }
        });
        wvShowtrainplanContent.loadUrl(url);
    }

    public void back(View view){
        finish();
    }
}
