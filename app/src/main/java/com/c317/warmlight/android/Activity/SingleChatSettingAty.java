package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/2.
 */

public class SingleChatSettingAty extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.rl_deletefriend_mymessage)
    RelativeLayout rlDeletefriendMymessage;
    private int mFriend_id;
    private int friend_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.mymessage_singlechatsetting_aty);
        ButterKnife.bind(this);
        ectractPutEra();
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("聊天设置");
        //监听设置
        ivBackMe.setOnClickListener(this);
        rlDeletefriendMymessage.setOnClickListener(this);
    }

    private void ectractPutEra() {
        mFriend_id = getIntent().getIntExtra("friend_id",friend_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.rl_deletefriend_mymessage:
                deletefriend();
                break;
        }
    }

    /**
     * 删除好友
     */
    private void deletefriend(){
        requestdeletefriend(mFriend_id);
    }

    public void requestdeletefriend(int mFriend_id){
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTFRIEND;
        RequestParams params = new RequestParams(url);
        params.addParameter("friend_id", mFriend_id);
        x.http().request(HttpMethod.DELETE, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    Toast.makeText(SingleChatSettingAty.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SingleChatSettingAty.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SingleChatSettingAty.this, "删除失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

}
