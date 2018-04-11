package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.SharedPrefUtility;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/9.
 */

public class GroupChatSettingAty extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_groupchatset_groupname1)
    TextView tvGroupchatsetGroupname1;
    @Bind(R.id.rl_groupchatset_groupname)
    RelativeLayout rlGroupchatsetGroupname;

    private String account;
    private String mGroupName;
    private int mGroup_id;
    private static final int EDIT_GROUPNAME = 4;//编辑昵称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.mymessage_groupchatsetting_aty);
        ButterKnife.bind(this);
        ectractPutEra();
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("聊天设置");
        tvGroupchatsetGroupname1.setText(mGroupName);
        account = UserManage.getInstance().getUserInfo(GroupChatSettingAty.this).account;
        initData();
        //监听设置
        ivBackMe.setOnClickListener(this);
        rlGroupchatsetGroupname.setOnClickListener(this);
    }

    private void ectractPutEra() {
        mGroupName = getIntent().getStringExtra("groupName");
        mGroup_id = getIntent().getIntExtra("group_id",mGroup_id);
    }

    private void initData() {
        String groupName = (String) SharedPrefUtility.getParam(this, AppConstants.GROUPNAME, AppConstants.GROUPNAME);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.rl_groupchatset_groupname:
                Intent intent = new Intent(GroupChatSettingAty.this, GroupChatEditGroupnameAty.class);
                intent.putExtra("group_id", mGroup_id);
                intent.putExtra("groupName",mGroupName);
                startActivityForResult(intent, EDIT_GROUPNAME);
                break;
        }
    }
}
