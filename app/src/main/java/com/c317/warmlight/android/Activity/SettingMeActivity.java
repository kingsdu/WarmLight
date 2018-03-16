package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.DataCleanManager;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/11.
 */

public class SettingMeActivity extends Activity {

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
        setContentView(R.layout.setting_me_aty);
        ButterKnife.bind(this);
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("设置");

        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlWipecacheMe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DataCleanManager.cleanInternalCache(SettingMeActivity.this);
                Toast.makeText(SettingMeActivity.this, "已清除", Toast.LENGTH_LONG).show();
            }
        });

        rlExitMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUtils.cacheToken(SettingMeActivity.this, "-1");
                exit();
            }
        });


    }

    @SuppressWarnings("deprecation")
    public void exit(){

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);
        } else {// android2.1
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.restartPackage(getPackageName());
        }
    }
}
