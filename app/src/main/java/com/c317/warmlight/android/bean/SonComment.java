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
        public int commentID;
        public int comFor;
        public int comParentID;
        public int userID;
        public int toUserID;
        public String toUserAccount;
        public String userName;
        public String toUserName;
        public String comContent;
        public String comTime;
    }
}
