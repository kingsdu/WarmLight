package com.c317.warmlight.android.tabpager;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseMenuDetailPager;

/**
 * Created by Administrator on 2018/4/6.
 */

public class MyMessageTabGroupDetails extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    public MyMessageTabGroupDetails(Activity activity) {
        super(activity);
    }
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_mymessagetabgroup_detail, null);
        return view;
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


}
