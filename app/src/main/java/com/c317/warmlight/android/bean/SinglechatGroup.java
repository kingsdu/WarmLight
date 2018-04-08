package com.c317.warmlight.android.bean;

/**
 * Created by Administrator on 2018/4/2.
 */

public class SinglechatGroup {
    public SinglechatGroup_content data;
    public static class SinglechatGroup_content{
        /**
         * 群组表唯一标识
         */
        public int group_id;

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

    }

}
