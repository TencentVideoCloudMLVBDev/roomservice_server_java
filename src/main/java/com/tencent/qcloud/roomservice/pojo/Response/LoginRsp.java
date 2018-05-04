package com.tencent.qcloud.roomservice.pojo.Response;

public class LoginRsp extends BaseRsp {
    private String userID = "";
    private String token = "";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
