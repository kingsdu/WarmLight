package com.c317.warmlight.android.views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Comment;
import com.c317.warmlight.android.bean.SonComment;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.CommonUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2018/4/2.
 */

public class CommentItemView extends LinearLayout implements View.OnClickListener {

    private int mPosition;
    private Comment.CommentItem mData;


    private PopupWindow mMorePopupWindow;
    private int mShowMorePopupWindowWidth;
    private int mShowMorePopupWindowHeight;


    private ImageView commentporait;
    private TextView commentnickname;
    private TextView commenttime;
    private TextView commnetcontent;
    private ImageButton commentbutton;
    private LinearLayout commentlayout;


    private OnCommentListener mCommentListener;


    public CommentItemView(Context context) {
        super(context);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置评论接口
    public interface OnCommentListener {
        void onComment(int position, boolean flag);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        commentporait = (ImageView) findViewById(R.id.lv_comment_portrait);
        commentnickname = (TextView) findViewById(R.id.tv_comment_nickname);
        commnetcontent = (TextView) findViewById(R.id.tv_commnet_content);
        commenttime = (TextView) findViewById(R.id.tv_comment_commenttime);
        commentbutton = (ImageButton) findViewById(R.id.ib_comment_morebtn);
        commentlayout = (LinearLayout) findViewById(R.id.comment_layout);
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }


    public void setCommentListener(OnCommentListener l) {
        this.mCommentListener = l;
    }

    public void setData(Comment.CommentItem data) {
        mData = data;

        commentnickname.setText(data.userName);
        commnetcontent.setText(data.comContent);
        commenttime.setText(data.comTime);

        updateComment();

        commentbutton.setOnClickListener(this);
    }

    private void updateComment() {
        if (mData.userName != null) {
            commentlayout.removeAllViews();
            commentlayout.setVisibility(View.VISIBLE);
            //取二级评论数据
            String secondComUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETOTHERCOMMENT;
            RequestParams params = new RequestParams(secondComUrl);
            params.addParameter("commentID", mData.commentID);
            x.http().get(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    SonComment.SonCommentItem sonCommentItem = gson.fromJson(result, SonComment.SonCommentItem.class);
                    TextView t = new TextView(getContext());
                    t.setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                    t.setBackgroundColor(getResources().getColor(R.color.white));
                    t.setTextSize(16);
                    t.setTextColor(getResources().getColor(R.color.black));
                    t.setPadding(5, 2, 0, 3);
                    t.setLineSpacing(3, (float) 1.5);
                    t.setText(sonCommentItem.comContent);
                    commentlayout.addView(t);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.d("a",ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            commenttime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            commentlayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_comment_morebtn:
                showMore(v);//点击评论图片，弹出评论详情
                break;
            case R.id.tv_comment_comment://点击评论按钮，进行评论
                if (mCommentListener != null) {
                    mCommentListener.onComment(mPosition, true);

                    if (mMorePopupWindow != null && mMorePopupWindow.isShowing()) {
                        mMorePopupWindow.dismiss();
                    }
                }
                break;
            case R.id.tv_comment_like:
                //点赞
                break;
        }
    }

    private void showMore(View moreBtnView) {
        if (mMorePopupWindow == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = li.inflate(R.layout.comment_more, null, false);

            mMorePopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mMorePopupWindow.setOutsideTouchable(true);
            mMorePopupWindow.setTouchable(true);

            content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mShowMorePopupWindowWidth = content.getMeasuredWidth();
            mShowMorePopupWindowHeight = content.getMeasuredHeight();

            View parent = mMorePopupWindow.getContentView();

            TextView like = (TextView) parent.findViewById(R.id.tv_comment_like);
            TextView comment = (TextView) parent.findViewById(R.id.tv_comment_comment);

            like.setOnClickListener(this);
            comment.setOnClickListener(this);
        }

        if (mMorePopupWindow.isShowing()) {
            mMorePopupWindow.dismiss();
        } else {
            int heightMoreBtnView = moreBtnView.getHeight();

            mMorePopupWindow.showAsDropDown(moreBtnView, -mShowMorePopupWindowWidth,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
        }
    }


    public int getPosition() {
        return mPosition;
    }

    public void addComment() {
        updateComment();
    }
}
