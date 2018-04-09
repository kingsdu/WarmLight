package com.c317.warmlight.android.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.c317.warmlight.android.fragment.GroupChat_Fragment;
import com.c317.warmlight.android.fragment.SingleChat_Fragment;

/**
 * Created by Administrator on 2018/4/8.
 */

public class MyMessageFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"单聊", "群聊"};
    public MyMessageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new GroupChat_Fragment();
        }

        return new SingleChat_Fragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
