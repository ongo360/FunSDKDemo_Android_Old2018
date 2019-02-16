package com.lib.sdk.struct;

public class SDK_SearchByTimeResultV2 {
	public int st_0_nInfoNum; // /< 通道的录像记录信息个数
	public SDK_SearchByTimeInfoV2 st_1_ByTimeInfo = null; // /< 通道的录像记录信息

	public SDK_SearchByTimeResultV2(int secNum) {
		st_1_ByTimeInfo = new SDK_SearchByTimeInfoV2(secNum);
	}

}
