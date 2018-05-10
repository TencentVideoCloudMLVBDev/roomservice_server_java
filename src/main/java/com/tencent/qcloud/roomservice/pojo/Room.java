package com.tencent.qcloud.roomservice.pojo;

import com.tencent.qcloud.roomservice.pojo.Response.BaseRsp;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间信息
 */
public class Room extends BaseRsp {
    private String roomID = "";
    private String roomInfo = "";
    private String roomCreator = "";
    private String mixedPlayURL = "";
    private ConcurrentHashMap<String, Integer> custom = new ConcurrentHashMap<>();
    private boolean actived = false;

    @JsonProperty(value = "pushers")
    private ArrayList<Pusher> pushers = new ArrayList<>();
    private ConcurrentHashMap<String, Pusher> pusherMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Audience> audienceMap = new ConcurrentHashMap<>();
    private long timestamp = 0;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }

    public String getRoomCreator() {
        return roomCreator;
    }

    public void setRoomCreator(String roomCreator) {
        this.roomCreator = roomCreator;
    }

    public String getMixedPlayURL() {
        return mixedPlayURL;
    }

    public void setMixedPlayURL(String mixedPlayURL) {
        this.mixedPlayURL = mixedPlayURL;
    }

    public Pusher getPusher(String userID) {
        return pusherMap.get(userID);
    }

    public void addPusher(Pusher pusher) {
        this.pusherMap.put(pusher.getUserID(), pusher);
        if (!this.pushers.contains(pusher)) {
            this.pushers.add(pusher);
        }
    }

    public void delPusher(String userID) {
        this.pushers.remove(this.pusherMap.get(userID));
        this.pusherMap.remove(userID);
    }

    @JsonIgnore
    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    @JsonIgnore
    public String getCustomInfo() {
        String json = "{}";
        if (custom.size() > 0) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                json = mapper.writeValueAsString(custom);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        } else {
            return "{}";
        }
    }

    public void setCustomInfo(String fieldName, String op) {
        if (!custom.containsKey(fieldName)) {
            custom.put(fieldName, 0);
        }
        int value = custom.get(fieldName);
        if (op.equals("inc")) {
            custom.put(fieldName, ++value);
        } else if (op.equals("dec")) {
            custom.put(fieldName, --value);
        }
    }

    @JsonIgnore
    public long getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonIgnore
    public int getPushersCnt() {
        return pusherMap.size();
    }

    @JsonIgnore
    public ConcurrentHashMap<String, Pusher> getPushersMap() {
        return pusherMap;
    }

    @JsonIgnore
    public int getAudiencesCnt() {
        return audienceMap.size();
    }

    @JsonIgnore
    public Audience getAudience(String userID) {
        return audienceMap.get(userID);
    }

    @JsonIgnore
    public ArrayList<Audience> getAudiences() {
        ArrayList<Audience> audiences = new ArrayList<>();
        for(Audience value : audienceMap.values()) {
            audiences.add(value);
        }
        return audiences;
    }

    public void addAudience(Audience audience) {
        this.audienceMap.put(audience.getUserID(), audience);
    }

    public void delAudience(String userID) {
        this.audienceMap.remove(userID);
    }
}