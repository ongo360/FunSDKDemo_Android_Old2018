package com.lib.funsdk.support.config;

import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CameraClearFog extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.CAMERA_CLEARFOG;

	public int level;
	public int enable;

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
		
		level = obj.optInt("Level");
		enable = obj.optInt("Enable");
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigNameOfChannel());
			//mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_json = null;
			if ( mJsonObj.isNull(getConfigNameOfChannel()) ) {
				c_json = new JSONObject();
			} else {
				c_json = mJsonObj.getJSONObject(getConfigNameOfChannel());
			}
			
			c_json.put("Level", level);
			c_json.put("Enable", enable);
			
			mJsonObj.put(getConfigNameOfChannel(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	public int getClearFogEnable() {
		return this.enable;
	}
	
	public void setClearFogEnable(int isEnable) {
		this.enable = isEnable;
	}
	
	public int getClearFogLevel() {
		return this.level;
	}
	
	public void setClearFogLevel(int levl) {
		this.level = levl;
	}
}
