package com.c317.warmlight.android.tabpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
    boolean isFirst = true;//篇listview刷新用
    private List<DateNews.DateNews_Detail> dateNews_details = new ArrayList<>();//ListView存储数据
    private DateNews dateNews_info;//服务端解析数据
    private DateAdapter dateAdapter;


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
        if (mType == -1) {
            isAlldata = true;
        }
        pullMydateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mType == -1) {
                    isAlldata = true;
                }
                mUrl = "http://14g97976j3.51mypc.cn:10759/youyue/getActivityList";
                getDataFromServerPullDown(mUrl, mType, isAlldata);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mType == -1) {
                    isAlldata = true;
                }
                PAGE++;//页数增加
                getDataFromServer(mUrl, mType, isAlldata);
            }
        });
        pullMydateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持
        return view;
    }


    //初始化数据
    public void initData() {
        if (mType != -1) {
            String cache = CacheUtils.getCache(mUrl + "&type=" + mType + "&page=" + PAGE, mActivity);
            if (!TextUtils.isEmpty(cache)) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                if (!(dateNews_info.data.detail.size() == 0)) {
                    dateNews_details.addAll(dateNews_info.data.detail);
                    processData(cache, true);
                } else {
                    getDataFromServer(mUrl, mType, false);//通过服务器获取数据
                }
            } else {
                getDataFromServer(mUrl, mType, false);//通过服务器获取数据
            }
        } else {
            String cache = CacheUtils.getCache(mUrl + "&page=" + PAGE, mActivity);
            if (!TextUtils.isEmpty(cache)) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                if (!(dateNews_info.data.detail.size() == 0)) {
                    processData(cache, true);
                } else {
                    getDataFromServer(mUrl, mType, true);//通过服务器获取数据
                }
            } else {
                getDataFromServer(mUrl, mType, true);//通过服务器获取数据
            }
        }
        pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, DateDetailActivity.class);
                DateNews.DateNews_Detail dateNews_detail = dateNews_details.get(position);
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


    private void processData(String cache, boolean isMore) {
        if (isFirst) {
            if (isMore) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                dateNews_details.addAll(dateNews_info.data.detail);
                dateAdapter = new DateAdapter();
                pullMydateRefresh.setAdapter(dateAdapter);
            } else {
                //Toast.makeText(mActivity, "到底了", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isFirst = false;
        } else {
            if (isMore) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                dateNews_details.addAll(dateNews_info.data.detail);
                dateAdapter.notifyDataSetChanged();
            } else {
                //Toast.makeText(mActivity, "到底了", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        pullMydateRefresh.onRefreshComplete();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getUserVisibleHint() && pullMydateRefresh.getVisibility() != View.VISIBLE) {
            initData();
        }
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 获取“除全部友约以外各类型数据”数据
     *
     * @params
     * @author Du
     * @Date 2018/3/12 19:49
     **/
    private void getDataFromServer(final String url, final int type, boolean isAllDate) {
        RequestParams params = new RequestParams(url);
        if (!isAllDate) {
            params.addParameter("type", type);
            params.addParameter("page", PAGE);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (UPPAGESIZE < dateNews.data.total) {
                        CacheUtils.setCache(url + "&type=" + type + "&page=" + PAGE, result, mActivity);
                        processData(result, true);
                        UPPAGESIZE++;
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
        } else {
            params.addParameter("page", PAGE);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (UPPAGESIZE < dateNews.data.total) {
                        CacheUtils.setCache(url + "&page=" + PAGE, result, mActivity);
                        processData(result, true);
                        UPPAGESIZE++;
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


    /**
     * 下拉刷新，显示最新数据
     *
     * @params
     * @author Du
     * @Date 2018/3/21 10:06
     **/
    private void getDataFromServerPullDown(final String mUrl, final int mType, boolean isAlldata) {
        RequestParams params = new RequestParams(mUrl);
        if (!isAlldata) {
            params.addParameter("type", mType);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (DOWNPAGESIZE < dateNews.data.total) {
                        if (dateNews.data.detail.get(0).activity_id.equals(dateNews_details.get(0).activity_id)) {
                            Toast.makeText(mActivity, "没有新数据", Toast.LENGTH_SHORT).show();
                        } else {
                            CacheUtils.setCache(mUrl + "&type=" + mType, result, mActivity);
                            processData(result, true);
                            Toast.makeText(mActivity, "刷新完成", Toast.LENGTH_SHORT).show();
                            DOWNPAGESIZE++;
                        }
                    } else {
                        Toast.makeText(mActivity, "无数据", Toast.LENGTH_SHORT).show();
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
        } else {
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (DOWNPAGESIZE < dateNews.data.total) {
                        if (dateNews.data.detail.get(0).activity_id.equals(dateNews_details.get(0).activity_id)) {
                            Toast.makeText(mActivity, "没有新数据", Toast.LENGTH_SHORT).show();
                        } else {
                            CacheUtils.setCache(mUrl + "&type=" + mType, result, mActivity);
                            processData(result, true);
                            Toast.makeText(mActivity, "刷新完成", Toast.LENGTH_SHORT).show();
                            DOWNPAGESIZE++;
                        }
                    } else {
                        Toast.makeText(mActivity, "无数据", Toast.LENGTH_SHORT).show();
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
            return dateNews_details.size();
        }

        @Override
        public Object getItem(int position) {
            return dateNews_details.get(position);
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
            DateNews.DateNews_Detail dateNews_detail = dateNews_details.get(position);
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + dateNews_detail.picture;
            Picasso.with(mActivity).load(imageUrl).into(holder.ivPic);
            holder.tvTitle.setText(dateNews_detail.title);
            holder.tvStartTime.setText(dateNews_detail.startTime);
            holder.tvPlace.setText(dateNews_detail.place);
            holder.tvMember.setText(String.valueOf(dateNews_detail.memberNum));
            holder.tvStartTime.setText(dateNews_detail.startTime);
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
