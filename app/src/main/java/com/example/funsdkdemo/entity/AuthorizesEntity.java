package com.example.funsdkdemo.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 用户信息-授权信息实体类
 * Created by hanzhenbo on 2017-08-05.
 */

public class AuthorizesEntity {

    private boolean mWxPms;
    private boolean mWxBind;

    @JSONField(name = "wxpms")
    public boolean isWxPms() {
        return mWxPms;
    }

    @JSONField(name = "wxpms")
    public void setWxPms(boolean wxpms) {
        mWxPms = wxpms;
    }

    @JSONField(name = "wxbind")
    public boolean isWxBind() {
        return mWxBind;
    }

    @JSONField(name = "wxbind")
    public void setWxBind(boolean wxBind) {
        mWxBind = wxBind;
    }
}
