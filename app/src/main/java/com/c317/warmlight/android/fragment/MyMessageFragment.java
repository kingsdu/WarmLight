package com.c317.warmlight.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.AddActivity;
import com.c317.warmlight.android.Activity.NewFriendActivity;
import com.c317.warmlight.android.Adapter.MyMessageFragmentPagerAdapter;
import com.c317.warmlight.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MyMessageFragment extends Fragment {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.ll_mymessage_newfriend)
    LinearLayout llMymessageNewfriend;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyMessageFragmentPagerAdapter myMessageFragmentPagerAdapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_mymessage_detail, container, false);
        initView();
        //结束Fragment
        ButterKnife.bind(this, view);
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的消息");
        tvTopbarRight.setText("添加");
        tvTopbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });
        llMymessageNewfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    private void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        myMessageFragmentPagerAdapter = new MyMessageFragmentPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(myMessageFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);


        //设置Tab的图标
        one.setIcon(R.drawable.grouppage_circle);
        two.setIcon(R.drawable.grouppage_groupchat);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}