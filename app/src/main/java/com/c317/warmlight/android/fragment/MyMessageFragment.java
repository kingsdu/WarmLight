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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.AddActivity;
import com.c317.warmlight.android.Activity.GroupChatActivity;
import com.c317.warmlight.android.Activity.NewFriendActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.GroupNewsDTO;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.tabpager.MyMessageTabDetails;
import com.c317.warmlight.android.tabpager.MyMessageTabGroupDetails;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MyMessageFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.vp_my_viewpager)
    ViewPager vpMyViewpager;
    @Bind(R.id.tp_my_indicator)
    TabPageIndicator tpMyIndicator;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.ll_mymessage_newfriend)
    LinearLayout llMymessageNewfriend;
    private String[] tabInfoMyMessage = {"单聊", "群聊"};

    private ArrayList<MyMessageTabDetails> mMessagePagers;//有读页面
    private String account;
    private GroupNewsDTO groupnewsDTO;
    private WarmLightDataBaseHelper dataBaseHelper;

    @Override
    public View initView() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Theme_PageIndicatorDefaults);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.my_mymessage_detail, null);
        ButterKnife.bind(this, view);
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的消息");
        tvTopbarRight.setText("添加");
        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(mActivity);
        account = UserManage.getInstance().getUserInfo(mActivity).account;
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
                mActivity.finish();
            }
        });

        //结束Fragment
        return view;
    }

    @Override
    public void initData() {
        getChatMessage();
        mMessagePagers = new ArrayList<>();
        for (int i = 0; i < tabInfoMyMessage.length; i++) {
            MyMessageTabDetails myPager = new MyMessageTabDetails(mActivity);
            mMessagePagers.add(myPager);
        }
//        for (int i = 1; i < tabInfoMyMessage.length+1; i++) {
//            MyMessageTabGroupDetails myPager = new MyMessageTabGroupDetails(mActivity);
//
//        }
        vpMyViewpager.setAdapter(new MyMessagePageAdapter());
        tpMyIndicator.setVisibility(View.VISIBLE);
        tpMyIndicator.setViewPager(vpMyViewpager);
        tpMyIndicator.setOnPageChangeListener(this);//此处必须给指示器设置监听，而不是mViewPager


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
     * 得到聊天信息（查询聊天信息）
     */
    private void getChatMessage(){
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTCHAT);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                groupnewsDTO = gson.fromJson(result, GroupNewsDTO.class);
                ArrayList<GroupNewsDTO.GroupNewsDTO_Content> groupNewsDTO_contents = new ArrayList<GroupNewsDTO.GroupNewsDTO_Content>();
                GroupNewsDTO.GroupNewsDTO_Content groupNewsDTO_content;
//                GroupNewsDTO.GroupNewsDTO_Content groupNewsDTO_content=new GroupNewsDTO.GroupNewsDTO_Content();
                for(int i=0;i<groupnewsDTO.data.size();i++){
                    groupNewsDTO_content = new GroupNewsDTO.GroupNewsDTO_Content();
                    groupNewsDTO_content.setChat_id(groupnewsDTO.data.get(i).getChat_id());
                    groupNewsDTO_content.setLastTime(groupnewsDTO.data.get(i).getLastTime());
                    groupNewsDTO_content.setAccount(groupnewsDTO.data.get(i).getAccount());
                    groupNewsDTO_content.setGroup_id(groupnewsDTO.data.get(i).getGroup_id());
                    groupNewsDTO_content.setChatTime(groupnewsDTO.data.get(i).getChatTime());
                    groupNewsDTO_content.setContent(groupnewsDTO.data.get(i).getContent());
                    groupNewsDTO_content.setRead(0);
                    groupNewsDTO_contents.add(groupNewsDTO_content);
//                    dataBaseHelper.InsertCollectInfoDate(groupnewsDTO.data.get(i));
                }

                dataBaseHelper.batchInsertGroupNews(groupNewsDTO_contents);
//                rightorleft();
//
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 我的友约
     */
    private class MyMessagePageAdapter extends PagerAdapter {

        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabInfoMyMessage[position];
        }

        @Override
        public int getCount() {
            return tabInfoMyMessage.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final MyMessageTabDetails pager = mMessagePagers.get(position);
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
