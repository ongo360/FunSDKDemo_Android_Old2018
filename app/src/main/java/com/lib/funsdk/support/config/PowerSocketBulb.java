package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class PowerSocketBulb extends BaseConfig {
	
	public static final String CONFIG_NAME = "PowerSocket.Bulb";

	private boolean Enable;	// 是否打开
	private int Red;	// 色彩R,0-100
	private int Green;	// 色彩G,0-100
	private int Blue;	// 色彩B,0-100
	private int Luma;	// 亮度,0-100
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONObject jsonObj = mJsonObj.getJSONObject(getConfigName());
			
			Enable = jsonObj.optBoolean("Enable");
			Red = jsonObj.optInt("Red");
			Green = jsonObj.optInt("Green");
			Blue = jsonObj.optInt("Blue");
			Luma = jsonObj.optInt("Luma");
			
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
			
			JSONObject jsonObj = null;
			if ( mJsonObj.isNull(getConfigName()) ) {
				jsonObj = new JSONObject();
			} else {
				jsonObj = mJsonObj.getJSONObject(getConfigName());
			}
			
			jsonObj.put("Enable", Enable);
			jsonObj.put("Red", Red);
			jsonObj.put("Green", Green);
			jsonObj.put("Blue", Blue);
			jsonObj.put("Luma", Luma);
			
			mJsonObj.put(getConfigName(), jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	public boolean enable() {
		return Enable;
	}
	
	public void setEnable(boolean enable) {
		Enable = enable;
	}
	
	public int getRed() {
		return Red;
	}
	
	public void setRed(int red) {
		Red = red;
	}
	
	public int getGreen() {
		return Green;
	}
	
	public void setGreen(int green) {
		Green = green;
	}
	
	public int getBlue() {
		return Blue;
	}
	
	public void setBlue(int blue) {
		Blue = blue;
	}
	
	public int getLuma() {
		return Luma;
	}
	
	public void setLuma(int luma) {
		Luma = luma;
	}
}
