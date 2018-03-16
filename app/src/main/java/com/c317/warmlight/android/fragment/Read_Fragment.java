package com.c317.warmlight.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.Activity.DateDetailActivity;
import com.c317.warmlight.android.Activity.NewsDetailActivity;
import com.c317.warmlight.android.Activity.TopDetailsActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.Bignews;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.bean.OrangeGuess;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.bean.Topnews;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.c317.warmlight.android.views.BigNewsController;
import com.c317.warmlight.android.views.BigNewsListView;
import com.c317.warmlight.android.views.NestListView;
import com.c317.warmlight.android.views.SmallNewsController;
import com.c317.warmlight.android.views.SmallNewsListView;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.easydone.swiperefreshendless.EndlessRecyclerOnScrollListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


/**
 * Created by Administrator on 2017/11/16.
 */

public class Read_Fragment extends BaseFragment {

    @Bind(R.id.rv_mainListView)
    RecyclerView rvMainListView;
    Handler mHandler;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    //    private CardAdapter cardAdapter;
    private SwipeCardsView swipeCardsView;

    private List<List<Object>> mAllDatas = new ArrayList();//初始化数据
    private List<Object> mDataType = new ArrayList();//布局类型

    private List<View> mViewList;
    private CardAdapter cardAdapter;
    private ViewPager vpBarner;
    private CirclePageIndicator btnBarner;
    //    //横向滑动布局
    private SubAdapterController mSubAdapterCrl;
    private SmallNewsController smallNewsController;
    private BigNewsController bigNewsController;

    private MainAdapter mainAdapter;

    private static int PAGESIZE = 1;
    private boolean first = true;//下拉刷新，判断是否为第一次
    private boolean upFlag = false;//上拉加载更多，区分下拉刷新

    @Override
    public View initView() {
        View view = UIUtils.getXmlView(R.layout.fragment_read);
        ButterKnife.bind(this, view);
//        tvTopbarTitle.setText("友读");
        swipeRefreshLayout.setColorSchemeResources(
                R.color.main_orange
        );
        //设置页面刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable
                        .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .map(new Func1<Long, Object>() {
                            @Override
                            public Object call(Long aLong) {
                                upFlag = true;//下拉刷新
                                mAllDatas.clear();
                                mDataType.clear();
                                initData();
                                return null;
                            }
                        }).subscribe();
            }
        });//设置刷新监听

        return view;
    }


    @Override
    public void initData() {
        List<String> urls = new ArrayList<>();
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.TOPNEWS + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITY + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ARTICLE + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.GUESS + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ARTICLE + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        getReadData(urls);
        PAGESIZE++;
    }


    private void getReadData(final List<String> url) {
        //1 头条
        RequestParams params = new RequestParams(url.get(0));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Topnews topnew = gson.fromJson(result, Topnews.class);
                for (int i = 0; i < 1; i++) {
                    List<Object> mAllData = new ArrayList();
                    for (int j = 0; j < topnew.data.size(); j++) {
                        mAllData.add(topnew.data.get(j));
                    }
                    mDataType.add(AppConstants.RV_TOP_NEWS);
                    mAllDatas.add(mAllData);
                }
                //2 友约
                RequestParams params = new RequestParams(url.get(1));
                x.http().get(params, new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        DateNews dateNews = gson.fromJson(result, DateNews.class);
                        for (int i = 0; i < 1; i++) {
                            List<Object> mAllData = new ArrayList();
                            for (int j = 0; j < dateNews.data.detail.size(); j++) {
                                mAllData.add(dateNews.data.detail.get(j));
                            }
                            mDataType.add(AppConstants.RV_DATE_NEWS);
                            mAllDatas.add(mAllData);
                        }
                        //3 小图新闻
                        RequestParams params = new RequestParams(url.get(2));
                        x.http().get(params, new CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Gson gson = new Gson();
                                Smallnews smallnew = gson.fromJson(result, Smallnews.class);
                                for (int i = 0; i < smallnew.data.detail.size(); i++) {
                                    List<Object> mAllData = new ArrayList();
                                    mAllData.add(smallnew.data.detail.get(i));
                                    mAllDatas.add(mAllData);
                                    mDataType.add(AppConstants.RV_ARTICLE_NEWS_SMALL);
                                }
                                //4 小聚猜猜（横向书籍）
                                RequestParams params = new RequestParams(url.get(3));
                                x.http().get(params, new CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Gson gson = new Gson();
                                        OrangeGuess orangeGuess = gson.fromJson(result, OrangeGuess.class);
                                        for (int i = 0; i < 1; i++) {
                                            List<Object> mAllData = new ArrayList();
                                            for (int j = 0; j < orangeGuess.data.detail.size(); j++) {
                                                mAllData.add(orangeGuess.data.detail.get(j));
                                            }
                                            mDataType.add(AppConstants.RV_GUESS_NEWS);
                                            mAllDatas.add(mAllData);
                                        }
                                        //5 大图新闻
                                        RequestParams params = new RequestParams(url.get(4));
                                        x.http().get(params, new CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Gson gson = new Gson();
                                                Bignews bignews = gson.fromJson(result, Bignews.class);
                                                for (int i = 0; i < 2; i++) {
                                                    List<Object> mAllData = new ArrayList();
                                                    mAllData.add(bignews.data.detail.get(i));
                                                    mAllDatas.add(mAllData);
                                                    mDataType.add(AppConstants.RV_ARTICLE_NEWS_BIG);
                                                }
                                                if (upFlag) {
                                                    //上拉加载更多
                                                    upGetMoreData(mAllDatas, mDataType);
                                                } else {
                                                    //下拉刷新
                                                    downGetMoreData(mAllDatas, mDataType);
                                                }

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

    /**
     * 上拉加载更多，使用新的数据重新覆盖就的数据
     * 注意，此处需要重新设置addOnScrollListener，否则上拉加载更多无法监听
     */
    private void upGetMoreData(List<List<Object>> mAllDatas, List<Object> mDataType) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvMainListView.setLayoutManager(mLayoutManager);
        mainAdapter = new MainAdapter(mActivity, mAllDatas, mDataType);
        rvMainListView.setAdapter(mainAdapter);
        rvMainListView.addItemDecoration(new SpaceItemDecoration(10));
        rvMainListView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            public void onLoadMore(int currentPage) {
                simulateLoadMoreData();
            }
        });
        swipeRefreshLayout.setRefreshing(false);//进度条消失
        upFlag = false;
    }


    /**
     * 下拉刷新，第一次调用会在调用initData时自动进行，之后走else
     */
    public void downGetMoreData(List<List<Object>> mAllDatas, List<Object> mDataType) {
        //首次直接调用初始化布局
        if (first) {
            rvMainListView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvMainListView.setLayoutManager(mLayoutManager);
            mainAdapter = new MainAdapter(mActivity, mAllDatas, mDataType);
            rvMainListView.setAdapter(mainAdapter);
            rvMainListView.addItemDecoration(new SpaceItemDecoration(10));
            rvMainListView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                public void onLoadMore(int currentPage) {
                    simulateLoadMoreData();//此处多刷新一次
                }
            });
            first = false;
        } else {
            //非首次调用，直接刷新页面即可
            mainAdapter.notifyDataSetChanged();
        }
    }

    private void simulateLoadMoreData() {
        Observable
                .timer(1, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        loadMoreData();
                        return null;
                    }
                }).subscribe();
    }


    private void loadMoreData() {
        initData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        /**
         * @Description
         * @params
         * @author Du
         * @Date 2017/12/6 10:41
         **/
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == AppConstants.RV_TOP_NEWS) {
                View view = mInflater.inflate(R.layout.top_news, parent, false);
                return new TopNewsHolder(view);
            } else if (viewType == AppConstants.RV_DATE_NEWS) {
                View view = mInflater.inflate(R.layout.card_switch, parent, false);
                return new CardNewsHolder(view);
            } else if (viewType == AppConstants.RV_ARTICLE_NEWS_SMALL) {
                View view = mInflater.inflate(R.layout.small_news, parent, false);
                return new SmallNewsHolder(view);
            } else if (viewType == AppConstants.RV_GUESS_NEWS) {
                View view = mInflater.inflate(R.layout.horizontal_news, parent, false);
                return new HorizontalHolder(view);
            } else if (viewType == AppConstants.RV_ARTICLE_NEWS_BIG) {
                View view = mInflater.inflate(R.layout.big_news, parent, false);
                return new BigNewsHolder(view);
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == AppConstants.RV_TOP_NEWS) {
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
            } else if (getItemViewType(position) == AppConstants.RV_ARTICLE_NEWS_SMALL) {
                smallNewsController = new SmallNewsController(mAllDatas.get(position), mContext);
                ((SmallNewsHolder) holder).smallNewsView.setAdapter(smallNewsController.getAdapter());
            } else if (getItemViewType(position) == AppConstants.RV_DATE_NEWS) {
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
                    public void onItemClick(View cardImageView, final int index) {
                        Intent intent = new Intent(mActivity, DateDetailActivity.class);
                        List<Object> cardData = mAll_datas.get(position);
                        DateNews.DateNews_Detail dateNews_detail = (DateNews.DateNews_Detail) cardData.get(index);
                        intent.putExtra("picUrl", dateNews_detail.picture);
                        intent.putExtra("title", dateNews_detail.title);
                        intent.putExtra("content", dateNews_detail.content);
                        intent.putExtra("readNum", dateNews_detail.readNum);
                        intent.putExtra("agreeNum", dateNews_detail.agreeNum);
                        intent.putExtra("activity_id", dateNews_detail.activity_id);
                        mActivity.startActivity(intent);
                    }
                });
            } else if (getItemViewType(position) == AppConstants.RV_GUESS_NEWS) {
                mSubAdapterCrl = new SubAdapterController(mAllDatas.get(position), mContext);
                //holder = (MainViewHolder)holder;
                //设置头标识
                if (position == 5) {
                    ((HorizontalHolder) holder).nestListView.setNestViewHeaderText("小桔猜猜");
                }
                ((HorizontalHolder) holder).nestListView.setAdapter(mSubAdapterCrl.getAdapter());
            } else if (getItemViewType(position) == AppConstants.RV_ARTICLE_NEWS_BIG) {
                bigNewsController = new BigNewsController(mAllDatas.get(position), mContext);
                ((BigNewsHolder) holder).bigNewsView.setAdapter(bigNewsController.getAdapter());
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
            public SmallNewsListView smallNewsView;//小图新闻

            public SmallNewsHolder(View itemView) {
                super(itemView);
                smallNewsView = (SmallNewsListView) itemView.findViewById(R.id.rv_smallListView);
            }
        }

        class BigNewsHolder extends RecyclerView.ViewHolder {
            public BigNewsListView bigNewsView;//小图新闻

            public BigNewsHolder(View itemView) {
                super(itemView);
                bigNewsView = (BigNewsListView) itemView.findViewById(R.id.rv_bignewsListview);
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
            DateNews.DateNews_Detail dateNews_detail = (DateNews.DateNews_Detail) cardnew.get(position);
            if (dateNews_detail.picture.equals(null) || dateNews_detail.picture.equals("")) {
                dateNews_detail.picture = "activity/20171227210800.png";
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
            tv_activityStar.setText(String.valueOf(dateNews_detail.commentNum));
            iv_activityCompliment.setImageResource(R.drawable.compliment);
            tv_activityCompliment.setText(String.valueOf(dateNews_detail.agreeNum));
            iv_activityComment.setImageResource(R.drawable.comment);
            tv_activityComment.setText(String.valueOf(dateNews_detail.readNum));
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + dateNews_detail.picture;
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            tv_actTitle.setText(dateNews_detail.title);
            tv_actContent.setText(dateNews_detail.content);
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
            final Topnews.Topnews_Info topnewsInfo = (Topnews.Topnews_Info) topnewsData.get(position);
            String imageUrl = topnewsInfo.pictureURL;
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                String url = "";

                @Override
                public void onClick(View v) {
                    String search_id = topnewsInfo.search_id;
                    if (CommonUtils.isLetter(search_id)) {
                        url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + "getTopicInfo" + AppNetConfig.PARAMETER + "search_id=" + search_id;
                        Intent intent = new Intent(mActivity, TopDetailsActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("search_id", search_id);
                        mActivity.startActivity(intent);
                    } else {
                        url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + "getArtCont" + AppNetConfig.SEPARATOR + search_id;
                        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                        intent.putExtra("url", url);
                        mActivity.startActivity(intent);
                    }
                }
            });
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

            //System.out.println("position"+parent.getChildPosition(view));
            //System.out.println("count"+parent.getChildCount());

            //if(parent.getChildPosition(view) != parent.getChildCount() - 1)
            outRect.bottom = space;

            //改成使用上面的间隔来设置
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

}






