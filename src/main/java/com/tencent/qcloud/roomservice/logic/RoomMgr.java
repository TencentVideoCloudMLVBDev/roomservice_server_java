package com.tencent.qcloud.roomservice.logic;

import com.tencent.qcloud.roomservice.common.Config;
import com.tencent.qcloud.roomservice.pojo.Audience;
import com.tencent.qcloud.roomservice.pojo.Pusher;
import com.tencent.qcloud.roomservice.pojo.Response.GetStreamStatusRsp;
import com.tencent.qcloud.roomservice.pojo.Room;
import com.tencent.qcloud.roomservice.utils.Utils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomMgr implements InitializingBean {
    public static final int MULTI_ROOM = 0;
    public static final int DOUBLE_ROOM = 1;
    public static final int LIVE_ROOM = 2;

    private ConcurrentHashMap<String, Room> liveRoomMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Room> doubleRoomMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Room> multiRoomMap = new ConcurrentHashMap<>();

    private HeartTimer heartTimer = new HeartTimer();
    private Timer timer = null;

    private static Logger log = LoggerFactory.getLogger(RoomMgr.class);

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 开启心跳检查定时器
        if (timer  == null) {
            timer = new Timer();
            timer.schedule(heartTimer, 5 * 1000, 5 * 1000);
        }
    }

    /**
     * 获取房间列表
     */
    public ArrayList<Room> getList(int cnt, int index, boolean withPushers, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        ArrayList<Room> resultList = new ArrayList<>();
        int cursor = 0;
        int roomCnt = 0;
        //遍历
        for (Room value : orgMap.values()) {
            if (roomCnt >= cnt)
                break;
            log.info("getRoomList, type:" + type + " , roomID:" + value.getRoomID() + " ,isActived: " + value.isActived() + " ,pushers count: " + value.getPushersCnt());
            if (value.isActived() && value.getPushersCnt() != 0) {
                if (cursor >= index) {
                    resultList.add(value);
                    ++roomCnt;
                } else {
                    ++cursor;
                    continue;
                }

            }
        }

        return resultList;
    }

    /**
     * 获取房间
     */
    public Room getRoom(String roomID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        if (orgMap.containsKey(roomID)) {
            return orgMap.get(roomID);
        } else {
            return null;
        }
    }

    /**
     * 创建房间
     */
    public void creatRoom(String roomID, String roomInfo, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = new Room();
        room.setRoomID(roomID);
        room.setActived(false);
        room.setRoomInfo(roomInfo);
        orgMap.put(roomID, room);
        log.info("creat_room, type: " + type + " , roomID: " + roomID + ", roomInfo: " + roomInfo);
    }

    /**
     * 房间是否存在
     */
    public boolean isRoomExist(String roomID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        return orgMap.containsKey(roomID);
    }

    /**
     * 是否是房间创建者
     */
    public boolean isRoomCreator(String roomID, String userID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = orgMap.get(roomID);
        if (room != null && room.getRoomCreator().equals(userID))
            return true;
        return false;
    }

    /**
     * 删除房间
     */
    public void delRoom(String roomID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        orgMap.remove(roomID);
        log.info("delRoom, type: " + type + " , roomID: " + roomID);
    }

    private ConcurrentHashMap<String, Room> getActuralMap(int type) {
        ConcurrentHashMap<String, Room> actMap;
        switch (type) {
            case MULTI_ROOM:
                actMap = multiRoomMap;
                break;
            case DOUBLE_ROOM:
                actMap = doubleRoomMap;
                break;
            case LIVE_ROOM:
                actMap = liveRoomMap;
                break;
            default:
                actMap = new ConcurrentHashMap<>();
        }
        return actMap;
    }

    /**
     * 新增房间并进房
     */
    public void addRoom(String roomID, String roomInfo, String userID, String mixedPlayUrl, String userName, String userAvatar, String pushURL, String streamID, String acceleratePlayUrl, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = new Room();
        room.setRoomID(roomID);
        room.setRoomInfo(roomInfo);
        room.setRoomCreator(userID);
        room.setMixedPlayURL(mixedPlayUrl);
        room.setActived(true);
        Pusher pusher = new Pusher();
        pusher.setUserID(userID);
        pusher.setUserName(userName);
        pusher.setUserAvatar(userAvatar);
        pusher.setPushURL(pushURL);
        pusher.setStreamID(streamID);
        pusher.setAccelerateURL(acceleratePlayUrl);
        pusher.setUserID(userID);
        pusher.setTimestamp(System.currentTimeMillis() / 1000);
        room.addPusher(pusher);
        orgMap.put(roomID, room);
        log.info("addRoom, type: " + type + " , roomID: " + roomID + ", roomInfo: " + roomInfo + ", userID: " + userID + ", streamID: " + streamID);
    }

    public boolean isMember(String roomID, String userID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = orgMap.get(roomID);
        if (room != null) {
            Pusher pusher = room.getPusher(userID);
            if (pusher != null)
                return true;
        }
        return false;
    }

    public void updateMember(String roomID, String userID, String mixedPlayUrl, String userName, String userAvatar, String pushURL, String streamID, String acceleratePlayUrl, int type) {
        addMember(roomID, userID, mixedPlayUrl, userName, userAvatar, pushURL, streamID, acceleratePlayUrl, type);
        updateMemberTS(roomID, userID, type);
    }

    /**
     * 心跳更新
     */
    public void updateMemberTS(String roomID, String userID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = orgMap.get(roomID);
        if (room != null) {
            Pusher pusher = room.getPusher(userID);
            if (pusher != null)
                pusher.setTimestamp(System.currentTimeMillis() / 1000);
        }
    }

    /**
     * 获取房间推流者人数
     */
    public int getMemberCnt(String roomID, int type) {
        int count = 0;
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = orgMap.get(roomID);
        if (room != null) {
            count = room.getPushersCnt();
        }
        return count;
    }

    /**
     * 新增推流者 - 进房
     */
    public void addMember(String roomID, String userID, String mixedPlayUrl, String userName, String userAvatar, String pushURL, String streamID, String acceleratePlayUrl, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = orgMap.get(roomID);
        if (room != null) {
            if (room.isActived() == false) {
                room.setActived(true);
                room.setRoomCreator(userID);
                room.setMixedPlayURL(mixedPlayUrl);
            }

            Pusher pusher = room.getPusher(userID);
            if (pusher != null) {
                pusher.setUserName(userName);
                pusher.setUserAvatar(userAvatar);
                pusher.setPushURL(pushURL);
                pusher.setStreamID(streamID);
                pusher.setAccelerateURL(acceleratePlayUrl);
                pusher.setTimestamp(System.currentTimeMillis() / 1000);
            } else {
                pusher = new Pusher();
                pusher.setUserID(userID);
                pusher.setUserName(userName);
                pusher.setUserAvatar(userAvatar);
                pusher.setPushURL(pushURL);
                pusher.setStreamID(streamID);
                pusher.setAccelerateURL(acceleratePlayUrl);
                pusher.setTimestamp(System.currentTimeMillis() / 1000);
            }
            room.addPusher(pusher);
            log.info("addMember, type: " + type + " , roomID: " + roomID + ", userID: " + userID + ", streamID: " + streamID);
        }
    }

    /**
     * 删除推流者
     */
    public void delPusher(String roomID, String userID, int type) {
        ConcurrentHashMap<String, Room> orgMap = getActuralMap(type);
        Room room = orgMap.get(roomID);
        if (room != null) {
            room.delPusher(userID);
            if (room.getPushersCnt() == 0) {
                orgMap.remove(roomID);
            }
        }
    }

    public String getCustomInfo(String roomID, int type) {
        Room room = getRoom(roomID, type);
        if (room != null) {
            return room.getCustomInfo();
        }
        return "";
    }

    public String setCustomInfo(String roomID, String fieldName, String operation, int type) {
        Room room = getRoom(roomID, type);
        if (room != null) {
            room.setCustomInfo(fieldName, operation);
            return room.getCustomInfo();
        }
        return "";
    }

    public class HeartTimer extends TimerTask {
        @Override
        public void run() {
            onTimer();
        }
    }

    private void onTimer() {
        onTimerCheckHeartBeat(multiRoomMap, Config.MultiRoom.heartBeatTimeout, MULTI_ROOM);
        onTimerCheckHeartBeat(doubleRoomMap, Config.DoubleRoom.heartBeatTimeout, DOUBLE_ROOM);
        onTimerCheckHeartBeat(liveRoomMap, Config.LiveRoom.heartBeatTimeout, LIVE_ROOM);
    }

    /**
     * 心跳超时检查
     * timeout 过期时间，单位秒
     */
    private void onTimerCheckHeartBeat(ConcurrentHashMap<String, Room> map, int timeout, int type) {
        // 遍历房间每个成员，检查pusher的时间戳是否超过timeout
        long currentTS = System.currentTimeMillis()/1000;
        for (Room room : map.values()) {
            if (room.isActived()) {
                for (Pusher pusher : room.getPushersMap().values()) {
                    // 心跳超时
                    if (pusher.getTimestamp() + timeout < currentTS) {
                        if (getStreamStatus(pusher.getStreamID()) == 1) {
                            // 流断了，删除用户
                            delPusher(room.getRoomID(), pusher.getUserID(), type);
                        } else {
                            // 补一个心跳
                            updateMemberTS(room.getRoomID(), pusher.getUserID(), type);
                        }
                    }
                }
            }
        }
    }

    /**
     * 辅助功能函数 - 心跳超时检查流状态
     * 0: 流状态是推流中
     * 1: 流状态是断流
     * 2: 请求错误
     */
    private int getStreamStatus (String streamID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 5分钟
        long txTime = System.currentTimeMillis() / 1000 + 300;
        String txSecret = Utils.getMD5(Config.Live.APIKEY + txTime);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://fcgi.video.qcloud.com/common_access?appid=" + Config.Live.APP_ID
                + "&interface=Live_Channel_GetStatus&Param.s.channel_id=" + streamID
                + "&t=" + txTime
                + "&sign=" + txSecret);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);

        // 错误
        if (response.getStatusCode().value() != HttpStatus.OK.value()) {
            log.warn("getStreamStatus出错, streamID: " + streamID + ", HttpCode: " + response.getStatusCode().value());
            return 2;
        }

        ObjectMapper mapper = new ObjectMapper();
        GetStreamStatusRsp rsp = null;

        try {
            rsp = mapper.readValue(response.getBody(), GetStreamStatusRsp.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 推流中
        if (rsp != null && rsp.getRet() == 0 && rsp.getOutput().get(0) != null && rsp.getOutput().get(0).getStatus() == 1) {
            return 0;
        }

        log.warn("getStreamStatus, 流已经断开 streamID:" + streamID + ", response :" + response.getBody());
        return 1;
    }

    public void addAudience(String roomID, String userID, String userInfo, int type) {
        // 只有直播房间有观众
        if (type != LIVE_ROOM)
            return;

        Room room = liveRoomMap.get(roomID);
        if (room != null) {
            Audience audience = new Audience();
            audience.setUserID(userID);
            audience.setUserInfo(userInfo);
            room.addAudience(audience);
            log.info("addAudience, roomID:" + roomID + ", userID :" + userID + ", userInfo:" + userInfo);
        }
    }

    public void delAudience(String roomID, String userID, int type) {
        // 只有直播房间有观众
        if (type != LIVE_ROOM)
            return;

        Room room = liveRoomMap.get(roomID);
        if (room != null) {
            room.delAudience(userID);
            log.info("delAudience, roomID:" + roomID + ", userID :" + userID);
        }
    }

    /**
     * 获取房间观众人数
     */
    public int getAudienceCnt(String roomID, int type) {
        int count = 0;
        // 只有直播房间有观众
        if (type == LIVE_ROOM) {
            Room room = liveRoomMap.get(roomID);
            if (room != null) {
                count = room.getAudiencesCnt();
            }
        }
        log.info("getAudienceCnt, roomID:" + roomID + ", count :" + count);
        return count;
    }

    public ArrayList<Audience> getAudiences(String roomID, int type) {
        // 只有直播房间有观众
        if (type == LIVE_ROOM) {
            Room room = liveRoomMap.get(roomID);
            if (room != null)
                return room.getAudiences();
        }
        return new ArrayList<>();
    }
}
