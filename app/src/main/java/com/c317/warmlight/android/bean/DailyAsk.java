package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/29.
 *
 * 每日一问
 */

public class DailyAsk{

    public DailyAsk_info data;

    public static class DailyAsk_info{
        public int total;
        public int page;
        public ArrayList<DailyAsk_details> detail;
    }

    public static class DailyAsk_details{
        public int question_id;
        public String title;
        public String content;
        public Date pubDate;
        public String proposer;
        public int commentNum;
    }
}
