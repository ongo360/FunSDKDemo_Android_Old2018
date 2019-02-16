package com.lib.funsdk.support.config;

import com.lib.funsdk.support.utils.MyUtils;
import com.lib.funsdk.support.utils.ParseVersionUtils;
import com.lib.funsdk.support.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SystemInfo extends BaseConfig {
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.SYSTEM_INFO;
	
	
	private String softwareVersion;
	private String buildTime;
	private String hardware;
	private String deviceRunTime;
	private String hardwareVersion;
	private String encryptVersion;
	private String serialNo;	// 设备序列号
	private int alarmInChannel;
	private int alarmOutChannel;
	private int talkInChannel;
	private int talkOutChannel;
	private int extraChannel;
	private int videoInChannel;
	private int videoOutChannel;
	

	public SystemInfo() {
		
	}
	
	public String getSerialNo() {
		return serialNo;
	}
	
	public String getBuildTime() {
		return buildTime;
	}
	
	public String getHardware() {
		return hardware;
	}
	
	public String getDeviceRunTime() {
		return deviceRunTime;
	}
	
	public String getHardwareVersion() {
		return hardwareVersion;
	}
	
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	
	public String getEncryptVersion() {
		return encryptVersion;
	}

	public int getAlarmInChannel() {
		return alarmInChannel;
	}
	
	public int getAlarmOutChannel() {
		return alarmOutChannel;
	}
	
	public int getTalkInChannel() {
		return talkInChannel;
	}
	
	public int getTalkOutChannel() {
		return talkOutChannel;
	}
	
	public int getExtraChannel() {
		return extraChannel;
	}
	
	public int getVideoInChannel() {
		return videoInChannel;
	}
	
	public int getVideoOutChannel() {
		return videoOutChannel;
	}

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(CONFIG_NAME);
			return onParse(c_jsonobj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj) {
			return false;
		}
		softwareVersion = obj.optString("SoftWareVersion");
		buildTime = obj.optString("BuildTime");
		hardware = obj.optString("HardWare");
		deviceRunTime = obj.optString("DeviceRunTime");
		hardwareVersion = obj.optString("HardWareVersion");
		encryptVersion = obj.optString("EncryptVersion");
		serialNo = obj.optString("SerialNo");
		alarmInChannel = obj.optInt("AlarmInChannel");
		alarmOutChannel = obj.optInt("AlarmOutChannel");
		talkInChannel = obj.optInt("TalkInChannel");
		talkOutChannel = obj.optInt("TalkOutChannel");
		extraChannel = obj.optInt("ExtraChannel");
		videoInChannel = obj.optInt("VideoInChannel");
		videoOutChannel = obj.optInt("VideoOutChannel");
		return true;
	}

	@Override
	public String getSendMsg() {
		return super.getSendMsg();
	}

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	public int getDevExpandType() {
		if (null != softwareVersion) {
			return ParseVersionUtils.getDevExpandType(softwareVersion);
		} else {
			return 0;
		}
	}
	
	public String getDeviceRunTimeWithFormat() {
		if ( null == deviceRunTime ) {
			return "";
		}
		
		int min = MyUtils.getIntFromHex(deviceRunTime);
		return TimeUtils.formatTimes(min);
	}
}
