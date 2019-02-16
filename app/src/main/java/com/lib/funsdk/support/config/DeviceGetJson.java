package com.lib.funsdk.support.config;

import com.lib.funsdk.support.models.FunDevice;

import java.util.HashMap;
import java.util.Map;

public class DeviceGetJson {

	public static Map<String, Class<?>> DeviceJsonClassMap = new HashMap<String, Class<?>>();
	
	// 所有的可用的配置解析都在这里定义
	static {
		DeviceJsonClassMap.put(SystemInfo.CONFIG_NAME, SystemInfo.class);
		DeviceJsonClassMap.put(StatusNetInfo.CONFIG_NAME, StatusNetInfo.class);
		
		DeviceJsonClassMap.put(DevCmdOPSCalendar.CONFIG_NAME, DevCmdOPSCalendar.class);
		DeviceJsonClassMap.put(DevCmdSearchFileNumJP.CONFIG_NAME, DevCmdSearchFileNumJP.class);
        DeviceJsonClassMap.put(DevCmdOPFileQueryJP.CONFIG_NAME, DevCmdOPFileQueryJP.class);
        DeviceJsonClassMap.put(DevCmdOPRemoveFileJP.CONFIG_NAME, DevCmdOPRemoveFileJP.class);
        DeviceJsonClassMap.put(OPPowerSocketGet.CONFIG_NAME, OPPowerSocketGet.class);
		DeviceJsonClassMap.put(PowerSocketAutoSwitch.CONFIG_NAME, PowerSocketAutoSwitch.class);
		DeviceJsonClassMap.put(PowerSocketAutoUsb.CONFIG_NAME, PowerSocketAutoUsb.class);
		DeviceJsonClassMap.put(PowerSocketAutoLight.CONFIG_NAME, PowerSocketAutoLight.class);
		DeviceJsonClassMap.put(PowerSocketDelaySwitch.CONFIG_NAME, PowerSocketDelaySwitch.class);
		DeviceJsonClassMap.put(PowerSocketDelayUsbPower.CONFIG_NAME, PowerSocketDelayUsbPower.class);
		DeviceJsonClassMap.put(PowerSocketDelayLight.CONFIG_NAME, PowerSocketDelayLight.class);
		DeviceJsonClassMap.put(PowerSocketArm.CONFIG_NAME, PowerSocketArm.class);
		DeviceJsonClassMap.put(PowerSocketBanLed.CONFIG_NAME, PowerSocketBanLed.class);
		DeviceJsonClassMap.put(PowerSocketImage.CONFIG_NAME, PowerSocketImage.class);
		DeviceJsonClassMap.put(PowerSocketSensitive.CONFIG_NAME, PowerSocketSensitive.class);
		DeviceJsonClassMap.put(PowerSocketWorkRecord.CONFIG_NAME, PowerSocketWorkRecord.class);
		
		DeviceJsonClassMap.put(PowerSocketBulb.CONFIG_NAME, PowerSocketBulb.class);
		
		DeviceJsonClassMap.put(DetectMotion.CONFIG_NAME, DetectMotion.class);
		DeviceJsonClassMap.put(DetectBlind.CONFIG_NAME, DetectBlind.class);
		DeviceJsonClassMap.put(DetectLoss.CONFIG_NAME, DetectLoss.class);
		DeviceJsonClassMap.put(LocalAlarm.CONFIG_NAME, LocalAlarm.class);
		DeviceJsonClassMap.put(AlarmOut.CONFIG_NAME, AlarmOut.class);
		
		DeviceJsonClassMap.put(EncodeCapability.CONFIG_NAME, EncodeCapability.class);
		DeviceJsonClassMap.put(CameraParam.CONFIG_NAME, CameraParam.class);
		DeviceJsonClassMap.put(CameraParamEx.CONFIG_NAME, CameraParamEx.class);
		DeviceJsonClassMap.put(GeneralLocation.CONFIG_NAME, GeneralLocation.class);
		DeviceJsonClassMap.put(SimplifyEncode.CONFIG_NAME, SimplifyEncode.class);
		DeviceJsonClassMap.put(FVideoOsdLogo.CONFIG_NAME, FVideoOsdLogo.class);
		DeviceJsonClassMap.put(ChannelTitle.CONFIG_NAME, ChannelTitle.class);
		DeviceJsonClassMap.put(ChannelTitleDotSet.CONFIG_NAME, ChannelTitleDotSet.class);
		DeviceJsonClassMap.put(AVEncVideoWidget.CONFIG_NAME, AVEncVideoWidget.class);
		
		DeviceJsonClassMap.put(ModifyPassword.CONFIG_NAME, ModifyPassword.class);
		
		DeviceJsonClassMap.put(OPPTZControl.CONFIG_NAME, OPPTZControl.class);
		DeviceJsonClassMap.put(OPPTZPreset.CONFIG_NAME, OPPTZPreset.class);
		
		DeviceJsonClassMap.put(StorageInfo.CONFIG_NAME, StorageInfo.class);
		DeviceJsonClassMap.put(OPStorageManager.CONFIG_NAME, OPStorageManager.class);
		DeviceJsonClassMap.put(RecordParam.CONFIG_NAME, RecordParam.class);
		DeviceJsonClassMap.put(RecordParamEx.CONFIG_NAME, RecordParamEx.class);
		DeviceJsonClassMap.put(CloudStorage.CONFIG_NAME, CloudStorage.class);
		DeviceJsonClassMap.put(CameraClearFog.CONFIG_NAME, CameraClearFog.class);
		DeviceJsonClassMap.put(GeneralGeneral.CONFIG_NAME, GeneralGeneral.class);
		
		DeviceJsonClassMap.put(SystemFunction.CONFIG_NAME, SystemFunction.class);
		DeviceJsonClassMap.put(NetWorkAlarmServer.CONFIG_NAME, NetWorkAlarmServer.class);
		
		DeviceJsonClassMap.put(OPTimeSetting.CONFIG_NAME, OPTimeSetting.class);
		DeviceJsonClassMap.put(OPTimeQuery.CONFIG_NAME, OPTimeQuery.class);
		DeviceJsonClassMap.put(OPTalk.CONFIG_NAME, OPTalk.class);
		DeviceJsonClassMap.put(CameraFishEye.CONFIG_NAME, CameraFishEye.class);
		DeviceJsonClassMap.put(FishEyePlatform.CONFIG_NAME, FishEyePlatform.class);
        DeviceJsonClassMap.put(CameraWhiteLight.CONFIGNAME, CameraWhiteLight.class);
	}
	
	public static BaseConfig buildConfig(String configName) {
		Class<?> devJsonCls = DeviceJsonClassMap.get(configName);
		if ( null == devJsonCls ) {
			return null;
		}
		
		try {
			return (BaseConfig)devJsonCls.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean onParse(FunDevice funDevice, String configName, String json) {
		if ( null == funDevice
				|| null == configName
				|| null == json ) {
			return false;
		}
		Class<?> devJsonCls = DeviceJsonClassMap.get(configName);
		if ( null == devJsonCls ) {
			return false;
		}
		
		try {
			BaseConfig devJson = funDevice.checkConfig(configName);
			if (configName.equals(EncodeCapability.CONFIG_NAME)) {
				// 解析配置
				if ( !devJson.onParse(json,funDevice.CurrChannel) ) {
					return false;
				}
			}else {
				// 解析配置
				if ( !devJson.onParse(json) ) {
					return false;
				}
			}
			// 保存信息到设备里面
			return funDevice.setConfig(devJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
