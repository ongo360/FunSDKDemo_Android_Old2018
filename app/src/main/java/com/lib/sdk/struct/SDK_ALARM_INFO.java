package com.lib.sdk.struct;

public class SDK_ALARM_INFO {
	public int st_0_nChannel;
	public int st_1_iEvent; // 报警事件码:见枚举SDK_EventCodeTypes
	public int st_2_iStatus;// 0:报警开始，1:报警结束
	public SDK_SYSTEM_TIME st_3_time = new SDK_SYSTEM_TIME();// 报警发生时间

	public static final int VIDEO_MOTION = 4;
	public static final int VIDEO_LOSS = 5;
	public static final int VIDEO_BLIND = 6;
	public static final int Storage_Not_Exist = 10;

	public static final int Storage_Failure = 11;

	public static final int Storage_Low_Space = 12;

	public static final int Video_Analyze = 25; // 智能分析

	public static final int IPC_Alarm = 26;

}
