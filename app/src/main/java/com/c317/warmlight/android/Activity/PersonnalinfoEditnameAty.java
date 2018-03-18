package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/18.
 */

public class PersonnalinfoEditnameAty extends Activity {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.et_editname_personnalinfo)
    EditText etEditnamePersonnalinfo;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    private String account;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_personnalinfoeditname_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("编辑昵称");
        tvTopbarRight.setText("保存");
        account = UserManage.getInstance().getUserInfo(PersonnalinfoEditnameAty.this).account;
        getNameFromServer();
        onClickListener();
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener();
                finish();
            }
        });
    }

    private void getNameFromServer() {
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
        etEditnamePersonnalinfo.setText(userInfo_content.username);
    }

    private void onClickListener() {
        tvTopbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editname(UserManage.getInstance().getUserInfo(PersonnalinfoEditnameAty.this).account,
                        etEditnamePersonnalinfo.getText().toString());
            }
        });
    }

    public void Editname(final String account, final String username){
        RequestParams params = new RequestParams("http://14g97976j3.51mypc.cn:10759/my/modifyUser");
        params.addParameter("account", account);
        params.addParameter("username", username);
        x.http().request(HttpMethod.PUT, params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(
                        PersonnalinfoEditnameAty.this,
                        "保存成功",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //失败
                Toast.makeText(
                        PersonnalinfoEditnameAty.this,
                        "保存失败",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


}
