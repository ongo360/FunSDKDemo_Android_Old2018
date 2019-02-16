/**
 * Android_NetSdk
 * SDK_NetWifiDeviceAll.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk
 * SDK_NetWifiDeviceAll.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_NetWifiDeviceAll {
	public int st_0_nDevNumber;
	public SDK_NetWifiDevice st_1_vNetWifiDeviceAll[] = new SDK_NetWifiDevice[32];
	public SDK_NetWifiDeviceAll() {
		for(int i = 0; i < 32; ++i)
			st_1_vNetWifiDeviceAll[i] = new SDK_NetWifiDevice();
	}
}
