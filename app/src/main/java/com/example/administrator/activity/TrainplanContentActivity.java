package com.example.administrator.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.runforlife.R;

public class TrainplanContentActivity extends AppCompatActivity {

    private WebView wvShowtrainplanContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainplan_content);

        getSupportActionBar().hide();

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
        wvShowtrainplanContent.setWebViewClient(new WebViewClient());
        wvShowtrainplanContent.loadUrl(url);
    }
}
