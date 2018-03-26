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
import com.c317.warmlight.android.bean.Smallnews;
import com.c317.warmlight.android.common.AppNetConfig;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 *
 * 获取数据，填充Adapter，监听Adapter点击事件
 *
 */

public class SmallNewsController {
    private List<Object> mData;
    private Context mContext;
    private SmallNewsAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener = null;

    public SmallNewsController(List<Object> data, Context context){
        //若初始化数据为空，则重新New数据
        mData = data;
        mContext = context;
    }


    public SmallNewsAdapter getAdapter(){
        if (!mData.isEmpty()) {
            if (null == mAdapter) {
                mAdapter = new SmallNewsAdapter(mContext);
                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Smallnews.Smallnews_Detail smallnews_detail = (Smallnews.Smallnews_Detail) mData.get(position);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String article_id = String.valueOf(smallnews_detail.article_id);
                        Intent intent = new Intent(mContext, NewsDetailActivity.class);
                        intent.putExtra("url","http://14g97976j3.51mypc.cn:10759/youdu/getArtCont/"+article_id);
                        intent.putExtra("article_id",article_id);
                        intent.putExtra("title",smallnews_detail.title);
                        intent.putExtra("introduce",smallnews_detail.introduce);
                        intent.putExtra("pubDate",sdf.format(smallnews_detail.pubDate));
                        intent.putExtra("pictureURL",smallnews_detail.pictureURL);
                        intent.putExtra("readNum",smallnews_detail.readNum+"");
                        intent.putExtra("agreeNum",smallnews_detail.agreeNum+"");
                        intent.putExtra("source",smallnews_detail.source);
                        mContext.startActivity(intent);
                        //自增阅读数
                        addReadNum(article_id);
                    }
                });
            }
            return mAdapter;
        }else {
            return null;
        }
    }

    /**
     * 阅读数自增
     * */
    private void addReadNum(String article_id) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + "addArtAgreeNum";
        RequestParams params = new RequestParams(url);
        params.addParameter("article_id",article_id);
        x.http().request(HttpMethod.PUT,params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {

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


    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public class SmallNewsAdapter extends RecyclerView.Adapter<SmallNewsAdapter.SmallNewsViewHolder> implements View.OnClickListener{
        private LayoutInflater mInflater;

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_NORMAL = 1;

        private View mHeaderView;

        public SmallNewsAdapter (Context context){
            mInflater = LayoutInflater.from(context);
        }

        public void setHeaderView(View headerView) {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }


        public View getHeaderView() {
            return mHeaderView;
        }

        @Override
        public int getItemViewType(int position) {
            if(mHeaderView == null) return TYPE_NORMAL;
            if(position == 0) return TYPE_HEADER;
            return TYPE_NORMAL;
        }

        public int getRealPosition(RecyclerView.ViewHolder holder) {
            int position = holder.getLayoutPosition();
            return mHeaderView == null ? position : position - 1;
        }

        @Override
        public SmallNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(mHeaderView != null && viewType == TYPE_HEADER)
                return new SmallNewsViewHolder(mHeaderView);
            View view = mInflater.inflate(R.layout.smallnews_details, parent, false);
            view.setOnClickListener(this);
            return new SmallNewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SmallNewsViewHolder holder, int position) {
            if(getItemViewType(position) == TYPE_HEADER) return;

            final int pos = getRealPosition(holder);
            Smallnews.Smallnews_Detail smallnews_detail = (Smallnews.Smallnews_Detail) mData.get(pos);
            if(holder instanceof SmallNewsViewHolder) {
                if(smallnews_detail.pictureURL == null){
                    return;
                }
                String imageUrl = "";
                String gif = smallnews_detail.pictureURL.substring(smallnews_detail.pictureURL.lastIndexOf(".")+1, smallnews_detail.pictureURL.length());
                if (gif.equals("gif")) {
                    imageUrl = smallnews_detail.pictureURL;
                    Glide.with(mContext).load(imageUrl).asGif().error(R.drawable.book1).into(((SmallNewsViewHolder) holder).small_image);
                }else{
                    imageUrl = smallnews_detail.pictureURL;
                    Picasso.with(mContext).load(imageUrl).into(((SmallNewsViewHolder) holder).small_image);
                }
                holder.small_title.setText(smallnews_detail.title);
                holder.small_user.setText(String.valueOf(smallnews_detail.readNum));
                holder.small_views.setText(String.valueOf(smallnews_detail.agreeNum));
                holder.itemView.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return  mHeaderView == null ? mData.size() : mData.size() + 1;
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

        public class SmallNewsViewHolder extends RecyclerView.ViewHolder{
            public ImageView small_image;
            public TextView small_title;
            public TextView small_user;
            public TextView small_views;

            public SmallNewsViewHolder(View itemView) {
                super(itemView);
                small_image = (ImageView) itemView.findViewById(R.id.iv_small_image);
                small_title = (TextView) itemView.findViewById(R.id.tv_small_title);
                small_user = (TextView) itemView.findViewById(R.id.tv_small_user);
                small_views = (TextView) itemView.findViewById(R.id.tv_small_views);
            }
        }
    }
}
