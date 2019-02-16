package com.lib.funsdk.support.config;

import com.lib.funsdk.support.FunLog;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraParamEx extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.CAMERA_PARAMEX;

	public int Dis;				// 电子防抖
	public int LowLuxMode;
	public int Ldc;
	public int AeMeansure;		// 测光模式
	public String Style;
	public String ExposureTime;
	
	public BroadTrends broadTrends = new BroadTrends();
	
	public class BroadTrends {
		public int AutoGain;	// 宽动态
		public int Gain;
	}
			
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
		
		Dis = obj.optInt("Dis");
		LowLuxMode = obj.optInt("LowLuxMode");
		Ldc = obj.optInt("Ldc");
		AeMeansure = obj.optInt("AeMeansure");
		
		Style = obj.optString("Style");
		ExposureTime = obj.optString("ExposureTime");
		
		if ( obj.has("BroadTrends") ) {
			JSONObject brObj = obj.getJSONObject("BroadTrends");
			this.broadTrends.AutoGain = brObj.getInt("AutoGain");
			this.broadTrends.Gain = brObj.getInt("Gain");
		}
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigNameOfChannel());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_json = null;
			if ( mJsonObj.isNull(getConfigNameOfChannel()) ) {
				c_json = new JSONObject();
			} else {
				c_json = mJsonObj.getJSONObject(getConfigNameOfChannel());
			}
			
			c_json.put("Dis", Dis);
			c_json.put("LowLuxMode", LowLuxMode);
			c_json.put("Ldc", Ldc);
			c_json.put("AeMeansure", AeMeansure);
			c_json.put("Style", Style);
			c_json.put("ExposureTime", ExposureTime);
			
			JSONObject brObj = new JSONObject();
			brObj.put("AutoGain", broadTrends.AutoGain);
			brObj.put("Gain", broadTrends.Gain);
			c_json.put("BroadTrends", brObj);
			
			mJsonObj.put(getConfigNameOfChannel(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	/**
	 * 获取电子防抖是否打开
	 * @return
	 */
	public boolean getDis() {
		return Dis != 0;
	}
	
	public void setDis(boolean enable) {
		Dis = enable?1:0;
	}
	
	/**
	 * 获取宽动态是否打开
	 * @return
	 */
	public boolean getWideDynamic() {
		return broadTrends.AutoGain != 0;
	}
	
	public void setWideDynamic(boolean enable) {
		broadTrends.AutoGain = enable?1:0;
	}
}
