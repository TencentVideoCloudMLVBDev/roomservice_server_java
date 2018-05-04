package com.tencent.qcloud.roomservice.pojo.Response;

public class GetPushUrlRsp extends BaseRsp {
    private String pushURL = "";

    public String getPushURL() {
        return pushURL;
    }

    public void setPushURL(String pushURL) {
        this.pushURL = pushURL;
    }
}
