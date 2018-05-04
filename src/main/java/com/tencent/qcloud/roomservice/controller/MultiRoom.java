package com.tencent.qcloud.roomservice.controller;

import com.tencent.qcloud.roomservice.logic.RoomMgr;
import com.tencent.qcloud.roomservice.pojo.Audience;
import com.tencent.qcloud.roomservice.pojo.Pusher;
import com.tencent.qcloud.roomservice.pojo.Request.*;
import com.tencent.qcloud.roomservice.pojo.Response.*;
import com.tencent.qcloud.roomservice.pojo.Room;
import com.tencent.qcloud.roomservice.service.RoomService;
import com.tencent.qcloud.roomservice.service.UtilService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Map;

/**
 * 多人房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/multi_room")
public class MultiRoom {

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
    public GetPushUrlRsp get_push_url(String userID, String token, @RequestBody GetPushUrlReq req){
        return utilService.getPushUrl(userID, token, req);
    }

    @ResponseBody
    @RequestMapping("get_room_list")
    public GetRoomListRsp get_room_list(String userID, String token, @RequestBody GetRoomListReq req){
        return roomService.getRoomList(userID, token, req, RoomMgr.MULTI_ROOM);
    }

    @ResponseBody
    @RequestMapping("get_pushers")
    public Room get_pushers(String userID, String token, @RequestBody GetPushersReq req){
        return roomService.getPushers(userID, token, req, RoomMgr.MULTI_ROOM);
    }

    @ResponseBody
    @RequestMapping("create_room")
    public CreateRoomRsp create_room(String userID, String token, @RequestBody CreateRoomReq req){
        return roomService.createRoom(userID, token, req, RoomMgr.MULTI_ROOM);
    }

    @ResponseBody
    @RequestMapping("destroy_room")
    public BaseRsp destroy_room(String userID, String token, @RequestBody DestroyRoomReq req) {
        return roomService.destroyRoom(userID, token, req, RoomMgr.MULTI_ROOM);
    }

    @ResponseBody
    @RequestMapping("add_pusher")
    public BaseRsp add_pusher(String userID, String token, @RequestBody AddPusherReq req) {
        return roomService.addPusher(userID, token, req, RoomMgr.MULTI_ROOM);
    }

    @ResponseBody
    @RequestMapping("delete_pusher")
    public BaseRsp delete_pusher(String userID, String token, @RequestBody DeletePusherReq req) {
        return roomService.deletePusher(userID, token, req, RoomMgr.MULTI_ROOM);
    }

    @ResponseBody
    @RequestMapping("pusher_heartbeat")
    public BaseRsp pusher_heartbeat(String userID, String token, @RequestBody PusherHeartbeatReq req) {
        return roomService.pusherHeartbeat(userID, token, req, RoomMgr.MULTI_ROOM);
    }
}
