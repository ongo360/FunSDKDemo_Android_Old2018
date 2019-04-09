package com.lib.sdk.bean.doorlock;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-05-15.
 * 消息推送权限信息
 */

public class MessagePushAuthBean {
    @JSONField(name = "DoorLockID")
    private String doorLockID;
    @JSONField(name = "DoorLockOpenType")
    private String doorLockOpenType;
    @JSONField(name = "DoorLockUserType")
    private String doorLockUserType;
    @JSONField(name = "DoorLockUserId")
    private String doorLockUserId;
    @JSONField(name = "MessagePushEnable")
    private boolean messagePushEnable;

    public String getDoorLockID() {
        return doorLockID;
    }

    public void setDoorLockID(String doorLockID) {
        this.doorLockID = doorLockID;
    }

    public String getDoorLockOpenType() {
        return doorLockOpenType;
    }

    public void setDoorLockOpenType(String doorLockOpenType) {
        this.doorLockOpenType = doorLockOpenType;
    }

    public String getDoorLockUserType() {
        return doorLockUserType;
    }

    public void setDoorLockUserType(String doorLockUserType) {
        this.doorLockUserType = doorLockUserType;
    }

    public String getDoorLockUserId() {
        return doorLockUserId;
    }

    public void setDoorLockUserId(String doorLockUserId) {
        this.doorLockUserId = doorLockUserId;
    }

    public boolean isMessagePushEnable() {
        return messagePushEnable;
    }

    public void setMessagePushEnable(boolean messagePushEnable) {
        this.messagePushEnable = messagePushEnable;
    }
}
