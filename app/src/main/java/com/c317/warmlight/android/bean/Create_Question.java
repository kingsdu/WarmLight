package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/1.
 *
 * 创建问题页面
 */

public class Create_Question {

    public ArrayList<Question_Info> data;

    public static class Question_Info{
        public int question_id;
        public String title;
        public String content;
        public String pubDate;
        public String proposer;
        public int commentNum;
    }

}
