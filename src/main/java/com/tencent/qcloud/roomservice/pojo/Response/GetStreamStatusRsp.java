package com.tencent.qcloud.roomservice.pojo.Response;

import java.util.ArrayList;

public class GetStreamStatusRsp {
    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<GetStreamStatusOutPut> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<GetStreamStatusOutPut> output) {
        this.output = output;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


    private int ret = 0;
    private int retcode = 0;
    private String errmsg = "";
    private String message = "";
    private ArrayList<GetStreamStatusOutPut> output = new ArrayList<>();
}
