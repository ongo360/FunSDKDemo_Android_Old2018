package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;
import com.lib.sdk.bean.JsonConfig;

/**
 * 
 * @ClassName: GeneralInfo
 * @Description: TODO(普通配置)
 * @author xxy
 * @date 2016年3月19日 下午4:45:41
 *
 */

public class GeneralGeneral extends BaseConfig {
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.GENERAL_GENERAL;
	
	public enum OverWriteType {
		
		OverWrite("OverWrite"),
		StopRecord("StopRecord");
		
		String mValue = null;
		OverWriteType(String value) {
			mValue = value;
		}
		
		public String getValue() {
			return mValue;
		}
		
		public static OverWriteType getType(String value) {
			for ( OverWriteType type : OverWriteType.values() ) {
				if ( type.getValue().equals(value) ) {
					return type;
				}
			}
			return OverWrite;
		}
	}
	
	public String VideoOutPut;
	public String MachineName;
	public String OverWrite;
	public int FontSize;
	public int LocalNo;
	public int AutoLogout;
	public int ScreenSaveTime;
	public int ScreenAutoShutdown;
	public int IranCalendarEnable;

	public GeneralGeneral() {
		
	}

	public void cloneValue(GeneralGeneral valueBean) {
		this.VideoOutPut = valueBean.VideoOutPut;
		this.MachineName = valueBean.MachineName;
		this.OverWrite = valueBean.OverWrite;
		this.FontSize = valueBean.FontSize;
		this.LocalNo = valueBean.LocalNo;
		this.AutoLogout = valueBean.AutoLogout;
		this.ScreenSaveTime = valueBean.ScreenSaveTime;
		this.ScreenAutoShutdown = valueBean.ScreenAutoShutdown;
		this.IranCalendarEnable = valueBean.IranCalendarEnable;
	}
	
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json) {
		if ( !super.onParse(json) ) {//
			return false;
		}
		
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(getConfigName());//
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

		VideoOutPut = obj.optString("VideoOutPut");
		MachineName = obj.optString("MachineName");
		OverWrite = obj.optString("OverWrite");
		FontSize = obj.optInt("FontSize");
		LocalNo = obj.optInt("LocalNo");
		AutoLogout = obj.optInt("AutoLogout");
		ScreenSaveTime = obj.optInt("ScreenSaveTime");
		ScreenAutoShutdown = obj.optInt("ScreenAutoShutdown");
		IranCalendarEnable = obj.optInt("IranCalendarEnable");
		
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
			
			c_json.put("VideoOutPut", VideoOutPut);
			c_json.put("MachineName", MachineName);
			c_json.put("OverWrite", OverWrite);
			c_json.put("FontSize", FontSize);
			c_json.put("LocalNo", LocalNo);
			c_json.put("AutoLogout", AutoLogout);
			c_json.put("ScreenSaveTime", ScreenSaveTime);
			c_json.put("ScreenAutoShutdown", ScreenAutoShutdown);
			c_json.put("IranCalendarEnable", IranCalendarEnable);
			mJsonObj.put(getConfigName(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	public OverWriteType getOverWrite() {
		return OverWriteType.getType(OverWrite);
	}
	
	public void setOverWrite(OverWriteType owType) {
		OverWrite = owType.getValue();
	}

}
