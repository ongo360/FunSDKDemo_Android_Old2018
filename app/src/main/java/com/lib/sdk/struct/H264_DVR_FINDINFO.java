package com.lib.sdk.struct;

import java.io.Serializable;
import java.util.Arrays;

public class H264_DVR_FINDINFO implements Serializable{
	public int st_0_nChannelN0; // 通道号
	public int st_1_nFileType; // 文件类型, 见SDK_File_Type
	public H264_DVR_TIME st_2_startTime = new H264_DVR_TIME(); // 开始时间
	public H264_DVR_TIME st_3_endTime = new H264_DVR_TIME(); // 结束时间
	public byte st_4_fileName[] = new byte[32]; // 文件名，为空的话，系统处理，有值，系统采用
	public int st_5_hWnd;
	public int st_6_StreamType;		// 查询的码流类型--》0：主码流；1：辅码流；2：全部
	
	public H264_DVR_FINDINFO() {
		st_5_hWnd = 0;
		st_1_nFileType = 0;
		st_6_StreamType = 2;
	}

	@Override
	public String toString() {
		return "H264_DVR_FINDINFO [st_0_nChannelN0=" + st_0_nChannelN0
				+ ", st_1_nFileType=" + st_1_nFileType + ", st_2_startTime="
				+ st_2_startTime + ", st_3_endTime=" + st_3_endTime
				+ ", st_4_fileName=" + Arrays.toString(st_4_fileName)
				+ ", st_5_hWnd=" + st_5_hWnd + "]";
	}
	
	
}
