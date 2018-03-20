package com.c317.warmlight.android.tabpager;

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
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
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

public class DateTabDetails extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    @Bind(R.id.pull_mydate_refresh)
    PullToRefreshListView pullMydateRefresh;
    private String mUrl;//请求链接
    private int mType;
    boolean isAlldata = false;
    private ArrayList<DateNews.DateNews_Detail> mDetail = new ArrayList<>();
    private DateNews dateNews_info;


    public DateTabDetails(Activity activity, String url, int type) {
        super(activity);
        mType = type;
        mUrl = url + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGE;
    }


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_mydate_detail, null);
        ButterKnife.bind(this, view);
        //判断是否全部友约
        if(mType==0){
            isAlldata = true;
        }
        pullMydateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if(mType==0){
                    isAlldata = true;
                }
                getDataFromServer(mUrl, DateTabDetails.this.mType,isAlldata);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if(mType==0){
                    isAlldata = true;
                }
                PAGE++;//页数增加
                getDataFromServer(mUrl, DateTabDetails.this.mType,isAlldata);
            }
        });
        pullMydateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持
        return view;
    }


    //初始化数据
    public void initData() {
        if(mType!=0){
            String cache = CacheUtils.getCache(mUrl + "&type=" + mType + "&page=" + PAGE, mActivity);
            if (!TextUtils.isEmpty(cache)) {
                processData(cache, true);
            } else {
                getDataFromServer(mUrl, mType,false);//通过服务器获取数据
            }
            pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mActivity, DateDetailActivity.class);
                    DateNews.DateNews_Detail dateNews_detail = mDetail.get(position - 1);
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
        }else{
            //全部友约
            String cache = CacheUtils.getCache(mUrl + "&type=" + mType, mActivity);
            if(!TextUtils.isEmpty(cache)){
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                if (!TextUtils.isEmpty(dateNews_info.data.detail.toString())) {
                    processData(cache, true);
                }else {
                    getDataFromServer(mUrl,mType,true);//通过服务器获取数据
                }
            }else {
                getDataFromServer(mUrl,mType,true);//通过服务器获取数据
            }
            pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mActivity, DateDetailActivity.class);
                    DateNews.DateNews_Detail dateNews_detail = mDetail.get(position - 1);
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
        }

    }


    private void processData(String cache, boolean isMore) {
        if (isMore) {
            Gson gson = new Gson();
            dateNews_info = gson.fromJson(cache, DateNews.class);
            DateAdapter dateAdapter = new DateAdapter();
            dateAdapter.notifyDataSetChanged();
            pullMydateRefresh.setAdapter(dateAdapter);
        } else {
            Toast.makeText(mActivity, "没有数据了", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pullMydateRefresh.onRefreshComplete();
    }

    /**
     * 获取“除全部友约以外各类型数据”数据
     *
     * @params
     * @author Du
     * @Date 2018/3/12 19:49
     **/
    private void getDataFromServer(final String url, final int type,boolean isAllDate) {
        RequestParams params = new RequestParams(url);
        if(!isAllDate){
            params.addParameter("type", type);
            params.addParameter("page", PAGE);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (PAGESIZE <= dateNews.data.total) {
                        CacheUtils.setCache(url + "&type=" + type + "&page=" + PAGE, result, mActivity);
                        processData(result, true);
                    } else {
                        //无新数据
                        processData(result, false);
                    }
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
        }else{
            params.addParameter("page", PAGE);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (PAGESIZE <= dateNews.data.total) {
                        CacheUtils.setCache(url + "&page=" + PAGE, result, mActivity);
                        processData(result, true);
                    } else {
                        //无新数据
                        processData(result, false);
                    }
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


    private class DateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dateNews_info.data.detail.size();
        }

        @Override
        public Object getItem(int position) {
            return dateNews_info.data.detail.get(position);
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
            final DateNews.DateNews_Detail DateNews_Details = dateNews_info.data.detail.get(position);;
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
