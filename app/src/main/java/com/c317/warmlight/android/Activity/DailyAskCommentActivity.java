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
import com.c317.warmlight.android.bean.Comment;
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

public class DailyAskCommentActivity extends Activity implements View.OnClickListener {


    @Bind(R.id.et_me_inputcomment)
    EditText etMeInputcomment;
    @Bind(R.id.btn_dailyask_release)
    Button btnDailyaskRelease;
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    private String question_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.me_dailyask_comment);
        ButterKnife.bind(this);
        tvTopbarTitle.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的回答");
        ivBackMe.setVisibility(View.VISIBLE);
        ivBackMe.setOnClickListener(this);
        btnDailyaskRelease.setOnClickListener(this);
        initData();
    }


    private void initData() {
        question_id = getIntent().getStringExtra("question_id");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dailyask_release:
                releaseAnswer();
                break;
            case R.id.iv_back_me:
                finish();
                break;
        }
    }


    private void releaseAnswer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ADDFIRSTCOMMENT;
        String comment = etMeInputcomment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            RequestParams params = new RequestParams(url);
            params.addParameter("searchID", question_id);
            params.addParameter("comContent", comment);
            params.addParameter("userID", UserManage.getInstance().getUserInfo(DailyAskCommentActivity.this).user_id);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Comment com = gson.fromJson(result, Comment.class);
                    if (com.code == 201) {
                        CommonUtils.showToastShort(DailyAskCommentActivity.this, "提交成功");
                    } else {
                        CommonUtils.showToastShort(DailyAskCommentActivity.this, "code is not 201");
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    CommonUtils.showToastShort(DailyAskCommentActivity.this, "评论失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
        finish();
    }



}
