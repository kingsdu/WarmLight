package com.c317.warmlight.android.holder;

import android.view.View;

/**
 * Created by Administrator on 2017/11/25.
 */

public abstract class BaseHolder<T> {

    private View mRootView;// item的布局对象
    private T data;// item对应的数据

    public BaseHolder() {
        mRootView = initView(data);// 初始化布局
        mRootView.setTag(this);// 给view设置tag
    }

    // 初始化布局的方法必须由子类实现
    public abstract View initView(T number);

    // 返回布局对象
    public View getRootView() {
        return mRootView;
    }

    // 设置数据
    public void setData(T data) {
        this.data = data;
        refreshView(data);
    }

    // 获取数据
    public T getData() {
        return data;
    }

    // 刷新界面,更新数据,子类必须实现
    public abstract void refreshView(T data);

}
