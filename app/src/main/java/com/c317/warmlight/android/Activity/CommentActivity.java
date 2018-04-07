package com.c317.warmlight.android.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Comment;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.views.CommentItemView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/28.
 */

public class CommentActivity extends AppCompatActivity implements View.OnClickListener,ListView.OnItemClickListener{

    @Bind(R.id.lv_read_comment)
    ListView lvReadComment;
    @Bind(R.id.et_read_input)
    EditText etReadInput;
    @Bind(R.id.btn_read_submit)
    Button btnReadSubmit;
    @Bind(R.id.ll_read_comment)
    LinearLayout llReadComment;
    String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETCOMMENT + AppNetConfig.PARAMETER;
    private String searchID;
    private Comment commentItem;

    private CommentAdapter commentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.read_comment_main);
        ButterKnife.bind(this);
        btnReadSubmit.setOnClickListener(this);
        lvReadComment.setOnItemClickListener(this);
        searchID = getIntent().getStringExtra("searchID");
        initData();
    }

    private void initData() {
        getCommentData(url);
    }

    private void getCommentData(String url) {
        RequestParams params = new RequestParams(url);
        params.addParameter("searchID", searchID);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                commentItem = gson.fromJson(result, Comment.class);
                if (commentItem.code == 200) {
                    commentAdapter = new CommentAdapter(CommentActivity.this);
                    lvReadComment.setAdapter(commentAdapter);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(CommentActivity.this,"评论失败");
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
        switch (v.getId()){
            case R.id.btn_read_submit:
                firstComment();
                break;


        }
    }


    /**
    * 某个评论被点击
    * @params
    * @author Du
    * @Date 2018/4/7 17:34
    **/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String userName = commentItem.data.get(position).userName;
        try {
            Date comDate = sdf.parse(commentItem.data.get(position).comTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }




    /**
    * 一级评论
    * @params
    * @author Du
    * @Date 2018/4/2 16:08
    **/
    private void firstComment() {
        String comment = etReadInput.getText().toString();
        if(!TextUtils.isEmpty(comment)){
            String insertUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ADDFIRSTCOMMENT;
            RequestParams params = new RequestParams(insertUrl);
            params.addParameter("searchID", searchID);
            params.addParameter("comContent", comment);
            params.addParameter("userID", UserManage.getInstance().getUserInfo(CommentActivity.this).user_id);
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Result retrunRes = gson.fromJson(result, Result.class);
                    if (retrunRes.code == 201) {
                        initData();
                        CommonUtils.showToastShort(CommentActivity.this, "评论成功");
                    }
                    etReadInput.setText("");
                    showInputLan(false);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    CommonUtils.showToastShort(CommentActivity.this, "评论失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else{
            CommonUtils.showToastShort(CommentActivity.this, "评论内容为空");
        }


    }


    private class CommentAdapter extends BaseAdapter implements CommentItemView.OnCommentListener {

        private Context context;
        private Map<Integer, CommentItemView> mCachedViews = new HashMap<>();

        public CommentAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return commentItem.data.size();
        }

        @Override
        public Comment.CommentItem getItem(int position) {
            return commentItem.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView != null) {
                view = convertView;
            } else {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.comment_item_view, null, false);//填充ListView
            }

            if (view instanceof CommentItemView) {
                //评论具体各部分填充数据
                Comment.CommentItem item = getItem(position);
                ((CommentItemView) view).setData(item);
                ((CommentItemView) view).setPosition(position);
                ((CommentItemView) view).setCommentListener(this);
                //根据位置，将View存起来，当下次评论时，以便取出
                cacheView(position, (CommentItemView) view);
            }
            return view;
        }

        private void cacheView(int position, CommentItemView view) {
            Iterator<Map.Entry<Integer, CommentItemView>> entries = mCachedViews.entrySet().iterator();

            while (entries.hasNext()) {

                Map.Entry<Integer, CommentItemView> entry = entries.next();
                if (entry.getValue() == view && entry.getKey() != position) {
                    mCachedViews.remove(entry.getKey());
                    break;
                }
            }
            mCachedViews.put(position, view);
        }

        @Override
        public void onComment(int position, boolean flag) {
            showCommentView(position, flag);
        }

        //弹出底部评论框，并监听发送按钮
        private void showCommentView(final int position, boolean flag) {
            showInputLan(flag);
            llReadComment.findViewById(R.id.btn_read_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText et = (EditText) llReadComment.findViewById(R.id.et_read_input);
                    String comment = et.getText().toString();

                    if (!TextUtils.isEmpty(comment)) {
                        //评论信息写入后台
                        String insertUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.ADDOTHERCOMMENT;
                        RequestParams params = new RequestParams(insertUrl);
                        params.addParameter("comParentID", commentItem.data.get(position).commentID);
                        params.addParameter("comFor", commentItem.data.get(position).commentID);
                        params.addParameter("userID", UserManage.getInstance().getUserInfo(CommentActivity.this).user_id);
                        params.addParameter("toUserID", commentItem.data.get(position).userID);
                        params.addParameter("comContent", comment);
                        x.http().post(params, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String result) {
                                Gson gson = new Gson();
                                Result retrunRes = gson.fromJson(result, Result.class);
                                if (retrunRes.code == 201) {
//                                    CommentItemView itemView = mCachedViews.get(position);//获取到用户评论的那个View
//                                    itemView.addComment(commentItem.data.get(position).commentID);
//                                    initData();
                                    btnReadSubmit.setOnClickListener(CommentActivity.this);
                                    CommonUtils.showToastShort(CommentActivity.this, "评论成功");
                                }
                                et.setText("");
                                showInputLan(false);
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
                }
            });
        }
    }


    private void showInputLan(boolean flag) {
        final boolean isFocus = flag;
        (new Handler()).postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        CommentActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    etReadInput.setFocusable(true);
                    etReadInput.requestFocus();
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(etReadInput.getWindowToken(), 0);
                }
            }
        }, 100);
    }


}