package com.lib.funsdk.support.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lib.FunSDK;

public class ParseVersionUtils {
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		if (context == null)
			return null;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return FunSDK.TS("can_not_find_version_name");
		}
	}

	/**
	 * 获取报名
	 * 
	 * @return 当前应用的报名
	 */
	public static String getPackageName(Context context) {
		if (context == null)
			return null;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String _package = info.packageName;
			return _package;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取APP版本
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		if (context == null)
			return 0;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			int versionCode = info.versionCode;
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @Title: getMobibleUUID
	 * @Description: TODO(获取手机UUID)
	 */
	public static String getMobibleUUID() {
		String snstr = "";
		try {
			Process p = Runtime.getRuntime().exec("getprop");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("[net.hostname]")) {
					return line;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return snstr;
	}

	/**
	 * @param devInfo
	 *            设备版本信息
	 * @Title: getDevExpandType
	 * @Description: TODO(获取设备扩展类型，如：直播小雨点)
	 * @return 默认 0 表示通用的类型 V4.02.R12.A6407510.10000.142302.10000
	 */
	public static int getDevExpandType(String devInfo) {
		String[] str = devInfo.split("\\.");
		if (StringUtils.isStringNULL(devInfo) || str.length < 7) {
			return 0;
		}
		
		try {
			return Integer.parseInt(str[6]) / 10000;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
