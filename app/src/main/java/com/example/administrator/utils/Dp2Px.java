package com.example.administrator.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by zhao on 2016/8/30.
 */
public class Dp2Px {

     public static int dp2px(int dp, Activity ctx){

         DisplayMetrics displayMetrics = new DisplayMetrics();
         ctx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         float density = displayMetrics.density;

         float v = dp * density;

         return  (int)v;
     }
}
