/**
 * Android_NetSdk
 * SDK_AlarmOutConfigAll.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_AlarmOutConfigAll.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_AlarmOutConfigAll {
	public SDK_AlarmOutConfig st_0_vAlarmOutConfigAll[] = new SDK_AlarmOutConfig[SDKCONST.NET_MAX_CHANNUM];
	public SDK_AlarmOutConfigAll() {
		for(int i = 0 ; i < SDKCONST.NET_MAX_CHANNUM;++i)
			st_0_vAlarmOutConfigAll[i] = new SDK_AlarmOutConfig();
	}
}
