package com.lib.funsdk.support.config;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StorageInfo extends BaseConfig {
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.STORAGE_INFO;
	
	
	public int PartNumber;
	public int PlysicalNo;
	public List<Partition> Partitions = new ArrayList<Partition>();

	public static class Partition {
		public int Status;
		public int DirverType;
		public int LogicSerialNo;
		public boolean IsCurrent;
		public String NewEndTime;
		public String RemainSpace;
		public String NewStartTime;
		public String TotalSpace;
		public String OldEndTime;
		public String OldStartTime;
		
		public Partition(JSONObject jsonObj) {
			parse(jsonObj);
		}
		
		public void parse(JSONObject jsonObj) {
			Status = jsonObj.optInt("Status");
			DirverType = jsonObj.optInt("DirverType");
			LogicSerialNo = jsonObj.optInt("LogicSerialNo");
			IsCurrent = jsonObj.optBoolean("IsCurrent");
			NewEndTime = jsonObj.optString("NewEndTime");
			RemainSpace = jsonObj.optString("RemainSpace");
			NewStartTime = jsonObj.optString("NewStartTime");
			TotalSpace = jsonObj.optString("TotalSpace");
			OldEndTime = jsonObj.optString("OldEndTime");
			OldStartTime = jsonObj.optString("OldStartTime");
		}
	}
	

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONArray c_jsonobj = mJsonObj.getJSONArray(CONFIG_NAME);
			if ( c_jsonobj.length() > 0 ) {
				return onParse(c_jsonobj.getJSONObject(0));
			}
			
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj) {
			return false;
		}
		
		PartNumber = obj.optInt("PartNumber");
		PlysicalNo = obj.optInt("PlysicalNo");
		
		JSONArray partions = obj.optJSONArray("Partition");
		for ( int i = 0; i < partions.length(); i ++ ) {
			Partitions.add(new Partition(partions.getJSONObject(i)));
		}
		
		return true;
	}

	@Override
	public String getSendMsg() {
		return super.getSendMsg();
	}

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	public List<Partition> getPartitions() {
		return this.Partitions;
	}
}
