package com.c317.warmlight.android.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/1.
 *
 * 友约详情页面
 *
 */

public class DateNews_detalis {

    public DateNews_content data;
    public int code;

    public static class DateNews_content{
        public String activity_id;
        public String title;
        public String picture;
        public String content;
        public int readNum;
        public int agreeNum;
        public int commentNum;
        public int memberTotalNum;
        public int memberNum;
        public String startTime;
        public String endTime;
        public String beginTime;
        public String deadline;
        public String proposer;
        public String place;
        public String coordinate;
        public String telephone;
        public int type;
        public int group_id;
    }

}
