package com.c317.warmlight.android.utils;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.c317.warmlight.android.common.Application_my;

/**
 * Created by Administrator on 2017/11/16.
 *
 * 封装常用UI操作代码
 * 和UI操作相关的便捷类
 */

public class UIUtils {


    public static Context getContext(){
        return Application_my.context;
    }

    public static View getXmlView(int layoutId){
        return View.inflate(getContext(),layoutId,null);
    }

    public static Handler handler(){
        return Application_my.handler;
    }


    public static int getcolor(int colorId){
        return ContextCompat.getColor(getContext(),colorId);
    }


    public static String[] getStringArr(int arrId){
        return getContext().getResources().getStringArray(arrId);
    }
}
