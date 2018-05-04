package com.tencent.qcloud.roomservice.pojo;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * 推流者信息
 */
public class Pusher {
    private String userID = "";
    private String userName = "";
    private String userAvatar = "";
    private String pushURL = "";
    private String streamID = "";
    private String accelerateURL = "";
    private long timestamp = 0;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @JsonIgnore
    public String getPushURL() {
        return pushURL;
    }

    public void setPushURL(String pushURL) {
        this.pushURL = pushURL;
    }

    @JsonIgnore
    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public String getAccelerateURL() {
        return accelerateURL;
    }

    public void setAccelerateURL(String accelerateURL) {
        this.accelerateURL = accelerateURL;
    }

    @JsonIgnore
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
