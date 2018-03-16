package com.c317.warmlight.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.PersonnalInfoActivity;
import com.c317.warmlight.android.Activity.SettingMeActivity;
import com.c317.warmlight.android.Activity.SettingMyDateActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.tabpager.MyDataTabPager;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Me_Fragment extends BaseFragment {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.iv_me_setting)
    ImageView ivMeSetting;
    @Bind(R.id.circleImageView)
    CircleImageView circleImageView;
    @Bind(R.id.tv_nickname_me)
    TextView tvNicknameMe;
    @Bind(R.id.tv_renown_me)
    TextView tvRenownMe;
    @Bind(R.id.btn_signIn)
    Button btnSignIn;
    @Bind(R.id.rl_myread)
    RelativeLayout rlMyread;
    @Bind(R.id.rl_mydate)
    RelativeLayout rlMydate;
    @Bind(R.id.rl_my_answer)
    RelativeLayout rlMyAnswer;
    @Bind(R.id.rl_mymessage)
    RelativeLayout rlMymessage;

    @Override
    public View initView() {
        View view = UIUtils.getXmlView(R.layout.fragment_me);
        ButterKnife.bind(this, view);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        ivMeSetting.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的");
        //个人资料初始化
        tvNicknameMe.setText("LBJ");
        tvRenownMe.setText("总得分：40000");
        btnSignIn.setText("签到");
        //监听事件初始化
        ivMeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SettingMeActivity.class);
                startActivity(intent);
            }
        });
        //圆形头像个人资料监听
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PersonnalInfoActivity.class);
                startActivity(intent);
            }
        });
        //我的友约监听
        rlMydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SettingMyDateActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
