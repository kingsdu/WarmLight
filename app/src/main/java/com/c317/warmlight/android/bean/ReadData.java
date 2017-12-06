package com.c317.warmlight.android.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class ReadData {

    public ArrayList<ReadDataMenuData> data;

    public static class ReadDataMenuData {
        public int id;
        public String title;
        public int type;
        public ArrayList<ReadData_Children> children;
    }

    public static class ReadData_Children{
        public int id;
        public String title;
        public int type;
        public String url;
    }
}
