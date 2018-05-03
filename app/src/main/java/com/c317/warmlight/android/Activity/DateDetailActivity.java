package com.c317.warmlight.android.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.Collect_Date_Details;
import com.c317.warmlight.android.bean.Collect_Date_Info;
import com.c317.warmlight.android.bean.DateNews;
import com.c317.warmlight.android.bean.DateNews_detalis;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.c317.warmlight.android.utils.CommonUtils;
import com.c317.warmlight.android.utils.SharedPrefUtility;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/2.
 * <p>
 * 友约详情页面
 */

public class DateDetailActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.iv_datePicture)
    ImageView ivDatePicture;
    @Bind(R.id.tv_dateTitle)
    TextView tvDateTitle;
    @Bind(R.id.iv_sign)
    ImageView ivSign;
    @Bind(R.id.tv_dateDate)
    TextView tvDateDate;
    @Bind(R.id.iv_browse)
    ImageView ivBrowse;
    @Bind(R.id.tv_readNum)
    TextView tvReadNum;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.tv_shareNum)
    TextView tvShareNum;
    @Bind(R.id.tv_dateContent)
    TextView tvDateContent;
    @Bind(R.id.ll_dashLine)
    LinearLayout llDashLine;
    @Bind(R.id.iv_clock)
    ImageView ivClock;
    @Bind(R.id.tv_clock)
    TextView tvClock;
    @Bind(R.id.iv_locate)
    ImageView ivLocate;
    @Bind(R.id.tv_locate)
    TextView tvLocate;
    @Bind(R.id.iv_attend)
    ImageView ivAttend;
    @Bind(R.id.tv_attend)
    TextView tvAttend;
    @Bind(R.id.iv_contact)
    ImageView ivContact;
    @Bind(R.id.tv_contact_1)
    TextView tvContact;
    @Bind(R.id.ll_bottomline)
    LinearLayout llBottomline;
    @Bind(R.id.iv_consult)
    ImageView ivConsult;
    @Bind(R.id.tv_consult)
    TextView tvConsult;
    @Bind(R.id.iv_mark)
    ImageView ivMark;
    @Bind(R.id.tv_mark)
    TextView tvMark;
    @Bind(R.id.tv_joinIn)
    TextView tvJoinIn;
    @Bind(R.id.ll_consult)
    LinearLayout llConsult;
    @Bind(R.id.ll_data_collect)
    LinearLayout llDataCollect;
    @Bind(R.id.ll_date_joinIn)
    LinearLayout llDateJoinIn;
    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.iv_all_comment)
    ImageView ivAllComment;
    private Activity mActivity;
    private String picUrl;
    private String mActivityid;
    private boolean iscollect;
    private String mAccount;
    private WarmLightDataBaseHelper dataBaseHelper;
    private String mTelephone;
    public int mGroup_id;
    private int mSave_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.data_details);
        ButterKnife.bind(this);
        //顶部图标
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("友约详情");
        ivAllComment.setVisibility(View.VISIBLE);
        ivAllComment.setOnClickListener(this);
        tvJoinIn.setOnClickListener(this);
        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(this);
        mAccount = UserManage.getInstance().getUserInfo(DateDetailActivity.this).account;
        initDate();
        ivBackMe.setOnClickListener(this);
        llConsult.setOnClickListener(this);

        tvJoinIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join(UserManage.getInstance().getUserInfo(DateDetailActivity.this).account, mGroup_id);
            }
        });
        llDataCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscollect) {
                    addDateCollect();
//                    unAddDateCollect();
                } else {
                    unAddDateCollect();
                }
            }
        });
    }


    private void initDate() {
        ectractPutEra();
        getCollectFromServer();
    }

    private void getDateDetails() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.GETACTIVITY;
        RequestParams params = new RequestParams(url);
        params.addParameter(AppConstants.ACTIVITY_ID, mActivityid);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews_detalis dateNews_detalis = gson.fromJson(result, DateNews_detalis.class);
                if (dateNews_detalis.code == 200) {
                    mTelephone = dateNews_detalis.data.telephone;
                    mGroup_id = dateNews_detalis.data.group_id;
                    setDataView(dateNews_detalis.data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(mActivity, "请求友约详情失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void getCollectFromServer() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTSAVE;
        RequestParams params = new RequestParams(url);
        params.addParameter(AppConstants.ACCOUNT, mAccount);
        params.addParameter(AppConstants.TYPE, "a");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Collect_Date_Info collect_date_info = gson.fromJson(result, Collect_Date_Info.class);
                if (collect_date_info.code == 400) {
                    iscollect = getIsCollect(collect_date_info, true);
                    getDateDetails();
                } else if (collect_date_info.code == 200) {
                    iscollect = getIsCollect(collect_date_info, false);
                    getDateDetails();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(mActivity, "获取网络收藏信息错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void unAddDateCollect() {
        if(mSave_id == 0){
            mSave_id = Integer.valueOf(dataBaseHelper.queryIsCollectDate_SaveID(mActivityid));
        }
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTSAVE;
        RequestParams params = new RequestParams(url);
        params.addParameter(AppConstants.SAVE_ID, mSave_id);
        x.http().request(HttpMethod.DELETE, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Result result_collect = gson.fromJson(result, Result.class);
                if(result_collect.code == 400){
                    Toast.makeText(DateDetailActivity.this, "SaveTable matching query does not exist", Toast.LENGTH_SHORT).show();
                }else{
                    dataBaseHelper.unUpdateCollectState(WarmLightDataBaseHelper.DATE_TABLENAME, mActivityid, WarmLightDataBaseHelper.DATE_ID, WarmLightDataBaseHelper.DATE_ISDEL);
                    tvMark.setText("收藏");
                    Toast.makeText(DateDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    iscollect = false;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DateDetailActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void addDateCollect() {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.READ + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTSAVE;
        RequestParams params = new RequestParams(url);
        params.addParameter(AppConstants.SAVE_CON, "a"+mActivityid);
        params.addParameter(AppConstants.ACCOUNT, UserManage.getInstance().getUserInfo(DateDetailActivity.this).account);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Collect_Date_Details collect_date_details = gson.fromJson(result, Collect_Date_Details.class);
                if (collect_date_details.code == 201) {
                    dataBaseHelper.InsertCollectInfoDate(collect_date_details);
                    dataBaseHelper.updateCollectState(WarmLightDataBaseHelper.DATE_TABLENAME, mActivityid, WarmLightDataBaseHelper.DATE_ID, WarmLightDataBaseHelper.DATE_ISDEL);
                    tvMark.setText("已收藏");
                    CommonUtils.showToastShort(DateDetailActivity.this, "收藏成功");
                    iscollect = true;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToastShort(DateDetailActivity.this, "收藏失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void ectractPutEra() {
        mActivityid = getIntent().getStringExtra("activity_id");
        mSave_id = getIntent().getIntExtra("save_id", 0);
        if(mSave_id == 0){
            mSave_id = Integer.valueOf(dataBaseHelper.queryIsCollectDate_SaveID(mActivityid));
        }
    }


    /**
     * 获取当前用户是否收藏的信息
     *
     * @params
     * @author Du
     * @Date 2018/3/13 22:22
     **/
    private boolean getIsCollect(Collect_Date_Info collect_date_info, boolean flag) {
        if (!flag) {
            for (int i = 0; i < collect_date_info.data.size(); i++) {
                if (mActivityid == collect_date_info.data.get(i).activity_id) {
                    mSave_id = collect_date_info.data.get(i).save_id;
                    return true;
                }
            }
        }
        String isCollect = dataBaseHelper.queryIsCollectDate_isDel(mActivityid);
        if(!isCollect.equals("0")){
            return true;
        }else{
            return false;
        }
    }


    private void setDataView(DateNews_detalis.DateNews_content dateNewsContent) {
        String dataTime = "活动时间：" + dateNewsContent.startTime + " - " + dateNewsContent.endTime.substring(dateNewsContent.endTime.indexOf(" ") + 1, dateNewsContent.endTime.length());
        String joinTime = "报名时间：" + dateNewsContent.beginTime + " - " + dateNewsContent.deadline.substring(dateNewsContent.deadline.indexOf(" ") + 1, dateNewsContent.deadline.length());
        String joinPeople = "已报名：" + dateNewsContent.memberNum + "/" + dateNewsContent.memberTotalNum + "人";
        if (TextUtils.isEmpty(dateNewsContent.picture)) {
            Picasso.with(mActivity).load(R.drawable.nopic1).into(ivDatePicture);
        } else {
            String pic = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + dateNewsContent.picture;
            Picasso.with(mActivity).load(pic).into(ivDatePicture);
        }
        tvDateTitle.setText(dateNewsContent.title);
        ivSign.setImageResource(R.drawable.sign);
        tvDateDate.setText(dataTime);
        ivBrowse.setImageResource(R.drawable.read_num);
        tvReadNum.setText(String.valueOf(dateNewsContent.readNum));
        ivShare.setImageResource(R.drawable.share);
        tvShareNum.setText(String.valueOf(dateNewsContent.commentNum));
        tvDateContent.setText(dateNewsContent.content);
        ivClock.setImageResource(R.drawable.time);
        tvClock.setText(joinTime);
        ivLocate.setImageResource(R.drawable.locate);
        tvLocate.setText("地点：" + dateNewsContent.place);
        ivAttend.setImageResource(R.drawable.join_people);
        tvAttend.setText(joinPeople);
        ivContact.setImageResource(R.drawable.consult_small);
        tvContact.setText("发起人：" + dateNewsContent.proposer);

        ivConsult.setImageResource(R.drawable.consult);
        tvConsult.setText("咨询");
        ivMark.setImageResource(R.drawable.mark);
        if (!iscollect) {
            tvMark.setText("收藏");
            tvMark.setTextColor(0xffBCBCBC);
        } else {
            tvMark.setText("已收藏");
            tvMark.setTextColor(0xff87CEEB);
        }
        tvJoinIn.setBackgroundResource(R.color.main_orange);
        tvJoinIn.setText("报名参加");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_consult:
                callPhone();
                break;
            case R.id.iv_all_comment:
                enterCommentAty();
                break;
            case R.id.iv_back_me:
                finish();
                break;
        }
    }

    /**
     * 进入评论页面
     *
     * @params
     * @author Du
     * @Date 2018/4/2 16:20
     **/
    private void enterCommentAty() {
        Intent intent = new Intent(DateDetailActivity.this, CommentActivity.class);
        intent.putExtra("searchID", "a" + mActivityid);
        startActivity(intent);
    }


    private void callPhone() {
        if (TextUtils.isEmpty(mTelephone)) {
            CommonUtils.showToastShort(getApplicationContext(), "新建友约时未输入咨询电话");
        } else {
            Uri uri = Uri.parse("tel:" + mTelephone);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void join(final String account, final int group_id) {
        RequestParams params = new RequestParams(AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.MY + AppNetConfig.SEPARATOR + AppNetConfig.ABOUTGROUPMEMBER);
        params.addParameter("account", account);
        params.addParameter("group_id", group_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
//                Gson gson = new Gson();
//                Result resultInfo = gson.fromJson(result, Result.class);
                Toast.makeText(DateDetailActivity.this, "报名成功，请关注“我的消息”中的“群聊", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DateDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
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
