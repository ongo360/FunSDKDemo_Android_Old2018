package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

public class OPTalk extends DevCmdGeneral{
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.OPERATION_TALK;
	public static final int JSON_ID = 1430;
	
	public String Action = "";
	
	@Override
	public String getConfigName() {
		// TODO Auto-generated method stub
		return CONFIG_NAME;
	}
	
	@Override
	public boolean onParse(String json) {
		// TODO Auto-generated method stub
		if (!super.onParse(json)) {
			return false;
		}
//		try {
//			JSONObject c_jsonobj = mJsonObj.getJSONObject(mJsonObj.getString("Name"));
//			Action = c_jsonobj.optString("Action");
//			return true;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}

	@Override
	public int getJsonID() {
		// TODO Auto-generated method stub
		return JSON_ID;
	}
	
	@Override
	public String getSendMsg() {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("Name", CONFIG_NAME);
			jsonObj.put("SessionID", "0x00000002");
			JSONObject c_jsonObj = new JSONObject();
			c_jsonObj.put("Action", Action);
			jsonObj.put(CONFIG_NAME, c_jsonObj);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return jsonObj.toString();
	}
}
