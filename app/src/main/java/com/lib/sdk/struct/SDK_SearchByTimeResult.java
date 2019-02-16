package com.lib.sdk.struct;

import com.lib.SDKCONST;

public class SDK_SearchByTimeResult {
	public int st_0_nInfoNum;										 ///< 通道的录像记录信息个数
	public SDK_SearchByTimeInfo st_1_ByTimeInfo[] = null;    ///< 通道的录像记录信息
	public SDK_SearchByTimeResult(){
		st_1_ByTimeInfo = new SDK_SearchByTimeInfo[SDKCONST.NET_MAX_CHANNUM];
		for(int i = 0; i < st_1_ByTimeInfo.length; ++i){
			st_1_ByTimeInfo[i] = new SDK_SearchByTimeInfo();
		}
	}
	
}
