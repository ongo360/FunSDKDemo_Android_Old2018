package com.lib.funsdk.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.funsdk.support.utils.FileUtils;

/**
 * 保存报警通知打开的设备
 * @author Jason.Jiang
 *
 */
public class FunAlarmNotification {

	private static FunAlarmNotification mInstance = null;
	
	private Map<String, Boolean> mDevAlarmEnableMap = new HashMap<String, Boolean>();
	
	private FunAlarmNotification() {
		load();
	}
	
	public static FunAlarmNotification getInstance() {
		if ( null == mInstance ) {
			mInstance = new FunAlarmNotification();
		}
		
		return mInstance;
	}
	
	private void load() {
		try {
			String path = FunPath.getAlarmNotifyPath();
			String text = FileUtils.readFromFile(path);
			JSONArray jsonArray = new JSONArray(text);
			for ( int i = 0; i < jsonArray.length(); i ++ ) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				String devMac = jsonObj.getString("mac");
				boolean enable = jsonObj.getBoolean("enable");
				
				mDevAlarmEnableMap.put(devMac, enable);
			}
		} catch (Exception e) {
			
		}
	}
	
	private void save() {
		try {
			String path = FunPath.getAlarmNotifyPath();
			
			JSONArray jsonArray = new JSONArray();
			Set<String> keys = mDevAlarmEnableMap.keySet();
			for ( String key : keys ) {
				boolean enable = mDevAlarmEnableMap.get(key);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("mac", key);
				jsonObj.put("enable", enable);
				jsonArray.put(jsonObj);
			}
			
			FileUtils.saveToFile(path, jsonArray.toString());
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 设置设备是否显示报警通知
	 * @param devMac
	 * @param enable
	 */
	public void setDeviceAlarmNotification(String devMac, boolean enable) {
		if ( null != mDevAlarmEnableMap ) {
			mDevAlarmEnableMap.put(devMac, enable);
			save();
		}
	}
	
	/**
	 * 通过设备号查询设备是否显示报警通知
	 * @param devMac
	 * @return
	 */
	public boolean getDeviceAlarmNotification(String devMac) {
		if ( null != mDevAlarmEnableMap ) {
			Boolean en = mDevAlarmEnableMap.get(devMac);
			if ( null == en ) {
				return false;
			}
			return en;
		}
		return false;
	}
}
