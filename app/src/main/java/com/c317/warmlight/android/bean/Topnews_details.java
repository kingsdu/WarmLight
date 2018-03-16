package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/29.
 *
 * 头条新闻详情
 */

public class Topnews_details {

    public Topnews_content data;

    public static class Topnews_content{
        public String title;
        public String author;
        public String isbn;
        public String pubCompany;
        public String pictureURL;
        public String authorIntr;
        public String bookIntr;
        public Date pubDate;
        public String proposer;
    }

}
