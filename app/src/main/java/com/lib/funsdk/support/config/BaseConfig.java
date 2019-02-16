package com.lib.funsdk.support.config;

import com.basic.G;
import com.lib.funsdk.support.utils.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseConfig implements JsonListener, Cloneable {
	protected JSONObject mJsonObj;
	private int ret;
	private int mChannel;
	protected String Config_Name_ofchannel;

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	@Override
	public boolean onParse(String json) {
		// TODO Auto-generated method stub
		if (StringUtils.isStringNULL(json)) {
			return false;
		}
		try {
			mJsonObj = new JSONObject(json);
			if (!mJsonObj.isNull("Ret")) {
				setRet(mJsonObj.getInt("Ret"));
				mJsonObj.remove("Ret");
			} else {
				ret = 100;
			}
			return ret == 100 || ret == -607;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getSendMsg() {
		// TODO Auto-generated method stub
		if (null == mJsonObj) {
			mJsonObj = new JSONObject();
		}
		return mJsonObj.toString();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mJsonObj == null ? "" : mJsonObj.toString();
	}

	public boolean getBoolean(Object obj) {
		return G.getBoolean(obj);
	}
	
//	public String getClassName() {
//		return getClass().getSimpleName();
//	}
	public abstract String getConfigName();

	public boolean onParse(String json, int channel) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
