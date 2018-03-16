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
import com.c317.warmlight.android.bean.DateNews_detalis;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.utils.WarmLightDataBaseHelper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
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
    private Activity mActivity;
    private String picUrl;
    private String title;
    private String content;
    private String readNum;
    private String agreeNum;
    private String activity_id;
    private String url;
    private boolean iscollect;

    private WarmLightDataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_details);
        ButterKnife.bind(this);
        picUrl = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.PICTURE + AppNetConfig.SEPARATOR + getIntent().getStringExtra("picUrl");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        readNum = getIntent().getStringExtra("readNum");
        agreeNum = getIntent().getStringExtra("agreeNum");
        activity_id = getIntent().getStringExtra("activity_id");
        url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + "getActivity" + AppNetConfig.PARAMETER + "activity_id=" + activity_id;
        dataBaseHelper = WarmLightDataBaseHelper.getDatebaseHelper(this);
        getDataFromServer();
        //查询当前用户是否收藏
        iscollect = getIsCollect(activity_id);
        llConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + 1008611);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                startActivity(intent);
            }
        });

        llDataCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!iscollect){
                    dataBaseHelper.updateCollectState(WarmLightDataBaseHelper.DATE_TABLENAME,activity_id,WarmLightDataBaseHelper.DATE_ID,WarmLightDataBaseHelper.DATE_ISCOLLECT);
                    tvMark.setText("已收藏");
                    Toast.makeText(DateDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                    iscollect = true;
                }else{
                    dataBaseHelper.unUpdateCollectState(WarmLightDataBaseHelper.DATE_TABLENAME,activity_id,WarmLightDataBaseHelper.DATE_ID,WarmLightDataBaseHelper.DATE_ISCOLLECT);
                    tvMark.setText("收藏");
                    Toast.makeText(DateDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    iscollect = false;
                }
            }
        });
    }

    /**
    * 获取当前用户是否收藏的信息
    * @params
    * @author Du
    * @Date 2018/3/13 22:22
    **/
    private boolean getIsCollect(String activity_id) {
        String isCollect = dataBaseHelper.queryIsCollect(WarmLightDataBaseHelper.DATE_TABLENAME, activity_id, WarmLightDataBaseHelper.DATE_ID);
        if(TextUtils.isEmpty(isCollect)){
            dataBaseHelper.InsertCollectInfo(WarmLightDataBaseHelper.DATE_TABLENAME,activity_id,"0",WarmLightDataBaseHelper.DATE_ID,WarmLightDataBaseHelper.DATE_ISCOLLECT);
        }else{
            if(isCollect.equals("1")){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DateNews_detalis dateNews_detalis = gson.fromJson(result, DateNews_detalis.class);
                setDataView(dateNews_detalis.data);
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


    private void setDataView(DateNews_detalis.DateNews_content dateNewsContent) {
        String dataTime = "活动时间：" + dateNewsContent.startTime + " - " + dateNewsContent.endTime.substring(dateNewsContent.endTime.indexOf(" ") + 1, dateNewsContent.endTime.length());
        String joinTime = "报名时间：" + dateNewsContent.beginTime + " - " + dateNewsContent.deadline.substring(dateNewsContent.deadline.indexOf(" ") + 1, dateNewsContent.deadline.length());
        String joinPeople = "已报名：" + dateNewsContent.memberNum + "/" + dateNewsContent.memberTotalNum + "人";
        Picasso.with(mActivity).load(picUrl).into(ivDatePicture);
        tvDateTitle.setText(title);
        ivSign.setImageResource(R.drawable.sign);
        tvDateDate.setText(dataTime);
        ivBrowse.setImageResource(R.drawable.read_num);
        tvReadNum.setText(String.valueOf(dateNewsContent.readNum));
        ivShare.setImageResource(R.drawable.share);
        tvShareNum.setText(String.valueOf(dateNewsContent.commentNum));
        tvDateContent.setText(content);
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
        if(!iscollect){
            tvMark.setText("收藏");
            tvMark.setTextColor(0xffBCBCBC);
        }
        else {
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
            default:
                break;
        }
    }


    private void callPhone() {
        Uri uri = Uri.parse("tel:" + 1008611);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            startActivity(intent);
            return;
        }
    }
}
