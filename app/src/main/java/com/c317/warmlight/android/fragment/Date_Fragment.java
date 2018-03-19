package com.c317.warmlight.android.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.c317.warmlight.android.Activity.AddDateActivity;
import com.c317.warmlight.android.Activity.DateDetailActivity;
import com.c317.warmlight.android.Activity.MapActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseFragment;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.UIUtils;
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
 * Created by Administrator on 2017/11/16.
 */

public class Date_Fragment extends BaseFragment {

    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.pull_to_refresh)
    PullToRefreshListView pullToRefresh;
    @Bind(R.id.iv_add_date)
    ImageView ivAddDate;
    @Bind(R.id.iv_locate_date)
    ImageView ivLocateDate;
    private int PAGESIZE = 1;
    String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ACTIVITYLIST + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE;
    private DateNews mDateNews = null;

    @Override
    public View initView() {
        View view = UIUtils.getXmlView(R.layout.fragment_date);
        ButterKnife.bind(this, view);
        ivAddDate.setVisibility(View.VISIBLE);
        ivLocateDate.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("友约");
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                getDataFromServer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                getDataFromServer();
            }
        });
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持
        pullToRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, DateDetailActivity.class);
                DateNews.DateNews_Detail dateNews_detail = mDateNews.data.detail.get(position-1);
                intent.putExtra("activity_id", dateNews_detail.activity_id);
                intent.putExtra("picUrl", dateNews_detail.picture);
                intent.putExtra("title", dateNews_detail.title);
                intent.putExtra("content", dateNews_detail.content);
                intent.putExtra("readNum", dateNews_detail.readNum+"");
                intent.putExtra("agreeNum", dateNews_detail.agreeNum+"");
                intent.putExtra("commentNum", dateNews_detail.commentNum+"");
                intent.putExtra("endTime", dateNews_detail.endTime);
                intent.putExtra("startTime", dateNews_detail.startTime);
                intent.putExtra("memberNum", dateNews_detail.memberNum+"");
                intent.putExtra("type", dateNews_detail.type+"");
                intent.putExtra("place", dateNews_detail.place);
                mActivity.startActivity(intent);
            }
        });
        //友约地图
        ivLocateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,MapActivity.class);
                startActivity(intent);
            }
        });
        //新增友约
        ivAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,AddDateActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        //先判断是否有缓存，有则加载,否则请求服务器数据
        String cache = CacheUtils.getCache(url, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, true);
        } else {
            //请求服务端数据（开源框架XUtils）
            getDataFromServer();//快速加载
        }

    }


    /**
     * 获取友约模块数据
     *
     * @params
     * @author Du
     * @Date 2018/3/7 8:24
     **/
    private void getDataFromServer() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews dateNews = gson.fromJson(result, DateNews.class);
                //判断下一页是否还有数据
                if (PAGESIZE <= dateNews.data.total) {
                    processData(result, true);
                    CacheUtils.setCache(url, result, mActivity);
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

    private void processData(String result, boolean isMoreData) {
        if (isMoreData) {
            //下一页有数据
            Gson gson = new Gson();
            DateNews dateNews = gson.fromJson(result, DateNews.class);
            mDateNews = dateNews;
            PAGESIZE++;//页数增加
            pullToRefresh.setAdapter(new DateListAdapter());
        } else {
            Toast.makeText(mActivity, "没有数据了", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pullToRefresh.onRefreshComplete();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    private class DateListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDateNews.data.detail.size();
        }

        @Override
        public Object getItem(int position) {
            return mDateNews.data.detail.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            ViewHolder holder;
            //重用ListView
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
            Picasso.with(getActivity()).load(imageUrl).into(holder.ivPic);
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
