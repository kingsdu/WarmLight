package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/16.
 */

public class AccountUsername {
    public ArrayList<AccountUsername_Content> data;
    public static class AccountUsername_Content{
        /**
         * 键
         */
        private String account;

        /**
         * 值
         */
        private String username;

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
    }

}
