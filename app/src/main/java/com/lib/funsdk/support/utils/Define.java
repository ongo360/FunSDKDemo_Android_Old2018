package com.lib.funsdk.support.utils;


import com.lib.SDKCONST.DEVICE_TYPE;

public class Define {

	public static final String HELP_SPORTS_REVIEW = "help_sposrts_view_is_clicked";

	public static final String HELP_SPORTS_VIDEO_REVIEW = "help_sposrts_video_view_is_clicked";

	public static final String HELP_SPORTS_IMAGE_REVIEW = "help_sposrts_image_view_is_clicked";

	public static final String USER_IS_REMOEBER_PWD = "user_is_remember_pwd";

	public static final String USER_IS_AUTO_LOGIN = "user_is_auto_login";

	public static final String USER_IS_CHECKED_AUTO_LOGIN = "user_is_checked_auto_login";

	public static final String USER_USERNAME = "user_username";

	public static final String USER_PASSWORD = "user_password";

	public static final String AUTO_USER_USERNAME = "auto_user_username";

	public static final String AUTO_USER_PASSWORD = "auto_user_password";

	public static final String DEVICE_DECODING_TYPE = "device_decoding_type";

	public static final String GUIDE_ACTIVITY = "guide_activity";

	public static final String AUTO_DL_UPGRADE_TYPE = "auto_dl_upgrade_type";

	public static final String DEVICE_PUSH_PREFIX = "device_push_";

	public static final String WIFI_PASSWORD_PREFIX = "wifi_pwd_";

	public static final String SOCKET_AUTO_SET_PROMPT = "socket_autoset_prompt_enable";

	public static final String SOCKET_MEDILE_FILE_PROMPT = "media_file_prompt_enable";

	public static final String Get_APP_UPDATE = "get_app_update";

	public static final String APP_UPDATE_ENABLE = "app_update_enable";

	public static final String APP_UPDATE_TITLE = "app_update_title";

	public static final String APP_UPDATE_TYPE = "app_update_type";

	public static final String APP_UPDATE_URL = "app_update_url";

	public static final String LOG_SHOW = "app_log_show";

	public static final String TEMP_DOWNLOAD_FILE_PREFIX = "temp";

	public static final String SP_LOGGING_UI = "logging_ui";

	public static final String SP_LOGGING_FILE = "logging_file";

	public static final String SP_LOGGING_NET = "logging_net";

	public static final String SP_LOGGING_SERVER = "logging_server";

	public static final String SP_LOGGING_PORT = "logging_port";

	public static final String AUTO_DOWNLOAD = "automatic_download";

	public static final String FIRST_QUICK_CONFIG = "first_quick_config";

	public static final String ROUTER_WIFI_SSID = "router_ssid";

	public static final String XMJP_WIFI_SSID = "xmjp_ssid";

	public static final int LOGIN_NONE = 0;

	public static final int LOGIN_BY_INTENTT = 1;

	public static final int LOGIN_BY_LOCAL = 2;

	public static final int LOGIN_BY_AP = 3;

	public static final int LOGIN_BY_TEST = 4;

	public static final int MEDIA_TYPE_DEVICE = 0;

	public static final int MEDIA_TYPE_CLOUD = 1;

	public static final int AUTO_DL_UPGRADE_NONE = 0;

	public static final int AUTO_DL_UPGRADE_WIFI = 1;

	public static final int AUTO_DL_UPGRADE_ALL = 2;

	public static final int ON_DOWNLOAD_IN_DOWNLOAD_LIST = -1;

	public static final int ON_DOWNLOAD_FAILED = 0;

	public static final int ON_DOWNLOAD_START = 1;

	public static final int ON_DOWNLOADING = 2;

	public static final int ON_DOWNLOAD_COMPLETE = 3;

	public static final int ON_DOWNLOAD_STOP = 4;

	public static final int ON_DOWNLOAD_PREPARE = 5;

	public static final int IMG = 1;

	public static final int VIDEO = 2;

	public static final int DEVICE_ADD_FLAG = 0;

	public static final int DEVICE_DELETE_FLAG = 1;

	public static final int LOGGING_NONE_MSG = 0; // 不输出

	public static final int LOGGING_UI_MSG = 1; // 日志输出到手机日志页面

	public static final int LOGGING_FILE_MSG = 2; // 写到日志文件

	public static final int LOGGING_NET_MSG = 4; // 输出到网络端

	public static final String DEFAULT_LOGGING_IP = "123.59.14.61";

	public static final int DEFAULT_LOGGING_PORT = 9911;

	public static final int MODE_NIGHT = 0;

	public static final int MODE_RELAX = 1;

	public static final int MODE_READ = 2;

	public static final int MODE_MOVIE = 3;

	public static final int MODE_DELAY = 4;

	public static final int MODE_SLEEP = 5;

	public static final int MODE_TIMING = 6;

	public static final int MODE_CHANGE = 7;

	public static final float WND_SCALE_16_9 = 16.0f / 9.0f;// 宽 高比例

	public static final float WND_SCALE_4_3 = 4.0f / 3.0f;// 宽 高比例

	public static final float WND_SCALE_2048_360 = 2048.0f / 360.0f;// 宽 高比例

	public static final float WND_SCALE = WND_SCALE_16_9;

	public static final float FISHEYEWND_SCALE = WND_SCALE_2048_360;

	/**
	 * 这些版本有对讲功能
	 */
	public static final boolean IsSupportTalk(int devType) {
		switch (devType) {
		case DEVICE_TYPE.BEYE:
		case DEVICE_TYPE.BOB:
		case DEVICE_TYPE.CAR:
		case DEVICE_TYPE.FEYE:
		case DEVICE_TYPE.ROBOT:
		case DEVICE_TYPE.SEYE:
		case DEVICE_TYPE.MONITOR:
			return true;
		default:
			return false;
		}
	}

	// TODO 是否支持小码流
	public static final boolean IsSupportSmallStream(int devType) {
		switch (devType) {
		// case DEVICE_TYPE.BEYE: 杨总要求去掉这个判断
		case DEVICE_TYPE.MOV:
			return true;
		default:
			return false;
		}
	}

	public static final int getDevSettingType(int devType) {
		int type;
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BULB:
		case DEVICE_TYPE.BULB_SOCKET:
			type = devType;
			break;
		default:
			type = DEVICE_TYPE.MONITOR;
		}
		return type;
	}

	/**
	 * 是否支持鱼眼设置
	 */
	public static final boolean IsSupportFeyeSet(int devType) {
		switch (devType) {
		case DEVICE_TYPE.FEYE:
		case DEVICE_TYPE.FBULB:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 是否支持亮度调节
	 */
	public static final boolean IsSupportBrightness(int devType) {
		switch (devType) {
		case DEVICE_TYPE.FBULB:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 支持本地升级
	 * 
	 * @param devType
	 * @return
	 */
	public static final boolean IsSupportLocalDevUpgrade(int devType) {
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BULB:
		case DEVICE_TYPE.BULB_SOCKET:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 是否支持云台
	 * 
	 * @param devType
	 * @return
	 */
	public static final boolean IsSupportPTZ(int devType) {
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BOB:
		case DEVICE_TYPE.BEYE:
		case DEVICE_TYPE.FBULB:
		case DEVICE_TYPE.FEYE:
		case DEVICE_TYPE.CAR:
		case DEVICE_TYPE.BULB_SOCKET:
		case DEVICE_TYPE.MOV:
		case DEVICE_TYPE.SEYE:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 是否支持监控预览
	 * 
	 * @param devType
	 * @return
	 */
	public static final boolean IsSupportMonitor(int devType) {
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BULB_SOCKET:
		case DEVICE_TYPE.BULB:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 
	 * @Title: IsSupportPushMsg
	 * @Description: TODO(是否支持推送功能)
	 */
	public static final boolean IsSupportPushMsg(int devType) {
		switch (devType) {
		case DEVICE_TYPE.BULB_SOCKET:
		case DEVICE_TYPE.BULB:
		case DEVICE_TYPE.MOV:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 是否支持debug模式
	 * 
	 * @param devType
	 * @return
	 */
	public static final boolean IsSupportDebug(int devType) {
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BULB_SOCKET:
		case DEVICE_TYPE.BULB:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 
	 * @Title: IsSupportRemoteCtrl
	 * @Description: TODO(是否支持遥控配置)
	 */
	public static final boolean IsSupportRemoteCtrl(int devType,
			int devExpandType) {
		devType = devExpandType == 0 ? devType
				: (devType * 100 + devExpandType);
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BULB_SOCKET:
		case DEVICE_TYPE.BULB:
		case DEVICE_TYPE.FBULB:
		case DEVICE_TYPE.MUSIC_BOX:
		case DEVICE_TYPE.SPEAKER:
		case DEVICE_TYPE.NSEYE:
		case DEVICE_TYPE.ROBOT:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 是否支持远程回放和图片下载
	 */
	public static final boolean IsSupportSDCard(int devType,
			int devExpandType) {
		devType = devExpandType == 0 ? devType
				: (devType * 100 + devExpandType);
		switch (devType) {
		case DEVICE_TYPE.NSEYE:
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.BULB:
		case DEVICE_TYPE.BULB_SOCKET:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 
	 * @Title: IsSupportDeviceLight
	 * @Description: TODO(是否支持设备指示灯)
	 */
	public static final boolean IsSupportDeviceLight(int devType) {
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
		case DEVICE_TYPE.CAR:
		case DEVICE_TYPE.BEYE:
		case DEVICE_TYPE.SEYE:
		case DEVICE_TYPE.ROBOT:
		case DEVICE_TYPE.FEYE:
		case DEVICE_TYPE.BOB:
			// case DEVICE_TYPE.MOV: 运动相机，放到功能配置里头了 以后APP发布了，删除此代码
			return true;
		default:
			return false;
		}
	}

	/**
	 * 
	 * @Title: IsSupportDeviceRecord
	 * @Description: TODO(是否可以修改录像配置)
	 */
	public static final boolean IsSupportRecordConfig(int devType) {
		switch (devType) {
		case DEVICE_TYPE.MOV:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 
	 * @Title: IsSupportDevModifyPwd
	 * @Description: TODO(是否支持修改设备密码)
	 */
	public static final boolean IsSupportDevModifyPwd(int devType) {
		switch (devType) {
		case DEVICE_TYPE.MOV:
			return false;
		default:
			return true;
		}
	}

	/**
	 * 是否支持远程夜灯和拍照
	 */
	public static final boolean IsSupportLightAndCap(int devType) {
		switch (devType) {
		case DEVICE_TYPE.SOCKET:
			return true;
		default:
			return false;
		}
	}

	public static final String[] CAN_TALK_DEVICE_LIST = { "07810", "07510",
			"07531", "14840", "07510", "07531", "12502", "07518", "7832" };

	public static final String[] STREAM_DEVICE_LIST = { "10842", "14841",
			"14840" };

	// public static final String[] WIFI_FIND_PREFIX = { "seye_", "car",
	// "robot_",
	// "Robot_", "card_", "NVR_", "DVR_", "beye_", "IPC_", "BOB_", "Car",
	// "IPC", "IPC_", "mov_" };

	// public static final String[] DEVICE_TYPE = { "seye", "beye", "socket",
	// "light", "yellow" };

	public enum EncrypType {
		WPA2_CCMP, WPA2_TKIP, WPA_TKIP, WPA_CCMP, WEP, NONE
	}
}
