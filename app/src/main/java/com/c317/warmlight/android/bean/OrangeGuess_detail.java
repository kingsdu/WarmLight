package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/1.
 */

public class OrangeGuess_detail {

    public OrangeGuess_content data;

    public static class OrangeGuess_content{
        public String proposer;
        public Date pubDate;
        public String pubCompany;
        public String isbn;
        public String bookIntr;
        public String authorIntr;
    }


}
