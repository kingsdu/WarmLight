package com.c317.warmlight.android.tabpager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.Activity.DateDetailActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseMenuDetailPager;
import com.c317.warmlight.android.bean.Collect_Date_Details;
import com.c317.warmlight.android.bean.Collect_Date_Info;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/12.
 */
@SuppressLint("ValidFragment")
public class MyDateTabDetails extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    @Bind(R.id.pull_mydate_refresh)
    PullToRefreshListView pullMydateRefresh;
    private String mUrl;//请求链接
    private int mType;
    private List<DateNews.DateNews_Detail> mDatadetail = new ArrayList<>();
    private String account;
    private WarmLightDataBaseHelper dataBaseHelper;
    boolean isFirst = true;//listview刷新用
    private boolean isHaveNextPage = true;//是否还有下一页
    private int startPage = 1;//初始页
    private MydateAdapter mDateAdapter;


    public MyDateTabDetails(Activity activity, String url, int type) {
        super(activity);
        mType = type;
        mUrl = url;
    }

    public MyDateTabDetails(){

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_mydate_detail, null);
        ButterKnife.bind(this, view);
        pullMydateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持
        return view;
    }



    //初始化数据
    public void initData() {
        account = UserManage.getInstance().getUserInfo(mActivity).account;
        pullMydateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                mUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETMYACTIVITYLIST;
                getDataFromServerPullDown(account, mUrl, MyDateTabDetails.this.mType);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (isHaveNextPage) {
                    PAGE++;//数据页数增加
                    UPPAGESIZE++;//总页数
                }
                getDataFromServer(account, mUrl, MyDateTabDetails.this.mType);
            }
        });
        if (mType != 4) {//不是收藏页面
            getDataFromServer(account, mUrl, mType);//通过服务器获取数据
            pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mActivity, DateDetailActivity.class);
                    DateNews.DateNews_Detail dateNews_detail = mDatadetail.get(position - 1);
                    intent.putExtra("activity_id", dateNews_detail.activity_id);
                    intent.putExtra("picUrl", dateNews_detail.picture);
                    intent.putExtra("title", dateNews_detail.title);
                    intent.putExtra("content", dateNews_detail.content);
                    intent.putExtra("readNum", dateNews_detail.readNum + "");
                    intent.putExtra("agreeNum", dateNews_detail.agreeNum + "");
                    intent.putExtra("commentNum", dateNews_detail.commentNum + "");
                    intent.putExtra("endTime", dateNews_detail.endTime);
                    intent.putExtra("startTime", dateNews_detail.startTime);
                    intent.putExtra("memberNum", dateNews_detail.memberNum + "");
                    intent.putExtra("type", dateNews_detail.type + "");
                    intent.putExtra("place", dateNews_detail.place);
                    mActivity.startActivity(intent);
                }
            });
        } else {
            //如果是收藏友约
            getCollectDataFromServer();
        }
    }




    private void getCollectDataFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTSAVE;
        RequestParams params = new RequestParams(url);
        params.addParameter(AppConstants.ACCOUNT,UserManage.getInstance().getUserInfo(mActivity).account);
        params.addParameter(AppConstants.TYPE,"a");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                final Collect_Date_Info collect_date_info = gson.fromJson(result, Collect_Date_Info.class);
                if(collect_date_info.code == 200){
                    pullMydateRefresh.setAdapter(new MyCollectAdapter(collect_date_info));
                    pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(mActivity, DateDetailActivity.class);
                            Collect_Date_Info.Collect_Date_Detail collect_date_details = collect_date_info.data.get(position - 1);
                            intent.putExtra("activity_id", collect_date_details.activity_id);
                            intent.putExtra("save_id", collect_date_details.save_id);
                            mActivity.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(mActivity,"获取友约收藏数据失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void processData(String cache, boolean isMore) {
        //在加入到dateNews_details必须先判断是否已有
        if (isFirst) {
            if (isMore) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(cache, DateNews.class);
                mDatadetail.addAll(dateNews.data.detail);
                mDateAdapter = new MydateAdapter();
                pullMydateRefresh.setAdapter(mDateAdapter);
            } else {
                //第一次，无数据
            }
            isFirst = false;
        }else{
            if (isMore) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(cache, DateNews.class);
                if (!haveRepeat(dateNews)) {
                    mDatadetail.addAll(dateNews.data.detail);
                    mDateAdapter.notifyDataSetChanged();
                }
            } else {
                //非第一次，无数据
            }
        }
        pullMydateRefresh.onRefreshComplete();
    }


    /**
     * 下拉刷新数据，更新数据
     *
     * @params
     * @author Du
     * @Date 2018/3/27 14:46
     **/
    private void processDataPullDown(String result) {
        //重新开始加载
        mDatadetail.clear();
        PAGE = 1;
        UPPAGESIZE = 0;
        //加载新数据
        Gson gson = new Gson();
        DateNews dateNews = gson.fromJson(result, DateNews.class);
        mDatadetail.addAll(dateNews.data.detail);
        pullMydateRefresh.setAdapter(mDateAdapter);
    }

    /**
     * 是否有重复项
     *
     * @params true有
     * @author Du
     * @Date 2018/3/27 14:21
     **/
    private boolean haveRepeat(DateNews dateNews_info) {
        ArrayList<DateNews.DateNews_Detail> detail = dateNews_info.data.detail;
        for (int i = 0; i < detail.size(); i++) {
            if (mDatadetail.get(i).activity_id.equals(detail.get(i).activity_id)) {
                return true;
            }
        }
        return false;
    }


    private void getDataFromServerPullDown(String account, String mUrl, int mType) {
        RequestParams params = new RequestParams(mUrl);
        params.addParameter("account", account);
        params.addParameter("type", mType);
        params.addParameter("page", startPage);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(result, DateNews.class);
                //判断下一页是否还有数据
                if (DOWNPAGESIZE <= dateNews.data.total) {
                    processDataPullDown(result);
                }
                pullMydateRefresh.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, "onError", Toast.LENGTH_SHORT).show();
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
     * 获取“我的友约”数据
     *
     * @params
     * @author Du
     * @Date 2018/3/12 19:49
     **/
    private void getDataFromServer(String account, final String url, final int type) {
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        params.addParameter("type", type);
        params.addParameter("page", PAGE);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(result, DateNews.class);
                //判断下一页是否还有数据
                if (UPPAGESIZE <= dateNews.data.total) {
                    processData(result, true);
                    isHaveNextPage = true;
                } else {
                    isHaveNextPage = false;
                }
                pullMydateRefresh.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, "onError", Toast.LENGTH_SHORT).show();
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


    private class MydateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatadetail.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatadetail.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_dates, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_date_itemPic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_date_itemTitle);
                holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_date_itemStartTime);
                holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_date_itemPlace);
                holder.tvMember = (TextView) convertView.findViewById(R.id.tv_date_itemMembernum);
                holder.ivTime = (ImageView) convertView.findViewById(R.id.iv_date_clock);
                holder.ivLocate = (ImageView) convertView.findViewById(R.id.iv_date_locate);
                holder.ivJoinPeople = (ImageView) convertView.findViewById(R.id.iv_date_joinpeople);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final DateNews.DateNews_Detail DateNews_Details = (DateNews.DateNews_Detail) getItem(position);
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + DateNews_Details.picture;
            Picasso.with(mActivity).load(imageUrl).into(holder.ivPic);
            holder.tvTitle.setText(DateNews_Details.title);
            holder.tvStartTime.setText(DateNews_Details.startTime);
            holder.tvPlace.setText(DateNews_Details.place);
            holder.tvMember.setText(String.valueOf(DateNews_Details.memberNum));
            holder.tvStartTime.setText(DateNews_Details.startTime);
            holder.ivTime.setImageResource(R.drawable.time);
            holder.ivLocate.setImageResource(R.drawable.locate);
            holder.ivJoinPeople.setImageResource(R.drawable.join_people);
            return convertView;
        }
    }


    private class MyCollectAdapter extends BaseAdapter {

        private Collect_Date_Info collect_date_info;

        public MyCollectAdapter(Collect_Date_Info collect_date_info){
            this.collect_date_info = collect_date_info;
        }
        @Override
        public int getCount() {
            return collect_date_info.data.size();
        }

        @Override
        public Collect_Date_Info.Collect_Date_Detail getItem(int position) {
            return collect_date_info.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_dates, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_date_itemPic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_date_itemTitle);
                holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_date_itemStartTime);
                holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_date_itemPlace);
                holder.tvMember = (TextView) convertView.findViewById(R.id.tv_date_itemMembernum);
                holder.ivTime = (ImageView) convertView.findViewById(R.id.iv_date_clock);
                holder.ivLocate = (ImageView) convertView.findViewById(R.id.iv_date_locate);
                holder.ivJoinPeople = (ImageView) convertView.findViewById(R.id.iv_date_joinpeople);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Collect_Date_Info.Collect_Date_Detail item = getItem(position);
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITYS + AppNetConfig.SEPARATOR + item.activity_id + ".jpg";
            Picasso.with(mActivity).load(imageUrl).into(holder.ivPic);
            holder.tvTitle.setText(item.title);
            holder.tvStartTime.setText(item.startTime);
            holder.tvPlace.setText(item.place);
            holder.tvMember.setText(0+"");
            holder.tvStartTime.setText(item.startTime);
            holder.ivTime.setImageResource(R.drawable.time);
            holder.ivLocate.setImageResource(R.drawable.locate);
            holder.ivJoinPeople.setImageResource(R.drawable.join_people);
            return convertView;
        }
    }


    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
        public TextView tvStartTime;
        public TextView tvPlace;
        public TextView tvMember;
        public ImageView ivTime;
        public ImageView ivLocate;
        public ImageView ivJoinPeople;
    }
}
