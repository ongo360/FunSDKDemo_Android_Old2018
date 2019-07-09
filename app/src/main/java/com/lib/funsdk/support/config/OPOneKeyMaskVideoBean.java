package com.lib.funsdk.support.config;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hws on 2018-07-04.
 * 一键遮蔽
 */

public class OPOneKeyMaskVideoBean {
    public static final String JSON_NAME = "General.OneKeyMaskVideo";
    @JSONField(name = "Enable")
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
