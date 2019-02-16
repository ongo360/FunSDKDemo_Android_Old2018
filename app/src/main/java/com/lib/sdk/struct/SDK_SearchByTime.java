package com.lib.sdk.struct;

public class SDK_SearchByTime {
	public int st_0_nHighChannel;			///< 33~64录像通道号掩码
	public int st_1_nLowChannel;			///< 1~32录像通道号掩码
	public int st_2_nFileType;              ///< 文件类型, 见SDK_File_Type
	public SDK_SYSTEM_TIME st_3_stBeginTime = new SDK_SYSTEM_TIME();	    ///< 查询开始时间
	public SDK_SYSTEM_TIME st_4_stEndTime = new SDK_SYSTEM_TIME();		///< 查询结束时间
	public int    st_5_iSync;               ///< 是否需要同步
	public int	st_6_nHighStreamType;		///< 33~64录像的码流类型
	public int st_7_nLowStreamType;			///< 1~32录像的码流类型
}
