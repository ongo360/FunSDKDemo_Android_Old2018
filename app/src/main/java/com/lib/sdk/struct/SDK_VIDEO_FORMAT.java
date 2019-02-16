/**
 * Android_NetSdk
 * SDK_VIDEO_FORMAT.java
 * Administrator
 * TODO
 * 2014-12-30
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk
 * SDK_VIDEO_FORMAT.java
 * @author huangwanshui
 * TODO
 * 2014-12-30
 */
public class SDK_VIDEO_FORMAT {
	public int		st_0_iCompression;			//  ѹ��ģʽ 	
	public int		st_1_iResolution;			//  �ֱ��� ����ö��SDK_CAPTURE_SIZE_t
	public int		st_2_iBitRateControl;		//  �������� ����ö��SDK_capture_brc_t
	public int		st_3_iQuality;				//  码流的画质 档次1-6	
	public int		st_4_nFPS;					//  帧率值，NTSC/PAL不区分,负数表示多秒一帧		
	public int		st_5_nBitRate;				//  0-4096k,该列表主要由客户端保存，设备只接收实际的码流值而不是下标。
	public int		st_6_iGOP;					//  ��������I֮֡��ļ��ʱ�䣬2-12 
}
