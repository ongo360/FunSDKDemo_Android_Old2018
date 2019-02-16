/**
 * Android_NetSdk
 * SDK_DriverInformation.java
 * Administrator
 * TODO
 * 2014-12-24
 */
package com.lib.sdk.struct;

/**
 * Android_NetSdk SDK_DriverInformation.java
 * 
 * @author huangwanshui TODO 2014-12-24
 */
public class SDK_DriverInformation {
	public int st_00_iDriverType; /// < ����������
	public boolean st_01_bIsCurrent; /// < �Ƿ�Ϊ��ǰ������
	public byte st_02_arg0[] = new byte[3];
	public int st_03_uiTotalSpace; /// < ��������MBΪ��λ
	public int st_04_uiRemainSpace; /// < ʣ��������MBΪ��λ
	public int st_05_iStatus; /// < �����־���ļ�ϵͳ��ʼ��ʱ������
	public int st_06_iLogicSerialNo; /// < �߼����
	public SDK_SYSTEM_TIME st_07_tmStartTimeNew = new SDK_SYSTEM_TIME(); /// <
																			/// ��¼��ʱ��εĿ�ʼʱ��
	public SDK_SYSTEM_TIME st_08_tmEndTimeNew = new SDK_SYSTEM_TIME(); /// <
																		/// ��¼��ʱ��εĽ���ʱ��
	public SDK_SYSTEM_TIME st_09_tmStartTimeOld = new SDK_SYSTEM_TIME(); /// <
																			/// ��¼��ʱ��εĿ�ʼʱ��
	public SDK_SYSTEM_TIME st_10_tmEndTimeOld = new SDK_SYSTEM_TIME(); /// <
																		/// ��¼��ʱ��εĽ���ʱ��

	@Override
	public String toString() {
		return "SDK_DriverInformation [st_03_uiTotalSpace=" + st_03_uiTotalSpace + "]";
	}

}
