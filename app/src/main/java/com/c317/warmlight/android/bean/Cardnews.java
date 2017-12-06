package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */

public class Cardnews {

    public ArrayList<Cardnews_menu> cardnews;

    public static class Cardnews_menu {
        public int id;
        public int type;
        public String title;
        public String content;
        public String star;
        public String compliment;
        public String imageurl;
        public String user;
    }

//    public static class Comment {
//        public int id;
//        public String comment;
//        public User user;
//    }

}
