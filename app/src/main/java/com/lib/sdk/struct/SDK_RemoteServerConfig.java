/**
 * Android_NetSdk
 * SDK_RemoteServerConfig.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_RemoteServerConfig.java
 * @author huangwanshui
 * 2014-12-10
 */
public class SDK_RemoteServerConfig {
	public byte st_0_ServerName[] = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];	///< ������
	public CONFIG_IPAddress st_1_ip = new CONFIG_IPAddress();						///< IP��ַ
	public int st_2_Port;							///< �˿ں�
	public byte st_3_UserName[] = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];		///< �û���
	public byte st_4_Password[] = new byte[SDKCONST.NET_NAME_PASSWORD_LEN];		///< ����	
	public int st_5_Anonymity;							///< �Ƿ�������¼
}
