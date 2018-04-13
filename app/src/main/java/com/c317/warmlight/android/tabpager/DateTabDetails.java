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
import com.c317.warmlight.android.bean.DateNews_detalis;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
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

public class DateTabDetails extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    @Bind(R.id.pull_mydate_refresh)
    PullToRefreshListView pullMydateRefresh;
    private String mUrl;//请求链接
    private int mType;
    boolean isAlldata = false;
    boolean isFirst = true;//listview刷新用
    private List<DateNews.DateNews_Detail> dateNews_details = new ArrayList<>();//ListView存储数据
    private DateNews dateNews_info;//服务端解析数据
    private DateAdapter dateAdapter;
    private boolean isHaveNextPage = true;//是否还有下一页
    private int startPage = 1;//初始页
    private String telephone;


    public DateTabDetails(Activity activity, String url, int type) {
        super(activity);
        mType = type;
        mUrl = url;
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
        //判断是否全部友约
        if (mType == -1) {
            isAlldata = true;
        }
        if (mType != -1) {
            getDataFromServer(mUrl, mType, false);//通过服务器获取数据
        } else {
            getDataFromServer(mUrl, mType, true);//通过服务器获取数据
        }
        //设置pullMydateRefresh监听
        pullMydateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mType == -1) {
                    isAlldata = true;
                }
                mUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITYLIST;
                getDataFromServerPullDown(mUrl, mType, isAlldata);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mType == -1) {
                    isAlldata = true;
                }
                if (isHaveNextPage) {
                    PAGE++;//数据页数增加
                    UPPAGESIZE++;//总页数
                }
                getDataFromServer(mUrl, mType, isAlldata);
            }
        });
        pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateNews.DateNews_Detail dateNews_detail = dateNews_details.get(position - 1);
                getTelphone(dateNews_detail);
            }
        });
    }




    /**
    * 获取咨询电话
    * @params
    * @author Du
    * @Date 2018/4/9 22:58
    **/
    private void getTelphone(final DateNews.DateNews_Detail dateNews_detail) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETACTIVITYDETAIL;
        RequestParams params = new RequestParams(url);
        params.addParameter("activity_id",dateNews_detail.activity_id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews_detalis dateNews_detalis = gson.fromJson(result, DateNews_detalis.class);
                if(dateNews_detalis.code == 200){
                    telephone = dateNews_detalis.data.telephone;
                    Intent intent = new Intent(mActivity, DateDetailActivity.class);
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
                    intent.putExtra("telephone", telephone);
                    mActivity.startActivity(intent);
                }else{
                    CommonUtils.showToastShort(getActivity(),"code is not 200");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(getActivity(),"咨询电话请求失败");
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
                dateNews_info = gson.fromJson(cache, DateNews.class);
                dateNews_details.addAll(dateNews_info.data.detail);
                dateAdapter = new DateAdapter();
                pullMydateRefresh.setAdapter(dateAdapter);
            } else {
                //第一次，无数据
            }
            isFirst = false;
        } else {
            if (isMore) {
                Gson gson = new Gson();
                dateNews_info = gson.fromJson(cache, DateNews.class);
                if (!haveRepeat(dateNews_info)) {
                    dateNews_details.addAll(dateNews_info.data.detail);
                    dateAdapter.notifyDataSetChanged();
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
        dateNews_details.clear();
        PAGE = 1;
        UPPAGESIZE = 0;
        //加载新数据
        Gson gson = new Gson();
        dateNews_info = gson.fromJson(result, DateNews.class);
        dateNews_details.addAll(dateNews_info.data.detail);
        dateAdapter = new DateAdapter();
        pullMydateRefresh.setAdapter(dateAdapter);
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
            if (dateNews_details.get(i).activity_id.equals(detail.get(i).activity_id)) {
                return true;
            }
        }
        return false;
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
                        processData(result, true);
                        isHaveNextPage = true;
                    } else {
                        //无新数据
                        isHaveNextPage = false;
                    }
                    pullMydateRefresh.onRefreshComplete();
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
                        processData(result, true);
                        isHaveNextPage = true;
                    } else {
                        //无新数据
                        isHaveNextPage = false;
                    }
                    pullMydateRefresh.onRefreshComplete();
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
            params.addParameter("page", startPage);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (DOWNPAGESIZE < dateNews.data.total) {
                        processDataPullDown(result);
                    } else {
                        //无数据
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
            params.addParameter("page", startPage);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DateNews dateNews = gson.fromJson(result, DateNews.class);
                    //判断下一页是否还有数据
                    if (DOWNPAGESIZE < dateNews.data.total) {
                        processDataPullDown(result);
                    } else {
                        //无数据
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
            if(TextUtils.isEmpty(dateNews_detail.picture)){
                Picasso.with(mActivity).load(R.drawable.nopic1).into(holder.ivPic);
            }else{
                String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + dateNews_detail.picture;
                Picasso.with(mActivity).load(imageUrl).into(holder.ivPic);
            }
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
