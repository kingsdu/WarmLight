package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AddActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.btn_mymessage_add)
    Button btnMymessageAdd;
    @Bind(R.id.et_mymessage_friendaccount)
    EditText etMymessageFriendaccount;
    private String account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.mymessage_add_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("添加");
        account = UserManage.getInstance().getUserInfo(AddActivity.this).account;
        ivBackMe.setOnClickListener(this);
        btnMymessageAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.btn_mymessage_add:
                Add();
                break;
        }

    }

    /**
     * 请求添加好友
     */
    private void Add() {
        String friendaccount = etMymessageFriendaccount.getText().toString();
        if (!TextUtils.isEmpty(friendaccount)) {
            requestAddFriend(UserManage.getInstance().getUserInfo(AddActivity.this).account, friendaccount);
        } else {
            CommonUtils.showToastShort(this, "输入用户名为空");
        }
        finish();
    }

    public void requestAddFriend(final String account, final String friendaccount) {
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTFRIEND);
        params.addParameter("requestMan", account);
        params.addParameter("responseMan", friendaccount);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
//                    Toast.makeText(AddActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(AddActivity.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(AddActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
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
