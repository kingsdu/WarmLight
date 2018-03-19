package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.DataCleanManager;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.SharedPrefUtility;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.c317.warmlight.android.common.Application_my.context;

/**
 * Created by Administrator on 2018/3/11.
 */

public class SettingMeActivity extends Activity implements View.OnClickListener{

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.rl_wipecache_me)
    RelativeLayout rlWipecacheMe;
    @Bind(R.id.rl_feedback_me)
    RelativeLayout rlFeedbackMe;
    @Bind(R.id.rl_exit_me)
    RelativeLayout rlExitMe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.setting_me_aty);
        ButterKnife.bind(this);
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("设置");
        //监听设置
        ivBackMe.setOnClickListener(this);
        rlWipecacheMe.setOnClickListener(this);
        rlExitMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.rl_wipecache_me:
                DataCleanManager.cleanInternalCache(SettingMeActivity.this);
                Toast.makeText(SettingMeActivity.this, "已清除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_exit_me:
                exit();
                break;
        }
    }

    /**
    * 退出
    * @params
    * @author Du
    * @Date 2018/3/18 22:29
    **/
    private void exit() {
        SharedPrefUtility.setParam(SettingMeActivity.this, SharedPrefUtility.IS_LOGIN, false);
        SharedPrefUtility.removeParam(SettingMeActivity.this, SharedPrefUtility.LOGIN_DATA);
        exitAllActivity();
    }


    /**
    * 退出所有activity
    * @params
    * @author Du
    * @Date 2018/3/19 8:22
    **/
    public void exitAllActivity(){
        Application_my.getInstance().removeALLActivity();
    }

}
