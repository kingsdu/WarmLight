package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 *
 *
 *  友约
 */

public class DateNews {

    public DateNews_Info data;

    public static class DateNews_Info {
        public int total;
        public int page;
        public ArrayList<DateNews_Detail> detail;
    }

    public static class DateNews_Detail{
        public String activity_id;
        public String title;
        public String content;
        public String picture;
        public int readNum;
        public int agreeNum;
        public int commentNum;
        public String endTime;
        public String startTime;
        public int memberNum;
        public String place;
        public int type;
        public int isCollect;
    }


}
