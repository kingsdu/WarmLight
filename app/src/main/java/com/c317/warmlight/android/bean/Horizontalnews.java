package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/2.
 */

public class Horizontalnews {

    public ArrayList<Horizontalnews_menu> Horizontalnews;

    public class Horizontalnews_menu{
        public int id;
        public String type;
        public String title;
        public String content;
        public String views;
        public String imageurl;
        public String author;
        public String user;
    }
}
