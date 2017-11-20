package com.c317.warmlight.android.common;

/**
 * Created by Administrator on 2017/11/16.
 *
 * 配置程序中所有的接口请求地址
 */

public class AppNetConfig {

    public static final String HOST = "192.168.0.120";

    public static final String BASEURL = "http://"+ HOST +":8080/";

    public static final String LOGIN = BASEURL + "login";

    public static final String PRODUCT = BASEURL + "product";

    public static final String INDEX = BASEURL + "index";
}
