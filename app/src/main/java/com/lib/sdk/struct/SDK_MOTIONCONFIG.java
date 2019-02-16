/**
 * Android_NetSdk
 * SDK_MOTIONCONFIG.java
 * Administrator
 * TODO
 * 2015-1-29
 */
package com.lib.sdk.struct;

import com.lib.SDKCONST;

/**
 * Android_NetSdk SDK_MOTIONCONFIG.java
 * 
 * @author huangwanshui TODO ��̬������� 2015-1-29
 */
public class SDK_MOTIONCONFIG {
	public boolean st_0_bEnable; // ��̬��⿪��
	public byte[] st_1_arg0 = new byte[3];
	public int st_2_iLevel; /// 警戒级别（灵敏度）0：高级（默认值）；1：中级；2：低级；
	public int st_3_mRegion[] = new int[SDKCONST.NET_MD_REGION_ROW]; // ����ÿһ��ʹ��һ�������ƴ�
	public SDK_EventHandler st_4_hEvent = new SDK_EventHandler(); // ��̬�������
}
