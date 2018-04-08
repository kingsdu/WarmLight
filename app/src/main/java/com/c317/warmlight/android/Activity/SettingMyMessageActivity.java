package com.c317.warmlight.android.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.fragment.MyMessageFragment;
import com.c317.warmlight.android.fragment.MydateFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class SettingMyMessageActivity extends FragmentActivity {
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_mymessage_fragment);
        ButterKnife.bind(this);

        //初始化数据
        initFragment();
    }


    /**
     * 初始化数据
     * @param
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开始事务
        transaction.replace(R.id.fl_mymessage_fragment, new MyMessageFragment());
        transaction.commit();
    }
}
