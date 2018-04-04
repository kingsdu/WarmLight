package com.c317.warmlight.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.Activity.AddDateActivity;
import com.c317.warmlight.android.Activity.DateDetailActivity;
import com.c317.warmlight.android.Activity.MapActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.tabpager.DateTabDetails;
import com.c317.warmlight.android.tabpager.MyDateTabDetails;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Date_Fragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.iv_add_date)
    ImageView ivAddDate;
    @Bind(R.id.iv_locate_date)
    ImageView ivLocateDate;
    @Bind(R.id.vp_my_viewpager)
    ViewPager vpMyViewpager;
    @Bind(R.id.tp_my_indicator)
    TabPageIndicator tpMyIndicator;
    private String[] tabInfoDateType = {"全部友约","知识分享", "书籍交流", "学术论坛", "社团活动", "其他"};

    private ArrayList<DateTabDetails> mDatePagers;//友约页面


    @Override
    public View initView() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Theme_PageIndicatorDefaults);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_date,null);
        ButterKnife.bind(this, view);
        ivAddDate.setVisibility(View.VISIBLE);
        ivLocateDate.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("友约");

        //友约地图
        ivLocateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MapActivity.class);
                startActivity(intent);
            }
        });
        //新增友约
        ivAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddDateActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        mDatePagers = new ArrayList<>();
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITYLIST;
        for (int i = 0; i < tabInfoDateType.length; i++) {
            DateTabDetails myPager = new DateTabDetails(mActivity, url, i-1);
            mDatePagers.add(myPager);
        }
        vpMyViewpager.setAdapter(new DatePageAdapter());
        tpMyIndicator.setVisibility(View.VISIBLE);
        tpMyIndicator.setViewPager(vpMyViewpager);
        tpMyIndicator.setOnPageChangeListener(this);//此处必须给指示器设置监听，而不是mViewPager
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 友约
     */
    private class DatePageAdapter extends PagerAdapter {

        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabInfoDateType[position];
        }

        @Override
        public int getCount() {
            return tabInfoDateType.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final DateTabDetails pager = mDatePagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
