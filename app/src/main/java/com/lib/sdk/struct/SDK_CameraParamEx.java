package com.lib.sdk.struct;

public class SDK_CameraParamEx {
	public SDK_GainCfg st_0_broadTrends = new SDK_GainCfg(); // 宽动态
	public int st_1_style; // enum SDK_IMG_TYPE
	public int st_2_exposureTime;// 实际生效的曝光时间
	public int st_3_Dis;// 电子防抖设置 0:关闭 1:开启
	public int st_4_Ldc; // 镜头畸变校正 0:关闭 1:开启**/
	public int st_5_AeMeansure; // **测光模式校正 0:平均测光 1:中央测光**/
	public int st_6_LowLuxMode; // 微光模式 mode：0 关闭 1开启 ==only imx291
	public int st_7_res[] = new int[49]; // 冗余
}
