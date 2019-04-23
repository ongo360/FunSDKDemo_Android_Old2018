# 【低功耗设备状态获取及唤醒操作】

![Demo效果图](https://gitlab.xmcloud.io/demo/FunSDKDemo_Android_Old2018/blob/master/wakeup.png)

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
    

# 【时区同步及夏令时设置】


> ## 【FunSupport封装FunSDK后调用说明】
>> **状态获取**：``` FunSupport.getInstance().requestSyncDevZone ``` <br>

> ## 或

> ## 【FunSDK调用说明】
>> **设置时区**：``` FunSDK.DevSetConfigByJson ``` <br>
**JsonName**: ``` "System.TimeZone"``` <br>
**Json数据**: ```
    {
       	"System.TimeZone":	{
       		"FirstUserTimeZone":	0,
       		"timeMin":	-480
       	},
       	"Name":	"System.TimeZone",
       	"SessionID":	"0x0000000066"
   }
    ``` <br>
>> **获取夏令时**:```FunSDK.DevGetConfigByJson ```<br>
**JsonName**:``` "General.Location" ``` <br>
**Json数据**``` 
    {
   	"Name":	"General.Location",
   	"SessionID":	"0x000000006e"
   }
    ``` <br>
>> **设置夏令时**:``` FunSDK.DevSetConfigByJson ``` <br>
**JsonName**:``` "General.Location" ``` <br>
**Json数据**: ```
    {
     "General.Location":	{
     	"DSTEnd":	{
     		"Day":	4,
     		"Hour":	0,
     		"Minute":	0,
     		"Month":	11,
     		"Week":	0,
     		"Year":	2019
     	},
     	"DSTRule":	"Off",
     	"DSTStart":	{
     		"Day":	11,
     		"Hour":	0,
     		"Minute":	0,
     		"Month":	3,
     		"Week":	0,
     		"Year":	2019
     	},
     	"DateFormat":	"YYMMDD",
     	"DateSeparator":	"-",
     	"Language":	"SimpChinese",
     	"TimeFormat":	"24",
     	"VideoFormat":	"PAL",
     	"WorkDay":	62
     },
     "Name":	"General.Location",
     "SessionID":	"0x000000006e"
    }
    ``` <br>

# 【获取YUV数据】
## 打开视频

<table >
<tr><td style="background-color:#ccc;text-align:center;width:35px;">定义</td><td colspan="2">int MediaRealPlay(int hUser, String devId, int  nChnIndex, int  nStreamType, Object hWnd, int  nSeq)</td></tr>
<tr><td style="background-color:#ccc;text-align:center">描述</td><td colspan="2">播放设备通道实时视频</td></tr>
<tr><td rowspan="6" style="background-color:#ccc;text-align:center">参数说明</td><td style="background-color:#ccc;text-align:center;width:20%;">名称</td><td style="background-color:#ccc;text-align:center">说明</td></tr>
<tr><td style="text-align:center">devId</td><td>设备序列号</td></tr>
<tr><td style="text-align:center">nChnIndex</td>
<td>通道号（从0开始）</td></tr>
<tr><td style="text-align:center">nStreamType</td>
<td>码流类型；0：主码流；1：子码流1</td></tr>
<tr><td style="text-align:center">hWnd</td>
<td>显示窗口GLSurfaceView20对象</td></tr>
<tr><td style="text-align:center">nSeq</td>
<td>操作序列号（用户自定义）</td></tr>
<tr><td style="background-color:#ccc;text-align:center">返回</td>
<td colspan="2">视频播放对象句柄，可用来实现本视频的开始、抓图、录像等操作</td></tr>
<tr><td rowspan="3" style="background-color:#ccc;text-align:center">结果消息
</td><td style="background-color:#ccc;text-align:center;width:20%;">名称</td><td style="background-color:#ccc;text-align:center;">说明
</td></tr>
<tr><td style="text-align:center">id</td>
<td>消息值：EUIMSG   .EMSG_START_PLAY</td></tr>
<tr><td style="text-align:center">arg1
</td><td>==EE_OK：成功；<0：失败，详见错误码说明</td></tr>
</table>

## 获取YUV数据
<table >
<tr><td style="background-color:#ccc;text-align:center;width:35px;">定义</td><td colspan="2">int SetIntAttr(int hObj, int nId, int nValue)</td></tr>
<tr><td style="background-color:#ccc;text-align:center">描述</td><td colspan="2">获取YUV数据</td></tr>
<tr><td rowspan="4" style="background-color:#ccc;text-align:center">参数说明</td><td style="background-color:#ccc;text-align:center;width:20%;">名称</td><td style="background-color:#ccc;text-align:center">说明</td></tr>
<tr><td style="text-align:center">hObj</td><td>播放句柄</td></tr>
<tr><td style="text-align:center">nId</td>
<td>EOA_MEDIA_YUV_USER</td></tr>
<tr><td style="text-align:center">nValue</td>
<td>回调ID</td></tr>
<tr><td rowspan="3" style="background-color:#ccc;text-align:center">结果消息
</td><td style="background-color:#ccc;text-align:center;width:20%;">名称</td><td style="background-color:#ccc;text-align:center;">说明
</td></tr>
<tr><td style="text-align:center">id</td>
<td>消息值：EUIMSG.ON_YUV_DATA</td></tr>
<tr><td style="text-align:center">arg1
</td><td>==EE_OK：成功；<0：失败，详见错误码说明</td></tr>
</table>


## FunSDKDemo例子：
```
//如果只要获取YUV数据 不需要显示的话 第五个参数 传null就可以了 其他参数和实时播放调用一样
int playHandle = FunSDK.MediaRealPlay(userId, funDevice.getDevSn(),0, SDKCONST.StreamType.Main, null,0);
FunSDK.SetIntAttr(playHandle,EOA_MEDIA_YUV_USER,userId);
```

```
case EUIMSG.ON_YUV_DATA:
  //Todo         
  break;
```
