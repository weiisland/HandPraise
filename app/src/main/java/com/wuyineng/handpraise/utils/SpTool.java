package com.wuyineng.handpraise.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wuyineng on 2016/4/17.
 * 描述：
 */
public class SpTool {

    public static void putBoolean(Context context,String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.IS_SETUP, Context.MODE_PRIVATE);

        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.IS_SETUP, Context.MODE_PRIVATE);

        return sp.getBoolean(key,value);
    }

    public static void putInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.INITIAL_PERSONAL_DATA, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();

    }

    public static int getInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.INITIAL_PERSONAL_DATA, Context.MODE_PRIVATE);

        return sp.getInt(key, value);
    }

    public static void putLong(Context context, String key, long value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.INITIAL_PERSONAL_DATA, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).commit();

    }

    public static long getLong(Context context, String key, long value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.INITIAL_PERSONAL_DATA, Context.MODE_PRIVATE);

        return sp.getLong(key, value);
    }

    public static void putString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.INITIAL_PERSONAL_DATA, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();

    }

    public static String getString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.INITIAL_PERSONAL_DATA, Context.MODE_PRIVATE);

        return sp.getString(key, value);
    }
}
