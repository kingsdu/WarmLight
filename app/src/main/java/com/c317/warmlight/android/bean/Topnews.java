package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/2.
 */

public class Topnews {
    public ArrayList<Topnews_menu> topnews;

    public static class Topnews_menu{
        public int id;
        public int type;
        public String imageurl;
        public String title;
        public String commnets;
    }
}
