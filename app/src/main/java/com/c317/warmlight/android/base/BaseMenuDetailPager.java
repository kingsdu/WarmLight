package com.c317.warmlight.android.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Administrator on 2018/3/12.
 *
 * 页签切换详情页面
 */

public abstract class BaseMenuDetailPager extends Fragment {

    public Activity mActivity;
    public View mRootView;//菜单详情页面根布局
    public int DOWNPAGESIZE = 0;
    public int UPPAGESIZE = 0;
    public int PAGE = 1;


    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }


    //初始化布局，子类实现
    public abstract View initView();

    //初始化数据
    public void initData(){}

}
