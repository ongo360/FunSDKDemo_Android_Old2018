/**
 * Android_NetSdk
 * SDK_NetDDNSConfig.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_NetDDNSConfig.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_NetDDNSConfig {
	public boolean st_0_Enable;	///< �Ƿ���
	public boolean st_1_Online;		///< �Ƿ�����
	public byte st_2_DDNSKey[] = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];	///< DDNS��������, Ŀǰ��: JUFENG
	public byte st_3_HostName[] = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];	///< ������
	public byte st_4_arg0[] = new byte[2];
	public SDK_RemoteServerConfig st_5_Server = new SDK_RemoteServerConfig();			///< DDNS������
}
