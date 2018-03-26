package com.c317.warmlight.android.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/12/4.
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
//    protected boolean isVisible;

    //fragment创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    //初始化Fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    //fragment依赖的activity的onCreate方法执行结束
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
    }

//    /**
//     * 在这里实现Fragment数据的缓加载.
//     * @param isVisibleToUser
//     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(getUserVisibleHint()) {
//            isVisible = true;
//            onVisible();
//        } else {
//            isVisible = false;
//            onInvisible();
//        }
//    }
//    protected void onVisible(){
//        lazyLoad();
//    }
//    protected abstract void lazyLoad();
//    protected void onInvisible(){}

    //子类实现初始化布局
    public abstract View initView();

    //初始化数据
    public abstract void initData();
}
