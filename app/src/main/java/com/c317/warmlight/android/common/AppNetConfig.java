package com.c317.warmlight.android.common;

import java.io.File;

/**
 * Created by Administrator on 2017/11/16.
 *
 * 配置程序中所有的接口请求地址
 */

public class AppNetConfig {

    public static final String HOST = "14g97976j3.51mypc.cn";

    public static final String BASEURL = "http://"+ HOST +":10759";

    public static final String TESTURL = "http://192.168.0.106:8000";


    /*******************************分模块信息*************************************/
    //有读
    public static final String READ = "youdu";

    //友约
    public static final String DATE = "youyue";

    //我的
    public static final String MY = "my";

    //所有图片
    public static final String PICTURE = "picture";

    //头条新闻
    public static final String TOPNEWS = "getTopics";

    //头条新闻详情
    public static final String TOPNEWS_DETAILS = "getTopicInfo";

    //友约
    public static final String ACTIVITY = "getActivities";

    //友约列表
    public static final String ACTIVITYLIST = "getActivityList";

    //文章列表
    public static final String ARTICLE = "getArtsList";

    //获取文章详情
    public static final String GETARTCONT = "getArtCont";

    //每日一问
    public static final String QUESTION = "getQuestionList";

    //小桔猜猜
    public static final String GUESS = "getBooksList";

    //创建友约
    public static final String CREATEACTIVITY = "createActivity";

    //上传图片
    public static final String UPLOADICON = "uploadIcon";

    //修改用户信息
    public static final String MODIFYUSER = "modifyUser";

    //获取用户信息
    public static final String GETUSERINFO = "getUserInfo";

    //头条信息
    public static final String GETTOPICINFO = "getTopicInfo";


    /*********************************基本操作符定义***************************************/
    public static final String SEPARATOR = File.separator;

    public static final String PARAMETER = "?";

    public static final String PAGE = "page";

    public static final String EQUAL = "=";

    public static final String SEARCH_ID ="search_id";

    public static final String ARTICLE_ID ="article_id";

    public static final String APP_SERVICE_PHONE ="15871466310";
//    public static final String HOST = "14g97976j3.51mypc.cn";
//
//    public static final String BASEURL = "http://"+ HOST +":8080/WarmLight/";
//
//    public static final String LOGIN = BASEURL + "login";
//
//    public static final String PRODUCT = BASEURL + "product";
//
//    public static final String INDEX = BASEURL + "index";
//
//
//    public static final String WARMBASEURL = "http://"+ HOST +":8080/zhbj/";
//
//    public static final String CATEGORY = WARMBASEURL + "warmLight.json";

//    public static final String LOGIN = BASEURL + "login";
//
//    public static final String PRODUCT = BASEURL + "product";
//
//    public static final String INDEX = BASEURL + "index";
//
//
//    public static final String WARMBASEURL = "http://"+ HOST +":10759/zhbj/";
//
//    public static final String CATEGORY = WARMBASEURL + "warmLight.json";




}
