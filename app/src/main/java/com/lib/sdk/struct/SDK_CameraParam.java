/**
 * Android_NetSdk SDK_CameraParam.java
 * 
 * @author huangwanshui
 */
package com.lib.sdk.struct;

public class SDK_CameraParam {
	public int st_00_whiteBalance; // 锟斤拷平锟斤拷
	public int st_01_dayNightColor; // 锟斤拷夜模式锟斤拷取值锟叫诧拷色锟斤拷锟皆讹拷锟叫伙拷锟酵黑帮拷
	public int st_02_elecLevel; // 锟轿匡拷锟斤拷平值
	public int st_03_apertureMode; // 锟皆讹拷锟斤拷圈模式
	public int st_04_BLCMode; // 锟斤拷锟解补锟斤拷模式
	public SDK_ExposureCfg st_05_exposureConfig = new SDK_ExposureCfg();// 锟截癸拷锟斤拷锟斤拷
	public SDK_GainCfg st_06_gainConfig = new SDK_GainCfg(); // 锟斤拷锟斤拷锟斤拷锟斤拷

	public int st_07_PictureFlip; // 上下反转
	public int st_08_PictureMirror; // 左右反转
	public int st_10_RejectFlicker; // 锟秸癸拷品锟斤拷锟斤拷锟斤拷锟�
	public int st_11_EsShutter; // 锟斤拷锟斤拷锟斤拷锟斤拷殴锟斤拷锟�

	public int st_12_ircut_mode; // IR-CUT锟叫伙拷 0 = 锟斤拷锟斤拷锟酵拷锟斤拷谢锟� 1 = 锟皆讹拷锟叫伙拷

	public int st_13_dnc_thr; // 锟斤拷夜转锟斤拷锟斤拷值
	public int st_14_ae_sensitivity; // ae锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷

	public int st_15_Day_nfLevel; // noise filter 锟饺硷拷锟斤拷0-5,0锟斤拷锟剿诧拷锟斤拷1-5
									// 值越锟斤拷锟剿诧拷效锟斤拷越锟斤拷锟斤拷
	public int st_16_Night_nfLevel;
	public int st_17_Ircut_swap; // ircut 锟斤拷锟斤拷= 0 锟斤拷锟斤拷= 1
}
