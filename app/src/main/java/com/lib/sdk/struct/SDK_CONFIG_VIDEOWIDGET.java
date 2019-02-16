/**
 * Android_NetSdk
 * Snippet.java
 * Administrator
 * TODO
 * 2015-5-6
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk Snippet.java
 * 
 * @author huangwanshui TODO 2015-5-6
 */
public class SDK_CONFIG_VIDEOWIDGET {
	public SDK_VIDEO_WIDGET st_0_dstCovers[] = new SDK_VIDEO_WIDGET[8];
	public SDK_VIDEO_WIDGET st_1_ChannelTitle = new SDK_VIDEO_WIDGET();
	public SDK_VIDEO_WIDGET st_2_TimeTitle = new SDK_VIDEO_WIDGET();
	public byte[] st_3_ChannelName = new byte[64];
	public long st_4_iSerialNo;
	public byte[] st_5_arg0 = new byte[4];
	public int st_6_iCoverNum; /* !< ��ǰ��ͨ���м������ӵ����� */

	public SDK_CONFIG_VIDEOWIDGET() {
		for (int i = 0; i < 8; ++i)
			st_0_dstCovers[i] = new SDK_VIDEO_WIDGET();
	}
}
