package com.tencent.qcloud.roomservice.pojo.Response;

public class GetTestPushUrlRsp {
    private String url_push = "";
    private String url_play_flv = "";
    private String url_play_rtmp = "";
    private String url_play_hls = "";
    private String url_play_acc = "";

    public String getUrl_push() {
        return url_push;
    }

    public void setUrl_push(String url_push) {
        this.url_push = url_push;
    }

    public String getUrl_play_flv() {
        return url_play_flv;
    }

    public void setUrl_play_flv(String url_play_flv) {
        this.url_play_flv = url_play_flv;
    }

    public String getUrl_play_rtmp() {
        return url_play_rtmp;
    }

    public void setUrl_play_rtmp(String url_play_rtmp) {
        this.url_play_rtmp = url_play_rtmp;
    }

    public String getUrl_play_hls() {
        return url_play_hls;
    }

    public void setUrl_play_hls(String url_play_hls) {
        this.url_play_hls = url_play_hls;
    }

    public String getUrl_play_acc() {
        return url_play_acc;
    }

    public void setUrl_play_acc(String url_play_acc) {
        this.url_play_acc = url_play_acc;
    }
}
