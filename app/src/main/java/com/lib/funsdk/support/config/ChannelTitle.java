package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;

import com.lib.funsdk.support.FunLog;

public class ChannelTitle extends DevCmdGeneral {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.CFG_CHANNELTITLE;
	public static final int JSON_ID = 1048;//EDEV_JSON_ID.Dev_Get_Chn_Name_REQ;1440;//

	private String ChannelTitle;
	
	@Override
	public int getJsonID() {
		return JSON_ID;
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
			JSONArray c_jsonarray = mJsonObj.getJSONArray(getConfigName());
			if ( c_jsonarray.length() > 0 ) {
				ChannelTitle = c_jsonarray.getString(0);
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
			
			JSONArray c_json = new JSONArray();
			
			c_json.put(ChannelTitle);
			
			mJsonObj.put(getConfigName(), c_json);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	public String getChannelTitle() {
		if ( null == ChannelTitle ) {
			return "";
		}
		return ChannelTitle;
	}
	
	public void setChannelTitle(String title) {
		ChannelTitle = title;
	}

}
