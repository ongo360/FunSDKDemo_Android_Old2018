package com.lib.sdk.struct;

public class SDK_CloudRecordConfig {
	public int st_0_Enable; /// < 是否开启 用掩码表示0全不存，1是定时存，2报警存，3定时&& 报警
	public int st_1_StreamType; // 码流类型，0主码流，1辅码流
	public int st_2_AlarmRecTypeMsk; // 需要上传录像的报警类型，AlarmRecType类型掩码 3
	public SDK_CONFIG_WORKSHEET st_3_wcWorkSheet = new SDK_CONFIG_WORKSHEET(); // 普通录像需存储的时间段

	@Override
	public String toString() {
		return "SDK_CloudRecordConfig [st_0_Enable=" + st_0_Enable + ", st_1_StreamType=" + st_1_StreamType
				+ ", st_2_AlarmRecTypeMsk=" + st_2_AlarmRecTypeMsk + ", st_3_wcWorkSheet=" + st_3_wcWorkSheet
				+ "]";
	}

}
