package com.example.common;

import com.lib.funsdk.support.config.AVEncVideoWidget;
import com.lib.funsdk.support.config.AlarmOut;
import com.lib.funsdk.support.config.CameraClearFog;
import com.lib.funsdk.support.config.CameraParam;
import com.lib.funsdk.support.config.CameraParamEx;
import com.lib.funsdk.support.config.CloudStorage;
import com.lib.funsdk.support.config.DetectBlind;
import com.lib.funsdk.support.config.DetectMotion;
import com.lib.funsdk.support.config.FVideoOsdLogo;
import com.lib.funsdk.support.config.JsonConfig;
import com.lib.funsdk.support.config.LocalAlarm;
import com.lib.funsdk.support.config.PowerSocketArm;
import com.lib.funsdk.support.config.PowerSocketImage;
import com.lib.funsdk.support.config.RecordParam;
import com.lib.funsdk.support.config.RecordParamEx;
import com.lib.funsdk.support.config.SimplifyEncode;

public class DeviceConfigType {
	//通道相关的配置类型
	public static final String[] DeviceConfigByChannel = {
			CameraParam.CONFIG_NAME,
			CameraParamEx.CONFIG_NAME,
			AVEncVideoWidget.CONFIG_NAME ,
			DetectMotion.CONFIG_NAME,
			DetectBlind.CONFIG_NAME,
			LocalAlarm.CONFIG_NAME,
			RecordParam.CONFIG_NAME,
			RecordParamEx.CONFIG_NAME,
			CameraClearFog.CONFIG_NAME,
	};
	//通道无关的配置类型
	public static final String[] DeviceConfigCommon = {
			CloudStorage.CONFIG_NAME,
			PowerSocketImage.CONFIG_NAME,
			SimplifyEncode.CONFIG_NAME,
			FVideoOsdLogo.CONFIG_NAME,
			PowerSocketArm.CONFIG_NAME,
			AlarmOut.CONFIG_NAME,
	};
}
