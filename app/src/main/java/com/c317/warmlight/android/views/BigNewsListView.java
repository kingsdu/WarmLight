package com.c317.warmlight.android.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/1/6.
 *
 *
 * viewHolder：linearLayout + RecyclerView
 *
 */

public class BigNewsListView extends LinearLayout {
    private RecyclerView mBigNewsListView;

    public BigNewsListView(Context context) {
        super(context);
        init(context);
    }

    public BigNewsListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BigNewsListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context){
        setOrientation(VERTICAL);
        initListView(context);
    }

    private void initListView(Context context) {
        mBigNewsListView = new RecyclerView(context);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mBigNewsListView.setLayoutParams(l);
        mBigNewsListView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBigNewsListView.setLayoutManager(mLayoutManager);
        addView(mBigNewsListView);
    }


    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter){
        if(null != mBigNewsListView) {
            mBigNewsListView.setAdapter(adapter);
        }
    }
}
