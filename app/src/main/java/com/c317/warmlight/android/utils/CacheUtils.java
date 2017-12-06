package com.c317.warmlight.android.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/12/4.
 *
 * 网络缓存工具
 */

public class CacheUtils {

    /**
     * @param url 为key
     * @param json 为value
     * @param ctx 保存在本地
     */
    public static void setCache(String url,String json,Context ctx){
        //若以文件缓存，则需要以MD5(url)为文件名，以json为文件内容
        PrefUtils.setString(ctx, url, json);
    }

    /**
     * @param url
     * @param ctx
     * @return 获取缓存
     */
    public static String getCache(String url,Context ctx){
        return PrefUtils.getString(ctx, url, null);
    }

}
