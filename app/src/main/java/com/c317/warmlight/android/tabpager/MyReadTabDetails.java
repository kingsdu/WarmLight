package com.c317.warmlight.android.tabpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import com.c317.warmlight.android.Activity.NewsDetailActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseMenuDetailPager;
import com.c317.warmlight.android.bean.Collect_Article_Info;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.bean.Topnews;
import com.c317.warmlight.android.bean.User_Comment_Info;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/18.
 */

public class MyReadTabDetails extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    @Bind(R.id.pull_mydate_refresh)
    PullToRefreshListView pullMydateRefresh;
    private int mType;
    private String mUrl;
    private String account;
    private List<Collect_Article_Info.Collect_Article_Details> collect_article_detailses;
    private String lastTime;
    public static final int QUERY_LASTTIME = 0;
    private WarmLightDataBaseHelper dataBaseHelper;

    public MyReadTabDetails(Activity activity, int type) {
        super(activity);
        mType = type;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_mydate_detail, null);
        ButterKnife.bind(this, view);
        account = UserManage.getInstance().getUserInfo(mActivity).account;
        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(mActivity);

        pullMydateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
            }
        });
        pullMydateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持

        pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.GETARTCONT + AppNetConfig.SEPARATOR;
                Collect_Article_Info.Collect_Article_Details collect_article_details = collect_article_detailses.get(position - 1);
                String article_id = String.valueOf(collect_article_details.article_id);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", url + article_id);
                intent.putExtra("article_id", article_id);
                intent.putExtra("title", collect_article_details.title);
                intent.putExtra("save_id", collect_article_details.save_id);
                intent.putExtra("lastTime", collect_article_details.lastTime);
                intent.putExtra("pictureURL", collect_article_details.pictureURL);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void initData() {
        super.initData();
        isNeedQuery();
    }


    private void getData() {
        if (mType == 0) {
            if (!TextUtils.isEmpty(lastTime)) {
                mUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTSAVE;
                if (compareLastTime()) {
                    //有新数据
                    getUserCollect(mUrl);
                }else {
                    //无新数据
                    String cache = CacheUtils.getCache(mUrl, mActivity);
                    if (!TextUtils.isEmpty(cache)) {
                        processCollectData(cache, false);
                    } else {
                        //第一次进入
                        getUserCollect(mUrl);
                    }
                }
            }
        }
        else if (mType == 1) {
            //获取用户评论对象
            mUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETMYCOMMENTFOR;
            getUserComment(mUrl);
        }
    }


    /**
     * 比较时间
     *
     * @params
     * @author Du
     * @Date 2018/4/13 9:39
     **/
    private boolean compareLastTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Collect_Article_Info.Collect_Article_Details> collect_article_detailses = dataBaseHelper.queryMultiIsCollectRead();
        String dateLastTime = collect_article_detailses.get(0).lastTime;
        try {
            Date parse = sdf.parse(dateLastTime);//sqllite数据库时间
            Date dateparse = sdf.parse(lastTime);//后台数据库当前时间
            if (parse.getTime() < dateparse.getTime()) {
                //有新的数据，需要更新
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_LASTTIME:
                    getData();
                    break;
            }
        }
    };


    /**
     * 查询是否需要重新获取评论数据，后台最新时间和Sqllite存储时间进行对比
     *
     * @params
     * @author Du
     * @Date 2018/4/13 9:27
     **/
    private void isNeedQuery() {
        //请求网络，比较时间，确定
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = QUERY_LASTTIME;
                getUserCollect(mUrl);
                handler.sendMessage(msg);
            }
        }).start();
    }


    private void processCollectData(String cache, boolean isMore) {
        Gson gson = new Gson();
        Collect_Article_Info collect_article_info = gson.fromJson(cache, Collect_Article_Info.class);
        if (collect_article_info.code == 200) {
            pullMydateRefresh.setAdapter(new MyReadCollectAdapter(collect_article_info));
        }
    }


    private void getUserCollect(final String url) {
        RequestParams params = new RequestParams(url);
        params.addParameter("account", UserManage.getInstance().getUserInfo(mActivity).account);
        params.addParameter("type", "w");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Collect_Article_Info collect_article_info = gson.fromJson(result, Collect_Article_Info.class);
                if (collect_article_info.code == 200) {
                    lastTime = collect_article_info.data.get(0).lastTime;
                    CacheUtils.setCache(url, result, mActivity);
                    processCollectData(result, true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(mActivity, "请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void getUserComment(String url) {
        RequestParams params = new RequestParams(url);
        params.addParameter("userID", UserManage.getInstance().getUserInfo(mActivity).user_id);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                User_Comment_Info user_comment_info = gson.fromJson(result, User_Comment_Info.class);
                if (user_comment_info.code == 200) {
                    //缓存

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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class MyReadCollectAdapter extends BaseAdapter {

        private Collect_Article_Info collect_article_info;

        public MyReadCollectAdapter(Collect_Article_Info collect_article_info) {
            this.collect_article_info = collect_article_info;
        }

        @Override
        public int getCount() {
            return collect_article_info.data.size();
        }

        @Override
        public Object getItem(int position) {
            return collect_article_info.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_my_reads, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_myread_itemPic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_myread_itemTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Collect_Article_Info.Collect_Article_Details collect_article_details = collect_article_info.data.get(position);
            Picasso.with(mActivity).load(collect_article_details.pictureURL).into(holder.ivPic);
            holder.tvTitle.setText(collect_article_details.title);
            holder.tvTitle.setTextColor(UIUtils.getcolor(R.color.back_orange));
            holder.tvIntro.setTextColor(UIUtils.getcolor(R.color.back_orange));
            return convertView;
        }
    }


//    private class MyCommentAdapter extends BaseAdapter {
//
//        private User_Comment_Info user_comment_info;
//
//        public MyCommentAdapter(User_Comment_Info user_comment_info){
//            this.user_comment_info = user_comment_info;
//        }
//
//        @Override
//        public int getCount() {
//            return user_comment_info.data.activity.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return smallnews_details_comment.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = View.inflate(mActivity, R.layout.list_my_reads, null);
//                holder = new ViewHolder();
//                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_myread_itemPic);
//                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_myread_itemTitle);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            Smallnews.Smallnews_Detail smallnews_detail = smallnews_details_comment.get(position);
//            Picasso.with(mActivity).load(smallnews_detail.pictureURL).into(holder.ivPic);
//            holder.tvTitle.setText(smallnews_detail.title);
//            holder.tvTitle.setTextColor(UIUtils.getcolor(R.color.back_orange));
//            holder.tvIntro.setText(smallnews_detail.introduce);
//            holder.tvIntro.setTextColor(UIUtils.getcolor(R.color.back_orange));
//            return convertView;
//        }
//
//    }


    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
        public TextView tvIntro;
    }

}
