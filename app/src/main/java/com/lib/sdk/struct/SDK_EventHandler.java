/**
 * Android_NetSdk
 * SDK_EventHandler.java
 * Administrator
 * TODO
 * 2014-12-10
 */
package com.lib.sdk.struct;
import com.lib.SDKCONST;

/**
 * Android_NetSdk
 * SDK_EventHandler.java
 * @author huangwanshui
 * TODO
 * 2014-12-10
 */
public class SDK_EventHandler {
	public  int	    st_00_dwRecord;				// ¼������ 
	public  int		st_01_iRecordLatch;			// ¼����ʱ��10��300 sec  	
	public  int	    st_02_dwTour;					// ��Ѳ���� 	
	public  int	    st_03_dwSnapShot;				// ץͼ���� 
	public  int	    st_04_dwAlarmOut;				// �������ͨ������ 
	public  int	    st_05_dwMatrix;				// �������� 
	public int		st_06_iEventLatch;			// ������ʼ��ʱʱ�䣬sΪ��λ 
	public int		st_07_iAOLatch;				// ���������ʱ��10��300 sec  
	public SDK_PtzLinkConfig st_08_PtzLink[] = new SDK_PtzLinkConfig[SDKCONST.NET_MAX_CHANNUM];		// ��̨������ 
	public SDK_CONFIG_WORKSHEET st_09_schedule = new SDK_CONFIG_WORKSHEET();		// ¼��ʱ���

	public boolean	st_10_bRecordEn;				// ¼��ʹ�� 
	public boolean	st_11_bTourEn;				// ��Ѳʹ�� 
	public boolean	st_12_bSnapEn;				// ץͼʹ�� 	
	public boolean	st_13_bAlarmOutEn;			// ����ʹ�� 
	public boolean	st_14_bPtzEn;

	// ��̨����ʹ�� 
	public boolean	st_15_bTip;					// ��Ļ��ʾʹ�� 	
	public boolean	st_16_bMail;					// �����ʼ� 	
	public boolean	st_17_bMessage;				// ������Ϣ���������� 	
	public boolean	st_18_bBeep;					// ���� 	
	public boolean	st_19_bVoice;					// ������ʾ 		
	public boolean	st_20_bFTP;					// ����FTP���� 
	public boolean	st_21_bMatrixEn;				// ����ʹ�� 
	public boolean	st_22_bLog;					// ��־ʹ��
	public boolean	st_23_bMessagetoNet;			// ��Ϣ�ϴ�������ʹ�� 

	public boolean    st_24_bShowInfo;              // �Ƿ���GUI�Ϻͱ�������ʾ������Ϣ
	public boolean st_25_arg0;
	public int    st_26_dwShowInfoMask;         // Ҫ������ʾ������Ϣ��ͨ������
	public byte   st_27_pAlarmInfo[] = new byte[SDKCONST.CHANNELNAME_MAX_LEN];//Ҫ��ʾ�ı�����Ϣ

	public boolean    st_28_bShortMsg;              //���Ͷ���
	public boolean    st_29_bMultimediaMsg;         //���Ͳ���
	public byte st_30_arg1[] = new byte[2];
	public SDK_EventHandler() {
		for(int i = 0 ; i < SDKCONST.NET_MAX_CHANNUM; ++i) {
			st_08_PtzLink[i] = new SDK_PtzLinkConfig();
		}
	}
}
