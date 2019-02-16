package com.lib.funsdk.support;

import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

public interface OnFunDeviceOptListener extends OnFunListener {

	/**
	 * 设备登录成功
	 * @param funDevice 设备对象
	 */
	void onDeviceLoginSuccess(final FunDevice funDevice);
	
	/**
	 * 设备登录失败
	 * @param funDevice 设备对象
	 * @param errCode 错误码,详见FunError定义
	 */
	void onDeviceLoginFailed(final FunDevice funDevice, final Integer errCode);
	
	/**
	 * 设备获取配置/运行状态成功
	 * @param funDevice
	 * @param configName
	 * @param nSeq
	 */
	void onDeviceGetConfigSuccess(final FunDevice funDevice, final String configName, final int nSeq);
	
	/**
	 * 设备获取配置/运行状态失败
	 * @param funDevice
	 * @param errCode
	 */
	void onDeviceGetConfigFailed(final FunDevice funDevice, final Integer errCode);
	
	/**
	 * 设备获取配置/运行状态成功
	 * @param funDevice
	 */
	void onDeviceSetConfigSuccess(final FunDevice funDevice,
								  final String configName);
	
	/**
	 * 设备获取配置/运行状态失败
	 * @param funDevice
	 * @param errCode
	 */
	void onDeviceSetConfigFailed(final FunDevice funDevice,
								 final String configName, final Integer errCode);
	
	/**
	 * 修改设备信息成功
	 * @param funDevice
	 */
	void onDeviceChangeInfoSuccess(final FunDevice funDevice);
	
	/**
	 * 修改设备信息失败
	 * @param funDevice
	 * @param errCode
	 */
	void onDeviceChangeInfoFailed(final FunDevice funDevice, final Integer errCode);
	
	/**
	 * EUIMSG.DEV_OPTION
	 * @param funDevice
	 */
	void onDeviceOptionSuccess(final FunDevice funDevice, final String option);
	
	/**
	 * EUIMSG.DEV_OPTION
	 * @param funDevice
	 */
	void onDeviceOptionFailed(final FunDevice funDevice, final String option, final Integer errCode);
	
	/**
	 * 设备文件列表更新通知
	 * @param funDevice
	 */
	void onDeviceFileListChanged(final FunDevice funDevice);

	/**
	 * 设备文件列表更新
	 * @param funDevice
	 * @param datas
	 */
	void onDeviceFileListChanged(final FunDevice funDevice, final H264_DVR_FILE_DATA[] datas);
    
    /**
     * 设备文件列表获取失败
     */
	void onDeviceFileListGetFailed(final FunDevice funDevice);
}
