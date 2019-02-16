/**
 * FutureFamily
 * WifiStateListener.java
 * Administrator
 * TODO
 * 2015-6-23
 */
package com.lib.funsdk.support.utils;

import android.net.NetworkInfo.State;

/**
 * FutureFamily WifiStateListener.java
 * 
 * @author huangwanshui TODO 2015-6-23
 */
public interface WifiStateListener {
	int DISCONNECT = 0;
	int CONNECTING = 1;
	int CONNECTED = 2;

	/**
	 * 网络状态
	 * 
	 * @param state
	 *            状态
	 * @param type
	 *            网络类型 0: WIFI 1:Mobile
	 * @param ssid
	 *            WiFi热点名称
	 */
	void onNetWorkState(State state, int type, String ssid);
}
