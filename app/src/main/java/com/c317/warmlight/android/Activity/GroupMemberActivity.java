package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.GroupMemberInfo;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.google.gson.Gson;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/9.
 */

public class GroupMemberActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.lv_groupmember_isadminlist)
    ListView lvGroupmemberIsadminlist;
    @Bind(R.id.lv_groupmember_isnotadminlist)
    ListView lvGroupmemberIsnotadminlist;


    private int mGroup_id;
    private GroupMemberInfo groupmemberinfo;
    private ArrayList<GroupMemberInfo.GroupMemberInfo_Content> isnotadmindata = new ArrayList();
    private ArrayList<GroupMemberInfo.GroupMemberInfo_Content> isadmindata = new ArrayList();
    private ArrayList<GroupMemberInfo.GroupMemberInfo_Content> data = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.groupchat_groupmember_aty);
        ButterKnife.bind(this);
        ectractPutEra();
        //顶部按钮初始化
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("群成员");
        //监听设置
        ivBackMe.setOnClickListener(this);
        initData();
    }

    public void initData() {
        getDataFromServer();
    }

    private void ectractPutEra() {
        mGroup_id = getIntent().getIntExtra("group_id", mGroup_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
        }
    }

    /**
     * 获取群成员
     */
    private void getDataFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUPMEMBER;
        RequestParams params = new RequestParams(url);
        params.addParameter("group_id", mGroup_id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                groupmemberinfo = gson.fromJson(result, GroupMemberInfo.class);
                for (int i = 0; i < groupmemberinfo.data.size(); i++) {
                    if (groupmemberinfo.data.get(i).isFounder) {
                        isadmindata.add(groupmemberinfo.data.get(i));
                    }
                    if (groupmemberinfo.data.get(i).isAdmin == false) {
                        isnotadmindata.add(groupmemberinfo.data.get(i));
                    }
                }
                AdminListAdapter adminlistAdapter = new AdminListAdapter();
                IsnotAdminListAdapter isnotadminlistAdapter = new IsnotAdminListAdapter();
                lvGroupmemberIsadminlist.setAdapter(adminlistAdapter);
                lvGroupmemberIsnotadminlist.setAdapter(isnotadminlistAdapter);
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
     * 创建群主、管理员列表适配器
     */
    private class AdminListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return isadmindata.size();
        }

        @Override
        public Object getItem(int position) {
            return isadmindata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(GroupMemberActivity.this, R.layout.list_item_groupmember, null);
                holder = new ViewHolder();
                holder.showName = (TextView) convertView.findViewById(R.id.tv_groupmember_itemshowName);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.civ_groupmember_circleImageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            final GroupMemberInfo.GroupMemberInfo_Content groupMemberInfo_Contents = isadmindata.get(position);
            holder.showName.setText(isadmindata.get(position).showName);
//            for(int i=0;i<truefrienddata.size();i++){
            String picname = "icon/" + isadmindata.get(position).account + "_thumbnail.jpg";
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(GroupMemberActivity.this).invalidate(uri);
            Picasso.with(GroupMemberActivity.this).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.ivPic);
            return convertView;
        }
    }

    private class IsnotAdminListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return isnotadmindata.size();
        }

        @Override
        public Object getItem(int position) {
            return isnotadmindata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(GroupMemberActivity.this, R.layout.list_item_groupmember, null);
                holder = new ViewHolder();
                holder.showName = (TextView) convertView.findViewById(R.id.tv_groupmember_itemshowName);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.civ_groupmember_circleImageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GroupMemberInfo.GroupMemberInfo_Content groupMemberInfo_Contents = isnotadmindata.get(position);
            holder.showName.setText(groupMemberInfo_Contents.showName);
//            for(int i=0;i<truefrienddata.size();i++){
            String picname = "icon/" + groupMemberInfo_Contents.account + "_thumbnail.jpg";
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(GroupMemberActivity.this).invalidate(uri);
            Picasso.with(GroupMemberActivity.this).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.ivPic);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView showName;
    }

}
