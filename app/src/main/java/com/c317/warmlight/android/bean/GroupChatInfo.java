package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/8.
 */

public class GroupChatInfo {
    public ArrayList<GroupChatInfo_Content> data;
    public static class GroupChatInfo_Content{
        public String picture;
        public int group_id;
        public String groupName;
        public Boolean isSingle;
        public String founder;

        public String getPicture() {
            return picture;
        }
        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getGroup_id() {
            return group_id;
        }
        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public String getGroupName() {
            return groupName;
        }
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public Boolean getIsSingle() {
            return isSingle;
        }
        public void setIsSingle(Boolean isSingle) {
            this.isSingle = isSingle;
        }

        public String getFounder() {
            return founder;
        }
        public void setFounder(String founder) {
            this.founder = founder;
        }
    }
}
