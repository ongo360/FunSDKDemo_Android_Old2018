package com.lib.funsdk.support.config;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Administrator
 * @name FunSDKDemo_Android_Old2018
 * @class name：com.lib.funsdk.support.config
 * @class describe
 * @time 2019-07-09 16:42
 * @change
 * @chang time
 * @class describe
 */
public class TimimgPtzTourBean {
    public static final String JSON_NAME = "General.TimimgPtzTour";
    @JSONField(name = "Enable")
    private boolean enable;// 功能开关
    @JSONField(name = "TimeInterval")
    private int timeInterval;// 时间间隔, 单位S

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }
}
