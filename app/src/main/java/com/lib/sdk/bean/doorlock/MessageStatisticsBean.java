package com.lib.sdk.bean.doorlock;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-05-15.
 * 消息统计
 */

public class MessageStatisticsBean {
    @JSONField(name = "Enable")
    private boolean enable;
    @JSONField(name = "Time")
    private String time;
    private String doorLockId;
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JSONField(serialize = false)
    public String getDoorLockId() {
        return doorLockId;
    }
    @JSONField(serialize = false)
    public void setDoorLockId(String doorLockId) {
        this.doorLockId = doorLockId;
    }
}
