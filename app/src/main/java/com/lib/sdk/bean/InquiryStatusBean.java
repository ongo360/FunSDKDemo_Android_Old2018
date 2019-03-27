package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-10-23.
 * 智联设备功能状态
 */

public class InquiryStatusBean {
    public static final String JSON_NAME = "InquiryStatus";
    @JSONField(name = "DevID")
    private String devID;
    @JSONField(name = "DevStatus")
    private int devStatus;
    @JSONField(name = "DevType")
    private int devType;

    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public int getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(int devStatus) {
        this.devStatus = devStatus;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }
}
