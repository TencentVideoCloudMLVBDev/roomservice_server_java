package com.tencent.qcloud.roomservice.service.impl;

import com.tencent.qcloud.roomservice.logic.IMMgr;
import com.tencent.qcloud.roomservice.logic.LiveUtil;
import com.tencent.qcloud.roomservice.logic.RoomMgr;
import com.tencent.qcloud.roomservice.pojo.Request.GetPushUrlReq;
import com.tencent.qcloud.roomservice.pojo.Response.GetPushUrlRsp;
import com.tencent.qcloud.roomservice.pojo.Response.GetTestPushUrlRsp;
import com.tencent.qcloud.roomservice.pojo.Response.GetTestRtmpAccUrlRsp;
import com.tencent.qcloud.roomservice.pojo.Response.MergeStreamRsp;
import com.tencent.qcloud.roomservice.service.UtilService;
import com.tencent.qcloud.roomservice.utils.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UtilServiceImpl implements UtilService {
    @Resource
    IMMgr imMgr;

    @Resource
    LiveUtil liveUtil;

    @Resource
    RoomMgr roomMgr;

    private static Logger log = LoggerFactory.getLogger(UtilServiceImpl.class);

    @Override
    public GetPushUrlRsp getPushUrl(String userID, String token) {
        GetPushUrlRsp rsp = new GetPushUrlRsp();
        if (userID == null || token == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("getPushUrl失败:缺少参数：" + "userID：" + userID + ",token: " + token);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("getPushUrl失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        rsp.setPushURL(liveUtil.genPushUrl(userID));
        return rsp;
    }

    @Override
    public GetPushUrlRsp getPushUrl(String userID, String token, GetPushUrlReq req) {
        GetPushUrlRsp rsp = new GetPushUrlRsp();
        String roomID = req.getRoomID();
        if (userID == null || token == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("getPushUrl失败:缺少参数：" + "userID：" + userID + ",token: " + token);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("getPushUrl失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        if (roomID != null && roomID.length() > 0) {
            userID = roomID + "_" + userID;
        }
        rsp.setPushURL(liveUtil.genPushUrl(userID));
        return rsp;
    }

    @Override
    public MergeStreamRsp mergeStream(String userID, String token, Map map) {
        MergeStreamRsp rsp = new MergeStreamRsp();
        if (userID == null || token == null || map == null || map.get("roomID") == null || map.get("mergeParams") == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("mergeStream失败:缺少参数：" + "userID：" + userID);
            return rsp;
        }
        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("mergeStream失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }
        JSONObject jsonObj = new JSONObject(liveUtil.mergeStream(map));
        MergeStreamRsp.Result result = new MergeStreamRsp.Result();
        result.setCode(jsonObj.optInt("code"));
        result.setMessage(jsonObj.optString("message"));
        result.setTimestamp(jsonObj.optLong("timestamp"));
        rsp.setResult(result);
        return rsp;
    }

    @Override
    public GetTestPushUrlRsp getTestPushUrl() {
        String userID = Utils.genUserIdByRandom();
        return liveUtil.getTestPushUrl(userID);
    }

    @Override
    public GetTestRtmpAccUrlRsp getTestRtmpAccUrl() {
        GetTestRtmpAccUrlRsp rsp = new GetTestRtmpAccUrlRsp();
        rsp.setUrl_rtmpacc(liveUtil.genAcceleratePlayUrl("testclock_rtmpacc"));
        return rsp;
    }
}
