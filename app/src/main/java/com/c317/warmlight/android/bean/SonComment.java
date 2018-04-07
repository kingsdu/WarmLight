package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/2.
 *
 */

public class SonComment {

    public ArrayList<SonCommentItem> data;
    public int code;

    public static class SonCommentItem{
        public String account;
        public String comTime;
        public int comParentID;
        public int userID;
        public String userName;
        public String toUserAccount;
        public String comContent;
        public int toUserID;
        public int commentID;
        public String toUserName;
    }
}
