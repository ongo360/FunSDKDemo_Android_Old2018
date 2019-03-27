package com.lib.funsdk.support.sensor;

public interface ISensorTips {
	
	/**
	 * 根据inquiryStatus返回的json适配对应的智联设备，返回tip
	 * @param jsonStr
	 * @return
	 */
	String getTips(String jsonStr);
}
