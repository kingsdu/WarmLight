package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/12.
 */

public class User_Comment_Info {

    public int code;
    public ArrayList<User_Comment_Details> data;

    public static class User_Comment_Details{
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
