package com.c317.warmlight.android.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.OrangeGuess;
import com.c317.warmlight.android.views.SubAdapterController;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> implements View.OnClickListener{

    private OnItemClickListener mOnItemClickListener = null;
    private HorizontalAdapter mAdapter;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Object> mData;

    public HorizontalAdapter(Context context,List<Object> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Toast.makeText(mContext, "onCreateViewHolder", Toast.LENGTH_SHORT).show();
        View view = mInflater.inflate(R.layout.sub_horizontal, parent, false);
        view.setOnClickListener(this);
        return new HorizontalAdapter.HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        Toast.makeText(mContext, "onBindViewHolder", Toast.LENGTH_SHORT).show();
        OrangeGuess.OrangeGuess_details orangeGuessInfo = (OrangeGuess.OrangeGuess_details) mData.get(position);
        String imageUrl = orangeGuessInfo.pictureURL;
        Picasso.with(mContext).load(imageUrl).into(holder.image);
        holder.title.setText(orangeGuessInfo.title);
        holder.author.setText(orangeGuessInfo.author);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }


    //Holder类
    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView title;
        public ImageView image;

        public HorizontalViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_horizontal_image);
            author = (TextView) view.findViewById(R.id.tv_horizontal_author);
            title = (TextView) view.findViewById(R.id.tv_horizontal_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //启动一个ACTIVITY
                    Toast.makeText(mContext, "SubViewHolder on button", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
