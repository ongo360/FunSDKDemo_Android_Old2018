/**
 * Android_NetSdk
 * SDK_LogList.java
 * Administrator
 * TODO
 * 2014-12-11
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_LogList.java
 * @author huangwanshui
 * TODO
 * 2014-12-11
 */
public class SDK_LogList {
	public int st_0_iNumLog;
	public SDK_LogItem st_1_Logs[] = new SDK_LogItem[SDKCONST.NET_MAX_RETURNED_LOGLIST];
	public SDK_LogList() {
		for(int i = 0 ; i < SDKCONST.NET_MAX_RETURNED_LOGLIST; ++i)
			st_1_Logs[i] = new SDK_LogItem();
	}
}
