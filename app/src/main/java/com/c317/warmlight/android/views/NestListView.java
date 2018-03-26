package com.c317.warmlight.android.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;

/**
 * Created by Administrator on 2017/12/5.
 */

public class NestListView extends LinearLayout {

    private RecyclerView mSubListView;
    private TextView mNestViewHeader;
    private String mHeaderDefaultText = "小桔猜猜";

    public NestListView(Context context) {
        super(context);
        init(context);
    }

    public NestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void setNestViewHeaderText(String text){
        mNestViewHeader.setText(text);
    }


    public void init(Context context){
        setOrientation(VERTICAL);
//        initNestViewHeader(context);
        initListView(context);
    }
//    android:background="@drawable/readbookborder"

    public void initNestViewHeader(Context context){
        if (!mHeaderDefaultText.isEmpty()) {
            mNestViewHeader = new TextView(context);
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mNestViewHeader.setLayoutParams(l);
            mNestViewHeader.setText(mHeaderDefaultText);
            mNestViewHeader.setTextColor(R.color.main_orange);
            addView(mNestViewHeader);
        }
    }

    public void initListView(Context context) {
        mSubListView = new RecyclerView(context);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSubListView.setLayoutParams(l);
        mSubListView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSubListView.setLayoutManager(mLayoutManager);
        addView(mSubListView);
    }



    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter){
        if(null != mSubListView) {
            mSubListView.setAdapter(adapter);
        }
    }


}
