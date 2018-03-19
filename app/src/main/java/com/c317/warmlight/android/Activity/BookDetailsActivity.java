package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.OrangeGuess_detail;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/1/7.
 * <p>
 * 书籍详情Activity
 */

public class BookDetailsActivity extends AppCompatActivity {

    @Bind(R.id.iv_bookPicture)
    ImageView ivBookPicture;
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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;

    private Activity mActivity;
    private String book_id;
    private String author;
    private String picURL;
    private String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.book_details);
        ButterKnife.bind(this);
        book_id = getIntent().getStringExtra("book_id");
        title = getIntent().getStringExtra("title");
        picURL = getIntent().getStringExtra("pictureURL");
        author = getIntent().getStringExtra("author");
        getDataFromServer();
        initToolBar();
        setToolBarLayout(title);
    }

    /**
     * @Description 获取书籍数据
     * @params
     * @author Du
     * @Date 2018/1/9 20:44
     **/
    private void getDataFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + "getBookInfo" + AppNetConfig.PARAMETER + "book_id=" + book_id;
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OrangeGuess_detail orange_detail = gson.fromJson(result, OrangeGuess_detail.class);
                OrangeGuess_detail.OrangeGuess_content orangeGuess_content = orange_detail.data;
                setBookData(orangeGuess_content);
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

    private void setBookData(OrangeGuess_detail.OrangeGuess_content orangeGuess_content) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String author_intro = "作者：" + orangeGuess_content.authorIntr;
        String pubCompany = "出版社：" + orangeGuess_content.pubCompany;
        String pubDate = "出版时间：" + formatter.format(orangeGuess_content.pubDate);
        String bookIntr = "书籍简介：" + orangeGuess_content.bookIntr;
        Picasso.with(mActivity).load(picURL).into(ivBookPicture);
        tvBookTitle.setText(title);
        ivBooksign1.setImageResource(R.drawable.sign);
        tvBookauthor.setText(author);
        ivBooksign2.setImageResource(R.drawable.sign);
        tvBookpublication.setText(pubCompany);
        ivBooksign3.setImageResource(R.drawable.sign);
        tvBookpubtime.setText(String.valueOf(pubDate));
        ivBooksign4.setImageResource(R.drawable.sign);
        tvBookintro.setText(bookIntr);
    }

    private void setToolBarLayout(String newsTitle) {
        collapsingToolbarLayout.setTitle(newsTitle);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.news_detail, menu);
//        return true;
//    }

    @Override
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
}
