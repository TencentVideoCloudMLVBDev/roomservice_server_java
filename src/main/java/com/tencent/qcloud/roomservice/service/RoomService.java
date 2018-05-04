package com.tencent.qcloud.roomservice.service;

import com.tencent.qcloud.roomservice.pojo.Request.*;
import com.tencent.qcloud.roomservice.pojo.Response.*;
import com.tencent.qcloud.roomservice.pojo.Room;

import java.util.Map;

public interface RoomService {
    LoginRsp login(String sdkAppID, String accountType, String userID, String userSig);

    BaseRsp logout(String userID, String token);

    GetRoomListRsp getRoomList(String userID, String token, GetRoomListReq req, int type);

    Room getPushers(String userID, String token, GetPushersReq req, int type);

    CreateRoomRsp createRoom(String userID, String token, CreateRoomReq req, int type);

    BaseRsp destroyRoom(String userID, String token, DestroyRoomReq req, int type);

    BaseRsp addPusher(String userID, String token, AddPusherReq req, int type);

    BaseRsp deletePusher(String userID, String token, DeletePusherReq req, int type);

    void test();

    BaseRsp pusherHeartbeat(String userID, String token, PusherHeartbeatReq req, int type);

    GetCustomInfoRsp getCustomInfo(String userID, String token, GetCustomInfoReq req, int type);

    GetCustomInfoRsp setCustomInfo(String userID, String token, SetCustomInfoReq req, int type);

    BaseRsp addAudience(String userID, String token, AddAudienceReq req, int type);

    BaseRsp delAudience(String userID, String token, DelAudienceReq req, int type);

    GetAudiencesRsp getAudiences(String userID, String token, GetAudiencesReq req, int type);
}
