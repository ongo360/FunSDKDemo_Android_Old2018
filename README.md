# 【低功耗设备状态获取及唤醒操作】

> ## 【FunSupport封装FunSDK后调用说明】
>> **状态获取**：``` FunSupport.getInstance().requestDeviceStatus ``` <br>
 **状态值**: ```
    {
       STATUS_UNKNOWN:0
       STATUS_ONLINE:1
       STATUS_OFFLINE:2
       STATUS_SLEEP:3
       STATUS_CAN_NOT_WAKE_UP:4 
    }
	       ``` <br>
**唤醒操作**:``` FunSupport.getInstance().requestDevWakeUp ``` <br>
**回调函数**:``` onWakeUpResult(boolean isSuccess, FunDevStatus state) ``` <br>
**休眠操作**:``` FunSupport.getInstance().requestDevSleep``` <br>
**回调函数**:``` onSleepResult(boolean isSuccess, FunDevStatus status)``` <br>

> ## 或

> ## 【FunSDK调用说明】
>> **状态获取**：``` FunSDK.SysGetDevState ``` <br>
**回调函数**: ``` OnFunSDKResult``` <br>
**消息ID**: ```
    //5009
    EUIMSG.SYS_GET_DEV_STATE
    ``` <br>
**状态值**: 
    ``` 
    int idrState = FunSDK.GetDevState(devId, SDKCONST.EFunDevStateType.IDR) 
    public interface EFunDevState {
        int UNKOWN = 0;
        int LINE = 1;
        int SLEEP = 2;
        int SLEEP_UNWEAK = 3;
        int OFF_LINE = -1;
        int NO_SUPPORT = -2;
        int NotAllowed = -3;
    }
    ``` <br>
**唤醒操作**:``` FunSDK.DevWakeUp ``` <br>
**回调函数**:``` OnFunSDKResult ``` <br>
**消息ID**: ```
    //5142
    EUIMSG.DEV_WAKEUP
    ``` <br>
**休眠操作**:``` 
    //要让设备休眠就需要登出设备，设备端发现没有客户端连接 就会自动休眠。
    //如果设备有多个客户端同时连接的情况下，调用下方的接口不能保证设备会进入休眠状态
    FunSDK.DevLogout 
    ``` <br>
