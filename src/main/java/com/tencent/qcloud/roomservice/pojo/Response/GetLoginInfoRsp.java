package com.tencent.qcloud.roomservice.pojo.Response;

public class GetLoginInfoRsp extends BaseRsp {
    private long sdkAppID = 0;
    private String accType = "";
    private String userID = "";
    private String userSig = "";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public long getSdkAppID() {
        return sdkAppID;
    }

    public void setSdkAppID(long sdkAppID) {
        this.sdkAppID = sdkAppID;
    }
}
