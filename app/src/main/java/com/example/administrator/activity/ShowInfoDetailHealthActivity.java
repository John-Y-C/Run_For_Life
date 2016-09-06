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

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");


        getDataFromServer(url);

    }

    private void getDataFromServer(String url) {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.i(TAG, "onSuccess--"+responseInfo.result);
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
        int width = this.getWindowManager().getDefaultDisplay().getWidth();*/

        //打开页面时， 自适应屏幕：
        WebSettings ws = wvShowinfodetailContent.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setUseWideViewPort(false);
        ws.setLoadWithOverviewMode(true);    // 缩放至屏幕的大小
        ws.setSupportZoom(true);      //支持缩放


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
        String imgF = "<p align='center'><img src='"+img+"' /></p>";

        //获取正文
        String message = healthInfoDetail.getResult().getMessage();

        //组合成网页内容
        String content = titleF+dateF+imgF+message;
        wvShowinfodetailContent.loadDataWithBaseURL(null,content,"text/html","utf-8",null);


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
