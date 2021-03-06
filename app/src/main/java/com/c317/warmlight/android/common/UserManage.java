package com.c317.warmlight.android.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.c317.warmlight.android.bean.UserInfo;
import com.c317.warmlight.android.utils.CacheUtils;

import static android.provider.UserDictionary.Words.APP_ID;

/**
 * Created by Administrator on 2018/3/6.
 *
 * 保存用户信息的管理类
 */

public class UserManage {


    private static UserManage instance;

    private UserManage() {
    }

    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }


    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, String account,int user_id) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ACCOUNT", account);
        editor.putInt("USER_ID", user_id);
        editor.commit();
    }


    /**
     * 获取用户信息model
     *
     * @param context
     * @param
     * @param
     */
    public UserInfo.UserInfo_content getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo.UserInfo_content  userInfo_content = new UserInfo.UserInfo_content();
        userInfo_content.setAccount(sp.getString("ACCOUNT", ""));
        userInfo_content.setUser_id(sp.getInt("USER_ID",0));
        return userInfo_content;
    }

    /**
     * userInfo中是否有数据
     */
    public boolean hasUserInfo(Context context) {
        UserInfo.UserInfo_content  userInfo_content = getUserInfo(context);
        if (userInfo_content.account != null) {
            if ((!TextUtils.isEmpty(userInfo_content.getAccount())) && (!TextUtils.isEmpty(userInfo_content.getPassword()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }



    /**
     * 删除用户存储信息
     * */
    public void deleteUserInfo(Context context,String url) {
//        instance.saveUserInfo();
        CacheUtils.cleanCache(context,url);
    }


}
