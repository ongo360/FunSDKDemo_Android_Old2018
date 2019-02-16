package com.lib.funsdk.support.config;


import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class OPTimeSetting extends BaseConfig {

	public static final String CONFIG_NAME = JsonConfig.OPTIME_SET;
	private String mSysTime;
	@Override
	public String getConfigName() {
		
		return CONFIG_NAME;
	}
	
	public String getmSysTime() {
		return mSysTime;
	}

	public void setmSysTime(String mSysTime) {
		this.mSysTime = mSysTime;
	}
	@Override
	public boolean onParse(String json) {
		if ( !super.onParse(json) ) {
			return false;
		}
		
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(getConfigName());
			return onParse(c_jsonobj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean onParse(JSONObject obj) throws JSONException {
		return null != obj;
	}
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", CONFIG_NAME);
			mJsonObj.put("SessionID", "0x00001234");
			mJsonObj.put(CONFIG_NAME, mSysTime);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(CONFIG_NAME, "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
}
