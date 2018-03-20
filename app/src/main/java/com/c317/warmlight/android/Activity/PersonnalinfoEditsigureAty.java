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
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
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
 * Created by Administrator on 2018/3/18.
 */

public class PersonnalinfoEditsigureAty extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.et_editsigure_personnalinfo)
    EditText etEditsigurePersonnalinfo;

    private String account;
    private String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.GETUSERINFO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.my_personnalinfoeditsigure_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("编辑个性签名");
        tvTopbarRight.setText("保存");
        account = UserManage.getInstance().getUserInfo(PersonnalinfoEditsigureAty.this).account;
        getUserSigure();//获取用户个性签名显示
        tvTopbarRight.setOnClickListener(this);
        ivBackMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_topbar_right:
                saveUserSigure();
                break;
            case R.id.iv_back_me:
                finish();
                break;
        }
    }

    /**
     * 存储用户个性签名
     */
    private void saveUserSigure() {
        String signature = etEditsigurePersonnalinfo.getText().toString();
        if (!TextUtils.isEmpty(signature)) {
            Editsigure(UserManage.getInstance().getUserInfo(PersonnalinfoEditsigureAty.this).account, signature);
            SharedPrefUtility.setParam(this, AppConstants.SIGNATURE, signature);
        } else {
            CommonUtils.showToastShort(this, "输入用户名为空");
        }
        finish();
    }
    private void getUserSigure(){
        String param = (String) SharedPrefUtility.getParam(this, AppConstants.SIGNATURE, AppConstants.SIGNATURE);
        if (!TextUtils.isEmpty(param) && param.equals(AppConstants.SIGNATURE)) {
            getSigureFromServer();//快速加载
        }
        if (!TextUtils.isEmpty(param)) {
            etEditsigurePersonnalinfo.setText(param);
        }else{
            String cache = CacheUtils.getCache(url, PersonnalinfoEditsigureAty.this);
            if (!TextUtils.isEmpty(cache)) {
                processData(cache);
            } else {
                getSigureFromServer();//快速加载
            }
        }
    }

    /**
     * 处理数据
     * @param cache
     */
    private void processData(String cache) {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(cache, UserInfo.class);
        UserInfo.UserInfo_content userInfo_content = userInfo.data;
        setUserInfoView(userInfo_content);
    }

    private void getSigureFromServer() {
        RequestParams params = new RequestParams(url);
        params.addParameter(AppConstants.ACCOUNT, account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserInfo userinfo = gson.fromJson(result, UserInfo.class);
                UserInfo.UserInfo_content userInfo_content = userinfo.data;
                CacheUtils.setCache(url, result, PersonnalinfoEditsigureAty.this);
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
        etEditsigurePersonnalinfo.setText(userInfo_content.signature);
    }

    public void Editsigure(final String account, final String signature) {
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.MODIFYUSER);
        params.addParameter(AppConstants.ACCOUNT, account);
        params.addParameter(AppConstants.SIGNATURE, signature);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 200) {
                    Toast.makeText(PersonnalinfoEditsigureAty.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonnalinfoEditsigureAty.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(PersonnalinfoEditsigureAty.this, "修改失败", Toast.LENGTH_SHORT).show();
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