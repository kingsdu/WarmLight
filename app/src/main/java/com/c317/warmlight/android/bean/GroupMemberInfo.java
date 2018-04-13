package com.c317.warmlight.android.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/10.
 */

public class GroupMemberInfo {
    public ArrayList<GroupMemberInfo_Content> data= new ArrayList();
    public static class GroupMemberInfo_Content{
        public String showName;
        public String account;
        public Boolean isFounder;
        public Boolean isAdmin;
        public String getShowName() {
            return showName;
        }
        public void setShowName(String showName) {
            this.showName = showName;
        }
        public String getAccount() {
            return account;
        }
        public void setAccount(String account) {
            this.account = account;
        }
        public Boolean getIsFounder() {
            return isFounder;
        }
        public void setIsFounder(Boolean isFounder) {
            this.isFounder = isFounder;
        }
        public Boolean getIsAdmin() {
            return isAdmin;
        }
        public void setIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
        }
    }
}
