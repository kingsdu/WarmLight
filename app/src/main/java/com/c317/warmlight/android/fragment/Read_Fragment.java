package com.c317.warmlight.android.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Image;
import com.c317.warmlight.android.bean.Index;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.UIUtils;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Read_Fragment extends Fragment {

    AsyncHttpClient client = new AsyncHttpClient();
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.vp_barner)
    ViewPager vpBarner;
    @Bind(R.id.btn_barner)
    CirclePageIndicator btnBarner;
    @Bind(R.id.ll_dateActivity)
    LinearLayout llDateActivity;


    private Index index;
    private LinearLayout ll_dateActivity;
    private View fragment_activity;
    private SwipeCardsView swipeCardsView;
    private CardAdapter cardAdapter;
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = UIUtils.getXmlView(R.layout.fragment_read);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initView() {
        vpBarner.setAdapter(new TopImageAdapter());
        btnBarner.setViewPager(vpBarner);
        btnBarner.setSnap(true);

        ll_dateActivity = (LinearLayout) getActivity().findViewById(R.id.ll_dateActivity);
        fragment_activity = LayoutInflater.from(getActivity()).inflate(
                R.layout.card_switch, null);
        swipeCardsView = (SwipeCardsView) fragment_activity.findViewById(R.id.swipCardsView);
        cardAdapter = new CardAdapter();
        swipeCardsView.setAdapter(cardAdapter);
        ll_dateActivity.addView(fragment_activity);

        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {

            @Override
            public void onShow(int count) {
            }

            @Override
            public void onCardVanish(int count, SwipeCardsView.SlideType type) {
                switch (type) {
                    case LEFT:
                        if (count == index.imageList.size() - 1)
                            doRetry();
                        break;
                    case RIGHT:
                        if (count == index.imageList.size() - 1)
                            doRetry();
                        break;
                }
            }

            @Override
            public void onItemClick(View cardImageView, int index) {

            }
        });


        //图片轮播
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    int currentItem = vpBarner.getCurrentItem();
                    currentItem++;
                    if(currentItem > index.imageListTop.size()-1){
                        currentItem = 0;
                    }
                    vpBarner.setCurrentItem(currentItem);
                    mHandler.sendEmptyMessageDelayed(0, 3000);//继续发送延时3s的信息，形成内循环
                }
            };
            //保证启动自动轮播只执行一次
            mHandler.sendEmptyMessageDelayed(0, 3000);//发送延时3s的信息
        }
    }

    ;


    /**
     * @Description 初始化数据
     * @params
     * @author Du
     * @Date 2017/11/21 20:16
     **/
    private void initData() {
        index = new Index();
        client.get(AppNetConfig.INDEX, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.e("Du----", "content" + content);
                JSONObject jsonObject = JSON.parseObject(content);
                String imageArr = jsonObject.getString("imageArr");
                String imageTop = jsonObject.getString("imageTop");
                List<Image> imageList = JSON.parseArray(imageArr, Image.class);
                List<Image> imageListTop = JSON.parseArray(imageTop, Image.class);
                index.imageList = imageList;
                index.imageListTop = imageListTop;
                initView();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.e("Du----", "onFailure" + error);
                super.onFailure(error, content);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class TopImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return index.imageListTop == null ? 0 : index.imageListTop.size();//容错
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imageUrl = index.imageListTop.get(position).IMAGETOP;
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    /**
     * 从头开始，重新浏览
     */
    public void doRetry() {
        if (cardAdapter == null) {
            cardAdapter = new CardAdapter();
            swipeCardsView.setAdapter(cardAdapter);
        } else {
            swipeCardsView.notifyDatasetChanged(-1);
        }
    }


    private class CardAdapter extends BaseCardAdapter {
        @Override
        public int getCount() {
            return index.imageList == null ? 0 : index.imageList.size();
        }

        @Override
        public int getCardLayoutId() {
            return R.layout.card_switch;
        }

        @Override
        public void onBindData(int position, View cardview) {
            if (index.imageList == null || index.imageList.size() == 0) {
                return;
            }
            ImageView imageView = (ImageView) cardview.findViewById(R.id.iv_pic1);
            TextView tv_actTitle = (TextView) cardview.findViewById(R.id.tv_actTitle);
            TextView tv_actContent = (TextView) cardview.findViewById(R.id.tv_actContent);
            ImageView iv_activityType = (ImageView) cardview.findViewById(R.id.iv_activityType);
            TextView tv_activityType = (TextView) cardview.findViewById(R.id.tv_activityType);
            ImageView iv_activityStar = (ImageView) cardview.findViewById(R.id.iv_activityStar);
            TextView tv_activityStar = (TextView) cardview.findViewById(R.id.tv_activityStar);
            ImageView iv_activityCompliment = (ImageView) cardview.findViewById(R.id.iv_activityCompliment);
            TextView tv_activityCompliment = (TextView) cardview.findViewById(R.id.tv_activityCompliment);
            ImageView iv_activityComment = (ImageView) cardview.findViewById(R.id.iv_activityComment);
            TextView tv_activityComment = (TextView) cardview.findViewById(R.id.tv_activityComment);


            iv_activityType.setImageResource(R.drawable.date_off);
            tv_activityType.setText("最新友约");
            iv_activityStar.setImageResource(R.drawable.star);
            tv_activityStar.setText("66");
            iv_activityCompliment.setImageResource(R.drawable.compliment);
            tv_activityCompliment.setText("66");
            iv_activityComment.setImageResource(R.drawable.comment);
            tv_activityComment.setText("66");
            String imageUrl = index.imageList.get(position).IMAURL;
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            tv_actTitle.setText("吃鸡");
            tv_actContent.setText("大吉大利吃鸡吃鸡！！！大吉大利吃鸡吃鸡！！！！");
        }

        @Override
        public int getVisibleCardCount() {
            return index.imageList.size();
        }
    }

}
