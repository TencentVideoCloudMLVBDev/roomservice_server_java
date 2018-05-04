package com.tencent.qcloud.roomservice.pojo.Request;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class NotifyPusherChangeReq {
    @JsonProperty(value = "GroupId")
    private String GroupId = "";
    @JsonProperty(value = "Content")
    private String Content = "";

    @JsonIgnore
    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    @JsonIgnore
    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public static class Content {
        private String cmd = "";
        private String data = "";

        public String getCmd() {
            return cmd;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
