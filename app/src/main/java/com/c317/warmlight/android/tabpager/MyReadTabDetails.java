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

import com.c317.warmlight.android.Activity.NewsDetailActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseMenuDetailPager;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.bean.Topnews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.UIUtils;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    private WarmLightDataBaseHelper dataBaseHelper;
    private List<Smallnews.Smallnews_Detail> smallnews_details;

    public MyReadTabDetails(Activity activity, String url, int type) {
        super(activity);
        mType = type;
        mUrl = url + AppNetConfig.PARAMETER + AppNetConfig.PAGE + AppNetConfig.EQUAL + PAGESIZE;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_mydate_detail, null);
        ButterKnife.bind(this, view);
        account = UserManage.getInstance().getUserInfo(mActivity).account;

        pullMydateRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                getDataFromServer(account, mUrl, MyReadTabDetails.this.mType);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                getDataFromServer(account, mUrl, MyReadTabDetails.this.mType);
            }
        });
        pullMydateRefresh.setMode(PullToRefreshBase.Mode.BOTH);//上拉下拉都支持

        pullMydateRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Smallnews.Smallnews_Detail smallnews_detail =  smallnews_details.get(position-1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String article_id = String.valueOf(smallnews_detail.article_id);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url","http://14g97976j3.51mypc.cn:10759/youdu/getArtCont/"+article_id);
                intent.putExtra("article_id",article_id);
                intent.putExtra("title",smallnews_detail.title);
                intent.putExtra("introduce",smallnews_detail.introduce);
                intent.putExtra("pubDate",sdf.format(smallnews_detail.pubDate));
                intent.putExtra("pictureURL",smallnews_detail.pictureURL);
                intent.putExtra("readNum",smallnews_detail.readNum+"");
                intent.putExtra("agreeNum",smallnews_detail.agreeNum+"");
                intent.putExtra("source",smallnews_detail.source);
                mActivity.startActivity(intent);
            }
        });
            String url = "";

////            final Topnews.Topnews_Info topnewsInfo = (Topnews.Topnews_Info) topnewsData.get(position);
//            @Override
//            public void onClick(View v) {
////                String search_id = topnewsInfo.search_id;
//                url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + "getArtCont" + AppNetConfig.SEPARATOR + search_id;
//                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
//                intent.putExtra("url", url);
//                mActivity.startActivity(intent);
//            }
//        });
        return view;
    }


    private void getDataFromServer(String account, String mUrl, int mType) {
    }


//    private void processData(String cache, boolean isMore) {
//        Gson gson = new Gson();
//        DateNews dateNews = gson.fromJson(cache, DateNews.class);
//        if (isMore) {
//            mDatadetail = dateNews.data.detail;
//            pullMydateRefresh.setAdapter(new MyDateTabDetails.MydateAdapter());
//            PAGESIZE++;//页数增加
//        } else {
//            Toast.makeText(mActivity, "没有数据了", Toast.LENGTH_SHORT).show();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        pullMydateRefresh.onRefreshComplete();
//    }


    @Override
    public void initData() {
        super.initData();
        if (mType != 1) {
//            String cache = CacheUtils.getCache(mUrl + "&type=" + mType, mActivity);
//            if (!TextUtils.isEmpty(cache)) {
//                processData(cache, true);
//            } else {
//                getDataFromServer(account, mUrl, mType);//通过服务器获取数据
//            }
        } else {
            String isCollect = 1 + "";
            dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(mActivity);
            smallnews_details = dataBaseHelper.queryMultiIsCollectRead(isCollect);

            pullMydateRefresh.setAdapter(new MyReadCollectAdapter());
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


    private class MyReadCollectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return smallnews_details.size();
        }

        @Override
        public Object getItem(int position) {
            return smallnews_details.get(position);
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
                holder.tvIntro = (TextView) convertView.findViewById(R.id.tv_myread_itemContent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Smallnews.Smallnews_Detail smallnews_detail = smallnews_details.get(position);
            Picasso.with(mActivity).load(smallnews_detail.pictureURL).into(holder.ivPic);
            holder.tvTitle.setText(smallnews_detail.title);
            holder.tvTitle.setTextColor(UIUtils.getcolor(R.color.back_orange));
            holder.tvIntro.setText(smallnews_detail.introduce);
            holder.tvIntro.setTextColor(UIUtils.getcolor(R.color.back_orange));
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
        public TextView tvIntro;
    }

}
