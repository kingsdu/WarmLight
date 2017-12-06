package com.c317.warmlight.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.ReadData;
import com.c317.warmlight.android.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Me_Fragment extends Fragment {

    AsyncHttpClient client = new AsyncHttpClient();
    @Bind(android.R.id.list)
    RecyclerView vRecyclerView;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private ReadData newsData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = UIUtils.getXmlView(R.layout.fragment_me);
        ButterKnife.bind(this, view);

        return view;
    }


//    private void initNewsData() {
//        newsData = new ReadData();
//        client.get(AppNetConfig.PRODUCT, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String content) {
//                Log.e("Du----", "content" + content);
//                JSONObject jsonObject = JSON.parseObject(content);
//                String news = jsonObject.getString("news");
//                List<News> newsList = JSON.parseArray(news, News.class);
//                newsData.newsList = newsList;
//                // 新闻List
//                vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                vRecyclerView.setAdapter(new TestAdapter());
//            }
//
//            @Override
//            public void onFailure(Throwable error, String content) {
//                Log.e("Du----", "onFailure" + error);
//                super.onFailure(error, content);
//            }
//        });
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class TestAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
