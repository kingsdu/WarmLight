package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/2.
 */

public class Smallnews {

    public ArrayList<Smallnews_menu> smallnews;

    public class Smallnews_menu {
        public int id;
        public int type;
        public String title;
        public String content;
        public int views;
        public String imageurl;
        public String user;
    }
}
