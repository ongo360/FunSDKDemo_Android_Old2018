package com.lib.funsdk.support.config;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class PowerSocketAutoLight extends BaseConfig {
	
	public static final String CONFIG_NAME = "PowerSocket.AutoLight";

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	private List<AutoTime> mAutoTimes = new ArrayList<AutoTime>();
	
	public List<AutoTime> getAutoTimes() {
		return mAutoTimes;
	}
	
	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray(getConfigName());
			for ( int i = 0; i < jsonArray.length(); i ++ ) {
				JSONObject tmObj = jsonArray.getJSONObject(i);
				mAutoTimes.add(new AutoTime(tmObj));
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigName());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONArray c_jsonArray = new JSONArray();
			
			for ( AutoTime autoTime : mAutoTimes ) {
				c_jsonArray.put(autoTime.toJson());
			}
			
			mJsonObj.put(getConfigName(), c_jsonArray);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	
	
}
