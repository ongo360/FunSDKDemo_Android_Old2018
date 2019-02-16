package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusNetInfo extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.STATUS_NATINFO;

	private String NaInfoCode;
	private String NatStatus;
	
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
		
		NaInfoCode = obj.optString("NaInfoCode");
		NatStatus = obj.optString("NatStatus");
		
		
		return true;
	}
	
	public String getNatInfoCode() {
		return NaInfoCode;
	}
	
	public String getNatStatus() {
		return NatStatus;
	}
}
