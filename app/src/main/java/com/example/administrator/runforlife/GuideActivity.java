package com.example.administrator.runforlife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.utils.Dp2Px;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vp_guideactivity_guidepage;
    private Button bt_guideactivity_enter;
    private LinearLayout ll_guideactivity_indicator;
    private View rp_guideactivity;
    private List<MyPageInfo> myPageInfoList ;
    private int width_in_pix;

    private final int PageCount =3;
    private int[] imgResIds = new int[]{R.drawable.guide1,R.drawable.guide2,R.drawable.guide3  };
    private SharedPreferences config;


    class MyPageInfo{

        ImageView pageIv;
        String pageTilte;
        int x;
        //可以加入很多信息。表示当前的page的信息。

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();


        vp_guideactivity_guidepage = (ViewPager) findViewById(R.id.vp_guideactivity_guidepage);
        vp_guideactivity_guidepage.setAdapter(new MyViewPageAdapter());
        vp_guideactivity_guidepage.setPageTransformer(true,new ZoomOutPageTransformer() );
        bt_guideactivity_enter = (Button) findViewById(R.id.bt_guideactivity_enter);
        ll_guideactivity_indicator = (LinearLayout) findViewById(R.id.ll_guideactivity_indicator);
        rp_guideactivity = findViewById(R.id.rp_guideactivity);

        initImageList();
        initIndicator();

        vp_guideactivity_guidepage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rp_guideactivity.getLayoutParams();
                layoutParams.leftMargin = position*width_in_pix*2 + (int)(width_in_pix*2*positionOffset);
                rp_guideactivity.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {


                if (position==PageCount-1){
                    bt_guideactivity_enter.setVisibility(View.VISIBLE);
                }else{
                    bt_guideactivity_enter.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


        config = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit = config.edit();
        edit.putBoolean("isShowGuide",true);
        edit.putBoolean("isFirstClick",true);
        edit.putInt("signday",0);
        edit.commit();

    }


    private void initImageList() {

        myPageInfoList = new ArrayList<MyPageInfo>();
        for (int i=0;i<PageCount;i++){
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(imgResIds[i]);

            MyPageInfo myPageInfo = new MyPageInfo();
            myPageInfo.pageIv=iv;
            myPageInfo.pageTilte="page"+i;
            myPageInfo.x=i;
            myPageInfoList.add(myPageInfo);
        }

    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private void initIndicator() {
        for (int i=0;i<PageCount;i++){

            View view = new View(this);
            //单位是像素
            width_in_pix = Dp2Px.dp2px(20, this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width_in_pix, width_in_pix);
            if (i!=0){
                layoutParams.leftMargin= width_in_pix;
            }
            view.setLayoutParams(layoutParams);

            view.setBackgroundResource(R.drawable.graypoint);
            ll_guideactivity_indicator.addView(view);
        }

    }

    private class MyViewPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return PageCount;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            MyPageInfo myPageInfo = (MyPageInfo) object;
            return view==myPageInfo.pageIv;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            MyPageInfo myPageInfo = myPageInfoList.get(position);
            container.addView(myPageInfo.pageIv);
            return myPageInfo;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            MyPageInfo myPageInfo = (MyPageInfo) object;
            container.removeView( myPageInfo.pageIv);
            //super.destroyItem(container, position, object);
        }
    }

    public void enterHome(View v){

        startActivity(new Intent(this,MainActivity.class));
        finish();

    }
    }



