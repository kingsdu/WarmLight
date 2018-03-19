package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/29.
 * <p>
 * 新闻详情页面
 */

public class NewsDetailActivity extends Activity {

    @Bind(R.id.wv_news_details)
    WebView wvNewsDetails;
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.iv_read_collect)
    ImageView ivReadCollect;
    @Bind(R.id.iv_read_uncollect)
    ImageView ivReadUncollect;
    private String mUrl;
    private String mArticleId;
    private boolean iscollect;//是否收藏
    private WarmLightDataBaseHelper dataBaseHelper;
    private String mTitle;
    private String mIntroduce;
    private String mPubDate;
    private String mPictureURL;
    private String mReadNum;
    private String mAgreeNum;
    private String mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_details);
        ButterKnife.bind(this);
        ivBackMe.setVisibility(View.VISIBLE);
        mUrl = getIntent().getStringExtra("url");
        mArticleId = getIntent().getStringExtra("article_id");
        mTitle = getIntent().getStringExtra("title");
        mIntroduce = getIntent().getStringExtra("introduce");
        mPubDate = getIntent().getStringExtra("pubDate");
        mPictureURL = getIntent().getStringExtra("pictureURL");
        mReadNum = getIntent().getStringExtra("readNum");
        mAgreeNum = getIntent().getStringExtra("agreeNum");
        mSource = getIntent().getStringExtra("source");
        //初始化已收藏，未收藏
        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(this);
        iscollect = queryIsCollect();
        if (iscollect) {
            ivReadCollect.setVisibility(View.VISIBLE);
        } else {
            ivReadUncollect.setVisibility(View.VISIBLE);
        }

        ivReadCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscollect) {
                    dataBaseHelper.updateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivReadUncollect.setVisibility(View.GONE);
                    ivReadCollect.setVisibility(View.VISIBLE);
                    Toast.makeText(NewsDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    iscollect = true;
                } else {
                    dataBaseHelper.unUpdateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivReadUncollect.setVisibility(View.VISIBLE);
                    ivReadCollect.setVisibility(View.GONE);
                    Toast.makeText(NewsDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    iscollect = false;
                }
            }
        });

        ivReadUncollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscollect) {
                    dataBaseHelper.updateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivReadUncollect.setVisibility(View.GONE);
                    ivReadCollect.setVisibility(View.VISIBLE);
                    Toast.makeText(NewsDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    iscollect = true;
                } else {
                    dataBaseHelper.unUpdateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivReadUncollect.setVisibility(View.VISIBLE);
                    ivReadCollect.setVisibility(View.GONE);
                    Toast.makeText(NewsDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    iscollect = false;
                }
            }
        });
        wvNewsDetails.loadUrl(mUrl);

        wvNewsDetails.setWebViewClient(new WebViewClient() {
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            //加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

            //所有url跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                view.loadUrl(request);
                return true;
            }
        });


        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始查询新闻是否被收藏
     *
     * @params
     * @author Du
     * @Date 2018/3/14 16:50
     **/
    private boolean queryIsCollect() {
        String isCollect = dataBaseHelper.queryIsCollectRead(mArticleId);
        if (!TextUtils.isEmpty(isCollect)) {
            if (isCollect.equals("1")) {
                return true;
            } else {
                return false;
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Smallnews.Smallnews_Detail smallnews_detail = new Smallnews.Smallnews_Detail();
                smallnews_detail.pubDate = sdf.parse(mPubDate);
                smallnews_detail.article_id = Integer.parseInt(mArticleId);
                smallnews_detail.title = mTitle;
                smallnews_detail.introduce = mIntroduce;
                smallnews_detail.pictureURL = mPictureURL;
                smallnews_detail.source = mSource;
                smallnews_detail.readNum = Integer.parseInt(mReadNum);
                smallnews_detail.agreeNum = Integer.parseInt(mAgreeNum);
                smallnews_detail.isCollect = 0;
                dataBaseHelper.InsertCollectInfoRead(smallnews_detail);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
