package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.funsdk.support.models.FunDevice;

public class OPPTZPreset extends BaseConfig{

	public static final String CONFIG_NAME = "Uart.PTZPreset";
	
	int[] ids;
	
	
	
	public int[] getIds() {
		return ids;
	}
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json) {
	    try {
	        JSONObject jsonObject1 = new JSONObject(json);
	        JSONArray jsonArray = jsonObject1.getJSONArray("Uart.PTZPreset.[0]");
	        
	        ids = new int[jsonArray.length()];
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
	            ids[i] = jsonObject.getInt("Id");
	        }
	        
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return false;

	}
	
}
