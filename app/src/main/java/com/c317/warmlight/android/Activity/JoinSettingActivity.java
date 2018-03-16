package com.c317.warmlight.android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.c317.warmlight.android.R;
import com.c317.warmlight.android.common.AppConstants;
import com.c317.warmlight.android.utils.CacheUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/15.
 */

public class JoinSettingActivity extends Activity {


    @Bind(R.id.iv_back_me)
    ImageView ivBackMe;
    @Bind(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @Bind(R.id.et_date_contactpho)
    EditText etDateContactpho;//联系电话
    @Bind(R.id.tv_date_enrollstashowtime)
    TextView tvDateEnrollstashowtime;//报名开始时间
    @Bind(R.id.rl_date_enrollstatime)
    RelativeLayout rlDateEnrollstatime;
    @Bind(R.id.tv_date_enrollendshowtime)
    TextView tvDateEnrollendshowtime;//报名结束时间
    @Bind(R.id.rl_date_enrollendtime)
    RelativeLayout rlDateEnrollendtime;
    @Bind(R.id.btn_date_saveset)
    Button btnDataSaveset;
    StringBuilder selectTime = null;//用户设置时间设置

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_setting_aty);
        ButterKnife.bind(this);
        CacheUtils.cleanCache(JoinSettingActivity.this, AppConstants.ENROLLSETTING);//清除页面缓存
        ivBackMe.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("报名设置");
        ivBackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final int themeHoloLight = AlertDialog.THEME_HOLO_LIGHT;
        //报名开始时间
        rlDateEnrollstatime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime = new StringBuilder();
                Calendar day = Calendar.getInstance();
                DatePickerDialog dayDialog = new DatePickerDialog(JoinSettingActivity.this, themeHoloLight,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectTime.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                                Calendar time = Calendar.getInstance();
                                Dialog timeDialog = new TimePickerDialog(JoinSettingActivity.this, themeHoloLight, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        selectTime.append(hourOfDay + ":" + minute);
                                        tvDateEnrollstashowtime.setVisibility(View.VISIBLE);
                                        tvDateEnrollstashowtime.setText(selectTime);
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
        //报名结束时间
        rlDateEnrollendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime = new StringBuilder();
                Calendar day = Calendar.getInstance();
                DatePickerDialog dayDialog = new DatePickerDialog(JoinSettingActivity.this, themeHoloLight,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectTime.append(year + "-" + (month + 1) + "-" + dayOfMonth + " ");
                                Calendar time = Calendar.getInstance();
                                Dialog timeDialog = new TimePickerDialog(JoinSettingActivity.this, themeHoloLight, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        selectTime.append(hourOfDay + ":" + minute);
                                        tvDateEnrollendshowtime.setVisibility(View.VISIBLE);
                                        tvDateEnrollendshowtime.setText(selectTime);
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


        //保存
        btnDataSaveset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enrollInfo = judgeParamsIsNull();
                if (!TextUtils.isEmpty(enrollInfo)) {
                    CacheUtils.setCache(AppConstants.ENROLLSETTING, enrollInfo, JoinSettingActivity.this);
                    Toast.makeText(JoinSettingActivity.this, "保存成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(JoinSettingActivity.this, "报名设置不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * 判断界面输入参数是否为空
     *
     * @params
     * @author Du
     * @Date 2018/3/15 21:20
     **/
    private String judgeParamsIsNull() {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(tvDateEnrollstashowtime.getText())) {
            Toast.makeText(JoinSettingActivity.this, "报名开始时间不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else if (TextUtils.isEmpty(tvDateEnrollendshowtime.getText())) {
            Toast.makeText(JoinSettingActivity.this, "报名截至时间不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        } else {
            sb.append(tvDateEnrollstashowtime.getText());
            sb.append("|");
            sb.append(tvDateEnrollendshowtime.getText());
            return sb.toString();
        }
    }

}
