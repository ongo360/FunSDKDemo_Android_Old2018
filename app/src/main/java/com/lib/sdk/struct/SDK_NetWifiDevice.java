/**
 * Android_NetSdk
 * SDK_NetWifiDevice.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk
 * SDK_NetWifiDevice.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_NetWifiDevice {
	public byte st_0_sSSID[] = new byte[36];            //SSID Number
	public int st_1_nRSSI;                 //SEE SDK_RSSI_SINGNAL
	public int st_2_nChannel;
	public byte st_3_sNetType[] = new byte[32];         //Infra, Adhoc
	public byte st_4_sEncrypType[] = new byte[32];      //NONE, WEP, TKIP, AES
	public byte st_5_sAuth[] = new byte[32];            //OPEN, SHARED, WEPAUTO, WPAPSK, WPA2PSK, WPANONE, WPA, WPA2
}
