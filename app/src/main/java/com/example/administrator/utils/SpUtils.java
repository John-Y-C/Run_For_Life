package com.example.administrator.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by quexiaming on 2016/9/9.
 */
public class SpUtils {

    private static SharedPreferences sp;

    public static String getString(Context context, String key, String defaultValue){
        if (sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        String value = sp.getString(key, defaultValue);
        return value;
    }
    public static void putString(Context context,String key,String value){
        if (sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }


    public static int getInt(Context context, String key, int defaultValue){
        if (sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        int value = sp.getInt(key, defaultValue);
        return value;
    }
    public static void putInt(Context context,String key,int value){
        if (sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }

    public static float getFloat(Context context, String key, float defaultValue){
        if (sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        float value = sp.getFloat(key, defaultValue);
        return value;
    }
    public static void putIFloat(Context context,String key,float value){
        if (sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putFloat(key,value).commit();
    }
}
