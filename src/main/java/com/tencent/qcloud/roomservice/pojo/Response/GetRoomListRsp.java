package com.tencent.qcloud.roomservice.pojo.Response;

import com.tencent.qcloud.roomservice.pojo.Room;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

public class GetRoomListRsp extends BaseRsp {
    @JsonProperty(value = "rooms")
    private ArrayList<Room> rooms;

    @JsonIgnore
    public ArrayList<Room> getList() {
        return rooms;
    }

    public void setList(ArrayList<Room> list) {
        this.rooms = list;
    }
}
