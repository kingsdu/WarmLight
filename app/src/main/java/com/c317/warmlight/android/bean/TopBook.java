package com.c317.warmlight.android.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/12.
 */

public class TopBook {

    public TopBook_content data;
    public int code;

    public static class TopBook_content{

        public String title;
        public String author;
        public String pubCompany;
        public String pictureURL;
        public String isbn;
        public String bookIntr;
        public String authorIntr;
        public Date pubDate;
        public String proposer;

    }
}
