package com.lib.funsdk.support;

import com.lib.funsdk.support.models.FunDevStatus;

/**
 * @author hws
 * @name OldFunSdkDemo
 * @class name：com.lib.funsdk.support
 * @class 设备唤醒及状态
 * @time 2019-02-15 19:21
 * @change
 * @chang time
 * @class describe
 */
public interface OnFunDeviceWakeUpListener extends OnFunListener{
    void onDeviceState(FunDevStatus state);
    void onWakeUpResult(boolean isSuccess,FunDevStatus state);
    void onSleepResult(boolean isSuccess,FunDevStatus status);
}
