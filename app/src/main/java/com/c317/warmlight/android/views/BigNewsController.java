package com.c317.warmlight.android.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.c317.warmlight.android.Activity.NewsDetailActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Bignews;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 *
 * 获取数据，填充Adapter，监听Adapter点击事件
 */

public class BigNewsController{

    private List<Object> mData;
    private Context mContext;
    private BigNewsController.BigNewsAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener = null;

    public BigNewsController(List<Object> data, Context context){
        //若初始化数据为空，则重新New数据
        mData = data;
        mContext = context;
    }

    public BigNewsAdapter getAdapter(){
        if (!mData.isEmpty()) {
            if (null == mAdapter) {
                mAdapter = new BigNewsAdapter(mContext);
                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bignews.Bignews_Detail bignewsDetail = (Bignews.Bignews_Detail) mData.get(position);
                        //启动一个ACTIVITY
                        String article_id = String.valueOf(bignewsDetail.article_id);
                        Intent intent = new Intent(mContext, NewsDetailActivity.class);
                        intent.putExtra("url","http://14g97976j3.51mypc.cn:10759/youdu/getArtCont/"+article_id);
                        mContext.startActivity(intent);
                    }
                });
            }
            return mAdapter;
        }else {
            return null;
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }


    public class BigNewsAdapter extends RecyclerView.Adapter<BigNewsAdapter.BigNewsViewHolder> implements View.OnClickListener{
        private LayoutInflater mInflater;

        public BigNewsAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public BigNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.bignews_details, parent, false);
            view.setOnClickListener(this);
            return new BigNewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BigNewsViewHolder holder, int position) {
            Bignews.Bignews_Detail bignewsDetail = (Bignews.Bignews_Detail) mData.get(position);
            if(bignewsDetail.pictureURL == null){
                return;
            }
            String imageUrl = "";
            String gif = bignewsDetail.pictureURL.substring(bignewsDetail.pictureURL.lastIndexOf(".")+1, bignewsDetail.pictureURL.length());
            if (gif.equals("gif")) {
                imageUrl = bignewsDetail.pictureURL;
                Glide.with(mContext).load(imageUrl).asGif().error(R.drawable.book1).into(((BigNewsViewHolder) holder).big_image);
            }else{
                imageUrl = bignewsDetail.pictureURL;
                Picasso.with(mContext).load(imageUrl).into(((BigNewsViewHolder) holder).big_image);
            }
            holder.big_title.setText(bignewsDetail.title);
            holder.big_content.setText(bignewsDetail.introduce);
            holder.itemView.setTag(position);
        }


        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
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

        public class BigNewsViewHolder extends RecyclerView.ViewHolder{
            public ImageView big_image;
            public TextView big_title;
            public TextView big_content;

            public BigNewsViewHolder(View itemView) {
                super(itemView);
                big_image = (ImageView) itemView.findViewById(R.id.iv_big_image);
                big_title = (TextView) itemView.findViewById(R.id.iv_big_title);
                big_content = (TextView) itemView.findViewById(R.id.iv_big_content);
            }
        }
    }
}
