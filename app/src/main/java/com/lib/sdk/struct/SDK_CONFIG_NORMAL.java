/**
 * FutureFamily
 * SDK_CONFIG_NORMAL.java
 * Administrator
 * TODO
 * 2015-6-29
 */
package com.lib.sdk.struct;

import java.util.Arrays;

/**
 * FutureFamily SDK_CONFIG_NORMAL.java
 * 
 * @author huangwanshui TODO 2015-6-29
 */
public class SDK_CONFIG_NORMAL {
	public SDK_SYSTEM_TIME st_00_sysTime = new SDK_SYSTEM_TIME(); // 系统时间
	public int st_01_iLocalNo; /* !< 本机编号:[0, 998] */
	public int st_02_iOverWrite; /* !< 硬盘满时处理 "0:StopRecord", "1:OverWrite" */
	public byte st_03_cIranCalendarEnable; // /<伊朗日历
	public byte st_04_arg0[] = new byte[3];
	public byte st_05_sMachineName[] = new byte[64]; // /< 机器名

	public int st_06_iVideoStartOutPut; /* !< 输出模式 */
	public int st_07_iAutoLogout; // /< 本地菜单自动注销(分钟) [0, 120]

	public int st_08_iVideoFormat; /* !< 视频制式:“PAL”, “NTSC”, “SECAM” */
	public int st_09_iLanguage; /*
								 * !< 语言选择:“English”, “SimpChinese”,
								 * “TradChinese”, “Italian”, “Spanish”,
								 * “Japanese”, “Russian”, “French”, “German”
								 */
	public int st_10_iDateFormat; /* !< 日期格式:“YYMMDD”, “MMDDYY”, “DDMMYY” */
	public int st_11_iDateSeparator; /* !< 日期分割符:“.”, “-”, “/” */
	public int st_12_iTimeFormat; /* !< 时间格式:“12”, “24” */
	public int st_13_iDSTRule; // /< 夏令时规则
	public int st_14_iWorkDay; // /< 工作
	public DSTPoint st_15_dDSTStart = new DSTPoint();
	public DSTPoint st_16_dDSTEnd = new DSTPoint();

	@Override
	public String toString() {
		return "SDK_CONFIG_NORMAL [st_00_sysTime=" + st_00_sysTime + ", st_01_iLocalNo=" + st_01_iLocalNo
				+ ", st_02_iOverWrite=" + st_02_iOverWrite + ", st_03_cIranCalendarEnable=" + st_03_cIranCalendarEnable
				+ ", st_04_arg0=" + Arrays.toString(st_04_arg0) + ", st_05_sMachineName="
				+ Arrays.toString(st_05_sMachineName) + ", st_06_iVideoStartOutPut=" + st_06_iVideoStartOutPut
				+ ", st_07_iAutoLogout=" + st_07_iAutoLogout + ", st_08_iVideoFormat=" + st_08_iVideoFormat
				+ ", st_09_iLanguage=" + st_09_iLanguage + ", st_10_iDateFormat=" + st_10_iDateFormat
				+ ", st_11_iDateSeparator=" + st_11_iDateSeparator + ", st_12_iTimeFormat=" + st_12_iTimeFormat
				+ ", st_13_iDSTRule=" + st_13_iDSTRule + ", st_14_iWorkDay=" + st_14_iWorkDay + ", st_15_dDSTStart="
				+ st_15_dDSTStart + ", st_16_dDSTEnd=" + st_16_dDSTEnd + "]";
	}

}
