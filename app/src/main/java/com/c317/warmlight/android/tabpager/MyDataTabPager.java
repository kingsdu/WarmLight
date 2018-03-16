package com.c317.warmlight.android.tabpager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseMenuDetailPager;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/12.
 */

public class MyDataTabPager extends BaseMenuDetailPager {


    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tp_my_indicator)
    TabPageIndicator tpMyIndicator;
    @Bind(R.id.vp_my_viewpager)
    ViewPager vpMyViewpager;
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    private String[] tabInfo = {"发起","待约","已约"};
    private ArrayList<MyDateTabDetails> mPagers;

    public MyDataTabPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.my_mydate_detail, null);
        ButterKnife.bind(this,view);
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的友约");
        return view;
    }

    public void initData(){
        mPagers = new ArrayList<>();
        for(int i=0;i<tabInfo.length;i++){
            MyDateTabDetails myPager = new MyDateTabDetails(mActivity,"http://14g97976j3.51mypc.cn:10759/youyue/getMyActivityList",i+1);
            mPagers.add(myPager);
        }
        vpMyViewpager.setAdapter(new MyDataPageAdapter());
        tpMyIndicator.setVisibility(View.VISIBLE);
        tpMyIndicator.setViewPager(vpMyViewpager);
//        tpMyIndicator.setOnPageChangeListener(this);//此处必须给指示器设置监听，而不是mViewPager
    }


    private class MyDataPageAdapter extends PagerAdapter {

        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabInfo[position];
        }

        @Override
        public int getCount() {
            return tabInfo.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final MyDateTabDetails pager = mPagers.get(position);
            View view = pager.mRootView;

            container.addView(pager.mRootView);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
