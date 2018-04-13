package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/12.
 *
 *
 */

public class Collect_Article_Info {

    public int code;
    public ArrayList<Collect_Article_Details> data;

    public static class Collect_Article_Details{
        public int save_id;
        public int article_id;
        public boolean isDelete;
        public String title;
        public String pictureURL;
        public String lastTime;
        public int isDel;//存储在前台数据库字段
    }

}
