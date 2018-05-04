package com.tencent.qcloud.roomservice.service;

import com.tencent.qcloud.roomservice.pojo.Request.AddAudienceReq;
import com.tencent.qcloud.roomservice.pojo.Request.GetCustomInfoReq;
import com.tencent.qcloud.roomservice.pojo.Request.GetPushUrlReq;
import com.tencent.qcloud.roomservice.pojo.Request.SetCustomInfoReq;
import com.tencent.qcloud.roomservice.pojo.Response.*;

import java.util.Map;

public interface UtilService {
    GetPushUrlRsp getPushUrl(String userID, String token);

    GetPushUrlRsp getPushUrl(String userID, String token, GetPushUrlReq req);

    MergeStreamRsp mergeStream(String userID, String token, Map map);

    GetTestPushUrlRsp getTestPushUrl();

    GetTestRtmpAccUrlRsp getTestRtmpAccUrl();
}
