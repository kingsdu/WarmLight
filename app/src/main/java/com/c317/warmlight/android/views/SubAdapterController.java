package com.c317.warmlight.android.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.BookDetailsActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.OrangeGuess;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 *
 * 书籍部分信息
 */

public class SubAdapterController {
    private List<Object> mData;
    private Context mContext;
    private SubAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener = null;

    public SubAdapterController(List<Object> data, Context context) {
        //若初始化数据为空，则重新New数据
        mData = data;
        mContext = context;
    }


    public SubAdapter getAdapter() {
        if (!mData.isEmpty()) {
            if (null == mAdapter) {
                mAdapter = new SubAdapter(mContext);
                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //新建页面，解析数据，填充页面
                        OrangeGuess.OrangeGuess_details orangeGuessInfo = (OrangeGuess.OrangeGuess_details) mData.get(position);
                        String book_id = String.valueOf(orangeGuessInfo.book_id);
                        Intent intent = new Intent(mContext, BookDetailsActivity.class);
                        intent.putExtra("book_id",book_id);
                        intent.putExtra("pictureURL",orangeGuessInfo.pictureURL);
                        intent.putExtra("author",orangeGuessInfo.author);
                        intent.putExtra("title",orangeGuessInfo.title);
                        mContext.startActivity(intent);
                    }
                });
            }
            return mAdapter;
        } else {
            return null;
        }

    }

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }


    class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> implements View.OnClickListener{
        private LayoutInflater mInflater;

        public SubAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        //初始化布局
        @Override
        public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.sub_horizontal, parent, false);
            view.setOnClickListener(this);
            return new SubViewHolder(view);
        }

        //设置数据
        @Override
        public void onBindViewHolder(SubViewHolder holder, int position) {
            OrangeGuess.OrangeGuess_details orangeGuessInfo = (OrangeGuess.OrangeGuess_details) mData.get(position);
            String imageUrl = orangeGuessInfo.pictureURL;
            Picasso.with(mContext).load(imageUrl).into(holder.image);
            holder.title.setText(orangeGuessInfo.title);
            holder.author.setText(orangeGuessInfo.author);
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
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
            }
        }
    }
}
