package com.lib.sdk.struct;

public class SDK_SearchByTimeInfo {
	public int st_0_iChannel;			    ///< 录像通道号
	///< 录像记录用720个字节的5760位来表示一天中的1440分钟
	///< 0000:无录像 0001:F_COMMON 0002:F_ALERT 0003:F_DYNAMIC 0004:F_CARD 0005:F_HAND
	public byte st_1_cRecordBitMap[] = new byte[720];
}
