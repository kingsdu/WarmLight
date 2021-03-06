package com.c317.warmlight.android.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.SettingMyDateActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.Collect_Article_Info;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.tabpager.MyDateTabDetails;
import com.c317.warmlight.android.tabpager.MyReadTabDetails;
import com.c317.warmlight.android.utils.CommonUtils;
import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/13.
 */
@SuppressLint("ValidFragment")
public class MydateFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tp_my_indicator)
    TabPageIndicator tpMyIndicator;
    @Bind(R.id.vp_my_viewpager)
    ViewPager vpMyViewpager;
    private String[] tabInfoDate = {"发起","待约","已约","收藏"};
    private String[] tabInfoRead = {"收藏","评论"};

    private ArrayList<MyDateTabDetails> mDatePagers;//有读页面
    private ArrayList<MyReadTabDetails> mReadPagers;//友约页面
    private String mType;
    private static final String TAG_READ = "TAG_READ";
    private static final String TAG_DATE = "TAG_DATE";
    private String lastTime = null;

    public MydateFragment(String type){
        mType = type;
    }

    public MydateFragment(){

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.my_mydate_detail,null);
        ButterKnife.bind(this, view);
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        return view;
    }

    /**
     * 1 初始化页面
     * 2 请求数据发起、待约、已约的数据（填充ListView）
     * 3 添加、初始化切换的页签详情页面，并传入填充ListView需要的数据（新建）
     * 4 填充页签详情页面中的ListView
     * @params
     * @author Du
     * @Date 2018/3/12 17:46
     **/
    @Override
    public void initData() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETMYACTIVITYLIST;
        if(mType.equals(TAG_DATE)){
            tvTopbarTitle.setText("我的友约");
            mDatePagers = new ArrayList<>();
            for(int i=0;i<tabInfoDate.length;i++){
                MyDateTabDetails myPager = new MyDateTabDetails(mActivity,url,i+1);
                mDatePagers.add(myPager);
            }
            vpMyViewpager.setAdapter(new MyDataPageAdapter());
            tpMyIndicator.setVisibility(View.VISIBLE);
            tpMyIndicator.setViewPager(vpMyViewpager);
            tpMyIndicator.setOnPageChangeListener(this);//此处必须给指示器设置监听，而不是mViewPager
        }else if(mType.equals(TAG_READ)){
            getLastTime();
            tvTopbarTitle.setText("我的有读");
            mReadPagers = new ArrayList<>();
            for(int i=0;i<tabInfoRead.length;i++){
                MyReadTabDetails myPager = new MyReadTabDetails(mActivity,i);
                mReadPagers.add(myPager);
            }
            vpMyViewpager.setAdapter(new MyReadPageAdapter());
            tpMyIndicator.setVisibility(View.VISIBLE);
            tpMyIndicator.setViewPager(vpMyViewpager);
            tpMyIndicator.setOnPageChangeListener(this);//此处必须给指示器设置监听，而不是mViewPager
        }
    }


    /**
    * 获取收藏的评论信息
    * @params
    * @author Du
    * @Date 2018/4/14 9:25
    **/
    private void getLastTime() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTSAVE;
        RequestParams params = new RequestParams(url);
        params.addParameter("account", UserManage.getInstance().getUserInfo(mActivity).account);
        params.addParameter("type", "w");
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Collect_Article_Info collect_article_info = gson.fromJson(result, Collect_Article_Info.class);
                if (collect_article_info.code == 200) {
                    lastTime = collect_article_info.data.get(collect_article_info.data.size()-1).lastTime;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(mActivity,"获取收藏数据失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
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
     * 我的友约
     * */
    private class MyDataPageAdapter extends PagerAdapter {

        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabInfoDate[position];
        }

        @Override
        public int getCount() {
            return tabInfoDate.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final MyDateTabDetails pager = mDatePagers.get(position);
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


    /**
     * 我的友读
     * */

    private class MyReadPageAdapter extends PagerAdapter {

        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabInfoRead[position];
        }

        @Override
        public int getCount() {
            return tabInfoRead.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final MyReadTabDetails myReadTabDetails = mReadPagers.get(position);
            View view = myReadTabDetails.mRootView;
            container.addView(view);
            myReadTabDetails.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
