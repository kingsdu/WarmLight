package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.DailyDetailsAsk;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/9.
 */

public class DailyAskAddActivity extends Activity implements View.OnClickListener{

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.et_me_addinputtitle)
    EditText etMeAddinputtitle;
    @Bind(R.id.et_me_addinputcontent)
    EditText etMeAddinputcontent;
    @Bind(R.id.btn_dailyask_addrelease)
    Button btnDailyaskAddrelease;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.me_dailyask_add);
        ButterKnife.bind(this);
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setVisibility(View.VISIBLE);
        ivBackMe.setOnClickListener(this);
        btnDailyaskAddrelease.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.btn_dailyask_addrelease:
                addQuestion();
                break;
        }
    }



    /**
    * 添加问题
    * @params
    * @author Du
    * @Date 2018/4/9 14:27
    **/
    private void addQuestion() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.CREATEQUESTION;
        String title = etMeAddinputtitle.getText().toString();
        String content = etMeAddinputcontent.getText().toString();
        if(TextUtils.isEmpty(title)){
            CommonUtils.showToastShort(DailyAskAddActivity.this,"请输入标题");
            return;
        }

        if(TextUtils.isEmpty(content)){
            CommonUtils.showToastShort(DailyAskAddActivity.this,"请输入内容");
            return;
        }

        RequestParams params = new RequestParams(url);
        params.addParameter("title",title);
        params.addParameter("content",content);
        params.addParameter("proposer", UserManage.getInstance().getUserInfo(DailyAskAddActivity.this).account);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DailyDetailsAsk dailyDetailsAsk = gson.fromJson(result, DailyDetailsAsk.class);
                if(dailyDetailsAsk.code == 201){
                    CommonUtils.showToastShort(DailyAskAddActivity.this,"提问成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(DailyAskAddActivity.this,"提问失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        finish();
    }
}
