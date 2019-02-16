package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class GeneralLocation extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.GENERAL_LOCATION + ".[0]";

	public int Dis;
	public int LowLuxMode;
	public int Ldc;
	public int AeMeansure;
	public String Style;
	public String ExposureTime;
	
	public BroadTrends broadTrends = new BroadTrends();
	
	public class BroadTrends {
		public int AutoGain;
		public int Gain;
	}
			
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(getConfigName());
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
		
		Dis = obj.optInt("Dis");
		LowLuxMode = obj.optInt("LowLuxMode");
		Ldc = obj.optInt("Ldc");
		AeMeansure = obj.optInt("AeMeansure");
		
		Style = obj.optString("Style");
		ExposureTime = obj.optString("ExposureTime");
		
		if ( obj.has("BroadTrends") ) {
			JSONObject brObj = obj.getJSONObject("BroadTrends");
			this.broadTrends.AutoGain = brObj.getInt("AutoGain");
			this.broadTrends.Gain = brObj.getInt("Gain");
		}
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigName());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_json = null;
			if ( mJsonObj.isNull(getConfigName()) ) {
				c_json = new JSONObject();
			} else {
				c_json = mJsonObj.getJSONObject(getConfigName());
			}
			
			c_json.put("Dis", Dis);
			c_json.put("LowLuxMode", LowLuxMode);
			c_json.put("Ldc", Ldc);
			c_json.put("AeMeansure", AeMeansure);
			c_json.put("Style", Style);
			c_json.put("ExposureTime", ExposureTime);
			
			JSONObject brObj = new JSONObject();
			brObj.put("AutoGain", broadTrends.AutoGain);
			brObj.put("Gain", broadTrends.Gain);
			c_json.put("BroadTrends", brObj);
			
			mJsonObj.put(getConfigName(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

}
