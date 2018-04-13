package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/12.
 */

public class User_Comment_Info {

    public int code;
    public User_Comment_Details data;

    public static class User_Comment_Details{
        public ArrayList<String> article;
        public ArrayList<String> book;
        public ArrayList<String> activity;
        public ArrayList<String> question;
    }

}
