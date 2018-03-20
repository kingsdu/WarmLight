package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/2.
 *
 * 头条新闻
 */

public class Topnews {

    public ArrayList<Topnews_Info> data;

    public static class Topnews_Info{
        public String search_id;
        public String pictureURL;
        public String introduce;
        public String title;
        public String source;
        public int agreeNum;
        public int commentNum;
        public int readNum;
        public Date pubDate;
    }
}
