/**
 * Android_NetSdk
 * SDK_EncodeInfo.java
 * Administrator
 * TODO
 * 2015-1-5
 */
package com.lib.sdk.struct;
/**
 * Android_NetSdk
 * SDK_EncodeInfo.java
 * @author huangwanshui
 * TODO
 * 2015-1-5
 */
public class SDK_EncodeInfo {
	public byte st_0_bEnable;			///< 使能�
	public byte st_1_bHaveAudio;		///< 是否支持音频�
	public byte st_2_res[] = new byte[2];
	public int  st_3_iStreamType;		///< 码流类型，capture_channel_t
	public int st_4_uiCompression;		///< capture_comp_t的掩码
	public int st_5_uiResolution;		///< SDK_CAPTURE_SIZE_t的掩码
}
