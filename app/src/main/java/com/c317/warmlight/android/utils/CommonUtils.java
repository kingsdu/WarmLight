package com.c317.warmlight.android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/7.
 */

public class CommonUtils {

    /**
     * @Description 是否是GIF图片（GIF OR Other）
     * @params
     * @author Du
     * @Date 2018/1/4 9:02
     **/
    public static boolean isLetter(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static void showToastShort(Context context,String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
