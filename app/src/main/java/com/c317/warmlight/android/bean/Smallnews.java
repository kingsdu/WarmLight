package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/2.
 *
 * 小图新闻
 */

public class Smallnews {

    public Smallnews_Info data;

    public static class Smallnews_Info {
        public int total;
        public int page;
        public ArrayList<Smallnews_Detail> detail;
    }

    public static class Smallnews_Detail{
        public int article_id;
        public String title;
        public Date pubDate;
        public String source;
        public String introduce;
        public String pictureURL;
        public int readNum;
        public int agreeNum;
        public int isCollect;
    }
}
