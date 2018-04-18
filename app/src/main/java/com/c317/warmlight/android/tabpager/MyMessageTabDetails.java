package com.c317.warmlight.android.tabpager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.Activity.DateDetailActivity;
import com.c317.warmlight.android.Activity.GroupChatActivity;
import com.c317.warmlight.android.Activity.NewFriendActivity;
import com.c317.warmlight.android.Activity.PersonnalInfoActivity;
import com.c317.warmlight.android.Activity.SingleChatActivity;
import com.c317.warmlight.android.R;
import com.c317.warmlight.android.base.BaseMenuDetailPager;
import com.c317.warmlight.android.bean.NewfriendInfo;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.bean.SinglechatGroup;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.SharedPrefUtility;
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
 * Created by Administrator on 2018/3/21.
 */
@SuppressLint("ValidFragment")
public class MyMessageTabDetails extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {


    @Bind(R.id.lv_mymessage_chatlist)
    ListView lvMymessageChatlist;
    private String account;
    private Boolean isFriend;
    private NewfriendInfo newfriendinfo;
    private ArrayList<NewfriendInfo.NewfriendInfo_Content> truefrienddata = new ArrayList();
    public int group_id;
    public int Friend_id;
    public MyMessageTabDetails(Activity activity) {
        super(activity);
    }
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_mymessage_detail, null);
        ButterKnife.bind(this, view);
        account = UserManage.getInstance().getUserInfo(mActivity).account;

        return view;
    }


    public MyMessageTabDetails(){

    }

    public void initData() {
        getDataFromServer();
        lvMymessageChatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewfriendInfo.NewfriendInfo_Content newfriendInfo_content = truefrienddata.get(position);
                int Friend_id=newfriendInfo_content.friend_id;
                CreateSinglechatGroup(account,newfriendInfo_content.account,Friend_id);

            }
        });
    }

    private void getDataFromServer() {
//        final String param = (String) SharedPrefUtility.getParam(mActivity, AppConstants.ISFRIEND, AppConstants.ISFRIEND);
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTFRIEND;
        RequestParams params = new RequestParams(url);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                newfriendinfo = gson.fromJson(result, NewfriendInfo.class);
                for(int i=0;i<newfriendinfo.data.size();i++){
                    if(newfriendinfo.data.get(i).isFriend){
                        truefrienddata.add(newfriendinfo.data.get(i));
                    }

                }
                SingleChatListAdapter singlechatlistAdapter = new SingleChatListAdapter();
//                singlechatlistAdapter.notifyDataSetChanged();
                lvMymessageChatlist.setAdapter(singlechatlistAdapter);
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
     * 创建单聊群组
     * @param account
     * @param
     */
    private void CreateSinglechatGroup(String account, final String friendaccount, final int Friend_id){
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUP);
        params.addParameter("founder", account);
        params.addParameter("account", friendaccount);
        params.addParameter("isSingle",true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                SinglechatGroup singlechatgroup = gson.fromJson(result, SinglechatGroup.class);
                int group_id = singlechatgroup.data.group_id;
                Intent intent = new Intent(mActivity, GroupChatActivity.class);
                intent.putExtra("account", friendaccount);
                intent.putExtra("group_id", group_id);
                intent.putExtra("friend_id", Friend_id);
                mActivity.startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    private class SingleChatListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return truefrienddata.size();
        }

        @Override
        public Object getItem(int position) {
            return truefrienddata.get(position);
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
                convertView = View.inflate(mActivity, R.layout.list_item_singlechat, null);
                holder = new ViewHolder();
                holder.tvaccount = (TextView) convertView.findViewById(R.id.tv_mymessage_itemaccount);
                holder.ivPic= (ImageView) convertView.findViewById(R.id.civ_mymessage_circleImageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final NewfriendInfo.NewfriendInfo_Content NewfriendInfo_Contents = truefrienddata.get(position);
            holder.tvaccount.setText(NewfriendInfo_Contents.account);
//            for(int i=0;i<truefrienddata.size();i++){
            String picname = "icon/" + NewfriendInfo_Contents.account + "_thumbnail.jpg";
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(mActivity).invalidate(uri);
            Picasso.with(mActivity).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.ivPic);
//            }
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvaccount;
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
