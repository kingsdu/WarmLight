package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/2.
 *
 * 小桔猜猜
 */

public class OrangeGuess {

    public OrangeGuess_Info data;

    public static class OrangeGuess_Info{
        public int total;
        public int page;
        public ArrayList<OrangeGuess_details> detail;
    }

    public static class OrangeGuess_details{
        public int book_id;
        public String title;
        public String author;
        public String pictureURL;
    }

}
