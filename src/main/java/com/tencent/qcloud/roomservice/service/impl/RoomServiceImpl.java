package com.tencent.qcloud.roomservice.service.impl;

import com.tencent.qcloud.roomservice.common.Config;
import com.tencent.qcloud.roomservice.logic.IMMgr;
import com.tencent.qcloud.roomservice.logic.LiveUtil;
import com.tencent.qcloud.roomservice.logic.RoomMgr;
import com.tencent.qcloud.roomservice.pojo.Request.*;
import com.tencent.qcloud.roomservice.pojo.Response.*;
import com.tencent.qcloud.roomservice.pojo.Room;
import com.tencent.qcloud.roomservice.pojo.StreamIDS;
import com.tencent.qcloud.roomservice.service.RoomService;
import com.tencent.qcloud.roomservice.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomServiceImpl implements RoomService {
    @Resource
    RoomMgr roomMgr;

    @Resource
    IMMgr imMgr;

    @Resource
    LiveUtil liveUtil;

    private static Logger log= LoggerFactory.getLogger(RoomServiceImpl.class);


    @Override
    public LoginRsp login(String sdkAppID, String accountType, String userID, String userSig) {
        return imMgr.verifySig(sdkAppID, accountType, userID, userSig);
    }

    @Override
    public BaseRsp logout(String userID, String token) {
        BaseRsp rsp = new BaseRsp();
        if (userID == null || token == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("logout失败:缺少参数：" + "userID：" + userID + ",token: " + token);
            return rsp;
        }
        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("logout失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }

        //删除session
        imMgr.delLoginSession(userID);
        return rsp;
    }

    @Override
    public GetRoomListRsp getRoomList(String userID, String token, GetRoomListReq req, int type) {
        GetRoomListRsp rsp = new GetRoomListRsp();
        if (userID == null || token == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("getRoomList失败:缺少参数：" + "userID：" + userID + ",token: " + token + ",type: " + type);
            return rsp;
        }
        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("getRoomList失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        rsp.setList(roomMgr.getList(req.getCnt(), req.getIndex(), true, type));
        return rsp;
    }

    @Override
    public Room getPushers(String userID, String token, GetPushersReq req, int type) {
        Room rsp = new Room();
        if (userID == null || token == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("getPushers失败:缺少参数：" + "userID：" + userID + ",token: " + token + ",type: " + type);
            return rsp;
        }
        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("getPushers失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        rsp = roomMgr.getRoom(req.getRoomID(), type);
        log.info("getRoom, type: " + type + " , roomID: " + req.getRoomID() + ", pushers counts : " + (rsp == null? 0 : rsp.getPushersCnt()));
        if (rsp == null) {
            rsp.setCode(1);
            rsp.setMessage("请求失败，房间不存在");
            log.error("getPushers失败:房间不存在：" + "userID：" + userID + ",roomID: " + req.getRoomID() + ",type: " + type);
        }

        return rsp;
    }

    @Override
    public CreateRoomRsp createRoom(String userID, String token, CreateRoomReq req, int type) {
        CreateRoomRsp rsp = new CreateRoomRsp();
        if (userID == null || token == null || req.getUserID() == null || req.getRoomInfo() == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("createRoom失败:缺少参数：" + "userID：" + userID + ",token: " + token + ",roomInfo：" + req.getRoomInfo() + ",type: " + type);
            return rsp;
        }

        if (req.getRoomInfo().length() > 1024) {
            rsp.setCode(11);
            rsp.setMessage("roomInfo 字符串长度不能超过1024字节");
            log.error("createRoom失败:roomInfo 字符串长度不能超过1024字节：" + "userID：" + userID + ",roomInfo：" + req.getRoomInfo());
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("createRoom失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        String roomID = req.getRoomID();
        if (roomID == null || roomID.length() == 0) {
            roomID = Utils.genRoomIdByRandom();
        } else {
            if (roomMgr.isRoomExist(roomID, type)) {
                rsp.setCode(11);
                rsp.setMessage("房间已经存在");
                return rsp;
            }
        }
        // 获取一个可用的roomid
        while (roomMgr.isRoomExist(roomID, type)) {
            roomID = Utils.genRoomIdByRandom();
        }

        // 先创建im群
        if (!imMgr.createGroup(roomID)) {
            rsp.setCode(6);
            rsp.setMessage("群组创建失败");
            log.error("createRoom失败:群组创建失败：" + "userID：" + userID + ", roomID: " + roomID);
            return rsp;
        }

        // 再创建房间
        roomMgr.creatRoom(roomID, req.getRoomInfo(), type);
        rsp.setRoomID(roomID);

        return rsp;
    }

    @Override
    public BaseRsp destroyRoom(String userID, String token, DestroyRoomReq req, int type) {
        BaseRsp rsp = new BaseRsp();
        if (userID == null || token == null || req.getUserID() == null || req.getRoomID() == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("destroyRoom失败:缺少参数：" + "userID：" + userID + ",token: " + token + ",roomID：" + req.getRoomID() + ",type: " + type);
            return rsp;
        }
        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("destroyRoom失败:鉴权失败：" + "userID：" + userID + ",roomID：" + req.getRoomID());
            return rsp;
        }

        if (!roomMgr.isRoomCreator(req.getRoomID(), req.getUserID(), type)) {
            rsp.setCode(3);
            rsp.setMessage("不是房间主人，无法销毁房间");
            log.error("destroyRoom失败:不是房间主人，无法销毁房间：" + ",userID：" + userID + ",roomID：" + req.getRoomID());
            return rsp;
        }

        roomMgr.delRoom(req.getRoomID(), type);
        imMgr.destroyGroup(req.getRoomID());

        return rsp;
    }

    @Override
    public BaseRsp addPusher(String userID, String token, AddPusherReq req, int type) {
        CreateRoomRsp rsp = new CreateRoomRsp();
        String roomID = req.getRoomID();
        String userName = req.getUserName();
        String userAvatar = req.getUserAvatar();
        String pushURL = req.getPushURL();
        if (userID == null
                || token == null
                || roomID == null
                || req.getUserID() == null
                || userName == null
                || userAvatar == null
                || pushURL == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("addPusher失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ", userName:" + userName + ", userAvatar:" + userAvatar + ", pushURL:" + pushURL + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("addPusher失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        // 如果房间不存在，先建房
        if (!roomMgr.isRoomExist(roomID, type)) {
            log.info("addPusher群组不存在，创建一下：" + "userID：" + userID + ", roomID：" + roomID);
            // 先创建im群
            if (!imMgr.createGroup(roomID)) {
                rsp.setCode(6);
                rsp.setMessage("群组创建失败");
                log.error("addPusher群组创建失败：" + "userID：" + userID + ", roomID：" + roomID);
                return rsp;
            }

            String roomInfo = "";
            if (req.getRoomInfo() != null) {
                roomInfo = req.getRoomInfo();
            }
            
            // 再创建房间
            roomMgr.creatRoom(userID, roomInfo, type);

            // 进房
            StreamIDS ids = liveUtil.getStreamIdFromPushUrl(pushURL);
            roomMgr.addRoom(roomID,
                    "",
                    userID,
                    liveUtil.genMixedPlayUrl(ids.getSubID(), "flv"),
                    userName,
                    userAvatar,
                    pushURL,
                    ids.getStreamID(),
                    liveUtil.genAcceleratePlayUrl(ids.getSubID()),
                    type);
            rsp.setRoomID(roomID);
            return rsp;
        }

        // 房间已经存在，直接进房，或更新成员信息。
        StreamIDS ids = liveUtil.getStreamIdFromPushUrl(pushURL);
        if (roomMgr.isMember(roomID, userID, type)) {
            roomMgr.updateMember(roomID,
                    userID,
                    liveUtil.genMixedPlayUrl(ids.getSubID(), "flv"),
                    userName,
                    userAvatar,
                    pushURL,
                    ids.getStreamID(),
                    liveUtil.genAcceleratePlayUrl(ids.getSubID()),
                    type);
            imMgr.notifyPushersChange(roomID);
            return rsp;
        }

        int maxMembers = 0;
        if (type == RoomMgr.DOUBLE_ROOM) {
            maxMembers = 2;
        } else if (type == RoomMgr.MULTI_ROOM) {
            maxMembers = Config.MultiRoom.maxMembers;
        } else if (type == RoomMgr.LIVE_ROOM) {
            maxMembers = Config.LiveRoom.maxMembers;
        }
        if (roomMgr.getMemberCnt(roomID, type) >= maxMembers) {
            rsp.setCode(5001);
            rsp.setMessage("超出房间人数上限");
            log.error("addPusher超出房间人数上限：" + "userID：" + userID + ", roomID：" + roomID);
            return rsp;
        }

        roomMgr.addMember(roomID,
                userID,
                liveUtil.genMixedPlayUrl(ids.getSubID(), "flv"),
                userName,
                userAvatar,
                pushURL,
                ids.getStreamID(),
                liveUtil.genAcceleratePlayUrl(ids.getSubID()),
                type);
        imMgr.notifyPushersChange(roomID);
        return rsp;
    }

    @Override
    public BaseRsp deletePusher(String userID, String token, DeletePusherReq req, int type) {
        BaseRsp rsp = new BaseRsp();
        String roomID = req.getRoomID();
        if (userID == null
                || token == null
                || roomID == null
                || req.getUserID() == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("deletePusher失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("deletePusher失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        if (!roomMgr.isMember(roomID, userID, type)) {
            rsp.setCode(5);
            rsp.setMessage("退出房间失败，不是房间成员");
            log.error("deletePusher失败:不是房间成员：" + "userID：" + userID + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        if (type != RoomMgr.LIVE_ROOM && Config.isCreatorDestroyRoom && roomMgr.isRoomCreator(roomID, userID, type)) {
            roomMgr.delRoom(roomID, type);
            imMgr.destroyGroup(roomID);
        } else if(roomMgr.isMember(roomID, userID, type)) {
            roomMgr.delPusher(roomID, userID, type);
            // notify
            imMgr.notifyPushersChange(roomID);


            if (roomMgr.getMemberCnt(roomID, type) == 0) {
                roomMgr.delRoom(roomID, type);
                imMgr.destroyGroup(roomID);
            }
        }

        return rsp;
    }

    @Override
    public void test() {
        String roomID = Utils.genRoomIdByRandom();
    }

    @Override
    public BaseRsp pusherHeartbeat(String userID, String token, PusherHeartbeatReq req, int type) {
        BaseRsp rsp = new BaseRsp();
        String roomID = req.getRoomID();
        if (userID == null
                || token == null
                || roomID == null
                || req.getUserID() == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("pusherHeartbeat失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("pusherHeartbeat失败:鉴权失败：" + "userID：" + userID);
            return rsp;
        }

        if (!roomMgr.isMember(roomID, userID, type)) {
            rsp.setCode(5);
            rsp.setMessage("请求失败，不是房间成员");
            log.error("pusherHeartbeat失败:不是房间成员：" + "userID：" + userID + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        roomMgr.updateMemberTS(roomID, userID, type);

        return rsp;
    }

    @Override
    public GetCustomInfoRsp getCustomInfo(String userID, String token, GetCustomInfoReq req, int type) {
        GetCustomInfoRsp rsp = new GetCustomInfoRsp();
        String roomID = req.getRoomID();
        if (userID == null || token == null || roomID == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("getCustomInfo失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("getCustomInfo失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }

        if (!roomMgr.isRoomExist(roomID, type)) {
            rsp.setCode(3);
            rsp.setMessage("请求失败，房间不存在");
            log.error("getCustomInfo失败:不是房间成员：" + "userID：" + userID + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        rsp.setCustomInfo(roomMgr.getCustomInfo(roomID, type));

        return rsp;
    }

    @Override
    public GetCustomInfoRsp setCustomInfo(String userID, String token, SetCustomInfoReq req, int type) {
        GetCustomInfoRsp rsp = new GetCustomInfoRsp();
        String roomID = req.getRoomID();
        String fieldName = req.getFieldName();
        String operation = req.getOperation();
        if (userID == null || token == null || roomID == null || fieldName == null || operation == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("setCustomInfo失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ", fieldName：" + fieldName + ", operation：" + operation + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("setCustomInfo失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }

        if (!roomMgr.isRoomExist(roomID, type)) {
            rsp.setCode(3);
            rsp.setMessage("请求失败，房间不存在");
            log.error("setCustomInfo失败:房间成员不存在：" + "userID：" + userID + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        rsp.setCustomInfo(roomMgr.setCustomInfo(roomID, fieldName, operation, type));

        return null;
    }

    @Override
    public BaseRsp addAudience(String userID, String token, AddAudienceReq req, int type) {
        BaseRsp rsp = new BaseRsp();
        String roomID = req.getRoomID();
        String userInfo = req.getUserInfo();
        if (userID == null || token == null || roomID == null || userInfo == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("addAudience失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ", userInfo：" + userInfo + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("addAudience失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }

        if (roomMgr.getAudienceCnt(roomID, RoomMgr.LIVE_ROOM) + 1 > Config.LiveRoom.maxAudiencesLen ) {
            rsp.setCode(5001);
            rsp.setMessage("观众列表满了");
            log.error("addAudience失败:观众列表满了：" + "userID：" + userID + ", roomID：" + roomID );
            return rsp;
        }

        roomMgr.addAudience(roomID, userID, userInfo, type);
        return rsp;
    }

    @Override
    public BaseRsp delAudience(String userID, String token, DelAudienceReq req, int type) {
        BaseRsp rsp = new BaseRsp();
        String roomID = req.getRoomID();
        if (userID == null || token == null || roomID == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("delAudience失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("delAudience失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }

        roomMgr.delAudience(roomID, userID, type);
        return rsp;
    }

    @Override
    public GetAudiencesRsp getAudiences(String userID, String token, GetAudiencesReq req, int type) {
        GetAudiencesRsp rsp = new GetAudiencesRsp();
        String roomID = req.getRoomID();
        if (userID == null || token == null || roomID == null) {
            rsp.setCode(2);
            rsp.setMessage("请求失败，缺少参数");
            log.error("getAudiences失败:缺少参数：" + "userID：" + userID + ",token: " + token + ", roomID：" + roomID + ",type: " + type);
            return rsp;
        }

        if (imMgr.validation(userID, token) != 0) {
            rsp.setCode(7);
            rsp.setMessage("请求失败，鉴权失败");
            log.error("getAudiences失败:鉴权失败：" + "userID：" + userID);
            return rsp;

        }
        rsp.setAudiences(roomMgr.getAudiences(roomID, type));
        return rsp;
    }
}
