/**
 * Android_NetSdk
 * SDK_DVR_WORKSTATE.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_DVR_WORKSTATE.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_DVR_WORKSTATE {
	public SDK_DVR_CHANNELSTATE st_0_vChnState[];
	public SDK_DVR_ALARMSTATE st_1_vAlarmState;
	public SDK_DVR_WORKSTATE() {
		st_0_vChnState = new SDK_DVR_CHANNELSTATE[SDKCONST.NET_MAX_CHANNUM];
		for(int i = 0 ; i < SDKCONST.NET_MAX_CHANNUM; ++i)
			st_0_vChnState[i] = new SDK_DVR_CHANNELSTATE();
		st_1_vAlarmState = new SDK_DVR_ALARMSTATE();
	}
}
