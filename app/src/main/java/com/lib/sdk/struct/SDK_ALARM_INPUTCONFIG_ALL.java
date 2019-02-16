/**
 * Android_NetSdk
 * SDK_ALARM_INPUTCONFIG_ALL.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_ALARM_INPUTCONFIG_ALL.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_ALARM_INPUTCONFIG_ALL {
	public SDK_ALARM_INPUTCONFIG st_0_vAlarmConfigAll[] = new SDK_ALARM_INPUTCONFIG[SDKCONST.NET_MAX_CHANNUM];
	public SDK_ALARM_INPUTCONFIG_ALL() {
		for(int i = 0 ; i < SDKCONST.NET_MAX_CHANNUM; ++i)
			st_0_vAlarmConfigAll[i] = new SDK_ALARM_INPUTCONFIG();
	}
}
