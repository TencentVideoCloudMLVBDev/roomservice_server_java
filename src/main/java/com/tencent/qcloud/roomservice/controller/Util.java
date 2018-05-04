package com.tencent.qcloud.roomservice.controller;


import com.tencent.qcloud.roomservice.logic.IMMgr;
import com.tencent.qcloud.roomservice.pojo.Request.GetLoginInfoReq;
import com.tencent.qcloud.roomservice.pojo.Response.GetLoginInfoRsp;
import com.tencent.qcloud.roomservice.pojo.Response.GetTestPushUrlRsp;
import com.tencent.qcloud.roomservice.pojo.Response.GetTestRtmpAccUrlRsp;
import com.tencent.qcloud.roomservice.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@ResponseBody
@RequestMapping("/weapp/utils")
public class Util {
    @Resource
    IMMgr imMgr;

    @Autowired
    UtilService utilService;

    @ResponseBody
    @RequestMapping(value = "get_login_info")
    public GetLoginInfoRsp get_login_info(@ModelAttribute GetLoginInfoReq req){
        return imMgr.getLoginInfo(req.getUserID());
    }

    @ResponseBody
    @RequestMapping(value = "get_test_pushurl", method = RequestMethod.GET)
    public GetTestPushUrlRsp get_test_pushurl(){
        return utilService.getTestPushUrl();
    }

    @ResponseBody
    @RequestMapping("get_test_rtmpaccurl")
    public GetTestRtmpAccUrlRsp get_test_rtmpaccurl(){
        return utilService.getTestRtmpAccUrl();
    }
}
