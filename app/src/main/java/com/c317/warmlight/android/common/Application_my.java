package com.c317.warmlight.android.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Application_my extends Application {

    private static Application_my instance;

    public Application_my() {
    }

    public synchronized static Application_my getInstance() {
        if (null == instance) {
            instance = new Application_my();
        }
        return instance;
    }

    public static Context context = null;

    public static Handler handler = null;

    public static Thread mainThread = null;

    public static int mainThreadId = 0;//判断当前代码是否在主线程中运行

    public List<Activity> oList = new ArrayList<>();

    @Override
    public void onCreate() {
        context = getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();//主线程
        mainThreadId = Process.myTid();//获取当前线程id。
        super.onCreate();
        x.Ext.init(this);
    }


    public void addActivity(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }


    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }


    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        try {
            for (Activity activity : oList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.exit(0);
        }

    }
}
