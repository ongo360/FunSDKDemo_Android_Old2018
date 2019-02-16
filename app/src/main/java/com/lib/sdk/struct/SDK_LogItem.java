/**
 * Android_NetSdk
 * SDK_LogItem.java
 * Administrator
 * TODO
 * 2014-12-11
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk
 * SDK_LogItem.java
 * @author huangwanshui
 * TODO
 * 2014-12-11
 */
public class SDK_LogItem {
	public byte st_0_sType[] = new byte[24];	///< ��־����
	public byte st_1_sUser[] = new byte[32];	///< ��־�û�
	public byte st_2_sData[] = new byte[68];	///< ��־����
	public SDK_SYSTEM_TIME st_3_stLogTime = new SDK_SYSTEM_TIME();	///< ��־ʱ��
	public int st_4_iLogPosition;			///< ���ϴβ�ѯ�Ľ���ʱ����־ָ��
}
