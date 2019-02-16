package com.lib.funsdk.support.config;

import org.json.JSONObject;

public class AutoTime {
	public boolean Enable;
	public int TimeStart;
	public int TimeStop;
	public int DayStart;
	public int DayStop;
	
	
	public AutoTime(){
		
	}
	public AutoTime(JSONObject jsonObj) {
		try {
			this.Enable = jsonObj.optBoolean("Enable");
			this.TimeStart = jsonObj.optInt("TimeStart");
			this.TimeStop = jsonObj.optInt("TimeStop");
			this.DayStart = jsonObj.optInt("DayStart");
			this.DayStop = jsonObj.optInt("DayStop");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject toJson() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Enable", this.Enable);
			jsonObj.put("TimeStart", this.TimeStart);
			jsonObj.put("TimeStop", this.TimeStop);
			jsonObj.put("DayStart", this.DayStart);
			jsonObj.put("DayStop", this.DayStop);
			return jsonObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
