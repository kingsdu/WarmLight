package com.c317.warmlight.android.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/6.
 *
 * 用户信息的bean
 */

public class UserInfo {
    public UserInfo_content data;
    public static class UserInfo_content{
        /**
         * 用户ID
         */
        public int user_id;

        /**
         * 登陆账户
         */
        public String account;

        /**
         * 密码
         */
        public String password;

        /**
         * 用户名
         */
        public String username;

        /**
         * 头像地址
         */
        public String picture;

        /**
         * 性别
         */
        public int sex;

        /**
         * 生日
         */
        public String birthday;

        /**
         * 电子邮箱
         */
        public String email;


        /**
         * 个性签名
         */
        public String signature;

        public int getUser_id() {
            return user_id;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public String getPicture() {
            return picture;
        }

        public int getSex() {
            return sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getEmail() {
            return email;
        }

        public String getSignature() {
            return signature;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }




}
