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
	public static final String OPERATION_CONSUMER_PRO_CMD = "OPConsumerProCmd";
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

	public static final String DOOR_LOCK_CMD = "OPDoorLockProCmd"; //配置头Name,与普通433区分（见OPERATION_CONSUMER_PRO_CMD）
	public static final String DOOR_LOCK_SET_TEMP_PSD= "SetTmpPasswd";//设置临时密码
	public static final String DOOR_LOCK_UNLOCK = "RemoteUnlock";//远程开锁
	public static final String DOOR_LOCK_USER_INFO= "GetUsrInfo";//门锁密码、指纹、门卡等用户信息
	public static final String DOOR_LOCK_CHANGE_NAME = "ChangeUsrName";//修改密码、指纹、门卡的用户昵称
	public static final String DOOR_LOCK_IS_ADDED = "Consumer.IsDoorLockAdded"; //是否已添加门铃
}
