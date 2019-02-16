/**
 * Android_NetSdk
 * SDK_NatConfig.java
 * Administrator
 * TODO
 * 2014-12-9
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk
 * SDK_NatConfig.java
 * @author huangwanshui
 * TODO
 * 2014-12-9
 */
public class SDK_NatConfig {
	public boolean st_0_bEnable;
	public boolean st_1_CloudpushEnable;
	public byte[] st_2_arg0 = new byte[2];
	public int st_3_nMTU;             // ��Χ (100,1400)
	public byte st_4_serverAddr[] = new byte[64];  //�Ʒ������������IP��ַ��
	public int  st_5_serverPort;      //�Ʒ���Ķ˿�;
	public CONFIG_IPAddress st_6_dnsSvr1 = new CONFIG_IPAddress();//�Ʒ����DNS��������ַ
	public CONFIG_IPAddress st_7_dnsSvr2 = new CONFIG_IPAddress();
	public byte st_8_pResv[] = new byte[16];//����
}