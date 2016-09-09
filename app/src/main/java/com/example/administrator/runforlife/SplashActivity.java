package com.example.administrator.runforlife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv_splashActivity_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv_splashActivity_image = (ImageView) findViewById(R.id.iv_splashActivity_image);

        showAnimation();

    }

    private void showAnimation() {

        AnimationSet as = new AnimationSet(false);
        RotateAnimation rotateAnimation =
                new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);

        as.addAnimation(rotateAnimation);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);

        iv_splashActivity_image.setAnimation(as);

        final SharedPreferences config = getSharedPreferences("config",MODE_PRIVATE);
        as.start();

        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean isShowGuide = config.getBoolean("isShowGuide",false);

                if(isShowGuide){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }

                finish();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

}
