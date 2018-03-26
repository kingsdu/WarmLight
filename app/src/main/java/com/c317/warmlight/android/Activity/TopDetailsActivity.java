package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Topnews_details;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.utils.CacheUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/9.
 * <p>
 * 头条新闻数据详情
 */

public class TopDetailsActivity extends AppCompatActivity {

    @Bind(R.id.iv_bookPicture)
    ImageView ivBookPicture;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_bookTitle)
    TextView tvBookTitle;
    @Bind(R.id.iv_booksign_1)
    ImageView ivBooksign1;
    @Bind(R.id.tv_bookauthor)
    TextView tvBookauthor;
    @Bind(R.id.iv_booksign_2)
    ImageView ivBooksign2;
    @Bind(R.id.tv_bookpublication)
    TextView tvBookpublication;
    @Bind(R.id.iv_booksign_3)
    ImageView ivBooksign3;
    @Bind(R.id.tv_bookpubtime)
    TextView tvBookpubtime;
    @Bind(R.id.iv_booksign_4)
    ImageView ivBooksign4;
    @Bind(R.id.tv_bookintro)
    TextView tvBookintro;
    private String url;
    private String search_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.book_details);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        search_id = getIntent().getStringExtra("search_id");
        initToolBar();
        setToolBarLayout();
        initData();
    }

    private void initData() {
        String cache = CacheUtils.getCache(url, this);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        } else {
            getDataFromServer();//快速加载
        }
    }

    private void processData(String cache) {
        Gson gson = new Gson();
        Topnews_details topnews_details = gson.fromJson(cache, Topnews_details.class);
        Topnews_details.Topnews_content topnews_content = topnews_details.data;
        setTopNewView(topnews_content);
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Topnews_details topnews_details = gson.fromJson(result, Topnews_details.class);
                Topnews_details.Topnews_content topnews_content = topnews_details.data;
                setTopNewView(topnews_content);
                CacheUtils.setCache(url, result, TopDetailsActivity.this);
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


    private void setTopNewView(Topnews_details.Topnews_content topnews_content) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String pubCompany = "出版社：" + topnews_content.pubCompany;
        String pubDate = "出版时间：" + formatter.format(topnews_content.pubDate);
        String bookIntr = "书籍简介：" + topnews_content.bookIntr;
        Picasso.with(this).load(topnews_content.pictureURL).into(ivBookPicture);
        tvBookTitle.setText(topnews_content.title);
        ivBooksign1.setImageResource(R.drawable.sign);
        tvBookauthor.setText(topnews_content.author);
        ivBooksign2.setImageResource(R.drawable.sign);
        tvBookpublication.setText(pubCompany);
        ivBooksign3.setImageResource(R.drawable.sign);
        tvBookpubtime.setText(String.valueOf(pubDate));
        ivBooksign4.setImageResource(R.drawable.sign);
        tvBookintro.setText(bookIntr);
    }

    private void setToolBarLayout() {
        collapsingToolbarLayout.setTitle(" ");
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.main_orange));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
