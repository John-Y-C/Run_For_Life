package com.example.administrator.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NoScrollViewPage extends ViewPager {

    public NoScrollViewPage(Context context) {
        super(context);
    }

    public NoScrollViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //禁用viewpager的左右滑动
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;       //super.onTouchEvent(ev);
    }
}
