package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.bean.DateNews_detalis;
import com.c317.warmlight.android.bean.Result;
import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.common.AppNetConfig;
import com.c317.warmlight.android.common.Application_my;
import com.c317.warmlight.android.common.UserManage;
import com.c317.warmlight.android.utils.CacheUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/15.
 */

public class AddDateActivity extends Activity {


    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.tv_date_publicate)
    TextView tvDatePublicate;
    @Bind(R.id.et_date_inTitle)
    EditText etDateInTitle;//标题
    @Bind(R.id.et_date_inContent)
    EditText etDateInContent;//内容
    @Bind(R.id.iv_add_date)
    ImageView ivAddDate;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.iv_locate_date)
    ImageView ivLocateDate;
    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.iv_me_setting)
    ImageView ivMeSetting;
    @Bind(R.id.iv_read_collect)
    ImageView ivReadCollect;
    @Bind(R.id.iv_read_uncollect)
    ImageView ivReadUncollect;
    @Bind(R.id.tv_date_showtime)
    TextView tvDateShowtime;//开始时间
    @Bind(R.id.rl_date_starttime)
    RelativeLayout rlDateStarttime;
    @Bind(R.id.rl_date_inendtime)
    RelativeLayout rlDateInendtime;
    @Bind(R.id.rl_date_inplace)
    RelativeLayout rlDateInplace;
    @Bind(R.id.iv_date_instarttime)
    ImageView ivDateInstarttime;
    @Bind(R.id.rl_date_selplace)
    RelativeLayout rlDateSelplace;
    @Bind(R.id.et_date_inplace)
    EditText etDateInplace;//友约地点
    @Bind(R.id.rl_date_joinsetting)
    RelativeLayout rlDateJoinsetting;
    @Bind(R.id.rl_date_jointype)
    RelativeLayout rlDateJointype;
    @Bind(R.id.et_date_joinnum)
    EditText etDateJoinnum;//参加总人数
    @Bind(R.id.tv_date_showendtime)
    TextView tvDateShowendtime;//结束时间
    @Bind(R.id.tv_date_joinshowtype)
    TextView tvDateJoinshowtype;//类型设置
    @Bind(R.id.btn_date_release)
    Button btnDateRelease;
    @Bind(R.id.btn_date_uploadpho)
    Button btnDateUploadpho;

    StringBuilder selectTime = null;//用户设置时间设置
    int mDateType = 10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application_my.getInstance().addActivity(this);
        setContentView(R.layout.adddate_date_aty);
        ButterKnife.bind(this);
        //初始化顶部
        ivBackMe.setVisibility(View.VISIBLE);
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTopbarTitle.setText("新建友约");
        final int themeHoloLight = AlertDialog.THEME_HOLO_LIGHT;
        //开始时间设置监听
        rlDateStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime = new StringBuilder();
                Calendar day = Calendar.getInstance();
                DatePickerDialog dayDialog = new DatePickerDialog(AddDateActivity.this, themeHoloLight,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectTime.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                                Calendar time = Calendar.getInstance();
                                Dialog timeDialog = new TimePickerDialog(AddDateActivity.this, themeHoloLight, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        selectTime.append(hourOfDay + ":" + minute);
                                        tvDateShowtime.setVisibility(View.VISIBLE);
                                        tvDateShowtime.setText(selectTime);
                                    }
                                }, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true);
                                timeDialog.show();
                            }
                        }, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
                //当前年份-1 >= 年份 <= 当前年份+1
                dayDialog.getDatePicker().setMaxDate(((new Date().getTime() / 1000) + 60 * 60 * 24 * 365) * 1000);
                dayDialog.getDatePicker().setMinDate(((new Date().getTime() / 1000) - 60 * 60 * 24 * 365) * 1000);
                dayDialog.show();
            }
        });
        //结束时间设置
        rlDateInendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime = new StringBuilder();
                Calendar day = Calendar.getInstance();
                DatePickerDialog dayDialog = new DatePickerDialog(AddDateActivity.this, themeHoloLight,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectTime.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                                Calendar time = Calendar.getInstance();
                                Dialog timeDialog = new TimePickerDialog(AddDateActivity.this, themeHoloLight, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        selectTime.append(hourOfDay + ":" + minute);
                                        tvDateShowendtime.setVisibility(View.VISIBLE);
                                        tvDateShowendtime.setText(selectTime);
                                    }
                                }, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true);
                                timeDialog.show();
                            }
                        }, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
                //当前年份-1 >= 年份 <= 当前年份+1
                dayDialog.getDatePicker().setMaxDate(((new Date().getTime() / 1000) + 60 * 60 * 24 * 365) * 1000);
                dayDialog.getDatePicker().setMinDate(((new Date().getTime() / 1000) - 60 * 60 * 24 * 365) * 1000);
                dayDialog.show();
            }
        });

        //友约地点地图选择
        rlDateSelplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDateActivity.this, DateSelplaceActivity.class);
                startActivity(intent);
            }
        });

        //报名设置
        rlDateJoinsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDateActivity.this, JoinSettingActivity.class);
                startActivity(intent);
            }
        });

        //活动类型设置
        rlDateJointype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateType();
            }
        });

        //发布友约
        btnDateRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enrollInfo = getEnrollInfo();
                CacheUtils.cleanCache(AddDateActivity.this, AppConstants.ENROLLSETTING);//清除页面缓存
                if(!TextUtils.isEmpty(getUserAccount())){
                    if (!TextUtils.isEmpty(enrollInfo)) {
                        DateNews_detalis.DateNews_content newDateParams = getNewDateParams();
                        newDateParams.proposer = getUserAccount();
                        String[] enrollInfos = enrollInfo.split("\\|");
                        if (enrollInfos.length == 2) {
                            newDateParams.beginTime = enrollInfos[0];
                            newDateParams.deadline = enrollInfos[1];
                            saveNewDateToServer(newDateParams);
                        } else {
                            Toast.makeText(AddDateActivity.this, "报名设置数据不完整",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AddDateActivity.this, "请设置报名设置模块",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddDateActivity.this, "无法请求到用户",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 存储新建友约的信息到服务端
     *
     * @params
     * @author Du
     * @Date 2018/3/15 22:18
     **/
    private void saveNewDateToServer(DateNews_detalis.DateNews_content news_content) {
        String url = AppNetConfig.BASEURL + AppNetConfig.SEPARATOR + AppNetConfig.DATE + AppNetConfig.SEPARATOR + AppNetConfig.CREATEACTIVITY;
        RequestParams params = new RequestParams(url);
        params.addParameter("title", news_content.title);
        params.addParameter("content", news_content.content);
        params.addParameter("memberTotalNum", news_content.memberTotalNum);
        params.addParameter("startTime", news_content.startTime);
        params.addParameter("endTime", news_content.endTime);
        params.addParameter("beginTime", news_content.beginTime);
        params.addParameter("deadline", news_content.deadline);
        params.addParameter("place", news_content.place);
        params.addParameter("type", news_content.type);
        params.addParameter("proposer", news_content.proposer);
        if(!TextUtils.isEmpty(news_content.telephone)){
            params.addParameter("telephone", news_content.telephone);
        }
        if(!TextUtils.isEmpty(news_content.picture)){
            params.addParameter("picture", news_content.picture);
        }
        if(!TextUtils.isEmpty(news_content.coordinate)){
            params.addParameter("coordinate", news_content.coordinate);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Result resultInfo = gson.fromJson(result, Result.class);
                if(resultInfo.code == 201){
                    Toast.makeText(AddDateActivity.this, "发布成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    CacheUtils.cleanCache(AddDateActivity.this,AppConstants.ENROLLSETTING);
                }else {
                    Toast.makeText(AddDateActivity.this, resultInfo.desc.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(AddDateActivity.this, "发布失败",
                        Toast.LENGTH_SHORT).show();
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
     * 获取报名设置信息
     *
     * @params
     * @author Du
     * @Date 2018/3/15 22:16
     **/
    private String getEnrollInfo() {
        String enrollSetting = CacheUtils.getCache(AppConstants.ENROLLSETTING, AddDateActivity.this);
        if (!TextUtils.isEmpty(enrollSetting)) {
            return enrollSetting;
        } else {
            return null;
        }
    }

    /**
     * 获取新建友约详细信息
     *
     * @params
     * @author Du
     * @Date 2018/3/15 21:10
     **/
    private DateNews_detalis.DateNews_content getNewDateParams() {
        if (TextUtils.isEmpty(etDateInTitle.getText())) {
            Toast.makeText(AddDateActivity.this, "友约标题不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(etDateInContent.getText())) {
            Toast.makeText(AddDateActivity.this, "友约主题描述不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(tvDateShowtime.getText())) {
            Toast.makeText(AddDateActivity.this, "友约开始时间不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(tvDateShowendtime.getText())) {
            Toast.makeText(AddDateActivity.this, "友约结束时间不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(etDateInplace.getText())) {
            Toast.makeText(AddDateActivity.this, "友约地点不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (mDateType == 10) {
            Toast.makeText(AddDateActivity.this, "友约类型不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(etDateJoinnum.getText())) {
            Toast.makeText(AddDateActivity.this, "友约活动总人数不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else {
            DateNews_detalis.DateNews_content dateNews_content = new DateNews_detalis.DateNews_content();
            dateNews_content.title = etDateInTitle.getText().toString();
            dateNews_content.content = etDateInContent.getText().toString();
            dateNews_content.startTime = tvDateShowtime.getText().toString();
            dateNews_content.endTime = tvDateShowendtime.getText().toString();
            dateNews_content.place = etDateInplace.getText().toString();
            dateNews_content.type = mDateType;
            dateNews_content.memberTotalNum = Integer.valueOf(etDateJoinnum.getText().toString());
            return dateNews_content;
        }
    }


    /**
     * 设置活动类型
     *
     * @params
     * @author Du
     * @Date 2018/3/15 17:19
     **/
    private void setDateType() {
        final String items[] = {"知识分享", "书籍交流", "学术论坛", "社团活动", "其他"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tvDateJoinshowtype.setVisibility(View.VISIBLE);
                tvDateJoinshowtype.setText(items[which]);
                mDateType = which;
            }
        });
        builder.create().show();
    }


    public String getUserAccount(){
        UserInfo.UserInfo_content  userInfo_content = UserManage.getInstance().getUserInfo(this);
        return userInfo_content.getUsername();
    }

}
