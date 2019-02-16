package com.lib.sdk.struct;

public class APPUpdate {
	public int  st_0_SignUpdate;
//	public int  st_1_nUpdateType;

	public byte[] st_1_APPVision = new byte[32];			//版本号
	public byte[] st_2_APPDownloadPath = new byte[128];	//版本下载地址
	public byte[] st_3_APPUpdateTip = new byte[1024];	//版本更新内容
}
