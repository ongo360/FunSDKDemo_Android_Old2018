package com.lib.sdk.struct;
import com.lib.SDKCONST;
import com.lib.SDKCONST.SDK_CAPTURE_SIZE_t;


/**
 * Android_NetSdk
 * CONFIG_EncodeAbility.java
 * @author huangwanshui
 * TODO
 * 2015-1-5
 */
public class CONFIG_EncodeAbility {
	public int st_0_iMaxEncodePower;		///< 支持的最大编码能力
	public int st_1_iChannelMaxSetSync;		///< 每个通道分辨率是否需要同步 0-不同步, 1 -同步
	public int st_2_nMaxPowerPerChannel[] = new int[SDKCONST.NET_MAX_CHANNUM];		///< 每个通道支持的最高编码能力
	public int st_3_ImageSizePerChannel[] = new int[SDKCONST.NET_MAX_CHANNUM];		///< 每个通道支持的图像分辨率
	public int st_4_ExImageSizePerChannel[] = new int[SDKCONST.NET_MAX_CHANNUM];		///< 每个通道支持的辅码流图像分辨率
	public SDK_EncodeInfo st_5_vEncodeInfo[] = new SDK_EncodeInfo[SDKCONST.SDK_CHL_FUNCTION_NUM];	///< 编码信息,暂时最大就4中码流
	public SDK_EncodeInfo st_6_vCombEncInfo[] = new SDK_EncodeInfo[SDKCONST.SDK_CHL_FUNCTION_NUM];	///< 组合编码信息,暂时最大就4中码流
	public int	st_7_iMaxBps;				///< 最高码流Kbps
	public int st_8_ExImageSizePerChannelEx[][] = new int[SDKCONST.NET_MAX_CHANNUM][SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_EXT_V3_NR];	///< 每个通道支持的辅码流图像分辨率
	public CONFIG_EncodeAbility() {
		for(int i = 0; i < SDKCONST.SDK_CHL_FUNCTION_NUM; ++i) {
			st_5_vEncodeInfo[i] = new SDK_EncodeInfo();
			st_6_vCombEncInfo[i] = new SDK_EncodeInfo();
		}
	}
}
