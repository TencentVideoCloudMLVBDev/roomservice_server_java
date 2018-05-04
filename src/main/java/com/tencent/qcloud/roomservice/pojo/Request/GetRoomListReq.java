package com.tencent.qcloud.roomservice.pojo.Request;

public class GetRoomListReq {
    private int cnt = 0;
    private int index = 0;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
