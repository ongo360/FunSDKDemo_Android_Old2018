package com.lib.sdk.struct;

import java.util.Arrays;

public class SDK_SystemFunction {

	/**
	 * 编码功能SDK_EncodeFunctionTypes
	 */
	public byte[] st_0_EncodeFunction = new byte[7];
	/**
	 * 报警功能AlarmFucntionTypes
	 */
	public byte[] st_1_AlarmFunction = new byte[14];
	/**
	 * 网络服务功能NetServerTypes
	 */
	public byte[] st_2_NetServerFunction = new byte[45];
	/**
	 * 预览功能PreviewTypes
	 */
	public byte[] st_3_PreviewFunction = new byte[3];
	/**
	 * 串口类型SDK_CommTypes
	 */
	public byte[] st_4_CommFunction = new byte[3];
	/**
	 * 输入法限制SDK_InPutMethod
	 */
	public byte[] st_5_InputMethodFunction = new byte[2];
	/**
	 * 报警标签显示SDK_TipShow
	 */
	public byte[] st_6_TipShowFunction = new byte[4];
	/**
	 * 车载功能
	 */
	public byte[] st_7_MobileCarFunction = new byte[6];
	/**
	 * 其他功能OtherFunction
	 */
	public byte[] st_8_OtherFunction = new byte[45];

	@Override
	public String toString() {
		return "SDK_SystemFunction [st_0_EncodeFunction="
				+ Arrays.toString(st_0_EncodeFunction)
				+ ", st_1_AlarmFunction=" + Arrays.toString(st_1_AlarmFunction)
				+ ", st_2_NetServerFunction="
				+ Arrays.toString(st_2_NetServerFunction)
				+ ", st_3_PreviewFunction="
				+ Arrays.toString(st_3_PreviewFunction)
				+ ", st_4_CommFunction=" + Arrays.toString(st_4_CommFunction)
				+ ", st_5_InputMethodFunction="
				+ Arrays.toString(st_5_InputMethodFunction)
				+ ", st_6_TipShowFunction="
				+ Arrays.toString(st_6_TipShowFunction)
				+ ", st_7_MobileCarFunction="
				+ Arrays.toString(st_7_MobileCarFunction)
				+ ", st_8_OtherFunction=" + Arrays.toString(st_8_OtherFunction)
				+ "]";
	}

}
