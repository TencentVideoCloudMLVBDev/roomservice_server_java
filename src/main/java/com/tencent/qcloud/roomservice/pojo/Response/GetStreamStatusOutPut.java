package com.tencent.qcloud.roomservice.pojo.Response;

public class GetStreamStatusOutPut {
    private int rate_type = 0;
    private int recv_type = 0;
    private int status = 0;
    private int height = 0;
    private int width = 0;

    public int getRate_type() {
        return rate_type;
    }

    public void setRate_type(int rate_type) {
        this.rate_type = rate_type;
    }

    public int getRecv_type() {
        return recv_type;
    }

    public void setRecv_type(int recv_type) {
        this.recv_type = recv_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
