package com.c317.warmlight.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/12/4.
 *
 * SharedPreferences封装
 */

public class PrefUtils {


    private static final String FILE_NAME = "share_date";

    public static final String INDEX="index";
    public static final String LOGIN_DATA="loginData";
    public static final String IS_LOGIN="isLogin";

    public static boolean getBoolean(Context ctx, String key, boolean defValue){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }


    public static void setBoolean(Context ctx,String key,boolean defValue){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,defValue).commit();
    }


    public static String getString(Context ctx,String key,String defValue){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }


    public static void setString(Context ctx,String key,String defValue){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key,defValue).commit();
    }

    public static void cleanString(Context ctx,String key){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static int getInt(Context ctx,String key,int defValue){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }


    public static void setInt(Context ctx,String key,int defValue){
        SharedPreferences sp =
                ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key,defValue).commit();
    }





}
