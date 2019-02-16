package com.lib.funsdk.support;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.funsdk.support.utils.FileUtils;

/**
 * 保存使用过的WiFi密码
 * @author Jason.Jiang
 *
 */
public class FunWifiPassword {
	
	private static FunWifiPassword mInstance = null;
	
	
	private class WifiPassword {
		String wifiSSID;
		String passWord;
	}
	
	// 使用过的WiFi密码
	private List<WifiPassword> mSavedWifiPasswds = new ArrayList<WifiPassword>();
	
	private FunWifiPassword() {
		load();
	}
	
	
	public static FunWifiPassword getInstance() {
		if ( null == mInstance ) {
			mInstance = new FunWifiPassword();
		}
		return mInstance;
	}
	
	public String getPassword(String ssid) {
		if ( null == ssid ) {
			return "";
		}
		
		if ( 0 == mSavedWifiPasswds.size() ) {
			return "";
		}
		
		for ( WifiPassword wifiPwd : mSavedWifiPasswds ) {
			if ( ssid.equals(wifiPwd.wifiSSID) ) {
				return wifiPwd.passWord;
			}
		}
		
		return "";
	}
	
	public void saveWifiPassword(String wifiSSID, String passWord) {
		if ( null == wifiSSID || null == passWord ) {
			return;
		}
		
		WifiPassword existUserInfo = null;
		for ( WifiPassword wifiPwd : mSavedWifiPasswds ) {
			if ( wifiSSID.equals(wifiPwd.wifiSSID) ) {
				existUserInfo = wifiPwd;
				break;
			}
		}
		
		if ( null != existUserInfo ) {
			mSavedWifiPasswds.remove(existUserInfo);
			existUserInfo.passWord = passWord;
		} else {
			existUserInfo = new WifiPassword();
			existUserInfo.wifiSSID = wifiSSID;
			existUserInfo.passWord = passWord;
		}
		
		mSavedWifiPasswds.add(0, existUserInfo);
		
		save();
	}
	
	private void load() {
		// 导入之前使用过的WiFi密码
		try {
			String path = FunPath.getWifiPasswordPath();
			String text = FileUtils.readFromFile(path);
			
			JSONArray jsonArray = new JSONArray(text);
			for ( int i = 0; i < jsonArray.length(); i ++ ) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				
				String wifiSSID = jsonObj.getString("ssid");
				String passWord = jsonObj.getString("passwd");
				
				if ( wifiSSID.length() > 0 && passWord.length() > 0 ) {
					WifiPassword wifiPwd = new WifiPassword();
					wifiPwd.wifiSSID = wifiSSID;
					wifiPwd.passWord = passWord;
					mSavedWifiPasswds.add(wifiPwd);
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	private void save() {
		try {
			String path = FunPath.getWifiPasswordPath();
			
			JSONArray jsonArray = new JSONArray();
			
			for ( WifiPassword wifiPwd : mSavedWifiPasswds ) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("ssid", wifiPwd.wifiSSID);
				jsonObj.put("passwd", wifiPwd.passWord);
				jsonArray.put(jsonObj);
			}
			
			FileUtils.saveToFile(path, jsonArray.toString());
		} catch (Exception e) {
			
		}
	}
}
