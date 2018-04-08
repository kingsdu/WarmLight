package com.c317.warmlight.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/2.
 */

public class GroupNewsDTO{
    public ArrayList<GroupNewsDTO_Content> data;
    public static class GroupNewsDTO_Content{
        //是否为图片
        private Boolean isImage = false;
        // 该信息在数据库中的最后修改时间
        private String lastTime;
        // 账户名
        private String account;
        // 群名A
        private int group_id;
        // 群消息的唯一标识
        private int chat_id;
        //是否删除
        private Boolean isDelete = false;
        // 消息正文
        private String content;
        // 时间
        private String chatTime;
        // 是否为自己发送的消息
        public int isme=0 ;
        // 是否已读
        private boolean isRead;


//        @Override
//        public boolean equals(Object o){
//            GroupNewsDTO_Content b = (GroupNewsDTO_Content)o;
//            if(this.account.equals(b.getAccount())){
//                return true;
//            }
//            return false;
//        }

        public Boolean getIsImage() {
            return isImage;
        }

        public void setIsImage(Boolean isImage) {
            this.isImage = isImage;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public int getChat_id() {
            return chat_id;
        }

        public void setChat_id(int chat_id) {
            this.chat_id = chat_id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public Boolean getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Boolean isDelete) {
            this.isDelete = isDelete;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getChatTime() {
            return chatTime;
        }

        public void setChatTime(String chatTime) {
            this.chatTime = chatTime;
        }

//        public Boolean getIsme() {
//            return isme;
//        }
//
//        public void setIsme(Boolean isme) {
//            this.isme = isme;
//        }

        public int getIsme() {
            return isme;
        }

        public void setIsme(int isme) {
            this.isme = isme;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(int isRead) {
            if (isRead == 1) {
                this.isRead = true;
            } else {
                this.isRead = false;
            }
        }

        /**
         * SQLite数据库存储boolean类型为整型,需要转换
         */
        public int getReadStatus() {
            if (this.isRead) {
                return 1;
            } else {
                return 0;
            }
        }
        /**
         * SQLite数据库存储boolean类型为整型,需要转换
         */

    }


}
