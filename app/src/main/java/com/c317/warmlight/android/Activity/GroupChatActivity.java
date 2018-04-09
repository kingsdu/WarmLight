package com.c317.warmlight.android.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.GroupNewsDTO;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.tabpager.DateTabDetails;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/1.
 */

public class GroupChatActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.mylv)
    PullToRefreshListView mylv;
    @Bind(R.id.et_sendwords)
    EditText etSendwords;
    @Bind(R.id.button_sendchat)
    Button buttonSendchat;
    @Bind(R.id.chat_more)
    ImageView chatMore;
    @Bind(R.id.imageView11)
    ImageView imageView11;
    @Bind(R.id.imageView21)
    ImageView imageView21;
    @Bind(R.id.imageView31)
    ImageView imageView31;
    @Bind(R.id.imageView12)
    ImageView imageView12;
    @Bind(R.id.imageView22)
    ImageView imageView22;
    @Bind(R.id.imageView32)
    ImageView imageView32;
    @Bind(R.id.imageView13)
    ImageView imageView13;
    @Bind(R.id.imageView23)
    ImageView imageView23;
    @Bind(R.id.imageView33)
    ImageView imageView33;
    @Bind(R.id.imageView14)
    ImageView imageView14;
    @Bind(R.id.imageView24)
    ImageView imageView24;
    @Bind(R.id.imageView34)
    ImageView imageView34;
    @Bind(R.id.imageView15)
    ImageView imageView15;
    @Bind(R.id.imageView25)
    ImageView imageView25;
    @Bind(R.id.imageView35)
    ImageView imageView35;
    @Bind(R.id.imageView16)
    ImageView imageView16;
    @Bind(R.id.imageView26)
    ImageView imageView26;
    @Bind(R.id.imageView36)
    ImageView imageView36;
    @Bind(R.id.imageView17)
    ImageView imageView17;
    @Bind(R.id.imageView27)
    ImageView imageView27;
    @Bind(R.id.imageView37)
    ImageView imageView37;
    @Bind(R.id.uploadfi)
    Button uploadfi;
    @Bind(R.id.uploadpic)
    Button uploadpic;
    @Bind(R.id.emojibox)
    LinearLayout emojibox;
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.iv_groupchat_me)
    ImageView ivGroupchatMe;
    private static final String TAG = "GroupChatActivity";
    private WarmLightDataBaseHelper dataBaseHelper;
    private int more = 0;
    private String mAccount;
    private String mGroupName;
    private int mGroup_id;
    private int group_id;
    private int mFriend_id;
    private int friend_id;
    private String account;

    private List<GroupNewsDTO.GroupNewsDTO_Content> chatList;
    private ChatItemAdapter adapter;
    private GroupNewsDTO groupnewsDTO;
    private Handler myHandler;
    private ListView actuaListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.mymessage_groupchat_aty);
        ButterKnife.bind(this);
        ectractPutEra();
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText(mGroupName);
        emojibox.setVisibility(View.GONE);
        ivGroupchatMe.setVisibility(View.VISIBLE);

        account = UserManage.getInstance().getUserInfo(GroupChatActivity.this).account;
//        group_id=mGroup_id;
        ivBackMe.setOnClickListener(this);
        ivGroupchatMe.setOnClickListener(this);
        etSendwords.setOnClickListener(this);
        buttonSendchat.setOnClickListener(this);
        chatList = new ArrayList<GroupNewsDTO.GroupNewsDTO_Content>();


        adapter = new ChatItemAdapter(this, chatList);
        actuaListView = mylv.getRefreshableView();
        actuaListView.setAdapter(adapter);
        myHandler = new MyHandler();
        getChatMessage();
        loadChats();
    }

    private void ectractPutEra() {
        mGroupName = getIntent().getStringExtra("groupName");
        mGroup_id = getIntent().getIntExtra("group_id",group_id);
        mFriend_id = getIntent().getIntExtra("friend_id",friend_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_me:
                finish();
                break;
            case R.id.et_sendwords:
                /* 隐藏表情框 */
                findViewById(R.id.emojibox).setVisibility(View.GONE);
                more = 0;
                break;
            case R.id.button_sendchat:
                sendWords();
                findViewById(R.id.emojibox).setVisibility(View.GONE);
                break;
            case R.id.iv_groupchat_me:
                Intent intent = new Intent(GroupChatActivity.this, GroupChatSettingAty.class);
                intent.putExtra("groupName", mGroupName);
                startActivity(intent);
                break;
        }
    }

    private void sendWords(){
        if (TextUtils.isEmpty(etSendwords.getText())) {
            Toast.makeText(GroupChatActivity.this, "请输入聊天内容",
                    Toast.LENGTH_LONG).show();
        }else {
            String content_str = etSendwords.getText().toString();
            etSendwords.setText("");
            requestsendWords(account,content_str);
            findViewById(R.id.emojibox).setVisibility(View.GONE);
            more = 0;
        }

    }

    /**
     * 发送聊天信息
     */
    private void requestsendWords(final String account,String content_str){
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTCHAT);
        params.addParameter("account", account);
        params.addParameter("content", content_str);
        params.addParameter("group_id", mGroup_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if (resultInfo.code == 201) {
                    getChatMessage();

                    Toast.makeText(GroupChatActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupChatActivity.this, resultInfo.desc, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(GroupChatActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
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
     * 得到聊天信息（查询聊天信息）
     */
    private void getChatMessage(){
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTCHAT);
        params.addParameter("account", account);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                groupnewsDTO = gson.fromJson(result, GroupNewsDTO.class);
                ArrayList<GroupNewsDTO.GroupNewsDTO_Content> groupNewsDTO_contents = new ArrayList<GroupNewsDTO.GroupNewsDTO_Content>();
                GroupNewsDTO.GroupNewsDTO_Content groupNewsDTO_content;
//                GroupNewsDTO.GroupNewsDTO_Content groupNewsDTO_content=new GroupNewsDTO.GroupNewsDTO_Content();
                for(int i=0;i<groupnewsDTO.data.size();i++){
                    groupNewsDTO_content = new GroupNewsDTO.GroupNewsDTO_Content();
                    groupNewsDTO_content.setChat_id(groupnewsDTO.data.get(i).getChat_id());
                    groupNewsDTO_content.setLastTime(groupnewsDTO.data.get(i).getLastTime());
                    groupNewsDTO_content.setAccount(groupnewsDTO.data.get(i).getAccount());
                    groupNewsDTO_content.setGroup_id(groupnewsDTO.data.get(i).getGroup_id());
                    groupNewsDTO_content.setChatTime(groupnewsDTO.data.get(i).getChatTime());
                    groupNewsDTO_content.setContent(groupnewsDTO.data.get(i).getContent());
                    groupNewsDTO_contents.add(groupNewsDTO_content);
//                    dataBaseHelper.InsertCollectInfoDate(groupnewsDTO.data.get(i));
                }
                adapter.notifyDataSetChanged();
                dataBaseHelper.batchInsertGroupNews(groupNewsDTO_contents);
//                adapter.notifyDataSetChanged();
//                rightorleft();
//
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
     * 加载聊天列表
     */
    private void loadChats(){
        // 加载已读信息
        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(GroupChatActivity.this);
        chatList=dataBaseHelper.queryMultiGroupNewsRead(mGroup_id);
        if(chatList.size()>0){
            adapter.clear();
            rightorleft();
            adapter.addAll(chatList);
//            mylv.onRefreshComplete();
        }
        // 开启线程加载未读信息
        new Thread(){
            @Override
            public void run(){
                while (true){
                    List<GroupNewsDTO.GroupNewsDTO_Content> list= dataBaseHelper
                            .queryMultiGroupNewsUnRead(mGroup_id);
                    if (list.size() > 0){
                        for (int i = 0; i < list.size(); i++){
                            if (!chatList.contains(list.get(i))) {
                                chatList.add(list.get(i));
                            }
                        }
                        rightorleft();

                        myHandler.sendEmptyMessage(1);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "sleep error!");
                            e.printStackTrace();
                        }

                    }
                }
            }
        }.start();

    }

    /**
     * 判断信息是自己还是他人发的
     */
    private void rightorleft(){
        for (int i = 0; i < chatList.size(); i++) {
            if (chatList.get(i).getAccount()
                    .equals(account)) {
                chatList.get(i).isme=1;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            adapter.clear();
            adapter.addAll(chatList);
//            mylv.onRefreshComplete();
            // 更新未读为已读
            dataBaseHelper.updataGroupNews(mGroup_id);
        }

    }

    private class ChatItemAdapter extends BaseAdapter{

        private Context context;
        private List<GroupNewsDTO.GroupNewsDTO_Content> chatList;
        public ChatItemAdapter(Context context, List<GroupNewsDTO.GroupNewsDTO_Content> chatList) {
            this.context = context;
            this.chatList = chatList;
        }

        @Override
        public int getCount() {
            return chatList.size();
        }

        @Override
        public Object getItem(int position) {
            return chatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //重用ListView
            ViewHolder holder= null;
            GroupNewsDTO.GroupNewsDTO_Content chat = chatList.get(position);

//            if (convertView == null) {
            if(chat.getIsme() == 1){
                convertView = View.inflate(GroupChatActivity.this, R.layout.chat_item_right, null);
            }else {
                convertView = View.inflate(GroupChatActivity.this, R.layout.chat_item, null);
            }
            holder = new ViewHolder();
            holder.tvContent= (TextView) convertView.findViewById(R.id.tvcontent);
            holder.tvAccount = (TextView) convertView.findViewById(R.id.tvaccount);
            holder.tvSendtime = (TextView) convertView.findViewById(R.id.tvconttime);
            holder.ivpic = (ImageView) convertView.findViewById(R.id.civ_chat_circleImageView);
            holder.iv_sendpic = (ImageView) convertView.findViewById(R.id.iv_sendpic);
//                Pattern mPatternPic = Pattern.compile("\\&(.*?)\\&");
//                Matcher matcherPic = mPatternPic.matcher(chat.getContent());
//                int ContentAfterPic;
//                if(matcherPic.find()) {
//                    MyTool.strToPic(holder.iv_sendpic, context, chat.getContent());
//                    holder.tvContent.setText("");
//                    holder.tvContent.setVisibility(View.GONE);
//                }else{
//                    holder.tvContent.setText(MyTool.strToSmiley(context,chat.getContent(),60));
//
//                }
            convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }

//            holder.tvAccount.setText(chat.getUserName());
            holder.tvSendtime.setText(chat.getChatTime().substring(0, 19));
            holder.tvContent.setText(chat.getContent());
            String picname = "icon/" + chat.getAccount() + "_thumbnail.jpg";
            String imageUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + picname;
            Uri uri = Uri.parse(imageUrl);
            Picasso.with(GroupChatActivity.this).invalidate(uri);
            Picasso.with(GroupChatActivity.this).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.ivpic);
            return convertView;
        }
        public void addAll(List<GroupNewsDTO.GroupNewsDTO_Content> data) {
            this.chatList.addAll(data);
            notifyDataSetChanged();
        }
        public void clear() {
            chatList.clear();
            notifyDataSetChanged();
        }

        private class ViewHolder {
            private TextView tvContent;
            private TextView tvAccount;
            private TextView tvSendtime;
            private ImageView ivpic;
            private ImageView iv_sendpic;
        }
    }



}
