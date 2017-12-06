package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/2.
 */

public class Bignews {

    public ArrayList<Bignews_menu> bignews;

    public class Bignews_menu{
        public int id;
        public String type;
        public String title;
        public String content;
        public String star;
        public String compliment;
        public String imageurl;
    }
}
