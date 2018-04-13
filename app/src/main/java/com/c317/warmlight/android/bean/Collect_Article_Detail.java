package com.c317.warmlight.android.bean;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Collect_Article_Detail {

    public int code;
    public Collect_Article_Item data;

    public static class Collect_Article_Item{
        public int article_id;
        public int save_id;
        public boolean isDelete;
        public String title;
        public String pictureURL;
        public String lastTime;
        public int isDel;//存储在前台数据库字段
    }
}
