package com.c317.warmlight.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.Bignews;
import com.c317.warmlight.android.bean.Cardnews;
import com.c317.warmlight.android.bean.Horizontalnews;
import com.c317.warmlight.android.bean.ReadData;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.bean.Topnews;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.c317.warmlight.android.views.AutoCycleView;
import com.c317.warmlight.android.views.NestListView;
import com.c317.warmlight.android.views.SubAdapterController;
import com.google.gson.Gson;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/11/16.
 */

public class Read_Fragment extends BaseFragment {

    @Bind(R.id.rv_mainListView)
    RecyclerView rvMainListView;
    private ArrayList<ReadData.ReadData_Children> newsData;
    Handler mHandler;
    //    private CardAdapter cardAdapter;
    private SwipeCardsView swipeCardsView;
    private List<List<Object>> mAllDatas;
    private List<Object> mDataType;//布局类型

    private List<View> mViewList;
    private CardAdapter cardAdapter;
    private ViewPager vpBarner;
    private CirclePageIndicator btnBarner;
    //    //横向滑动布局
    private SubAdapterController mSubAdapterCrl;

    public Read_Fragment(ArrayList<ReadData.ReadData_Children> children) {
        newsData = children;
//        Log.e("Du----", "newsData" +  children.size());
    }

    @Override
    public View initView() {
        View view = UIUtils.getXmlView(R.layout.fragment_read);
        ButterKnife.bind(this, view);
//        rvMainListView.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        rvMainListView.setLayoutManager(mLayoutManager);
//        rvMainListView.setAdapter(new MainAdapter(mActivity));
        return view;
    }


    @Override
    public void initData() {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < newsData.size(); i++) {
            urls.add(newsData.get(i).url);
        }
        getReadData(urls);
    }

    private void getReadData(final List url) {
        mAllDatas = new ArrayList();
        mDataType = new ArrayList();
        //1 TopNews
        RequestParams params = new RequestParams(AppNetConfig.WARMBASEURL + url.get(0));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Topnews topnew = gson.fromJson(result, Topnews.class);
                for (int i = 0; i < 1; i++) {
                    List<Object> mAllData = new ArrayList();
                    for (int j = 0; j < topnew.topnews.size(); j++) {
                        mAllData.add(topnew.topnews.get(j));
                    }
                    mDataType.add(AppConstants.RV_TOP_NEWS);
                    mAllDatas.add(mAllData);
                }
                //2 Cardnews
                RequestParams params = new RequestParams(AppNetConfig.WARMBASEURL + url.get(1));
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Cardnews cardnews = gson.fromJson(result, Cardnews.class);
                        for (int i = 0; i < 1; i++) {
                            List<Object> mAllData = new ArrayList();
                            for (int j = 0; j < cardnews.cardnews.size(); j++) {
                                mAllData.add(cardnews.cardnews.get(j));
                            }
                            mDataType.add(AppConstants.RV_CARD_NEWS);
                            mAllDatas.add(mAllData);
                        }
                        //3 Bignews
                        RequestParams params = new RequestParams(AppNetConfig.WARMBASEURL + url.get(2));
                        x.http().get(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Gson gson = new Gson();
                                Smallnews smallnew = gson.fromJson(result, Smallnews.class);
                                for (int i = 0; i < smallnew.smallnews.size(); i++) {
                                    List<Object> mAllData = new ArrayList();
                                    mAllData.add(smallnew.smallnews.get(i));
                                    mAllDatas.add(mAllData);
                                    mDataType.add(AppConstants.RV_SMALL_NEWS);
                                }
                                //4 Horizontalnews
                                RequestParams params = new RequestParams(AppNetConfig.WARMBASEURL + url.get(3));
                                x.http().get(params, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Gson gson = new Gson();
                                        Horizontalnews horizontalnews = gson.fromJson(result, Horizontalnews.class);
                                        for (int i = 0; i < 1; i++) {
                                            List<Object> mAllData = new ArrayList();
                                            for (int j = 0; j < horizontalnews.Horizontalnews.size(); j++) {
                                                mAllData.add(horizontalnews.Horizontalnews.get(j));
                                            }
                                            mDataType.add(AppConstants.RV_HORIZONTAL_NEWS);
                                            mAllDatas.add(mAllData);
                                        }
                                        //5 Bignews
                                        RequestParams params = new RequestParams(AppNetConfig.WARMBASEURL + url.get(4));
                                        x.http().get(params, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Gson gson = new Gson();
                                                Bignews bignews = gson.fromJson(result, Bignews.class);
                                                for (int i = 0; i < 2; i++) {
                                                    List<Object> mAllData = new ArrayList();
                                                    mAllData.add(bignews.bignews.get(i));
                                                    mAllDatas.add(mAllData);
                                                    mDataType.add(AppConstants.RV_BIG_NEWS);
                                                }
                                                rvMainListView.setHasFixedSize(true);
                                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                                rvMainListView.setLayoutManager(mLayoutManager);
                                                rvMainListView.setAdapter(new MainAdapter(mActivity, mAllDatas, mDataType));
                                                rvMainListView.addItemDecoration(new SpaceItemDecoration(10));
                                            }

                                            //请求异常后的回调方法
                                            @Override
                                            public void onError(Throwable ex, boolean isOnCallback) {
                                                Toast.makeText(mActivity, ex.toString(), Toast.LENGTH_SHORT).show();
                                            }

                                            //主动调用取消请求的回调方法
                                            @Override
                                            public void onCancelled(CancelledException cex) {
                                            }

                                            @Override
                                            public void onFinished() {
                                            }
                                        });
                                    }

                                    //请求异常后的回调方法
                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        Toast.makeText(mActivity, ex.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    //主动调用取消请求的回调方法
                                    @Override
                                    public void onCancelled(CancelledException cex) {
                                    }

                                    @Override
                                    public void onFinished() {
                                    }
                                });
                            }

                            //请求异常后的回调方法
                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Toast.makeText(mActivity, ex.toString(), Toast.LENGTH_SHORT).show();
                            }

                            //主动调用取消请求的回调方法
                            @Override
                            public void onCancelled(CancelledException cex) {
                            }

                            @Override
                            public void onFinished() {
                            }
                        });
                    }

                    //请求异常后的回调方法
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(mActivity, ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                    //主动调用取消请求的回调方法
                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });

            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, ex.toString(), Toast.LENGTH_SHORT).show();
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
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


    private class MainAdapter extends RecyclerView.Adapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private final List<List<Object>> mAll_datas;
        private final List<Object> mData_type;


        public MainAdapter(Context context, List<List<Object>> mAllData, List<Object> mDataType) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mAll_datas = mAllData;
            mData_type = mDataType;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == AppConstants.RV_TOP_NEWS) {
                View view = mInflater.inflate(R.layout.top_news, parent, false);
                return new TopNewsHolder(view);
            } else if (viewType == AppConstants.RV_CARD_NEWS) {
                View view = mInflater.inflate(R.layout.card_switch, parent, false);
                return new CardNewsHolder(view);
            } else if (viewType == AppConstants.RV_BIG_NEWS) {
                View view = mInflater.inflate(R.layout.big_news, parent, false);
                return new BigNewsHolder(view);
            } else if (viewType == AppConstants.RV_SMALL_NEWS) {
                View view = mInflater.inflate(R.layout.small_news, parent, false);
                return new SmallNewsHolder(view);
            } else if (viewType == AppConstants.RV_HORIZONTAL_NEWS) {
                View view = mInflater.inflate(R.layout.horizontal_news, parent, false);
                return new HorizontalHolder(view);
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == AppConstants.RV_TOP_NEWS) {
//                initCycleViewPager(topnew);
//                ((TopNewsHolder) holder).cycleView.setViewList(mViewList);//设置数据
//                ((TopNewsHolder) holder).cycleView.startCycle();//启动自动轮播
                vpBarner.setAdapter(new TopImageAdapter(mAll_datas.get(position)));
                btnBarner.setViewPager(vpBarner);
                btnBarner.setSnap(true);
                //图片轮播
                if (mHandler == null) {
                    final List<Object> finalTopnew = mAll_datas.get(position);
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            int currentItem = vpBarner.getCurrentItem();
                            currentItem++;
                            if (currentItem > finalTopnew.size() - 1) {
                                currentItem = 0;
                            }
                            vpBarner.setCurrentItem(currentItem);
                            mHandler.sendEmptyMessageDelayed(0, 3000);//继续发送延时3s的信息，形成内循环
                        }
                    };
                    //保证启动自动轮播只执行一次
                    mHandler.sendEmptyMessageDelayed(0, 3000);//发送延时3s的信息
                }
            } else if (getItemViewType(position) == AppConstants.RV_SMALL_NEWS) {
                Smallnews.Smallnews_menu smallnews_menu = (Smallnews.Smallnews_menu) mAll_datas.get(position).get(0);
                Picasso.with(getActivity()).load(smallnews_menu.imageurl).into(((SmallNewsHolder) holder).small_image);
                ((SmallNewsHolder) holder).small_title.setText(smallnews_menu.title);
                ((SmallNewsHolder) holder).small_user.setText(smallnews_menu.user);
                ((SmallNewsHolder) holder).small_views.setText(String.valueOf(smallnews_menu.views));
            } else if (getItemViewType(position) == AppConstants.RV_CARD_NEWS) {
                cardAdapter = new CardAdapter(mAll_datas.get(position));
                swipeCardsView.setAdapter(cardAdapter);
                final List<Object> finalCardnew = mAll_datas.get(position);
                swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
                    @Override
                    public void onShow(int count) {
                    }

                    @Override
                    public void onCardVanish(int count, SwipeCardsView.SlideType type) {
                        switch (type) {
                            case LEFT:
                                if (count == finalCardnew.size() - 1) {
                                    doRetry(finalCardnew);
                                }
                                break;
                            case RIGHT:
                                if (count == finalCardnew.size() - 1) {
                                    doRetry(finalCardnew);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onItemClick(View cardImageView, int index) {

                    }
                });
            } else if (getItemViewType(position) == AppConstants.RV_HORIZONTAL_NEWS) {
                mSubAdapterCrl = new SubAdapterController(mAllDatas.get(position), mContext);
                //holder = (MainViewHolder)holder;
                //设置头标识
                if (position == 5) {
                    ((HorizontalHolder) holder).nestListView.setNestViewHeaderText("小桔猜猜");
                }
                ((HorizontalHolder) holder).nestListView.setAdapter(mSubAdapterCrl.getAdapter());
            } else if (getItemViewType(position) == AppConstants.RV_BIG_NEWS) {
                Bignews.Bignews_menu bignews_menu = (Bignews.Bignews_menu) mAll_datas.get(position).get(0);
                Picasso.with(getActivity()).load(bignews_menu.imageurl).into(((BigNewsHolder) holder).big_image);
                ((BigNewsHolder) holder).big_title.setText(bignews_menu.title);
                ((BigNewsHolder) holder).big_content.setText(bignews_menu.content);
            }
        }

        @Override
        public int getItemCount() {
            return mAll_datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (int) (mData_type.get(position));//获取布局类型
        }

        class TopNewsHolder extends RecyclerView.ViewHolder {
            //            public AutoCycleView cycleView;
            public TopNewsHolder(View itemView) {
                super(itemView);
//                cycleView = (AutoCycleView) itemView.findViewById(R.id.cycle_view);
                vpBarner = (ViewPager) itemView.findViewById(R.id.vp_barner);
                btnBarner = (CirclePageIndicator) itemView.findViewById(R.id.btn_barner);
            }
        }

        class CardNewsHolder extends RecyclerView.ViewHolder {
            public CardNewsHolder(View itemView) {
                super(itemView);
                swipeCardsView = (SwipeCardsView) itemView.findViewById(R.id.swipCardsView);
            }
        }

        class HorizontalHolder extends RecyclerView.ViewHolder {
            public NestListView nestListView;

            public HorizontalHolder(View itemView) {
                super(itemView);
                nestListView = (NestListView) itemView.findViewById(R.id.nestlistview);
            }
        }

        class SmallNewsHolder extends RecyclerView.ViewHolder {
            public ImageView small_image;
            public TextView small_title;
            public TextView small_user;
            public TextView small_views;

            public SmallNewsHolder(View itemView) {
                super(itemView);
                small_image = (ImageView) itemView.findViewById(R.id.iv_small_image);
                small_title = (TextView) itemView.findViewById(R.id.tv_small_title);
                small_user = (TextView) itemView.findViewById(R.id.tv_small_user);
                small_views = (TextView) itemView.findViewById(R.id.tv_small_views);
            }
        }

        class BigNewsHolder extends RecyclerView.ViewHolder {
            public ImageView big_image;
            public TextView big_title;
            public TextView big_content;

            public BigNewsHolder(View itemView) {
                super(itemView);
                big_image = (ImageView) itemView.findViewById(R.id.iv_big_image);
                big_title = (TextView) itemView.findViewById(R.id.iv_big_title);
                big_content = (TextView) itemView.findViewById(R.id.iv_big_content);
            }
        }


    }


    /**
     * 从头开始，重新浏览
     */
    public void doRetry(List<Object> cardnews) {
        if (cardAdapter == null) {
            cardAdapter = new CardAdapter(cardnews);
            swipeCardsView.setAdapter(cardAdapter);
        } else {
            swipeCardsView.notifyDatasetChanged(-1);
        }
    }


    private class CardAdapter extends BaseCardAdapter {

        private final List<Object> cardnew;

        public CardAdapter(List<Object> data) {
            cardnew = data;
        }

        @Override
        public int getCount() {
            return cardnew == null ? 0 : cardnew.size();
        }

        @Override
        public int getCardLayoutId() {
            return R.layout.card_switch;
        }

        @Override
        public void onBindData(int position, View cardview) {
            Cardnews.Cardnews_menu cardnews_menu = (Cardnews.Cardnews_menu) cardnew.get(position);
            if (cardnews_menu.imageurl == null) {
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
            tv_activityStar.setText(cardnews_menu.star);
            iv_activityCompliment.setImageResource(R.drawable.compliment);
            tv_activityCompliment.setText(cardnews_menu.compliment);
            iv_activityComment.setImageResource(R.drawable.comment);
            tv_activityComment.setText(cardnews_menu.user);
            String imageUrl = cardnews_menu.imageurl;
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            tv_actTitle.setText(cardnews_menu.title);
            tv_actContent.setText(cardnews_menu.content);
        }

        @Override
        public int getVisibleCardCount() {
            return cardnew.size();
        }
    }


    private class TopImageAdapter extends PagerAdapter {

        private final List<Object> topnewsData;

        public TopImageAdapter(List<Object> data) {
            topnewsData = data;
        }

        @Override
        public int getCount() {
            return topnewsData == null ? 0 : topnewsData.size();//容错
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Topnews.Topnews_menu topnews_menu = (Topnews.Topnews_menu) topnewsData.get(position);
            String imageUrl = topnews_menu.imageurl;
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


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            //设置左右的间隔如果想设置的话自行设置，我这用不到就注释掉了
/*          outRect.left = space;
            outRect.right = space;*/

            //       System.out.println("position"+parent.getChildPosition(view));
            //       System.out.println("count"+parent.getChildCount());

            //         if(parent.getChildPosition(view) != parent.getChildCount() - 1)
            outRect.bottom = space;

            //改成使用上面的间隔来设置
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

}






