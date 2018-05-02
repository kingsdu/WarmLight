package com.c317.warmlight.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2018/3/27.
 */

public class NewsWebView extends WebView {

    private BottomListener bottomListener;

    private onScrollListener scrollListener;

    public NewsWebView(Context context) {
        super(context);
    }

    public NewsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
        if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
            //监听滑动到底部的事件
            if (null != bottomListener) {
                bottomListener.onBottom();
            }
        }

        if (null != scrollListener) {
            scrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }


    public void setBottomListener(BottomListener bottomListener) {
        this.bottomListener = bottomListener;
    }

    public void setScrollListener(onScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }


    public interface onScrollListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }

    public interface BottomListener {

        public void onBottom();

    }
}
