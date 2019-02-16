package com.lib.funsdk.support;

import com.lib.funsdk.support.models.FunDevice;

public interface OnFunDeviceSerialListener extends OnFunListener {

	/**
	 * 串口打开成功
	 * @param funDevice 设备对象
	 */
	void onDeviceSerialOpenSuccess(final FunDevice funDevice);
	
	/**
	 * 串口打开失败
	 * @param funDevice
	 */
	void onDeviceSerialOpenFailed(final FunDevice funDevice, final Integer errCode);
	
	/**
	 * 写串口成功
	 * @param funDevice
	 */
	void onDeviceSerialWriteSuccess(final FunDevice funDevice);
	
	/**
	 * 写串口失败
	 * @param funDevice
	 */
	void onDeviceSerialWriteFailed(final FunDevice funDevice, final Integer errCode);
	
	/**
	 * 收到串口数据
	 * @param funDevice
	 * @param pData
	 */
	void onDeviceSerialTransmitData(final FunDevice funDevice, final byte[] pData);
}
