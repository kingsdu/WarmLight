package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Collect_Date_Info {

    public int code;
    public ArrayList<Collect_Date_Details> data;

    public static class Collect_Date_Details{
        public int save_id;
        public String startTime;
        public String endTime;
        public String coordinate;
        public String place;
        public String picture;
        public String title;
        public String pictureURL;
        public boolean isDelete;
        public String lastTime;
    }

}
