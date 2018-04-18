package com.c317.warmlight.android.bean;

/**
 * Created by Administrator on 2018/4/17.
 */

public class Collect_Date_Details {

    public int code;
    public Collect_Date_Item data;

    public static class Collect_Date_Item{
        public String activity_id;
        public String title;
        public String picture;
        public String place;
        public String coordinate;
        public String startTime;
        public String endTime;
        public boolean isDel;
        public String lastTime;
        public int save_id;
    }
}
