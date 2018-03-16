package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/1.
 *
 * 书籍信息创建
 */

public class Create_OrangeGuess {

    public ArrayList<Create_OrangeGuess_details> data;

    public static class Create_OrangeGuess_details{
        public String title;
        public String author;
        public String isbn;
        public String proposer;
        public String pubCompany;
        public String pictureURL;
        public String bookIntr;
        public String authorIntr;
    }
}
