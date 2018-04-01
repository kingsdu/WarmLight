package com.c317.warmlight.android.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.c317.warmlight.android.views.NewsWebView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/29.
 * <p>
 * 新闻详情页面
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener,NewsWebView.BottomListener, NewsWebView.onScrollListener {

    @Bind(R.id.wv_news_details)
    NewsWebView wvNewsDetails;
    @Bind(R.id.iv_newsdetails_back)
    ImageView ivNewsdetailsBack;
    @Bind(R.id.iv_newsdetails_comment)
    ImageView ivNewsdetailsComment;
    @Bind(R.id.iv_newsdetails_uncollect)
    ImageView ivNewsdetailsUncollect;
    @Bind(R.id.iv_newsdetails_collect)
    ImageView ivNewsdetailsCollect;
    @Bind(R.id.ll_news_bottom)
    LinearLayout llNewsBottom;
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

    //滑动动画
    private GestureDetector mGestureDetector;
    private float viewSlop;
    //按下的y坐标
    private float lastY;
    //记录手指是否向上滑动
    private boolean isUpSlide;
    //工具栏是否是隐藏状态
    private boolean isToolHide;
    //上部布局是否是隐藏状态
    private boolean isTopHide = false;
    //默认的动画时间
    private static final int TIME_ANIMATION = 300;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_details);
        ButterKnife.bind(this);
        //初始化已收藏，未收藏
        ivNewsdetailsBack.setVisibility(View.VISIBLE);
        ivNewsdetailsComment.setVisibility(View.VISIBLE);

        //表示滑动的时候，手的移动要大于viewSlop这个距离才开始移动控件
        viewSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(this, new DetailGestureListener());

        wvNewsDetails.setBottomListener(this);
        wvNewsDetails.setScrollListener(this);

        ivNewsdetailsComment.setOnClickListener(this);

        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(this);
        initData();
        iscollect = queryIsCollect();
        if (iscollect) {
            ivNewsdetailsCollect.setVisibility(View.VISIBLE);
        } else {
            ivNewsdetailsUncollect.setVisibility(View.VISIBLE);
        }
        ivNewsdetailsCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscollect) {
                    dataBaseHelper.updateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivNewsdetailsUncollect.setVisibility(View.GONE);
                    ivNewsdetailsCollect.setVisibility(View.VISIBLE);
                    Toast.makeText(NewsDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    iscollect = true;
                } else {
                    dataBaseHelper.unUpdateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivNewsdetailsUncollect.setVisibility(View.VISIBLE);
                    ivNewsdetailsCollect.setVisibility(View.GONE);
                    Toast.makeText(NewsDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    iscollect = false;
                }
            }
        });

        ivNewsdetailsUncollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscollect) {
                    dataBaseHelper.updateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivNewsdetailsUncollect.setVisibility(View.GONE);
                    ivNewsdetailsCollect.setVisibility(View.VISIBLE);
                    Toast.makeText(NewsDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    iscollect = true;
                } else {
                    dataBaseHelper.unUpdateCollectState(WarmLightDataBaseHelper.READ_TABLENAME, mArticleId, WarmLightDataBaseHelper.READ_ID, WarmLightDataBaseHelper.READ_ISCOLLECT);
                    ivNewsdetailsUncollect.setVisibility(View.VISIBLE);
                    ivNewsdetailsCollect.setVisibility(View.GONE);
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

        ivNewsdetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        wvNewsDetails.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float disY = event.getY() - lastY;
                        //垂直方向滑动
                        if (Math.abs(disY) > viewSlop) {
                            //设置了TextView的点击事件之后，会导致这里的disY的数值出现跳号现象，最终导致的效果就是
                            //下面的tool布局在手指往下滑动的时候，先显示一个，然后再隐藏，这是完全没必要的
                            //是否向上滑动
                            isUpSlide = disY < 0;
                            //实现底部tools的显示与隐藏
                            if (isUpSlide) {
                                if (!isToolHide)
                                    hideTool();
                            } else {
                                if (isToolHide)
                                    showTool();
                            }
                        }
                        lastY = event.getY();
                        break;
                }

                mGestureDetector.onTouchEvent(event);

                return false;
            }


        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_newsdetails_comment:
                commentActivity();
                break;
            default:
                break;
        }
    }


    /**
    * 启动评论Activity
    * @params
    * @author Du
    * @Date 2018/4/1 9:13
    **/
    private void commentActivity() {
        Intent intent = new Intent();//传ActivityId
    }

    public void initData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("url"))) {
            mUrl = getIntent().getStringExtra("url");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("article_id"))) {
            mArticleId = getIntent().getStringExtra("article_id");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            mTitle = getIntent().getStringExtra("title");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("introduce"))) {
            mIntroduce = getIntent().getStringExtra("introduce");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("pubDate"))) {
            mPubDate = getIntent().getStringExtra("pubDate");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("pictureURL"))) {
            mPictureURL = getIntent().getStringExtra("pictureURL");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("readNum"))) {
            mReadNum = getIntent().getStringExtra("readNum");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("agreeNum"))) {
            mAgreeNum = getIntent().getStringExtra("agreeNum");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("source"))) {
            mSource = getIntent().getStringExtra("source");
        }
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
                setDefaultData();
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


    /**
     * 设置为空
     *
     * @params
     * @author Du
     * @Date 2018/3/20 23:00
     **/
    private void setDefaultData() {
        if (TextUtils.isEmpty(mPubDate)) {
            mPubDate = 0 + "";
        }
        if (TextUtils.isEmpty(mArticleId)) {
            mArticleId = 0 + "";
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = "mTitle is null";
        }
        if (TextUtils.isEmpty(mIntroduce)) {
            mIntroduce = "mIntroduce is null";
        }
        if (TextUtils.isEmpty(mPictureURL)) {
            mPictureURL = R.drawable.musi01 + "";
        }
        if (TextUtils.isEmpty(mSource)) {
            mSource = "mSource is null";
        }
        if (TextUtils.isEmpty(mReadNum)) {
            mReadNum = 0 + "";
        }
        if (TextUtils.isEmpty(mAgreeNum)) {
            mAgreeNum = 0 + "";
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {

    }

    @Override
    public void onBottom() {
        if (isToolHide) {
            showTool();
        }
    }


    /**
     * 显示工具栏，代表的是底部的评论部分
     */
    private void showTool() {
        Log.d("TAG", "------------showTool-----------");

        int startY = getWindow().getDecorView()
                .getHeight() - getStatusHeight(this);
        ObjectAnimator anim = ObjectAnimator.ofFloat(llNewsBottom, "y", startY,
                startY - llNewsBottom.getHeight());
        anim.setDuration(TIME_ANIMATION);
        anim.start();
        isToolHide = false;
    }

    /**
     * 隐藏工具栏
     */
    private void hideTool() {
        Log.d("TAG", "------------hideTool-----------");

        int startY = getWindow().getDecorView()
                .getHeight() - getStatusHeight(this);
        ObjectAnimator anim = ObjectAnimator.ofFloat(llNewsBottom, "y", startY - llNewsBottom.getHeight(),
                startY);
        anim.setDuration(TIME_ANIMATION);
        anim.start();
        isToolHide = true;

    }



    /**
     * 手势指示器
     */
    private class DetailGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        //单次点击事件
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //如果都是隐藏状态，那么都显示出来
            if (isTopHide && isToolHide) {
                showTool();
            } else if (!isToolHide && isTopHide) {
                //如果上面隐藏，下面显示，就显示上面
            } else if (!isTopHide && isToolHide) {
                //如果上面显示，下面隐藏，那么就显示下面
                showTool();
            } else {
                //都在显示，那么就都隐藏
                hideTool();
            }
            return super.onSingleTapConfirmed(e);
        }
    }


    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

}
