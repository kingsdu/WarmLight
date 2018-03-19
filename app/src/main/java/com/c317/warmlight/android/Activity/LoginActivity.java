package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.c317.warmlight.android.MainActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.fragment.Read_Fragment;
import com.c317.warmlight.android.utils.MD5utils;
import com.c317.warmlight.android.utils.PrefUtils;
import com.c317.warmlight.android.utils.SharedPrefUtility;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/3/2.
 */

public class LoginActivity extends Activity {

    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btnRegister)
    Button btnRegister;
    @Bind(R.id.pic)
    LinearLayout pic;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.login_aty);
        ButterKnife.bind(this);
        btnRegister.getBackground().setAlpha(150);
        //注册
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        //登陆
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(account.getText())) {
                    Toast.makeText(LoginActivity.this,
                            R.string.account_can_not_be_empty,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(LoginActivity.this,
                            R.string.password_can_not_be_empty,
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    login(account.getText().toString(), MD5utils.md5(password.getText().toString()));
                }
            }
        });
    }


    private void login(final String account, final String password) {
        RequestParams params = new RequestParams("http://14g97976j3.51mypc.cn:10759/my/userLogin");
        params.addParameter("account", account);
        params.addParameter("password", password);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    UserManage.getInstance().saveUserInfo(LoginActivity.this, account, password);
                    //保存登录状态
                    SharedPrefUtility.setParam(LoginActivity.this, SharedPrefUtility.IS_LOGIN, true);
                    //保存登录个人信息
                    SharedPrefUtility.setParam(LoginActivity.this, SharedPrefUtility.LOGIN_DATA, result);
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", true);
                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", true);
                Toast.makeText(LoginActivity.this, "取消登陆", Toast.LENGTH_SHORT).show();
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
