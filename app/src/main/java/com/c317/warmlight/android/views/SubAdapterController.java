package com.c317.warmlight.android.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Horizontalnews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SubAdapterController {
    private List<Object> mData;
    private Context mContext;
    private SubAdapter mAdapter;

    public SubAdapterController(List<Object> data, Context context) {
        //若初始化数据为空，则重新New数据
        mData = data;
        mContext = context;
    }


    public SubAdapter getAdapter() {
        if (!mData.isEmpty()) {
            if (null == mAdapter) {
                mAdapter = new SubAdapter(mContext);
            }
            return mAdapter;
        } else {
            return null;
        }

    }

    class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {
        private LayoutInflater mInflater;

        public SubAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        //初始化布局
        @Override
        public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.sub_horizontal, parent, false);
            return new SubViewHolder(view);
        }

        //设置数据
        @Override
        public void onBindViewHolder(SubViewHolder holder, int position) {
            Horizontalnews.Horizontalnews_menu horizontalnews_menu = (Horizontalnews.Horizontalnews_menu) mData.get(position);
            Picasso.with(mContext).load(horizontalnews_menu.imageurl).into(holder.image);
            holder.title.setText(horizontalnews_menu.title);
            holder.author.setText(horizontalnews_menu.author);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        //Holder类
        public class SubViewHolder extends RecyclerView.ViewHolder {
            public TextView author;
            public TextView title;
            public ImageView image;

            public SubViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.iv_horizontal_image);
                author = (TextView) view.findViewById(R.id.tv_horizontal_author);
                title = (TextView) view.findViewById(R.id.tv_horizontal_title);
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //启动一个ACTIVITY
//                        Toast.makeText(mContext, ((TextView)v.findViewById(R.id.main_title)).getText(), Toast.LENGTH_SHORT).show();
//                    }
//                });
////                image = (ImageView) findViewById(R.id.image);
            }
        }
    }
}
