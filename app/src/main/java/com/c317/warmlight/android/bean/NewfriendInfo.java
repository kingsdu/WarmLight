package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/22.
 */

public class NewfriendInfo {


    public ArrayList<NewfriendInfo_Content>data;
    public ArrayList<NewfriendInfo_Content>truefrienddata;
    public static class NewfriendInfo_Content{

        public int friend_id;
        public String account;
        public String username;
        public Boolean isFriend;

        public int getFriend_id() {
            return friend_id;
        }
        public void setFriend_id(int friend_id) {
            this.friend_id = friend_id;
        }

        public String getAccount() {
            return account;
        }
        public void setAccount(String account) {
            this.account = account;
        }

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public Boolean getIsFriend() {
            return isFriend;
        }
        public void setIsFriend(Boolean isFriend) {
            this.isFriend = isFriend;
        }
    }
}
