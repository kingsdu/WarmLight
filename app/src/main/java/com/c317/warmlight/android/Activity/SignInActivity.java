package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.utils.MD5utils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/2.
 */

public class SignInActivity extends Activity {

    @Bind(R.id.oldPassword)
    TextView oldPassword;
    @Bind(R.id.et_oldPassword)
    EditText et_account;
    @Bind(R.id.tv_newPassword)
    TextView tvNewPassword;
    @Bind(R.id.et_newPassword)
    EditText et_password;
    @Bind(R.id.tv_newPassword2)
    TextView tvNewPassword2;
    @Bind(R.id.et_newPassword2)
    EditText et_password2;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.signin_aty);
        ButterKnife.bind(this);
        tvTopbarTitle.setText("注册新用户");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TextUtils.isEmpty(et_account.getText()))) {
                    Toast.makeText(SignInActivity.this, "请填写用户名",
                            Toast.LENGTH_LONG).show();
                } else if (et_password.getText().toString().length() < 6) {
                    Toast.makeText(SignInActivity.this, "密码不能少于6位！",
                            Toast.LENGTH_LONG).show();
                } else if ((TextUtils.isEmpty(et_password.getText()))) {
                    Toast.makeText(SignInActivity.this, "请填写密码",
                            Toast.LENGTH_LONG).show();
                } else if ((TextUtils.isEmpty(et_password2.getText()))) {
                    Toast.makeText(SignInActivity.this, "请再次确认密码",
                            Toast.LENGTH_LONG).show();
                } else if (!et_password2.getText().toString().equals(et_password.getText().toString())) {
                    Toast.makeText(SignInActivity.this, "密码两次设置值不同，请重新确认",
                            Toast.LENGTH_LONG).show();
                } else {
                    signIn(et_account.getText().toString(), MD5utils.md5(et_password.getText().toString()));
                }
            }
        });


    }


    private void signIn(String account, String password) {
        RequestParams params = new RequestParams("http://14g97976j3.51mypc.cn:10759/my/createUser");
        params.addParameter("account", account);
        params.addParameter("password", password);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(
                        SignInActivity.this,
                        "注册成功",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //失败
                Toast.makeText(
                        SignInActivity.this,
                        "注册失败",
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

