package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.DailyAsk;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/8.
 */

public class MyDailyAskActivity extends Activity implements View.OnClickListener, ListView.OnItemClickListener {

    @Bind(R.id.lv_dailyask_askitem)
    ListView lvDailyaskAskitem;
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    int page = 1;
    @Bind(R.id.iv_all_newask)
    ImageView ivAllNewask;
    private DailyAsk dailyAsk;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.me_dailyask_content);
        ButterKnife.bind(this);
        tvTopbarTitle.setVisibility(View.VISIBLE);
        ivBackMe.setVisibility(View.VISIBLE);
        ivAllNewask.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("我的由答");
        ivBackMe.setOnClickListener(this);
        ivAllNewask.setOnClickListener(this);
        lvDailyaskAskitem.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.QUESTION;
        RequestParams params = new RequestParams(url);
        String account = UserManage.getInstance().getUserInfo(MyDailyAskActivity.this).account;
        params.addParameter(AppConstants.ACCOUNT,account);
        params.addParameter(AppConstants.PAGE, page);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                dailyAsk = gson.fromJson(result, DailyAsk.class);
                lvDailyaskAskitem.setAdapter(new DailyAskAdapter(dailyAsk));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.iv_all_newask:
                addNewAsk();
                break;
        }
    }

    /**
     * 新建由答
     *
     * @params
     * @author Du
     * @Date 2018/4/8 22:30
     **/
    private void addNewAsk() {
        Intent intent = new Intent(MyDailyAskActivity.this,DailyAskAddActivity.class);
        startActivityForResult(intent,2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            initData();
        }
    }

    @OnClick(R.id.iv_all_newask)
    public void onViewClicked() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (dailyAsk.code == 200) {
            Intent intent = new Intent(MyDailyAskActivity.this, DailyAskDetailActivity.class);
            int question_id = dailyAsk.data.detail.get(position).question_id;
            intent.putExtra("question_id", "q" + question_id + "");
            intent.putExtra("title", dailyAsk.data.detail.get(position).title);
            intent.putExtra("content", dailyAsk.data.detail.get(position).content);
            intent.putExtra("pubDate", sdf.format(dailyAsk.data.detail.get(position).pubDate));
            intent.putExtra("proposer", dailyAsk.data.detail.get(position).proposer);
            startActivity(intent);
        } else {
            CommonUtils.showToastShort(MyDailyAskActivity.this, "dailyAsk无数据");
        }
    }


    private class DailyAskAdapter extends BaseAdapter {

        private DailyAsk dailyAsk;

        public DailyAskAdapter(DailyAsk dailyAsk) {
            this.dailyAsk = dailyAsk;
        }

        @Override
        public int getCount() {
            return dailyAsk.data.detail.size();
        }

        @Override
        public DailyAsk.DailyAsk_details getItem(int position) {
            return dailyAsk.data.detail.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DailyAskHolder holder;
            DailyAsk.DailyAsk_details item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_dailyask_detailitem, parent, false);
                holder = new DailyAskHolder();
                convertView.setTag(holder);
                holder.title = (TextView) convertView.findViewById(R.id.tv_dailyask_title);
                holder.content = (TextView) convertView.findViewById(R.id.tv_dailyask_content);
                holder.commentNum = (TextView) convertView.findViewById(R.id.tv_dailyask_complimentnum);
            } else {
                holder = (DailyAskHolder) convertView.getTag();
            }
            final CharSequence no = "暂无";
            holder.title.setHint(no);
            holder.commentNum.setHint(no);
            holder.content.setHint(no);

            holder.title.setText(item.title);
            holder.commentNum.setText(item.commentNum + "人回答");
            holder.content.setText(item.content);

            return convertView;
        }

    }


    static class DailyAskHolder {
        TextView title;
        TextView content;
        TextView commentNum;
    }
}
