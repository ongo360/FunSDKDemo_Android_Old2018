package com.lib.funsdk.support;

import com.lib.MsgContent;
import com.lib.funsdk.support.models.FunDevice;

/**
 * Created by zhangyongyong on 2017-07-25-08:55.
 */

public interface OnAddSubDeviceResultListener extends OnFunListener{
    /***子设备添加成功
     * @param funDevice
     * @param msgContent
     */
    void onAddSubDeviceFailed(FunDevice funDevice, MsgContent msgContent);

    /****子设备添加失败
     *
     * @param funDevice
     * @param msgContent
     */
    void onAddSubDeviceSuccess(FunDevice funDevice, MsgContent msgContent);

}
