package com.tencent.qcloud.roomservice.pojo.Request;

public class DelAudienceReq {
    private String roomID = "";
    private String userID = "";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
