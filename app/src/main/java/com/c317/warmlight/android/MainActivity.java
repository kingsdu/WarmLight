package com.c317.warmlight.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.c317.warmlight.android.fragment.Date_Fragment;
import com.c317.warmlight.android.fragment.Me_Fragment;
import com.c317.warmlight.android.fragment.Read_Fragment;
import com.c317.warmlight.android.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {


    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.iv_read)
    ImageView ivRead;
    @Bind(R.id.tv_read)
    TextView tvRead;
    @Bind(R.id.ll_read)
    LinearLayout llRead;
    @Bind(R.id.iv_date)
    ImageView ivDate;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.ll_date)
    LinearLayout llDate;
    @Bind(R.id.iv_me)
    ImageView ivMe;
    @Bind(R.id.tv_me)
    TextView tvMe;
    @Bind(R.id.ll_me)
    LinearLayout llMe;
    private Date_Fragment date_fragment;
    private Me_Fragment me_fragment;
    private Read_Fragment read_fragment;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDate();
    }

    private void initDate() {
        setSelect(0);//默认选中首页
    }


    @OnClick({R.id.ll_read,R.id.ll_date,R.id.ll_me})
    public void changeTab(View view){
        switch (view.getId()){
            case R.id.ll_read:
                setSelect(0);
                break;
            case R.id.ll_date:
                setSelect(1);
                break;
            case R.id.ll_me:
                setSelect(2);
                break;
        }
    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        hideFragment();
        resetTab();
        switch (i) {
            case 0:
                if (read_fragment == null) {
                    read_fragment = new Read_Fragment();
                    ft.add(R.id.content, read_fragment);
                }
                ivRead.setImageResource(R.drawable.read_on);
                tvRead.setTextColor(UIUtils.getcolor(R.color.home_back_selected));
                ft.show(read_fragment);
                break;
            case 1:
                if (date_fragment == null) {
                    date_fragment = new Date_Fragment();
                    ft.add(R.id.content, date_fragment);
                }
                ivDate.setImageResource(R.drawable.date_on);
                tvDate.setTextColor(UIUtils.getcolor(R.color.home_back_selected));
                ft.show(date_fragment);
                break;
            case 2:
                if (me_fragment == null) {
                    me_fragment = new Me_Fragment();
                    ft.add(R.id.content, me_fragment);
                }
                ivMe.setImageResource(R.drawable.me_on);
                tvMe.setTextColor(UIUtils.getcolor(R.color.home_back_selected));
                ft.show(me_fragment);
                break;
        }
        ft.commit();
    }


    //重置Fragment的状态
    private void resetTab() {
        ivRead.setImageResource(R.drawable.read_off);
        ivDate.setImageResource(R.drawable.date_off);
        ivMe.setImageResource(R.drawable.me_off);

        tvRead.setTextColor(UIUtils.getcolor(R.color.home_back_unselected));
        tvDate.setTextColor(UIUtils.getcolor(R.color.home_back_unselected));
        tvMe.setTextColor(UIUtils.getcolor(R.color.home_back_unselected));
    }


    //隐藏前一个显示的Fragment
    private void hideFragment() {
        if(read_fragment != null){
            ft.hide(read_fragment);
        }
        if(date_fragment != null){
            ft.hide(date_fragment);
        }
        if(me_fragment != null){
            ft.hide(me_fragment);
        }
    }


}
