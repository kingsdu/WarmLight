package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/2.
 *
 * 大图新闻
 */

public class Bignews {

    public Bignews_Info data;

    public static class Bignews_Info {
        public int total;
        public int page;
        public ArrayList<Bignews_Detail> detail;
    }

    public static class Bignews_Detail{
        public int article_id;
        public String title;
        public Date pub_date;
        public String source;
        public String introduce;
        public String pictureURL;
        public int read_num;
        public int agree_num;
    }
}
