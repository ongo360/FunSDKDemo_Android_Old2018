package com.lib.sdk.bean;

/**
 * 
 * @ClassName: JSONCONFIG
 * @Description: TODO(各配置名称字符)
 * @author xxy
 * @date 2016年3月19日 下午4:59:32
 * 
 */

public class JsonConfig {

	public static final String FAST_JSON_OBJECT = "com.alibaba.fastjson.JSONObject"; // 反射得到对象的类名
	public static final String FAST_JSON_ARRAY = "com.alibaba.fastjson.JSONArray"; // 反射得到对象的类名
	public static final String CAMERA_PARAM = "Camera.Param"; // 摄像机参数
	public static final String CAMERA_PARAMEX = "Camera.ParamEx"; // 摄像机扩展参数
	public static final String CAMERA_CLEARFOG = "Camera.ClearFog"; // 摄像机去雾
	public static final String OPSTORAGE_MANAGER = "OPStorageManager"; // 格式化内存卡
	public static final String SYSTEM_INFO = "SystemInfo"; // 系统信息
	public static final String STATUS_NATINFO = "Status.NatInfo";// nat 状态
	public static final String DETECT_MOTIONDETECT = "Detect.MotionDetect"; // 移动侦测
	public static final String DETECT_BLINDDETECT = "Detect.BlindDetect"; // 视频遮挡
	public static final String DETECT_LOSSDETECT = "Detect.LossDetect"; // 视频丢失
	public static final String SIMPLIFY_ENCODE = "Simplify.Encode"; // 编码配置
	public static final String ENCODE_CAPABILITY = "EncodeCapability"; // 编码能力
	public static final String SUPPORT_EXTRECORD = "SupportExtRecord"; // 支持主辅码流录像的能力
	public static final String RECORD = "Record"; // 主码流录像配置
	public static final String EXRECORD = "ExtRecord"; // 辅码流录像配置
	public static final String USERS = "Users"; // 用户信息（用户名密码，权限等）
	public static final String MODIFY_PASSWORD = "ModifyPassword"; // 修改密码
	public static final String SENSOR_DETECT = "Consumer.ConsSensorAlarm";
	public static final String STORAGE_INFO = "StorageInfo"; // 存储配置
	public static final String GENERAL_GENERAL = "General.General"; // 普通配置
	public static final String GENERAL_LOCATION = "General.Location"; // 普通配置
	public static final String SYSTEM_TIMEZONE = "System.TimeZone";// 时区配置
	public static final String OPTIME_SET = "OPTimeSetting";// 时间设置
	public static final String OPTIME_QUERY = "OPTimeQuery";// 获取时间配置
	public static final String SYSTEM_FUNCTION = "SystemFunction";// 能力集
	public static final String AVEnc_EncodeEx_3 = "AVEnc.EncodeEx.Dst3irdFmt";// 第三码流
	public static final String AVEnc_EncodeEx_4 = "AVEnc.EncodeEx.Dst4irdFmt";// 第四码流
	public static final String OPERATION_MACHINE = "OPMachine";
	public static final String OPERATION_DEFAULT_CONFIG = "OPDefaultConfig";// 恢复默认配置
	public static final String OPERATION_PTZ = "OPPTZControl";
	public static final String OPERATION_MONITOR = "OPMonitor";
	public static final String OPERATION_PLAYBACK = "OPPlayBack";
	public static final String OPERATION_TALK = "OPTalk";
	public static final String OPERATION_DISK_MANAGER = "OPStorageManager";
	public static final String OPERATION_LOG_MANAGER = "OPLogManager";
	public static final String OPERATION_SYSTEM_UPGRADE = "OPSystemUpgrade";
	public static final String OPERATION_FILE_QUERY = "OPFileQuery";
	public static final String OPERATION_LOG_QUERY = "OPLogQuery";
	public static final String OPERATION_TIME_SETTING = "OPTimeSetting";
	public static final String OPERATION_NET_KEYBOARD = "OPNetKeyboard";
	public static final String OPERATION_NET_ALARM = "OPNetAlarm";
	public static final String OPERATION_SNAP = "OPSNAP";
	public static final String OPERATION_TRANS = "OPTrans";
	public static final String OPERATION_UPDATA = "OPTUpData";
	public static final String OPERATION_TIME_SETTING_NORTC = "OPTimeSettingNoRTC";
	public static final String OPERATION_CPCDATA = "OPCPCData";
	public static final String OPERATION_LOCALSEARCH = "OPLocalSearch";
	public static final String OPERATION_MAILTEST = "OPMailTest";
	public static final String OPERATION_PHONE = "OPPhone";
	public static final String OPERATION_STORAGE = "OPNetSerach";// Ó²ÅÌÈÝÁ¿¡¢Â¼Ïñ×´Ì¬¡¢Â¼ÏñÊ±¼ä²éÑ¯
	public static final String OPERATION_FILE_CONTRAL = "OPNetFileContral";
	public static final String OPERATION_MUSICFILE_QUERY = "OPMusicFileQuery";
	public static final String OPERATION_MUSIC_PLAY = "OPMusicPlay";
	public static final String OPERATION_DIG_SETIP = "OPDIGSetIP";
	public static final String OPERATION_UTC_TIME_SETTING = "OPUTCTimeSetting";
	public static final String OPERATION_SET_OSDINFO = "OPSetOSDInfo";
	public static final String OPERATION_SET_OSDINFO_V2 = "OPSetOSD";// OSDÐÅÏ¢µþ¼Ó£¬²»±£´æÅäÖÃ£¬µ¥ÐÐ×î¶àµþ¼Ó31¸öºº×Ö
	public static final String OPERATION_UPGRADE_VERSION_LIST = "OPVersionList";
	public static final String OPERATION_UPGRADE_VERSION = "OPReqVersion";
	public static final String OPERATION_NEW_UPGRADE_VERSION_REQ = "OPVersionReq"; // ÐÂ°æ±¾ÔÆÉý¼¶²éÑ¯ÇëÇó
	public static final String OPERATION_NEW_UPGRADE_VERSION_REP = "OPVersionRep";// ÐÂ°æ±¾ÔÆÉý¼¶²éÑ¯½á¹û·´À¡
	public static final String OPERATION_NEW_START_UPGRADE = "OPStartUpgradeReq";// ÐÂ°æ±¾ÔÆÉý¼¶ÏÂÔØÇëÇó
	public static final String OPERATION_FTPTEST = "OPFTPTest";
	public static final String OPERATION_SET_LOGO = "OPLogoSetting";
	//以下为智联中心相关命令
	public static final String OPERATION_CONSUMER_PRO_CMD = "OPConsumerProCmd";
	public static final String OPERATION_CMD_GET = "GetAllDevList";
	public static final String OPERATION_CMD_ADD = "StartAddDev";
	public static final String OPERATION_CMD_STOP = "StopAddDev";
	public static final String OPERATION_CMD_DEL = "DeleteDev";
	public static final String OPERATION_CMD_RENAME = "ChangeDevName";
	public static final String OPERATION_CMD_STATUS = "ChangeDevStatus";
	public static final String OPERATION_CMD_CUR_MODE = "GetModeConfig"; //获取当前模式下传感器
	public static final String OPERATION_CMD_MODE_LIST = "GetAllModeList";//获取所模式列表
	public static final String OPERATION_CMD_MODE_RENAME = "ChangeModeName";
	public static final String OPERATION_CMD_CHANGE_MODE = "ChangeMode";
	public static final String OPERATION_CMD_GET_LINK_STATE = "GetLinkState";  //获取在线设备
	public static final String OPERATION_CMD_INQUIRY_STATUS = "InquiryStatus"; //获取信息
	public static final String OPERATION_CMD_CONSOR_ALARM = "SetConsSensorAlarm";//设置智联设备报警联动
	public static final String OPERATION_CMD_GET_CONSOR_ALARM = "Consumer.SensorAlarm";//获取智联报警联动
	public static final String OPERATION_CMD_SET_SWITCH_STATE = "ChangeSwitchState";//改变墙壁开关状态
	public static final String OPERATION_CMD_SET_CURTAIN_STATE = "ChangeCurtainState";//改变窗帘状态
	//---

	public static final String OPERATION_DDNSAPPLY = "OPDDNSAPPLY"; // ddns
	// apply°´¼ü¹¦ÄÜ
	public static final String OPERATION_OPMACHINE = "OPMachine";// 重启/关闭操作

	public static final String OPEARTION_CALENDAR = "OPSCalendar";
	public static final String OPEARTION_COMPRESS_PICTURE = "OPCompressPic";
	public static final String OPEARTION_SPLIT_CONTROL = "OPSplitControl";

	public static final String OPEARTION_MARK_FILE = "MarkFile";
	public static final String CFG_WIFI = "NetWork.Wifi";
	public static final String CFG_WIDEOWIDGET = "AVEnc.VideoWidget";
	public static final String CFG_CHANNELTITLE = "ChannelTitle";
	public static final String CFG_FbExtraStateCtrl = "FbExtraStateCtrl";
	public static final String CFG_PTZ = "Uart.PTZ";
	public static final String CFG_FISH_EYE_PARAM = "Camera.FishEye";
	public static final String CFG_FISH_EYE_PLATFORM = "FishEyePlatform";
	public static final String CFG_ATHORITY = "PowerSocket.Authority";
	public static final String CFG_CLOUD_STORAGE = "NetWork.CloudStorage";
	public static final String CFG_OSD_LOGO = "fVideo.OsdLogo";
	public static final String CFG_KEY_SEF_DEFINE = "System.KeySefDefine";

	public static final String CFG_STORAGE_GLOBAL = "StorageGlobal";// 关键录像配置修改
	public static final String CFG_HIGH_TEMPER_PROTECT = "System.HighTemperProtect";// 高温保护
	public static final String CFG_RECORD_SLOW_MOTION = "System.RecordOrSlowmotion";// 慢动作、录像切换

	public static final String CFG_XMMODE_SWITCH_GET = "XMModeSwitch.Mode";
	public static final String CFG_XMMODE_SWITCH_SET = "XMModeSwitch.ModeIndex";
	public static final String OPTALK = "OPTalk";// 对讲控制
	public static final String OPCOMPRESSPIC = "OPCompressPic";
	public static final String CFG_DETECT_ANALYZE = "Detect.Analyze";
	public static final String CFG_PMS = "NetWork.PMS";
	public static final String DETECT_ANALYZE = "Detect.Analyze";
	public static final String NET_COMMON = "NetWork.NetCommon";
	public static final String WHITE_LIGHT = "Camera.WhiteLight";
	public static final String NETWORK_MODE = "System.NetWorkMode";
	public static final String DEVICE__SUPPORT_LANGUAGE = "MultiLanguage";
	public static final String DEVICE_LANGUAGE = "General.Location.Language";
	public static final String PRODUCTION_ADDRESS = "Camera.FishLensParam";
	public static final String NETWORK_WIFI = "NetWork.Wifi";
	public static final String NETWORK_MESSAGE_PUSH = "NetWork.PMS";
	public static final String SYSTEM_MANAGE_SHUTDOWN = "System.ManageShutDown";
	public static final String ALARM_PIR = "Alarm.PIR";
	public static final String WIFI_ROUTE_INFO = "WifiRouteInfo";

	public static final String SMATR_H264 = "AVEnc.SmartH264";
	public static final String ENCODE_264_ABILITY = "Encode264ability";
	public static final String SMART_H264V2 = "AVEnc.SmartH264V2";
	public static final String DETECT_LOCAL_ALARM ="Alarm.WifiAlarm";

	public static final String OPENBREATH ="System.OpenBreathLamp";
	public static final String CAPTURE_PRIORITY="WorkMode.CapturePriority"; //拍照优先级
	public static final String WIFI_WAKEUP="NetWork.WifiWakeupType";//wifi唤醒
	public static final String PUSH_MSG="NetWork.PushMsg";//电量低推送
	public static final String SET_ENABLE_VIDEO = "NetWork.SetEnableVideo";//录像本地保存
	public static final String STORAGE_SNAPSHOT= "Storage.Snapshot";//拍照本地保存
	//门锁，在app上视为433智联的一种，因此除了下面这几种，也同时又智联中心相关命令（line 77）
	public static final String DOOR_LOCK_CMD = "OPDoorLockProCmd"; //配置头Name,与普通433区分（见OPERATION_CONSUMER_PRO_CMD）
	public static final String DOOR_LOCK_SET_TEMP_PSD= "SetTmpPasswd";//设置临时密码
	public static final String DOOR_LOCK_UNLOCK = "RemoteUnlock";//远程开锁
	public static final String DOOR_LOCK_USER_INFO= "GetUsrInfo";//门锁密码、指纹、门卡等用户信息
	public static final String DOOR_LOCK_CHANGE_NAME = "ChangeUsrName";//修改密码、指纹、门卡的用户昵称
	public static final String DOOR_LOCK_IS_ADDED = "Consumer.IsDoorLockAdded"; //是否已添加门铃

	public static final String IDR_RESERVE_WAKE_UP = "Consumer.ReserveWakeUp"; //门铃来电预约
	public static final String IDR_NO_DISTURB = "Consumer.NoDisturbing";//免打扰
	public static final String OPERATION_ELEC_PTZ = "OPElecPTZControl";
	public static final String INTERVAL_WAKE_UP = "Consumer.IntervalWakeUp";//录像间隔
	public static final String CHANGE_MSG_PUSH_AUTH = "ChangeMessagePushAuth";//修改消息推送权限
	public static final String SET_MSG_STATISTICS = "SetMessageStatistics";//设置消息统计开关
	public static final String CFG_DETECT_TRACK = "Detect.DetectTrack";//人形跟随
	public static final String CFG_DEV_RING_CTRL = "Consumer.DevRingControl";//设备响铃控制
	public static final String CFG_FORCE_SHUT_DOWN_MODE = "Consumer.ForceShutDownMode";//关机控制
	public static final String CFG_NOTIFY_LIGHT = "Consumer.NotifyLight";//设备呼吸灯
	public static final String CFG_DEV_HORN_VOLUME = "fVideo.Volume";//设备喇叭音量
	public static final String CFG_DEV_MIC_VOLUME = "fVideo.InVolume";//设备Mic音量
	public static final String CFG_DEV_APP_BIND_FLAG ="General.AppBindFlag";//获取设备是否处于初始化状态
	public static final String CMD_SYSTEM_INFO_EX = "SystemInfoEx";//扩展系统信息 包括了单片机版本号
	public static final String CMD_ENCYPT_CHIP_INFO = "EncyptChipInfo";//加密芯片信息
	public static final String CFG_VOICE_TIP_TYPE = "Ability.VoiceTipType";//获取报警声种类
	public static final String CFG_BROWSER_LANGUAGE = "BrowserLanguage";//设置报警声音的语言
}
