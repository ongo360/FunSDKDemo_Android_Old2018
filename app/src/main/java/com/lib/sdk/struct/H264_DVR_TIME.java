package com.lib.sdk.struct;

import java.io.Serializable;

public class H264_DVR_TIME  implements Serializable{
	public int st_0_dwYear; // 年
	public int st_1_dwMonth; // 月
	public int st_2_dwDay; // 日
	public int st_3_dwHour; // 时
	public int st_4_dwMinute; // 分
	public int st_5_dwSecond; // 秒

	@Override
	public String toString() {
		return "H264_DVR_TIME [st_0_dwYear=" + st_0_dwYear + ", st_1_dwMonth="
				+ st_1_dwMonth + ", st_2_dwDay=" + st_2_dwDay
				+ ", st_3_dwHour=" + st_3_dwHour + ", st_4_dwMinute="
				+ st_4_dwMinute + ", st_5_dwSecond=" + st_5_dwSecond + "]";
	}

}
