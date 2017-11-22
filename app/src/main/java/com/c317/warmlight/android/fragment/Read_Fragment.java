package com.c317.warmlight.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Image;
import com.c317.warmlight.android.bean.Index;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Read_Fragment extends Fragment {

    AsyncHttpClient client = new AsyncHttpClient();

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.vp_barner)
    ViewPager vpBarner;
    @Bind(R.id.btn_barner)
    CirclePageIndicator btnBarner;
    private Index index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = UIUtils.getXmlView(R.layout.fragment_read);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * @Description 初始化数据
     * @params
     * @author Du
     * @Date 2017/11/21 20:16
     **/
    private void initData() {
        index = new Index();
        client.get(AppNetConfig.INDEX, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                Log.e("Du----","content"+content);
                JSONObject jsonObject = JSON.parseObject(content);
                String imageArr = jsonObject.getString("imageArr");
                List<Image> imageList = JSON.parseArray(imageArr, Image.class);
                index.imageList = imageList;
                vpBarner.setAdapter(new TopImageAdapter());
                btnBarner.setViewPager(vpBarner);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    private class TopImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return index.imageList == null ? 0 :index.imageList.size();//容错
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String imageUrl = index.imageList.get(position).IMAURL;
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
