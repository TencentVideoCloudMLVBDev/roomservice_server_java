package com.tencent.qcloud.roomservice.controller;

import com.tencent.qcloud.roomservice.logic.RoomMgr;
import com.tencent.qcloud.roomservice.pojo.Request.*;
import com.tencent.qcloud.roomservice.pojo.Response.*;
import com.tencent.qcloud.roomservice.pojo.Room;
import com.tencent.qcloud.roomservice.service.RoomService;
import com.tencent.qcloud.roomservice.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 直播-连麦 房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/live_room")
public class LiveRoom {
    @Autowired
    RoomService roomService;

    @Autowired
    UtilService utilService;

    @ResponseBody
    @RequestMapping("login")
    public LoginRsp login(String sdkAppID, String accountType, String userID, String userSig){
        return roomService.login(sdkAppID, accountType, userID, userSig);
    }

    @ResponseBody
    @RequestMapping("logout")
    public BaseRsp logout(String userID, String token){
        return roomService.logout(userID, token);
    }

    @ResponseBody
    @RequestMapping("get_push_url")
    public GetPushUrlRsp get_push_url(String userID, String token){
        return utilService.getPushUrl(userID, token);
    }

    @ResponseBody
    @RequestMapping("get_room_list")
    public GetRoomListRsp get_room_list(String userID, String token, @RequestBody GetRoomListReq req){
        return roomService.getRoomList(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("get_pushers")
    public Room get_pushers(String userID, String token, @RequestBody GetPushersReq req){
        return roomService.getPushers(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("create_room")
    public CreateRoomRsp create_room(String userID, String token, @RequestBody CreateRoomReq req){
        return roomService.createRoom(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("destroy_room")
    public BaseRsp destroy_room(String userID, String token, @RequestBody DestroyRoomReq req) {
        return roomService.destroyRoom(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("add_pusher")
    public BaseRsp add_pusher(String userID, String token, @RequestBody AddPusherReq req) {
        return roomService.addPusher(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("delete_pusher")
    public BaseRsp delete_pusher(String userID, String token, @RequestBody DeletePusherReq req) {
        return roomService.deletePusher(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("pusher_heartbeat")
    public BaseRsp pusher_heartbeat(String userID, String token, @RequestBody PusherHeartbeatReq req) {
        return roomService.pusherHeartbeat(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("merge_stream")
    public MergeStreamRsp merge_stream(String userID, String token, @RequestBody Map map) {
        return utilService.mergeStream(userID, token, map);
    }

    @ResponseBody
    @RequestMapping("get_custom_info")
    public GetCustomInfoRsp get_custom_info(String userID, String token, @RequestBody GetCustomInfoReq req) {
        return roomService.getCustomInfo(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("set_custom_field")
    public GetCustomInfoRsp set_custom_field(String userID, String token, @RequestBody SetCustomInfoReq req) {
        return roomService.setCustomInfo(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("add_audience")
    public BaseRsp add_audience(String userID, String token, @RequestBody AddAudienceReq req) {
        return roomService.addAudience(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("delete_audience")
    public BaseRsp delete_audience(String userID, String token, @RequestBody DelAudienceReq req) {
        return roomService.delAudience(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @RequestMapping("get_audiences")
    public GetAudiencesRsp get_audiences(String userID, String token, @RequestBody GetAudiencesReq req) {
        return roomService.getAudiences(userID, token, req, RoomMgr.LIVE_ROOM);
    }
}
