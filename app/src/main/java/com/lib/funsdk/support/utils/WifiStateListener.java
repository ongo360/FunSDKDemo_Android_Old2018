/**
 * FutureFamily
 * WifiStateListener.java
 * Administrator
 * TODO
 * 2015-6-23
 */
package com.lib.funsdk.support.utils;

import android.net.NetworkInfo;

/**
 * FutureFamily WifiStateListener.java
 * 
 * @author huangwanshui TODO 2015-6-23
 */
public interface WifiStateListener {
	public static final int DISCONNECT = 0;// 网络断开
	public static final int CONNECTING = 1;// 正在连接
	public static final int CONNECTED = 2;// 网络连接成功

	/**
	 * 网络状态
	 *
	 * @param state 状态
	 * @param type  网络类型 0: WIFI 1:Mobile
	 * @param ssid  WiFi热点名称
	 */
	public abstract void onNetWorkState(NetworkInfo.DetailedState state, int type, String ssid);

	public abstract void onIsWiFiAvailable(boolean isWiFiAvailable);

	void onNetWorkChange(NetworkInfo.DetailedState state, int type, String SSid);
}
