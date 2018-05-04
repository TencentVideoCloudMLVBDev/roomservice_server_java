package com.tencent.qcloud.roomservice.pojo;

/*
 * 观众信息
 */
public class Audience {
    private String userID = "";
    private String userInfo = "";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
