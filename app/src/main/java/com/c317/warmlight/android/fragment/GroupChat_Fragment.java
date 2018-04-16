package com.c317.warmlight.android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.c317.warmlight.android.Activity.CreateGroupActivity;
import com.c317.warmlight.android.Activity.GroupChatActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.GroupChatInfo;
import com.c317.warmlight.android.bean.NewfriendInfo;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/8.
 */

public class GroupChat_Fragment extends Fragment implements
        PullToRefreshBase.OnRefreshListener2<ListView> {

    @Bind(R.id.ll_mymessage_creatgroup)
    LinearLayout llMymessageCreatgroup;
    @Bind(R.id.lv_mymessage_groupchatlist)
    ListView lvMymessageGroupchatlist;

    private String account;
    private GroupChatInfo groupchatinfo;
    private ArrayList<GroupChatInfo.GroupChatInfo_Content> groupchatdata = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_mymessage_groupchat, container, false);
        ButterKnife.bind(this, view);
        account = UserManage.getInstance().getUserInfo(getActivity()).account;
        initData();
        llMymessageCreatgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void initData() {
        getDataFromServer();
        lvMymessageGroupchatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                GroupChatInfo.GroupChatInfo_Content groupChatinfo_content= groupchatdata.get(position);
                intent.putExtra("groupName", groupChatinfo_content.groupName);
                intent.putExtra("group_id", groupChatinfo_content.group_id);
                intent.putExtra("founder", groupChatinfo_content.founder);
                getActivity().startActivity(intent);
            }
        });
    }

    private void getDataFromServer() {
//        final String param = (String) SharedPrefUtility.getParam(mActivity, AppConstants.ISFRIEND, AppConstants.ISFRIEND);
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUP;
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                groupchatinfo = gson.fromJson(result, GroupChatInfo.class);
                for (int i = 0; i < groupchatinfo.data.size(); i++) {
                    if (!groupchatinfo.data.get(i).isSingle) {
                        groupchatdata.add(groupchatinfo.data.get(i));
                    }

                }
                GroupChatListAdapter groupchatlistAdapter = new GroupChatListAdapter();
//                singlechatlistAdapter.notifyDataSetChanged();
                lvMymessageGroupchatlist.setAdapter(groupchatlistAdapter);
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

    private class GroupChatListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return groupchatdata.size();
        }

        @Override
        public Object getItem(int position) {
            return groupchatdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_item_groupchat, null);
                holder = new ViewHolder();
                holder.tvgroupname = (TextView) convertView.findViewById(R.id.tv_groupchat_itemgroupname);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.civ_mymessage_circleImageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GroupChatInfo.GroupChatInfo_Content GroupChatInfo_Contents = groupchatdata.get(position);
            if(TextUtils.isEmpty(GroupChatInfo_Contents.picture)){
                Picasso.with(getActivity()).load(R.drawable.nopic1).into(holder.ivPic);
            }else{
                String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + GroupChatInfo_Contents.picture;
                Picasso.with(getActivity()).load(imageUrl).into(holder.ivPic);
            }
            holder.tvgroupname.setText(GroupChatInfo_Contents.groupName);
//            for(int i=0;i<truefrienddata.size();i++){
//            String picname = "icon/" + NewfriendInfo_Contents.account + "_thumbnail.jpg";
//            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
//            Uri uri = Uri.parse(imageUrl);
//            Picasso.with(getActivity()).invalidate(uri);
//            Picasso.with(getActivity()).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.ivPic);
//            }
            return convertView;
        }
    }
    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvgroupname;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {

    }
}
