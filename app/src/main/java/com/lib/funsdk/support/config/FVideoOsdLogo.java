package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class FVideoOsdLogo extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.CFG_OSD_LOGO;

	public int BgTrans;
	public boolean Enable;
	public int FgTrans;
	public int Left;
	public int Top;
	
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
		
		BgTrans = obj.optInt("BgTrans");
		Enable = obj.optBoolean("Enable");
		FgTrans = obj.optInt("FgTrans");
		Left = obj.optInt("Left");
		Top = obj.optInt("Top");
		
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
			
			c_json.put("BgTrans", BgTrans);
			c_json.put("Enable", Enable);
			c_json.put("FgTrans", FgTrans);
			c_json.put("Left", Left);
			c_json.put("Top", Top);
			
			mJsonObj.put(getConfigName(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	/**
	 * 获取水印是否打开
	 * @return
	 */
	public boolean getEnable() {
		return Enable;
	}
	
	public void setEnable(boolean enable) {
		Enable = enable;
	}
}
