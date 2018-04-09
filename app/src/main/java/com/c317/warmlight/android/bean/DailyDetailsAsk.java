package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/9.
 */

public class DailyDetailsAsk {

    public int code;
    public DailyAsk_detail data;

    public static class DailyAsk_detail{
        public int question_id;
        public String title;
        public String content;
        public Date pubDate;
        public String proposer;
        public int commentNum;
    }
}
