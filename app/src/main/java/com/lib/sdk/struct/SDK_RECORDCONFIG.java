/**
 * Android_NetSdk
 * SDK_RECORDCONFIG.java
 * @author huangwanshui
 * 2014-12-15
 */
package com.lib.sdk.struct;

import java.util.Arrays;

import com.lib.SDKCONST;

public class SDK_RECORDCONFIG {
	public int st_0_iPreRecord; /// < 预录时间，为零时表示关闭
	public byte st_1_bRedundancy; /// < 冗余开关
	public byte st_2_bSnapShot; /// < 快照开关
	public byte[] st_3_arg0 = new byte[2];
	public int st_4_iPacketLength; /// < 录像打包长度（分钟）[1, 255]
	public int st_5_iRecordMode; /// < 录像模式，0 关闭，1 禁止 2 配置
	public SDK_CONFIG_WORKSHEET st_6_wcWorkSheet = new SDK_CONFIG_WORKSHEET(); /// <
																				/// 录像时间段
	public int st_7_typeMask[][] = new int[SDKCONST.NET_N_WEEKS][SDKCONST.NET_N_TSECT]; /// <
																						/// 录像类型掩码

	@Override
	public String toString() {
		return "SDK_RECORDCONFIG [st_0_iPreRecord=" + st_0_iPreRecord + ", st_1_bRedundancy="
				+ st_1_bRedundancy + ", st_2_bSnapShot=" + st_2_bSnapShot + ", st_3_arg0="
				+ Arrays.toString(st_3_arg0) + ", st_4_iPacketLength=" + st_4_iPacketLength
				+ ", st_5_iRecordMode=" + st_5_iRecordMode + ", st_6_wcWorkSheet=" + st_6_wcWorkSheet
				+ ", st_7_typeMask=" + Arrays.toString(st_7_typeMask) + "]";
	}

}
