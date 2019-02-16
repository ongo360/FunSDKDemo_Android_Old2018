/**
 * Android_NetSdk
 * SDK_NetWifiConfig.java
 * Administrator
 * TODO
 * 2014-12-11
 */
package com.lib.sdk.struct;

import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_NetWifiConfig.java
 * @author huangwanshui
 * 2014-12-11
 */
public class SDK_NetWifiConfig {
	public boolean st_00_bEnable;
	public byte st_01_sSSID[] = new byte[36];            //SSID Number
	public byte st_02_arg0[] = new byte[3];
	public int st_03_nChannel;                   		 //channel
	public byte st_04_sNetType[] = new byte[32];         //Infra, Adhoc
	public byte st_05_sEncrypType[] = new byte[32];      //NONE, WEP, TKIP, AES
	public byte st_06_sAuth[] = new byte[32];            //OPEN, SHARED, WEPAUTO, WPAPSK, WPA2PSK, WPANONE, WPA, WPA2
	public int  st_07_nKeyType;                  		 //0:Hex 1:ASCII
	public byte st_08_sKeys[] = new byte[SDKCONST.NET_IW_ENCODING_TOKEN_MAX];
	public CONFIG_IPAddress st_09_HostIP = new CONFIG_IPAddress();		///< host ip
	public CONFIG_IPAddress st_10_Submask = new CONFIG_IPAddress();		///< netmask
	public CONFIG_IPAddress st_11_Gateway = new CONFIG_IPAddress();		///< gateway
}
