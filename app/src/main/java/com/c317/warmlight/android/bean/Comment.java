package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/1.
 */

public class Comment {

    public ArrayList<CommentItem> data;
    public int code;

    public static class CommentItem{
        public String comContent;
        public int commentID;
        public String searchID;
        public String comTime;
        public String userID;
        public String userName;
        public String agreeNum;
    }

}
