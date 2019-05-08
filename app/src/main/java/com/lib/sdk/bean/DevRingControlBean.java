package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-07-11.
 * 外机按铃声音控制
 * true的时候打开设备响铃声音, false的时候关闭设备响铃声音
 */

public class DevRingControlBean {
    public static final String JSON_NAME = "Consumer.DevRingControl";
    @JSONField(name = "Enable")
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
