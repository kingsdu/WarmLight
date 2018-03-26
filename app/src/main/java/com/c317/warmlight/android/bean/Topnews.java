package com.c317.warmlight.android.bean;

import java.util.ArrayList;

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
    }
}
