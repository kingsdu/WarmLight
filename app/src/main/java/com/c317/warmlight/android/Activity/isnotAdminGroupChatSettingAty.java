package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.GroupMemberInfo;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/9.
 */

public class isnotAdminGroupChatSettingAty extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_isnotadminset_groupchat1)
    TextView tvIsnotadminsetGroupchat1;
    @Bind(R.id.rl_exitgroup_groupchat)
    RelativeLayout rlExitgroupGroupchat;
    @Bind(R.id.tv_groupmember_num)
    TextView tvGroupmemberNum;
    @Bind(R.id.rl_groupmember_groupchat)
    RelativeLayout rlGroupmemberGroupchat;

    private String account;
    private String mGroupName;
    private int mGroup_id;
    private GroupMemberInfo groupmemberinfo;
    private int Num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.groupchat_isnotadminsetting_aty);
        ButterKnife.bind(this);
        ectractPutEra();
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("聊天设置");
        tvIsnotadminsetGroupchat1.setText(mGroupName);
        account = UserManage.getInstance().getUserInfo(isnotAdminGroupChatSettingAty.this).account;
        getGroupMemberNum();
        //监听设置
        ivBackMe.setOnClickListener(this);
        rlGroupmemberGroupchat.setOnClickListener(this);
        rlExitgroupGroupchat.setOnClickListener(this);
    }

    private void ectractPutEra() {
        mGroupName = getIntent().getStringExtra("groupName");
        mGroup_id = getIntent().getIntExtra("group_id", mGroup_id);
    }

    private void getGroupMemberNum() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUPMEMBER;
        RequestParams params = new RequestParams(url);
        params.addParameter("group_id", mGroup_id);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                groupmemberinfo = gson.fromJson(result, GroupMemberInfo.class);
                Num = groupmemberinfo.data.size();
                tvGroupmemberNum.setText(Num + "名群成员");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.rl_groupmember_groupchat:
                Intent intent = new Intent(isnotAdminGroupChatSettingAty.this, GroupMemberActivity.class);
                intent.putExtra("group_id", mGroup_id);
                startActivity(intent);
                break;
            case R.id.rl_exitgroup_groupchat:
                exitgroup(mGroup_id);
                break;
        }
    }

    public void exitgroup(final int mGroup_id) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUPMEMBER;
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        params.addParameter("group_id", mGroup_id);
        x.http().request(HttpMethod.DELETE, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    Toast.makeText(isnotAdminGroupChatSettingAty.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(isnotAdminGroupChatSettingAty.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(isnotAdminGroupChatSettingAty.this, "删除失败", Toast.LENGTH_SHORT).show();
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
