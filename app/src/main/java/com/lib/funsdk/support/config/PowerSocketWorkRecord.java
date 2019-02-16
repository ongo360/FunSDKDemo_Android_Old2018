package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;


public class PowerSocketWorkRecord extends BaseConfig {
	
	public static final String CONFIG_NAME = "PowerSocket.WorkRecord";

	public int TotalEnergy; 
	public int TotalTime;
	public int EnergyOfThisMon;
	public int TimeOfThisMon;
	public int EnergyRecently;
	public int TimeRecently;
	public int DeviceType;
	public int DevicePower;
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
     
	public int getTotalEnergy(){
		return TotalEnergy;
	}
	public int getTotalTime() {
		return TotalTime;
	}
	public int getEnergyOfThisMon() {
		return EnergyOfThisMon;
	}
	public int getTimeOfThisMon() {
		return TimeOfThisMon;
	}
	public int getEnergyRecently() {
		return EnergyRecently;
	}
	public int getTimeRecently() {
		return TimeRecently;
	}
	public int getDeviceType() {
		return DeviceType;
	}
	public int getDevicePower() {
		return DevicePower;
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
		TotalEnergy = obj.optInt("TotalEnergy");
		TotalTime = obj.optInt("TotalTime");
		EnergyOfThisMon = obj.optInt("EnergyOfThisMon");
		TimeOfThisMon = obj.optInt("TimeOfThisMon");
		EnergyRecently = obj.optInt("EnergyRecently");
		DeviceType = obj.optInt("DeviceType");
		DevicePower = obj.optInt("DevicePower");
		return true;
	}
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", CONFIG_NAME);
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_jsonObj = null;
			if ( mJsonObj.isNull(CONFIG_NAME) ) {
				c_jsonObj = new JSONObject();
			} else {
				c_jsonObj = mJsonObj.getJSONObject(CONFIG_NAME);
			}
			
			c_jsonObj.put("TotalEnergy", TotalEnergy);
			c_jsonObj.put("TotalTime", TotalTime);
			c_jsonObj.put("EnergyOfThisMon", EnergyOfThisMon);
			c_jsonObj.put("TimeOfThisMon", TimeOfThisMon);
			c_jsonObj.put("EnergyRecently", EnergyRecently);
			c_jsonObj.put("DeviceType", DeviceType);
			c_jsonObj.put("DevicePower", DevicePower);
			
			mJsonObj.put(CONFIG_NAME, c_jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(CONFIG_NAME, "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
	public void setDevicePower(int pow) {
		DevicePower = pow;
	}

	
}
