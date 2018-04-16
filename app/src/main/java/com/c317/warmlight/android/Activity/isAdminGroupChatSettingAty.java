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
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.SharedPrefUtility;
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

public class isAdminGroupChatSettingAty extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_isadminset_groupchat1)
    TextView tvIsadminsetGroupchat1;
    @Bind(R.id.rl_isadminset_groupchat)
    RelativeLayout rlIsadminsetGroupchat;
    @Bind(R.id.rl_deletegroup_groupchat)
    RelativeLayout rlDeletegroupGroupchat;
    @Bind(R.id.rl_groupmember_groupchat)
    RelativeLayout rlGroupmemberGroupchat;
    @Bind(R.id.tv_groupmember_num)
    TextView tvGroupmemberNum;

    private String account;
    private String mGroupName;
    private int mGroup_id;
    private static final int EDIT_GROUPNAME = 4;//编辑昵称
    private GroupMemberInfo groupmemberinfo;
    private int Num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.groupchat_isadminsetting_aty);
        ButterKnife.bind(this);
        SharedPrefUtility.removeParam(isAdminGroupChatSettingAty.this,AppConstants.GROUPNAME);
        ectractPutEra();
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("聊天设置");

        account = UserManage.getInstance().getUserInfo(isAdminGroupChatSettingAty.this).account;
        getGroupMemberNum();
        initData();
        //监听设置
        ivBackMe.setOnClickListener(this);
        rlIsadminsetGroupchat.setOnClickListener(this);
        rlGroupmemberGroupchat.setOnClickListener(this);
        rlDeletegroupGroupchat.setOnClickListener(this);

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
                tvGroupmemberNum.setText(Num+"名群成员");
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

    private void initData() {
        String groupName = (String) SharedPrefUtility.getParam(this, AppConstants.GROUPNAME, AppConstants.GROUPNAME);
        if((groupName.equals(AppConstants.GROUPNAME)) ){
            tvIsadminsetGroupchat1.setText(mGroupName);
        }else{
            if (!(groupName.equals(AppConstants.GROUPNAME))) {
                tvIsadminsetGroupchat1.setText(groupName);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.rl_isadminset_groupchat:
                Intent intent = new Intent(isAdminGroupChatSettingAty.this, GroupChatEditGroupnameAty.class);
                intent.putExtra("group_id", mGroup_id);
                intent.putExtra("groupName", mGroupName);
                startActivityForResult(intent, EDIT_GROUPNAME);
                break;
            case R.id.rl_groupmember_groupchat:
                intent = new Intent(isAdminGroupChatSettingAty.this, GroupMemberActivity.class);
                intent.putExtra("group_id", mGroup_id);
                startActivity(intent);
                break;
            case R.id.rl_deletegroup_groupchat:
                deletegroup(mGroup_id);
                break;
        }
    }

    public void deletegroup(final int mGroup_id) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUP;
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
                    Toast.makeText(isAdminGroupChatSettingAty.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(isAdminGroupChatSettingAty.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(isAdminGroupChatSettingAty.this, "删除失败", Toast.LENGTH_SHORT).show();
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
