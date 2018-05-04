package com.tencent.qcloud.roomservice.pojo.Request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class DestroyGroupReq {
    @JsonProperty(value = "GroupId")
    private String GroupId = "";

    @JsonIgnore
    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }
}
