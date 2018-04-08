package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.FriendName;
import com.c317.warmlight.android.bean.NewfriendInfo;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.SharedPrefUtility;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewFriendActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_topbar_right)
    TextView tvTopbarRight;
    @Bind(R.id.lv_newfriend_newfriendlist)
    ListView lvNewfriendNewfriendlist;

    private String account;
    private NewfriendInfo newfriendinfo;
    public String username;
    private ArrayList<NewfriendInfo.NewfriendInfo_Content> mcontent = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.mymessage_newfriend_aty);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("新朋友");
        tvTopbarRight.setText("添加");
        account = UserManage.getInstance().getUserInfo(NewFriendActivity.this).account;
        ivBackMe.setOnClickListener(this);
        tvTopbarRight.setOnClickListener(this);

        getDataFromServer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.tv_topbar_right:
                Intent intent = new Intent(NewFriendActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取用户新朋友（及相关好友：你的好友以及被人请求你你没答应的人）
     */
    private void getDataFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTFRIEND;
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                newfriendinfo = gson.fromJson(result, NewfriendInfo.class);


                NewfriendListAdapter newfriendlistAdapter = new NewfriendListAdapter();
//                newfriendlistAdapter.notifyDataSetChanged();
                lvNewfriendNewfriendlist.setAdapter(newfriendlistAdapter);
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

    /**
     * 获取该账号所有相关好友昵称
     */
    private void getusernameFromServer(final String friendaccount){
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.GETFRIENDNAME;
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
//                username= (String) resultInfo.data.get(friendaccount);
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

    public class NewfriendListAdapter extends BaseAdapter {

//        @Bind(R.id.btn_newfriend_itemisFriend)
//        Button btnNewfriendItemisFriend;

        @Override
        public int getCount() {
            return newfriendinfo.data.size();
        }

        @Override
        public Object getItem(int position) {
            return newfriendinfo.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(NewFriendActivity.this, R.layout.list_item_newfriend, null);
                holder = new ViewHolder();
                holder.tvusername = (TextView) convertView.findViewById(R.id.tv_newfriend_itemusername);
                holder.tvisFriend = (TextView) convertView.findViewById(R.id.tv_newfriend_itemisFriend);
                holder.btnisFriend = (Button) convertView.findViewById(R.id.btn_newfriend_itemisFriend);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final NewfriendInfo.NewfriendInfo_Content NewfriendInfo_Contents = newfriendinfo.data.get(position);
            getusernameFromServer(NewfriendInfo_Contents.account);
            holder.tvusername.setText(NewfriendInfo_Contents.account);
            if (NewfriendInfo_Contents.isFriend) {
                holder.tvisFriend.setVisibility(View.VISIBLE);
                holder.tvisFriend.setText("已同意");
                holder.btnisFriend.setVisibility(View.GONE);
            }
            if (!NewfriendInfo_Contents.isFriend) {
                holder.btnisFriend.setVisibility(View.VISIBLE);
                holder.btnisFriend.setText("同意");
                holder.tvisFriend.setVisibility(View.GONE);
            }

            holder.btnisFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AgreeNewFriend(NewfriendInfo_Contents.friend_id);
//                    SharedPrefUtility.setParam(NewFriendActivity.this, AppConstants.ISFRIEND, NewfriendInfo_Contents.isFriend);
                    finish();
                }
            });


            return convertView;
        }

        public void AgreeNewFriend(final int friend_id){
            RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTFRIEND);
            params.addParameter(AppConstants.FRIEND_ID, friend_id);
            x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //成功
                    Gson gson = new Gson();
                    Result resultInfo = gson.fromJson(result, Result.class);
                    if (resultInfo.code == 200) {
                        Toast.makeText(NewFriendActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewFriendActivity.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(NewFriendActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
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



    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvaccount;
        public TextView tvisFriend;
        public Button btnisFriend;
        public TextView tvusername;
    }
}
