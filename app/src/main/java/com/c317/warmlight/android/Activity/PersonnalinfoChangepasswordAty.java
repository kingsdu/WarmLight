package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.MD5utils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.c317.warmlight.android.R.id.account;
import static com.c317.warmlight.android.R.id.et_newPassword;
import static com.c317.warmlight.android.R.id.et_newPassword2;
import static com.c317.warmlight.android.R.id.et_oldPassword;

/**
 * Created by Administrator on 2018/3/14.
 */

public class PersonnalinfoChangepasswordAty extends Activity {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(et_oldPassword)
    EditText etOldPassword;
    @Bind(et_newPassword)
    EditText etNewPassword;
    @Bind(et_newPassword2)
    EditText etNewPassword2;
    @Bind(R.id.btn_changepassword_finish)
    Button btnChangepasswordFinish;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.my_personnalinfochangepassword_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("修改密码");
        String account = UserManage.getInstance().getUserInfo(PersonnalinfoChangepasswordAty.this).account;
        clickSendBtn();
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 发送密码信息
     */
    private void clickSendBtn(){
        btnChangepasswordFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TextUtils.isEmpty(etOldPassword.getText()))) {
                    Toast.makeText(PersonnalinfoChangepasswordAty.this, "请填写原始密码",
                            Toast.LENGTH_LONG).show();
                } else if ((TextUtils.isEmpty(etNewPassword.getText()))) {
                    Toast.makeText(PersonnalinfoChangepasswordAty.this, "请填写新密码",
                            Toast.LENGTH_LONG).show();
                }else if ((TextUtils.isEmpty(etNewPassword2.getText()))) {
                    Toast.makeText(PersonnalinfoChangepasswordAty.this, "请再次确认新密码",
                            Toast.LENGTH_LONG).show();
                } else if (!etNewPassword2.getText().toString().equals(etNewPassword.getText().toString())) {
                    Toast.makeText(PersonnalinfoChangepasswordAty.this, "新密码两次设置值不同，请重新确认",
                            Toast.LENGTH_LONG).show();
                }else{
                    ChangePassword(UserManage.getInstance().getUserInfo(PersonnalinfoChangepasswordAty.this).account,MD5utils.md5(etNewPassword.getText().toString()));
                }
            }
        });
    }

    private void ChangePassword(final String account,final String password){
        RequestParams params = new RequestParams("http://14g97976j3.51mypc.cn:10759/my/modifyUser");
        params.addParameter("account", account);
        params.addParameter("password", password);
        x.http().request(HttpMethod.PUT, params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(
                        PersonnalinfoChangepasswordAty.this,
                        "密码修改成功",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //失败
                Toast.makeText(
                        PersonnalinfoChangepasswordAty.this,
                        "密码修改失败",
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
