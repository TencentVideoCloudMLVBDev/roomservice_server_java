package com.tencent.qcloud.roomservice.pojo.Request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class CreateGroupReq {
    @JsonProperty(value = "Owner_Account")
    private String Owner_Account;
    @JsonProperty(value = "Type")
    private String Type = "";
    @JsonProperty(value = "GroupId")
    private String GroupId = "";
    @JsonProperty(value = "Name")
    private String Name = "";

    @JsonIgnore
    public String getOwner_Account() {
        return Owner_Account;
    }

    public void setOwner_Account(String owner_Account) {
        Owner_Account = owner_Account;
    }

    @JsonIgnore
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @JsonIgnore
    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    @JsonIgnore
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
