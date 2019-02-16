package com.lib.sdk.struct;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.basic.G;
import com.lib.SDKCONST.StreamType;
import com.lib.funsdk.support.utils.MyUtils;

public class H264_DVR_FILE_DATA implements Serializable {
	public int st_0_ch = 0; // 通道号
	public int st_1_size = 0; // 文件大小
	public byte st_2_fileName[] = new byte[108];// /< 文件名
	public SDK_SYSTEM_TIME st_3_beginTime = new SDK_SYSTEM_TIME(); // /< 文件开始时间
	public SDK_SYSTEM_TIME st_4_endTime = new SDK_SYSTEM_TIME(); // /< 文件结束时间
	public int st_5_wnd = 0;
	public int st_6_StreamType;// 不是获取到的码流类型，以getStreamType()为准

	// add by ww
	public int downloadType;
	public boolean isChecked;
	public double currentPos = 0;
	// add by hws
	private int orderNum;// 时间相同下的序号
	private int fileType;
	public int seekPosition;

	public boolean isEffective = false;// 是否有效数据

	public String fileName;// 唯一标识

	public H264_DVR_FILE_DATA() {
	}

	@Override
	public String toString() {
		return "H264_DVR_FILE_DATA [st_0_ch=" + st_0_ch + ", st_1_size="
				+ st_1_size + ", st_2_fileName=" + G.ToString(st_2_fileName)
				+ ", st_3_beginTime=" + st_3_beginTime + ", st_4_endTime="
				+ st_4_endTime + ", st_5_wnd=" + st_5_wnd + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (null != o && o instanceof H264_DVR_FILE_DATA) {
			return this.toString().equals(o.toString());
		} else {
			return false;
		}
	}

	public String getStartDate() {
		return String.format("%04d/%02d/%02d", st_3_beginTime.st_0_year,
				st_3_beginTime.st_1_month, st_3_beginTime.st_2_day);
	}

	public String getStartTimeOfDay() {
		return String.format("%02d:%02d:%02d", st_3_beginTime.st_4_hour,
				st_3_beginTime.st_5_minute, st_3_beginTime.st_6_second);
	}

	public String getStartTimeOfYear() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date(
				st_3_beginTime.st_0_year - 1900, st_3_beginTime.st_1_month - 1,
				st_3_beginTime.st_2_day, st_3_beginTime.st_4_hour,
				st_3_beginTime.st_5_minute, st_3_beginTime.st_6_second));
	}

	public long getLongTimes(int hour, int minute, int second) {
		long times = hour * 3600 + minute * 60 + second;
		return times;
	}

	public long getLongStartTime() {
		return getLongTimes(st_3_beginTime.st_4_hour,
				st_3_beginTime.st_5_minute, st_3_beginTime.st_6_second);
	}

	public String getEndTimeOfDay() {
		return String.format("%02d:%02d:%02d", st_4_endTime.st_4_hour,
				st_4_endTime.st_5_minute, st_4_endTime.st_6_second);
	}

	public String getEndTimeOfYear() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(new Date(
				st_4_endTime.st_0_year - 1900, st_4_endTime.st_1_month - 1,
				st_4_endTime.st_2_day, st_4_endTime.st_4_hour,
				st_4_endTime.st_5_minute, st_4_endTime.st_6_second));
	}

	public long getLongEndTime() {
		return getLongTimes(st_4_endTime.st_4_hour, st_4_endTime.st_5_minute,
				st_4_endTime.st_6_second);
	}

	public boolean isContain(long times) {
		return times >= getLongStartTime() && times <= getLongEndTime();
	}

	/**
	 * 
	 * @Title: contrast
	 * @param times
	 *            待对比的参数
	 * @Description: TODO(比较：0==》相等1==》参数大-1==》参数小)
	 */
	public int contrast(long times) {
		if (isContain(times)) {
			return 0;
		} else if (times >= getLongEndTime()) {
			return 1;
		} else {
			return -1;
		}
	}

	public void setFileName(String fileName) {
		if (null != fileName) {
			this.st_2_fileName = fileName.getBytes();
			this.fileName = fileName;
		}
	}

	public String getFileName() {
		if (null == fileName) {
			fileName = G.ToString(st_2_fileName);
		}
		return fileName;
	}
	
	public static int getStreamType(String fileName) {
		if (MyUtils.isStringNULL(fileName)) {
			return StreamType.Main;
		} else {
			int index_0 = fileName.indexOf("(", 0);
			int index_1 = fileName.indexOf(")", index_0);
			if (index_0 == index_1) {
				return StreamType.Main;
			}
			String type = fileName.substring(index_0 + 1, index_1);
			if (MyUtils.isInteger(type)) {
				return Integer.parseInt(type);
			} else {
				return StreamType.Main;
			}
		}
	}
	public int getStreamType() {
		// TODO Auto-generated method stub
		return getStreamType(G.ToString(st_2_fileName));
	}

}
