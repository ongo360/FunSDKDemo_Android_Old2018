package com.lib.funsdk.support;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.funsdk.support.utils.FileUtils;

/**
 * 保存登录过的设备密码(用户名一般默认是admin)
 * @author Jason.Jiang
 *
 */
public class FunDevicePassword {
	
	private static FunDevicePassword mInstance = null;
	
	
	public class DevicePassword {
		String devSn;
		String loginName = "admin";
		String loginPswd;
	}
	
	// 使用过的WiFi密码
	private List<DevicePassword> mSavedDevPasswds = new ArrayList<DevicePassword>();
	
	private FunDevicePassword() {
		load();
	}
	
	
	public static FunDevicePassword getInstance() {
		if ( null == mInstance ) {
			mInstance = new FunDevicePassword();
		}
		return mInstance;
	}
	
	public String getDevicePassword(String sn) {
		if ( null == sn ) {
			return null;
		}
		
		if ( 0 == mSavedDevPasswds.size() ) {
			return null;
		}
		
		for ( DevicePassword devPwd : mSavedDevPasswds ) {
			if ( sn.equals(devPwd.devSn) ) {
				return devPwd.loginPswd;
			}
		}
		
		return null;
	}
	
	public void saveDevicePassword(String devSn, String loginPswd) {
		
		if ( null == devSn 
				|| null == loginPswd ) {
			return;
		}
		
		DevicePassword existDevInfo = null;
		for ( DevicePassword devPwd : mSavedDevPasswds ) {
			if ( devSn.equals(devPwd.devSn) ) {
				existDevInfo = devPwd;
				break;
			}
		}
		
		if ( null != existDevInfo ) {
			// 已经存在,判断用户名和密码有没有变化,如果有变化才保存
			if ( loginPswd.equals(existDevInfo.loginPswd) ) {
				// 没有变化
				return;
			}
			
			// 改变了,保存之
			mSavedDevPasswds.remove(existDevInfo);
			existDevInfo.loginPswd = loginPswd;
		} else {
			existDevInfo = new DevicePassword();
			existDevInfo.devSn = devSn;
			existDevInfo.loginPswd = loginPswd;
		}
		
		mSavedDevPasswds.add(0, existDevInfo);
		
		save();
	}
	
	private void load() {
		// 导入之前使用过的设备登录密码
		try {
			String path = FunPath.getDevicePasswordPath();
			String text = FileUtils.readFromFile(path);
			
			JSONArray jsonArray = new JSONArray(text);
			for ( int i = 0; i < jsonArray.length(); i ++ ) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				
				String devSn = jsonObj.getString("sn");
				String loginName = jsonObj.getString("name");
				String loginPswd = jsonObj.getString("passwd");
				
				if ( devSn.length() > 0 && loginPswd.length() > 0 ) {
					DevicePassword devPwd = new DevicePassword();
					devPwd.devSn = devSn;
					devPwd.loginName = loginName;
					devPwd.loginPswd = loginPswd;
					mSavedDevPasswds.add(devPwd);
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	private void save() {
		try {
			String path = FunPath.getDevicePasswordPath();
			
			JSONArray jsonArray = new JSONArray();
			
			for ( DevicePassword wifiPwd : mSavedDevPasswds ) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("sn", wifiPwd.devSn);
				jsonObj.put("name", wifiPwd.loginName);
				jsonObj.put("passwd", wifiPwd.loginPswd);
				jsonArray.put(jsonObj);
			}
			
			FileUtils.saveToFile(path, jsonArray.toString());
		} catch (Exception e) {
			
		}
	}
}
