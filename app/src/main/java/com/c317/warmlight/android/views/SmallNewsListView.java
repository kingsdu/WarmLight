package com.c317.warmlight.android.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/1/5.
 *
 * viewHolder：linearLayout + RecyclerView
 */

public class SmallNewsListView extends LinearLayout {

    private RecyclerView mSmallNewsListView;

    public SmallNewsListView(Context context) {
        super(context);
        init(context);
    }

    public SmallNewsListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SmallNewsListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        setOrientation(VERTICAL);
        initListView(context);
    }

    private void initListView(Context context) {
        mSmallNewsListView = new RecyclerView(context);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSmallNewsListView.setLayoutParams(l);
        mSmallNewsListView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSmallNewsListView.setLayoutManager(mLayoutManager);
        addView(mSmallNewsListView);
    }


    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter){
        if(null != mSmallNewsListView) {
            mSmallNewsListView.setAdapter(adapter);
        }
    }
}
