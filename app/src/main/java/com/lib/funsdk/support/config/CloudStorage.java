package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class CloudStorage extends BaseConfig {
	
	public static final String CONFIG_NAME = JsonConfig.CFG_CLOUD_STORAGE;
	
	private int alarmRecTypeMsk;
	private int enableMsk;
	private int streamType;
	private String[][] timeSection;

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
			JSONObject c_jsonobj = mJsonObj.getJSONObject( mJsonObj.getString("Name") );
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
		
		alarmRecTypeMsk = obj.optInt("AlarmRecTypeMsk");
		enableMsk = obj.optInt("EnableMsk");
		streamType = obj.optInt("StreamType");
		
		JSONArray timeSectionArray = obj.getJSONArray("TimeSection");
		if ( null != timeSectionArray ) {
			timeSection = new String[timeSectionArray.length()][];
			
			for ( int i = 0; i < timeSectionArray.length(); i ++ ) {
				JSONArray pArray = timeSectionArray.getJSONArray(i);
				if ( pArray.length() > 0 ) {
					String[] timeSecs = new String[pArray.length()];
					for ( int j = 0; j < pArray.length(); j ++ ) {
						timeSecs[j] = pArray.getString(j);
					}
					timeSection[i] = timeSecs;
				}
			}
		}
		
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
			
			c_json.put("AlarmRecTypeMsk", alarmRecTypeMsk);
			c_json.put("EnableMsk", enableMsk);
			c_json.put("StreamType", streamType);
			
			JSONArray timeSectionArray = new JSONArray();
			if ( null != timeSection ) {
				for ( int i = 0; i < timeSection.length; i ++ ) {
					if ( null != timeSection[i] && timeSection[i].length > 0 ) {
						JSONArray pArray = new JSONArray();
						for ( int j = 0; j < timeSection[i].length; j ++ ) {
							pArray.put(timeSection[i][j]);
						}
						timeSectionArray.put(pArray);
					}
				}
			}
			c_json.put("TimeSection", timeSectionArray);
			
			mJsonObj.put(getConfigNameOfChannel(), c_json);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		FunLog.d(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	public int getAlarmRecTypeMsk() {
		return alarmRecTypeMsk;
	}
	
	public void setAlarmRecTypeMsk(int alarmMsk) {
		alarmRecTypeMsk = alarmMsk;
	}

	public int getEnableMsk() {
		return enableMsk;
	}
	
	public void setEnableMsk(int isEable) {
		enableMsk = isEable;
	}

	public int getStreamType() {
		return streamType;
	}
	
	public void setStreamType(int type) {
		streamType = type;
	}
	
}
