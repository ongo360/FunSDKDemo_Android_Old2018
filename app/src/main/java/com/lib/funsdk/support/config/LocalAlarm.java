package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class LocalAlarm extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = "Alarm.LocalAlarm";

	// 是否使能
	public boolean Enable;	// 报警输入开关
	public String SensorType;	// 传感器类型常开 or 常闭, “NC”, “NO”
	
	// EventHandler
	public EventHandler event = new EventHandler();
	
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	public String getConfigNameOfChannel(){
		return Config_Name_ofchannel;
	}

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			Config_Name_ofchannel = mJsonObj.getString("Name");
			JSONObject c_jsonobj = mJsonObj.getJSONObject(mJsonObj.getString("Name"));
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
		
		Enable = obj.optBoolean("Enable");
		SensorType = obj.optString("SensorType");
		
		JSONObject evnObj = obj.optJSONObject("EventHandler");
		if ( null != evnObj ) {
			event.parse(evnObj);
		}
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigNameOfChannel());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_jsonObj = null;
			if ( mJsonObj.isNull(getConfigNameOfChannel()) ) {
				c_jsonObj = new JSONObject();
			} else {
				c_jsonObj = mJsonObj.getJSONObject(getConfigNameOfChannel());
			}
			
			c_jsonObj.put("Enable", Enable);
			c_jsonObj.put("SensorType", SensorType);
			
			JSONObject evnObj = event.toJson();
			
			c_jsonObj.put("EventHandler", evnObj);
			
			mJsonObj.put(getConfigNameOfChannel(), c_jsonObj);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
}
