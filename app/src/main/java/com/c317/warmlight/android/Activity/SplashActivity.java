package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.c317.warmlight.android.MainActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.utils.SharedPrefUtility;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/6.
 */

public class SplashActivity extends Activity {

    private static final int GO_HOME = 0;//去主页
    private static final int GO_LOGIN = 1;//去登录页
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    /**
     * 跳转判断
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME://去主页
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_LOGIN://去登录页
                    Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);
        rlRoot.startAnimation(animAlpha);//
        boolean isLogin = (Boolean) SharedPrefUtility.getParam(this, SharedPrefUtility.IS_LOGIN, false);
        if (isLogin)//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
        {
            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
        } else {
            mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
        }
    }
}
