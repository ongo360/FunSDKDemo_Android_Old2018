package com.lib.funsdk.support.config;

import com.lib.EDEV_JSON_ID;

public class ChannelTitleDotSet extends DevCmdGeneral {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = "TitleDot";
	public static final int JSON_ID = EDEV_JSON_ID.CONFIG_CHANNELTILE_DOT_SET;

	
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
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		return mJsonObj.toString();
	}

}
