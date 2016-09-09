package com.example.administrator.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.bean.HealthInfoDetail;
import com.example.administrator.runforlife.R;
import com.example.administrator.utils.SharedPrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShowInfoDetailHealthActivity extends AppCompatActivity {

    private static final String TAG = "ShowInfoDetailHealth";
    @InjectView(R.id.ib_showinfodetail_back)
    ImageButton ibShowinfodetailBack;
    @InjectView(R.id.wv_showinfodetail_content)
    WebView wvShowinfodetailContent;
    @InjectView(R.id.pb_showinfodetail_loading)
    ProgressBar pbShowinfodetailLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_detail_health);
        ButterKnife.inject(this);

        getSupportActionBar().hide();

        ImageButton im_healthinfo_share = (ImageButton)findViewById(R.id.im_healthinfo_share);

        im_healthinfo_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(ShowInfoDetailHealthActivity.this);
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
                oks.show(ShowInfoDetailHealthActivity.this);

            }
        });

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");

        String jsonFromCache = SharedPrefUtils.getJsonFromCache(url,this);

        if (jsonFromCache.isEmpty()){
            //从服务器上去拿数据
            Log.i(TAG,"缓存为空,从服务器上去拿数据");
            getDataFromServer(url);
        }else {
            //缓存不为空，则直接用缓存去解析json
            Log.i(TAG,"缓存不为空,直接用缓存去解析json");
            parseJsonString(jsonFromCache);
        }

    }

    //从服务器上去拿数据
    private void getDataFromServer(String url) {

        final String urlkey = url;

        Log.i(TAG, "getDataFromServer="+url);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, urlkey, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.i(TAG,"url:"+urlkey);
                Log.i(TAG, "onSuccess--"+responseInfo.result);
                //把从服务器上拿到的JsonString缓存起来
                SharedPrefUtils.saveJsonToCache(urlkey,responseInfo.result,ShowInfoDetailHealthActivity.this);
                parseJsonString(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                Log.i(TAG, "onFailure--" + s);
                Toast.makeText(ShowInfoDetailHealthActivity.this, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseJsonString(String result) {

        Gson gson = new Gson();
        HealthInfoDetail healthInfoDetail = gson.fromJson(result, HealthInfoDetail.class);

/*        //获取屏幕宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        String widthF = "<head><style>img{max-width:"+widthPixels+"px !important;}</style></head>";*/


        //获取标题
        String title = healthInfoDetail.getResult().getTitle();
        String titleF ="<h1>"+title+"</h1> ";

        //获取时间
        long time = healthInfoDetail.getResult().getTime();
        Date datePub = new Date(time);
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datePub);
        String dateF = "<p>"+dateFormat+"</p> ";

        //获取图片
        String img = healthInfoDetail.getResult().getImg();
        String imgF = "<p align='center'><img src='"+img+"' width='90%' /></p>";

        //获取正文
        String message = healthInfoDetail.getResult().getMessage();

        //组合成网页内容
        String content = titleF+dateF+imgF+message;
        wvShowinfodetailContent.loadDataWithBaseURL(null,content,"text/html","utf-8",null);

        //打开页面时， 自适应屏幕：
        WebSettings ws = wvShowinfodetailContent.getSettings();
        ws.setBlockNetworkImage(false);         //true:阻止网络图片数据；false:解除网络图片数据
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setUseWideViewPort(false);
        ws.setLoadWithOverviewMode(true);    // 缩放至屏幕的大小
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setSupportZoom(true);      //支持缩放


        wvShowinfodetailContent.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                pbShowinfodetailLoading.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                pbShowinfodetailLoading.setVisibility(View.GONE);
                Toast.makeText(ShowInfoDetailHealthActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back(View view){
        finish();
    }
}
