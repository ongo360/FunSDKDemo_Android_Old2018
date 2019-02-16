package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;


public class PowerSocketImage extends BaseConfig {
	
	public static final String CONFIG_NAME = "PowerSocket.Image";

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	private int mFlip = 0;
	
	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(CONFIG_NAME);
			mFlip = c_jsonobj.getInt("flip");
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
			mJsonObj.put("Name", CONFIG_NAME);
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_jsonObj = null;
			if ( mJsonObj.isNull(CONFIG_NAME) ) {
				c_jsonObj = new JSONObject();
			} else {
				c_jsonObj = mJsonObj.getJSONObject(CONFIG_NAME);
			}
			
			c_jsonObj.put("flip", mFlip);
			
			mJsonObj.put(CONFIG_NAME, c_jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(CONFIG_NAME, "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
	
	public int getFlip() {
		return mFlip;
	}
	
	public void setFlip(int flip) {
		mFlip = flip;
	}
}
