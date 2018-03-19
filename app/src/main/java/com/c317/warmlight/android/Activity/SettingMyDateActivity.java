package com.c317.warmlight.android.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.fragment.MydateFragment;
import com.c317.warmlight.android.tabpager.MyDateTabDetails;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/12.
 * <p>
 * 我的模块，我的友约详情
 */

public class SettingMyDateActivity extends FragmentActivity {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_mydate_fragment);
        ButterKnife.bind(this);
        String tag = getIntent().getStringExtra("TAG");
        //初始化数据
        initFragment(tag);
    }

    /**
     * 初始化页面fragment
     *
     * @params
     * @author Du
     * @Date 2018/3/13 8:10
     **/
    private void initFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开始事务
        transaction.replace(R.id.fl_mydate_fragment, new MydateFragment(tag));
        transaction.commit();
    }


}
