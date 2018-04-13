package com.c317.warmlight.android.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Comment;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/9.
 */

public class DailyAskDetailActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    @Bind(R.id.tv_readdailyask_title)
    TextView tvReaddailyaskTitle;
    @Bind(R.id.tv_readdailyask_username)
    TextView tvReaddailyaskUsername;
    @Bind(R.id.tv_readdailyask_pubtime)
    TextView tvReaddailyaskPubtime;
    @Bind(R.id.tv_readdailyask_content)
    TextView tvReaddailyaskContent;
    @Bind(R.id.iv_newsdetails_back)
    ImageView ivNewsdetailsBack;
    @Bind(R.id.iv_newsdetails_comment)
    ImageView ivNewsdetailsComment;
    @Bind(R.id.ll_dailyask_bottom)
    LinearLayout llDailyaskBottom;
    @Bind(R.id.ll_read_dailyaskdetail)
    ListView llReadDailyaskdetail;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    //按下的y坐标
    private float lastY;
    private float viewSlop;
    //记录手指是否向上滑动
    private boolean isUpSlide;
    //工具栏是否是隐藏状态
    private boolean isToolHide;
    //上部布局是否是隐藏状态
    private boolean isTopHide = false;
    //默认的动画时间
    private static final int TIME_ANIMATION = 300;
    //滑动动画
    private GestureDetector mGestureDetector;
    private String question_id;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.read_dailyask_detail);
        ButterKnife.bind(this);
        tvTopbarTitle.setVisibility(View.VISIBLE);
        ivNewsdetailsBack.setVisibility(View.VISIBLE);
        ivNewsdetailsComment.setVisibility(View.VISIBLE);
        ivNewsdetailsBack.setOnClickListener(this);
        ivNewsdetailsComment.setOnClickListener(this);
        //表示滑动的时候，手的移动要大于viewSlop这个距离才开始移动控件
        viewSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(this, new DetailGestureListener());
        initData();
    }

    private void initData() {
        tvTopbarTitle.setText("问问详情");
        getIntentData();
        getDataFromServer();
    }

    private void getDataFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETCOMMENT;
        RequestParams params = new RequestParams(url);
        params.addParameter("searchID", question_id);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Comment comment = gson.fromJson(result, Comment.class);
                if (comment.code == 200) {
                    llReadDailyaskdetail.setAdapter(new DailyAskCommentAdapter(comment));
                }
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

    private void getIntentData() {
        question_id = getIntent().getStringExtra("question_id");
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String proposer = getIntent().getStringExtra("proposer");
        String pubDate = getIntent().getStringExtra("pubDate");

        final CharSequence no = "暂无";
        tvReaddailyaskTitle.setHint(no);
        tvReaddailyaskUsername.setHint(no);
        tvReaddailyaskPubtime.setHint(no);
        tvReaddailyaskContent.setHint(no);


        tvReaddailyaskTitle.setText(title);
        tvReaddailyaskUsername.setText(proposer);
        tvReaddailyaskPubtime.setText(pubDate);
        tvReaddailyaskContent.setText(content);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_readdailyask_content:
                ContentViewOntouch(event);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_newsdetails_back:
                finish();
                break;
            case R.id.iv_newsdetails_comment:
                addAnswer();
                break;
        }
    }

    private void addAnswer() {
        Intent intent = new Intent(DailyAskDetailActivity.this, DailyAskCommentActivity.class);
        intent.putExtra("question_id", question_id);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getDataFromServer();
        }
    }

    private void ContentViewOntouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disY = event.getY() - lastY;
                //垂直方向滑动
                if (Math.abs(disY) > viewSlop) {
                    isUpSlide = disY < 0;
                    //实现底部tools的显示与隐藏
                    if (isUpSlide) {
                        if (!isToolHide)
                            hideTool();
                    } else {
                        if (isToolHide)
                            showTool();
                    }
                }
                lastY = event.getY();
                break;
        }
        mGestureDetector.onTouchEvent(event);
    }


    /**
     * 手势指示器
     */
    private class DetailGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        //单次点击事件
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //如果都是隐藏状态，那么都显示出来
            if (isTopHide && isToolHide) {
                showTool();
            } else if (!isToolHide && isTopHide) {
                //如果上面隐藏，下面显示，就显示上面
            } else if (!isTopHide && isToolHide) {
                //如果上面显示，下面隐藏，那么就显示下面
                showTool();
            } else {
                //都在显示，那么就都隐藏
                hideTool();
            }
            return super.onSingleTapConfirmed(e);
        }
    }


    /**
     * 隐藏工具栏
     */
    private void hideTool() {
        int startY = getWindow().getDecorView()
                .getHeight() - getStatusHeight(this);
        ObjectAnimator anim = ObjectAnimator.ofFloat(llDailyaskBottom, "y", startY - llDailyaskBottom.getHeight(),
                startY);
        anim.setDuration(TIME_ANIMATION);
        anim.start();
        isToolHide = true;

    }


    /**
     * 显示工具栏，代表的是底部的评论部分
     */
    private void showTool() {
        int startY = getWindow().getDecorView()
                .getHeight() - getStatusHeight(this);
        ObjectAnimator anim = ObjectAnimator.ofFloat(llDailyaskBottom, "y", startY,
                startY - llDailyaskBottom.getHeight());
        anim.setDuration(TIME_ANIMATION);
        anim.start();
        isToolHide = false;
    }


    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    private class DailyAskCommentAdapter extends BaseAdapter {

        private Comment comment;

        public DailyAskCommentAdapter(Comment comment) {
            this.comment = comment;
        }

        @Override
        public int getCount() {
            return comment.data.size();
        }

        @Override
        public Comment.CommentItem getItem(int position) {
            return comment.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AskCommentHolder holder;
            Comment.CommentItem item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_dailyask_listitem, parent, false);
                holder = new AskCommentHolder();
                convertView.setTag(holder);
                holder.username = (TextView) convertView.findViewById(R.id.tv_listdailyask_username);
                holder.content = (TextView) convertView.findViewById(R.id.tv_listdailyask_content);
                holder.pubtime = (TextView) convertView.findViewById(R.id.tv_listdailyask_pubtime);
            } else {
                holder = (AskCommentHolder) convertView.getTag();
            }

            final CharSequence no = "暂无";
            holder.username.setHint(no);
            holder.pubtime.setHint(no);
            holder.content.setHint(no);

            holder.username.setText(item.account);
            holder.pubtime.setText(item.comTime);
            holder.content.setText(item.comContent);

            return convertView;
        }

    }


    static class AskCommentHolder {
        TextView username;
        TextView content;
        TextView pubtime;
    }
}
