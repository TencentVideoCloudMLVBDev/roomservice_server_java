package com.tencent.qcloud.roomservice.pojo;

/**
 * 流ID，完整的(推流状态检查) + 去掉bizid前缀的(生成对应的播放地址)
 */
public class StreamIDS {
    private String streamID = "";
    private String subID = "";

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }
}
