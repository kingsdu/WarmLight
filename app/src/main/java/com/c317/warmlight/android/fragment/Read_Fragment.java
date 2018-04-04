package com.c317.warmlight.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.c317.warmlight.android.Activity.DateDetailActivity;
import com.c317.warmlight.android.Activity.NewsDetailActivity;
import com.c317.warmlight.android.Activity.TopDetailsActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.AllDataBean;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.bean.OrangeGuess;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.bean.Topnews;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.flingSwipe.SwipeFlingAdapterView;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.c317.warmlight.android.views.NestListView;
import com.c317.warmlight.android.views.SmallNewsController;
import com.c317.warmlight.android.views.SmallNewsListView;
import com.c317.warmlight.android.views.SubAdapterController;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Read_Fragment extends BaseFragment implements PullToRefreshListener {

    @Bind(R.id.rv_mainListView)
    PullToRefreshRecyclerView rvMainListView;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    Handler mHandler;

    private List<List<Object>> mAllDatas = new ArrayList();//初始化数据
    private List<Object> mDataType = new ArrayList();//布局类型

    private ViewPager vpBarner;
    private CirclePageIndicator btnBarner;
    //    //横向滑动布局
    private SubAdapterController mSubAdapterCrl;
    private SmallNewsController smallNewsController;
    private SwipeFlingAdapterView readCardDates;

    private MainAdapter mainAdapter;
    public int cardWidth;
    public int cardHeight;

    //小桔猜猜部分
    public TextView dailyAskNoChange;
    public TextView dailyAskTitle;

    private static int PAGESIZE = 1;
    private boolean first = true;//下拉刷新，判断是否为第一次
    private CardAdapter mAdapter;
    private List<Object> mCardDatas = new ArrayList<>();//保存CardData,随机生成数据
    private List<String> urls;


    @Override
    public View initView() {
        View view = UIUtils.getXmlView(R.layout.fragment_read);
        ButterKnife.bind(this, view);
        tvTopbarTitle.setText("有读");
        rvMainListView.setLoadingMoreEnabled(true);
        rvMainListView.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        rvMainListView.displayLastRefreshTime(true);
        //设置刷新回调
        rvMainListView.setPullToRefreshListener(this);
        return view;
    }

    @Override
    public void initData() {
        getUrlDatas();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getReadData(urls);
            }
        });
    }



    private void getUrlDatas() {
        urls = new ArrayList<>();
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.TOPNEWS + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITY + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ARTICLE + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.GUESS + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE);
        urls.add(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ARTICLE + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE + 1);
        PAGESIZE++;
    }


    /**
     * RecycleView布局数据
     *
     * @params
     * @author Du
     * @Date 2018/4/2 9:04
     **/
    private void initRecycleViewData() {
        if (first) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvMainListView.setLayoutManager(mLayoutManager);
            mainAdapter = new MainAdapter(mActivity, mAllDatas, mDataType);
            rvMainListView.setAdapter(mainAdapter);
            first = false;
        } else {
            mainAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh() {
        //上拉刷新
        rvMainListView.setRefreshComplete();
    }

    @Override
    public void onLoadMore() {
        //下拉加载更多
        rvMainListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                rvMainListView.setLoadMoreComplete();
            }
        }, 3000);
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


    private class MainAdapter extends RecyclerView.Adapter implements SwipeFlingAdapterView.onFlingListener,
            SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener {
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
                View view = mInflater.inflate(R.layout.read_swipe_card, parent, false);
                return new CardNewsHolder(view);
            } else if (viewType == AppConstants.RV_ARTICLE_NEWS_SMALL) {
                View view = mInflater.inflate(R.layout.small_news, parent, false);
                return new SmallNewsHolder(view);
            } else if (viewType == AppConstants.RV_GUESS_NEWS) {
                View view = mInflater.inflate(R.layout.horizontal_news, parent, false);
                return new HorizontalHolder(view);
            } else if (viewType == AppConstants.RV_DAILYASK) {
                View view = mInflater.inflate(R.layout.read_daily_ask, parent, false);
                return new DailyAskHolder(view);
            } else if (viewType == AppConstants.RV_DIVIDER) {
                View view = mInflater.inflate(R.layout.read_recy_header, parent, false);
                return new DivideHolder(view);
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
                DisplayMetrics dm = getResources().getDisplayMetrics();
                float density = dm.density;
                cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
                cardHeight = (int) (dm.heightPixels - (510 * density));//数字部分越大，高度越小
                if (readCardDates != null) {
                    readCardDates.setIsNeedSwipe(true);
                    readCardDates.setFlingListener(this);
                    readCardDates.setOnItemClickListener(this);
                    mCardDatas = mAllDatas.get(position);
                    mAdapter = new CardAdapter(mAllDatas.get(position), mContext);
                    readCardDates.setAdapter(mAdapter);
                }
            } else if (getItemViewType(position) == AppConstants.RV_GUESS_NEWS) {
                mSubAdapterCrl = new SubAdapterController(mAllDatas.get(position), mContext);
                ((HorizontalHolder) holder).nestListView.setAdapter(mSubAdapterCrl.getAdapter());
            } else if (getItemViewType(position) == AppConstants.RV_DAILYASK) {
                dailyAskNoChange.setText("[每日一问]第一期");
                dailyAskTitle.setText("毕业生找工作应该注意什么？");
            } else if (getItemViewType(position) == AppConstants.RV_DIVIDER) {
                List<Object> objects = mAllDatas.get(position);
                String divideName = (String) objects.get(0);
                CharSequence no = "暂无";
                ((DivideHolder) holder).readDivideName.setHint(no);
                ((DivideHolder) holder).readDivideName.setText(divideName);
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

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onItemClicked(MotionEvent event, View v, Object dataObject) {
            DateNews.DateNews_Detail dateNews_detail = (DateNews.DateNews_Detail) dataObject;
            String activity_id = dateNews_detail.activity_id;
            String picture = dateNews_detail.picture;
            String title = dateNews_detail.title;
            String content = dateNews_detail.content;
            int readNum = dateNews_detail.readNum;
            int agreeNum = dateNews_detail.agreeNum;
            int commentNum = dateNews_detail.commentNum;
            String endTime = dateNews_detail.endTime;
            String startTime = dateNews_detail.startTime;
            int memberNum = dateNews_detail.memberNum;
            int type = dateNews_detail.type;
            String place = dateNews_detail.place;
            Intent intent = new Intent(mActivity, DateDetailActivity.class);
            intent.putExtra("activity_id", activity_id);
            intent.putExtra("picture", picture);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("readNum", readNum);
            intent.putExtra("agreeNum", agreeNum);
            intent.putExtra("commentNum", commentNum);
            intent.putExtra("endTime", endTime);
            intent.putExtra("startTime", startTime);
            intent.putExtra("memberNum", memberNum);
            intent.putExtra("type", type);
            intent.putExtra("place", place);
            mActivity.startActivity(intent);
        }

        @Override
        public void removeFirstObjectInAdapter() {
            mAdapter.remove(0);
        }

        @Override
        public void onLeftCardExit(Object dataObject) {

        }

        @Override
        public void onRightCardExit(Object dataObject) {

        }

        @Override
        public void onAdapterAboutToEmpty(int itemsInAdapter) {
            if (itemsInAdapter == 3) {
                loadData();
            }
        }

        @Override
        public void onScroll(float progress, float scrollXProgress) {

        }

        //随机排序友约
        private void loadData() {
            new AsyncTask<Void, Void, Set<Object>>() {
                @Override
                protected Set<Object> doInBackground(Void... params) {
                    Set<Object> sets = new HashSet<>();
                    for (int i = 0; i < mCardDatas.size(); i++) {
                        sets.add(mCardDatas.get(i));
                    }
                    return sets;
                }

                @Override
                protected void onPostExecute(Set<Object> set) {
                    super.onPostExecute(set);
                    mAdapter.addAll(set);
                }
            }.execute();
        }


        class TopNewsHolder extends RecyclerView.ViewHolder {
            public TopNewsHolder(View itemView) {
                super(itemView);
                vpBarner = (ViewPager) itemView.findViewById(R.id.vp_barner);
                btnBarner = (CirclePageIndicator) itemView.findViewById(R.id.btn_barner);
            }
        }

        class CardNewsHolder extends RecyclerView.ViewHolder {
            public CardNewsHolder(View itemView) {
                super(itemView);
                readCardDates = (SwipeFlingAdapterView) itemView.findViewById(R.id.swipe_view);
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

        class DailyAskHolder extends RecyclerView.ViewHolder {

            public DailyAskHolder(View itemView) {
                super(itemView);
                dailyAskNoChange = (TextView) itemView.findViewById(R.id.read_dailyask_nochange);
                dailyAskTitle = (TextView) itemView.findViewById(R.id.read_dailyask_title);
            }
        }


        class DivideHolder extends RecyclerView.ViewHolder {
            public LinearLayout readHeaderView;//分割线布局
            public TextView readDivideName;//布局名称

            public DivideHolder(View itemView) {
                super(itemView);
                readHeaderView = (LinearLayout) itemView.findViewById(R.id.rl_readnews_header);
                readDivideName = (TextView) itemView.findViewById(R.id.tv_read_dividename);
            }
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
            ImageView imageView = new ImageView(mActivity);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(mActivity).load(imageUrl).into(imageView);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                String url = "";

                @Override
                public void onClick(View v) {
                    String search_id = topnewsInfo.search_id;
                    if (CommonUtils.isLetter(search_id)) {
                        url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.GETTOPICINFO + AppNetConfig.PARAMETER + AppNetConfig.SEARCH_ID + AppNetConfig.EQUAL + search_id;
                        Intent intent = new Intent(mActivity, TopDetailsActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("search_id", search_id);
                        mActivity.startActivity(intent);
                    } else {
                        url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.GETARTCONT + AppNetConfig.SEPARATOR + search_id;
                        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("title", topnewsInfo.title);
                        intent.putExtra("introduce", topnewsInfo.introduce);
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

    static class CardViewHolder {
        ImageView portraitView;
        TextView title;
        TextView content;
        TextView startNum;
        TextView eduView;
        TextView workView;
    }


    private class CardAdapter extends BaseAdapter {

        private List<Object> mData;
        private Context mContext;

        public CardAdapter(List<Object> data, Context context) {
            mData = data;
            mContext = context;
        }


        public void addAll(Collection<Object> collection) {
            if (isEmpty()) {
                mData.addAll(collection);
                notifyDataSetChanged();
            } else {
                mData.addAll(collection);
            }
        }

        public boolean isEmpty() {
            return mData.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < mData.size()) {
                mData.remove(index);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public DateNews.DateNews_Detail getItem(int position) {
            DateNews.DateNews_Detail dateNews_detail = (DateNews.DateNews_Detail) mData.get(position);
            if (dateNews_detail == null || mData.size() == 0) return null;
            return (DateNews.DateNews_Detail) mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CardViewHolder holder;
            DateNews.DateNews_Detail dateNews_detail = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_item, parent, false);
                holder = new CardViewHolder();
                convertView.setTag(holder);
                convertView.getLayoutParams().width = cardWidth;
                holder.portraitView = (ImageView) convertView.findViewById(R.id.iv_read_portrait);
                holder.portraitView.getLayoutParams().height = cardHeight;
                holder.title = (TextView) convertView.findViewById(R.id.tv_read_acttitle);
                holder.content = (TextView) convertView.findViewById(R.id.tv_read_content);
                holder.startNum = (TextView) convertView.findViewById(R.id.tv_read_startnum);
                holder.eduView = (TextView) convertView.findViewById(R.id.tv_read_complicationnum);
                holder.workView = (TextView) convertView.findViewById(R.id.tv_read_commentnum);
            } else {
                holder = (CardViewHolder) convertView.getTag();
            }
            if (TextUtils.isEmpty(dateNews_detail.picture)) {
                Picasso.with(mContext).load(R.drawable.musi01).into(holder.portraitView);
            } else {
                Picasso.with(mContext).load(dateNews_detail.picture).into(holder.portraitView);
            }
            holder.portraitView.setScaleType(ImageView.ScaleType.FIT_XY);

            final CharSequence no = "暂无";

            holder.title.setHint(no);
            holder.title.setText(dateNews_detail.title);

            holder.content.setHint(no);
            holder.content.setText(dateNews_detail.content);

            holder.startNum.setHint(no);
            holder.startNum.setText(dateNews_detail.agreeNum + "");
            holder.startNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);

            holder.eduView.setHint(no);
            holder.eduView.setText(dateNews_detail.readNum + "");
            holder.eduView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.compliment, 0, 0, 0);

            holder.workView.setHint(no);
            holder.workView.setText(dateNews_detail.commentNum + "");
            holder.workView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment, 0, 0, 0);
            return convertView;
        }
    }


    private void getReadData(final List<String> url) {
        //1 头条11
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
                        // 3 友读部分头布局
                        mDataType.add(AppConstants.RV_DIVIDER);
                        List<Object> mAllData = new ArrayList();
                        mAllData.add("友读");
                        mAllDatas.add(mAllData);
                        //4 友读新闻
                        RequestParams params = new RequestParams(url.get(2));
                        x.http().get(params, new CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Gson gson = new Gson();
                                Smallnews smallnew = gson.fromJson(result, Smallnews.class);
                                if (smallnew.data.detail.size() > 3) {
                                    for (int i = 0; i < 3; i++) {
                                        List<Object> mAllData = new ArrayList();
                                        mAllData.add(smallnew.data.detail.get(i));
                                        mAllDatas.add(mAllData);
                                        mDataType.add(AppConstants.RV_ARTICLE_NEWS_SMALL);
                                    }
                                } else {
                                    for (int i = 0; i < smallnew.data.detail.size(); i++) {
                                        List<Object> mAllData = new ArrayList();
                                        mAllData.add(smallnew.data.detail.get(i));
                                        mAllDatas.add(mAllData);
                                        mDataType.add(AppConstants.RV_ARTICLE_NEWS_SMALL);
                                    }
                                }
                                // 5 书籍部分头布局
                                mDataType.add(AppConstants.RV_DIVIDER);
                                List<Object> mAllData = new ArrayList();
                                mAllData.add("好书推荐");
                                mAllDatas.add(mAllData);
                                // 6  横向书籍
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
                                        // 7 每日一问部分头布局
                                        mDataType.add(AppConstants.RV_DIVIDER);
                                        List<Object> mAllData = new ArrayList();
                                        mAllData.add("每日一问");
                                        mAllDatas.add(mAllData);
                                        // 8 每日一问
                                        mDataType.add(AppConstants.RV_DAILYASK);
                                        mAllDatas.add(mAllData);

                                        // 9 书籍部分头布局
                                        mDataType.add(AppConstants.RV_DIVIDER);
                                        mAllData = new ArrayList();
                                        mAllData.add("友读");
                                        mAllDatas.add(mAllData);
                                        //10  友读
                                        RequestParams params = new RequestParams(url.get(4));
                                        x.http().get(params, new CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Gson gson = new Gson();
                                                Smallnews smallnew = gson.fromJson(result, Smallnews.class);
                                                if (smallnew.data.detail.size() > 3) {
                                                    for (int i = 0; i < 3; i++) {
                                                        List<Object> mAllData = new ArrayList();
                                                        mAllData.add(smallnew.data.detail.get(i));
                                                        mAllDatas.add(mAllData);
                                                        mDataType.add(AppConstants.RV_ARTICLE_NEWS_SMALL);
                                                    }
                                                } else {
                                                    for (int i = 0; i < smallnew.data.detail.size(); i++) {
                                                        List<Object> mAllData = new ArrayList();
                                                        mAllData.add(smallnew.data.detail.get(i));
                                                        mAllDatas.add(mAllData);
                                                        mDataType.add(AppConstants.RV_ARTICLE_NEWS_SMALL);
                                                    }
                                                }
                                                initRecycleViewData();
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
}


