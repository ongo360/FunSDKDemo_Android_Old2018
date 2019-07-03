package com.example.funsdkdemo.entity;

import com.lib.sdk.bean.AlarmInfoBean;

/**
 * Created by zhangyongyong on 2017-09-13-13:43.
 */

public class AlarmInfoMapEntity {
    AlarmInfoBean mAlarmInfo;
    String devId;

    public AlarmInfoMapEntity() {
    }

    public AlarmInfoMapEntity(AlarmInfoBean alarmInfo, String devId) {
        mAlarmInfo = alarmInfo;
        this.devId = devId;
    }

    public AlarmInfoBean getAlarmInfo() {
        return mAlarmInfo;
    }

    public void setAlarmInfo(AlarmInfoBean alarmInfo) {
        mAlarmInfo = alarmInfo;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }
}
