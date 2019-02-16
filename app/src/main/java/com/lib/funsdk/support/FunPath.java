package com.lib.funsdk.support;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.basic.G;
import com.lib.funsdk.support.utils.FileDataUtils;
import com.lib.funsdk.support.utils.StringUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.SDK_SYSTEM_TIME;


public class FunPath {

	public static String DEFAULT_PATH;
	public static String PATH_PHOTO;
	public static String PATH_PHOTO_TEMP;
	public static String PATH_LOGO;
	public static String PATH_CAPTURE_TEMP;
	public static String PATH_SPT_TEMP;// 运动伴侣临时文件目录
	public static String PATH_SPT;
	public static String PATH_SPT_SHOT;// 运动伴侣延时拍、连拍 转MP4 临时原文件目录，生成MP4后要删除该目录
	public static String PATH_VIDEO;
	public static String PATH_PUSH_PHOTO;
	public static String PATH_LOG;
	public static String PATH_DEVICES_INFO_FILE;
	public static String PATH_ORI_VIDEO;// 原始录像目录
	public static String PATH_MUSIC;// 音乐目录
	
	public static String PATH_LOCAL_DB;//  本地登录DB路径
	
	public static String PATH_ALARM_NOGIFY;	// 保存报警通知设备列表
	public static String PATH_LOGIN_HISTORY;	// 保存历史登录用户
	public static String PATH_WIFI_PASSWORD;	// 保存使用过的WiFi密码
	public static String PATH_DEVICE_PASSWORD;	// 保存的设备登录密码
	
	public static String PATH_DOWNLOAD_CACHE;	// 网络下载图片的缓存目录
	
	public static String PATH_LOCAL_DEVICE_PASSWORD; //库函数实现本地保存密码路径
	public static String PATH_DEVICE_UPDATE_FILE_PATH; //设备升级文件保存路径
	public static String PATH_DEVICE_CONFIG_PATH; //保存SDK相关配置文件路径
	
	public static void init(Context context, String rootDirName) {
		DEFAULT_PATH = getMediaPath(context) 
				+ File.separator
				+ rootDirName
				+ File.separator;
		
		PATH_PHOTO = DEFAULT_PATH + "snapshot";
		PATH_PHOTO_TEMP = DEFAULT_PATH + "temp_images";
		PATH_LOGO = DEFAULT_PATH + "logo";
		PATH_CAPTURE_TEMP = DEFAULT_PATH + "temp_capture";
		PATH_VIDEO = DEFAULT_PATH + "videorecord";
		PATH_PUSH_PHOTO = DEFAULT_PATH + "push" + File.separator + "img";
		PATH_LOG = DEFAULT_PATH + "log";
		PATH_SPT = DEFAULT_PATH + "sport";
		PATH_ORI_VIDEO = PATH_VIDEO + File.separator + "original";
		PATH_MUSIC = DEFAULT_PATH + "music/";
		PATH_DEVICES_INFO_FILE = DEFAULT_PATH + "ConfigPath/ap.txt";
		PATH_LOCAL_DB = DEFAULT_PATH + "DBFile.db";
		
		PATH_ALARM_NOGIFY = DEFAULT_PATH + "alarm.txt";
		PATH_LOGIN_HISTORY = DEFAULT_PATH + "login.txt";
		PATH_WIFI_PASSWORD = DEFAULT_PATH + "wifi.txt";
		PATH_DEVICE_PASSWORD = DEFAULT_PATH + "devpasswd.txt";
		
		PATH_DOWNLOAD_CACHE = DEFAULT_PATH + "cache";
		
		PATH_LOCAL_DEVICE_PASSWORD = DEFAULT_PATH + "ConfigPath/password.txt";
		PATH_DEVICE_UPDATE_FILE_PATH = DEFAULT_PATH + "UpgradeFiles/";
		PATH_DEVICE_CONFIG_PATH = DEFAULT_PATH + "devSDK/";
		
		onCreatePath();
	}
	
	public static String getDefaultPath() {
		return DEFAULT_PATH;
	}
	
	public static String getDeviceApPath() {
		return PATH_DEVICES_INFO_FILE;
	}
	
	public static String getLocalDB() {
		return PATH_LOCAL_DB;
	}
	
	public static String getAlarmNotifyPath() {
		return PATH_ALARM_NOGIFY;
	}
	
	public static String getLoginHistoryPath() {
		return PATH_LOGIN_HISTORY;
	}
	
	public static String getWifiPasswordPath() {
		return PATH_WIFI_PASSWORD;
	}
	
	public static String getDevicePasswordPath() {
		return PATH_DEVICE_PASSWORD;
	}
	
	public static void onCreatePath() {
		File pFile = new File(DEFAULT_PATH);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_PHOTO);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_PHOTO_TEMP);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_LOGO);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_CAPTURE_TEMP);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_VIDEO);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_PUSH_PHOTO);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_LOG);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_SPT);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_ORI_VIDEO);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_MUSIC);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_DOWNLOAD_CACHE);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
	}
	
	/**
	 * @param sn 序列号
	 * @Title: onCreateTempPath
	 * @Description: TODO(创建临时文件目录，主要存放缩略图)
	 */
	public static void onCreateSptTempPath(String sn) {
		PATH_SPT_TEMP = PATH_SPT + File.separator + sn;
		File pFile = new File(PATH_SPT);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
		pFile = new File(PATH_SPT_TEMP);
		if (pFile != null && !pFile.exists())
			makeRootDirectory(pFile.getPath());
	}
	
	public static String getMediaPath(Context context) {
		String path = "";
		File dirFile = null;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			dirFile = Environment.getExternalStorageDirectory();
//			if (dirFile == null || !dirFile.canWrite() || !dirFile.canRead()) {
//				dirFile = context.getExternalFilesDir(null);
//			}
//		} else {
			String exStorageState = Environment.getExternalStorageState();
			if (exStorageState == null || exStorageState.equals(Environment.MEDIA_MOUNTED)
					|| exStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
				dirFile = Environment.getExternalStorageDirectory();
			} else {
				dirFile = context.getExternalFilesDir(null);
			}
//		}

		if (dirFile == null) {
			dirFile = context.getFilesDir();
		} else {
			path = dirFile.getAbsolutePath();
		}

		return path == null ? "" : path;
	}
	
	public static boolean makeRootDirectory(String filePath) {
		File file = null;
		String newPath = null;
		String[] path = filePath.split("/");
		for (int i = 0; i < path.length; i++) {
			if (newPath == null) {
				newPath = path[i];
			} else {
				newPath = newPath + "/" + path[i];
			}
			if (StringUtils.isStringNULL(newPath))
				continue;
			file = new File(newPath);
			if (!file.exists()) {
				return file.mkdir();
			}
		}
		return true;
	}
	
	public static String getSptTempPath() {
		return PATH_SPT_TEMP;
	}
	
	public static String getTime(SDK_SYSTEM_TIME tm, int type) {
		if (type == 0) {
			return String.format("%04d%02d%02d%02d%02d%02d", tm.st_0_year,
					tm.st_1_month, tm.st_2_day, tm.st_4_hour, tm.st_5_minute, tm.st_6_second);
		} else if (type == 1) {
			return String.format("%04d-%02d-%02d %02d:%02d:%02d", tm.st_0_year,
					tm.st_1_month, tm.st_2_day, tm.st_4_hour, tm.st_5_minute, tm.st_6_second);
		} else {
			return "";
		}
	}
	
	// 未来避免时间单位最小为秒，达不到毫秒级别时候，图片无法显示的问题
	public static String getDownloadFileNameByData(H264_DVR_FILE_DATA data, int type, boolean bThumbnail) {
		StringBuffer sb = new StringBuffer();
		if (null != data) {
			sb.append(getTime(data.st_3_beginTime, 0));
			sb.append("_");
			sb.append(getTime(data.st_4_endTime, 0));
			if (type == 1) {
				sb.append("_");
				int orderNum = FileDataUtils.getOrderNum(G.ToString(data.st_2_fileName), 1);
				sb.append(orderNum);
				if (bThumbnail) {
					sb.append("_thumb");
				}
				sb.append(".jpg");
			} else if (type == 0) {
				sb.append("_");
				sb.append(data.st_6_StreamType);
				if (bThumbnail)
					sb.append("_thumb");
				sb.append(".mp4");
			}
		}
		return sb.toString();
	}
	
	public static long isFileExists(String path) {
		File file = new File(path);
		if ( file.exists() ) {
			return file.length();
		} else {
			return 0;
		}
	}
	
	public static boolean deleteFile(String fileStr) {
		if (!TextUtils.isEmpty(fileStr)) {
			File file = new File(fileStr);
			if (null != file && file.exists()) {
				file.delete();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static boolean isValidPath(String path) {
		return null != path && path.length() > 0;
	}
	
	/**
	 * 获取一个截图保存的路径
	 * @return
	 */
	public static String getCapturePath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(new Date());
        strDate = strDate + System.currentTimeMillis();
        return FunPath.PATH_CAPTURE_TEMP + File.separator + strDate + ".jpg";
	}

    public static String getTempPicPath(){
        return FunPath.PATH_PHOTO + File.separator + "temp" + ".jpg";
    }

    public static String getRecordPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(new Date());
        return FunPath.PATH_VIDEO + File.separator + strDate + ".mp4";
    }
    
    public static String getConfigPassword() {
    	return PATH_LOCAL_DEVICE_PASSWORD;
    }
    
    public static String getDeviceUpdatePath(){
    	return PATH_DEVICE_UPDATE_FILE_PATH;
    }
    public static String getDeviceConfigPath(){
    	return PATH_DEVICE_CONFIG_PATH;
    }   
}

