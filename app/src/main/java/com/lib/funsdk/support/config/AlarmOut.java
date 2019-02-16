package com.lib.funsdk.support.config;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

import org.json.JSONArray;

public class AlarmOut extends BaseConfig{
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = "Alarm.AlarmOut";
	protected String Config_Name_ofchannel;
	
	public String Name  ;
	public List<AlarmOutInfo> Alarms = new ArrayList<AlarmOutInfo>();
	
	public JSONObject jsonObject = null;
	public AlarmOutInfo alarmInfo = null;
	
    public class AlarmOutInfo{
      public String AlarmOutType;
      public String AlarmOutStatus;
      
      @Override
    	public String toString() {
    		// TODO Auto-generated method stub
    		return "AlarmOutInfo = " + "[ AlarmOutType = " + AlarmOutType + "," + "AlarmOutStatus = " + AlarmOutStatus + "]";
    	}
    }
    
	@Override
	public String getConfigName() {
		// TODO Auto-generated method stub
		return CONFIG_NAME;
	}
	
	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONArray c_jsonarr = mJsonObj.getJSONArray(getConfigName());
			return onParse(c_jsonarr);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean onParse(JSONArray obj) throws JSONException {
		if (null == obj) {
			return false;
		}
		
		for (int i = 0; i < obj.length(); i++) {
			jsonObject = obj.getJSONObject(i);
			alarmInfo = new AlarmOutInfo();
			alarmInfo.AlarmOutStatus = jsonObject.optString("AlarmOutStatus");
			alarmInfo.AlarmOutType = jsonObject.optString("AlarmOutType");
			
			Alarms.add(alarmInfo);
		}
		System.out.println("TTT-----Alrams= " + Alarms.toString());
		return true;
	}
	
	@Override
	public String getSendMsg() {
		// TODO Auto-generated method stub
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigName());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONArray c_json = null;
			JSONObject c_jobject = null;
			c_json = new JSONArray();
			for (AlarmOutInfo alarmInfo : Alarms) {
				c_jobject = new JSONObject();
				c_jobject.put("AlarmOutStatus", alarmInfo.AlarmOutStatus);
				c_jobject.put("AlarmOutType", alarmInfo.AlarmOutType);
				c_json.put(c_jobject);
			}
			
			mJsonObj.put(getConfigName(), c_json);
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
   }
