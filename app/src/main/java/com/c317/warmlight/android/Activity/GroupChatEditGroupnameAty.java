package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
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

public class GroupChatEditGroupnameAty extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.et_editgroupname_groupchat)
    EditText etEditgroupnameGroupchat;
    private String account;
    private int mGroup_id;
    private String mGroupName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.groupchat_editgroupname_aty);
        ButterKnife.bind(this);
        ectractPutEra();
        account = UserManage.getInstance().getUserInfo(GroupChatEditGroupnameAty.this).account;
        etEditgroupnameGroupchat.setText(mGroupName);//快速加载
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("编辑群名称");
        tvTopbarRight.setText("保存");
        ivBackMe.setOnClickListener(this);
        tvTopbarRight.setOnClickListener(this);
    }

    private void ectractPutEra() {
        mGroup_id = getIntent().getIntExtra("group_id",mGroup_id);
        mGroupName = getIntent().getStringExtra("groupName");
    }

//    private void getGroupName(){
//        String param = (String) SharedPrefUtility.getParam(this, AppConstants.GROUPNAME, AppConstants.GROUPNAME);
//        if (!TextUtils.isEmpty(param) && param.equals(AppConstants.GROUPNAME)) {
//            etEditgroupnameGroupchat.setText(mGroupName);//快速加载
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.tv_topbar_right:
                saveGroupName();
                break;
        }
    }

    /**
     * 存储用户个性签名
     *
     * @params
     * @author Du
     * @Date 2018/3/19 9:00
     **/
    private void saveGroupName() {
        String groupName = etEditgroupnameGroupchat.getText().toString();
        if (!TextUtils.isEmpty(groupName)) {
            Editgroupname(UserManage.getInstance().getUserInfo(GroupChatEditGroupnameAty.this).account, groupName);
            SharedPrefUtility.setParam(this, AppConstants.GROUPNAME, groupName);
        } else {
            CommonUtils.showToastShort(this, "输入用户名为空");
        }
        finish();
    }

    public void Editgroupname(final String account, final String groupName) {
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUP);
        params.addParameter(AppConstants.ACCOUNT, account);
        params.addParameter("group_id", mGroup_id);
        params.addParameter(AppConstants.GROUPNAME, groupName);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    Toast.makeText(GroupChatEditGroupnameAty.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupChatEditGroupnameAty.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(GroupChatEditGroupnameAty.this, "修改失败", Toast.LENGTH_SHORT).show();
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
