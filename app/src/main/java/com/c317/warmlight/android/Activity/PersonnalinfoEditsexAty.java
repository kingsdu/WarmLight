package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/18.
 */

public class PersonnalinfoEditsexAty extends Activity {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.tv_editsex_personnalinfo)
    TextView tvEditsexPersonnalinfo;
    private String sex1;
    private String account;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_personnalinfoeditsex_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("编辑性别");
        tvTopbarRight.setText("保存");
        account = UserManage.getInstance().getUserInfo(PersonnalinfoEditsexAty.this).account;
        getSexFromServer();
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSexFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + "getUserInfo";
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserInfo userinfo = gson.fromJson(result, UserInfo.class);
                UserInfo.UserInfo_content userInfo_content = userinfo.data;
                setUserInfoView(userInfo_content);
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

    private void setUserInfoView(UserInfo.UserInfo_content userInfo_content) {
        if (userInfo_content.sex == 0) {
            sex1 = "男";
        } else {
            sex1 = "女";
        }

        tvEditsexPersonnalinfo.setText(sex1);
    }
}
