package com.lib.funsdk.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Base64;

import com.lib.funsdk.support.utils.FileUtils;

/**
 * 保存用户登录历史信息
 * @author Jason.Jiang
 *
 */
public class FunLoginHistory {
	
	private static FunLoginHistory mInstance = null;
	
	
	private class SavedLoginUserInfo {
		String userName;
		String passWord;
		String loginDate;
	}
	
	// 历史登录的用户名称/密码
	private List<SavedLoginUserInfo> mSavedLoginUserInfo = new ArrayList<SavedLoginUserInfo>();
	
	private FunLoginHistory() {
		load();
	}
	
	
	public static FunLoginHistory getInstance() {
		if ( null == mInstance ) {
			mInstance = new FunLoginHistory();
		}
		return mInstance;
	}
	
	public String getLastLoginUserName() {
		if ( mSavedLoginUserInfo.size() == 0 ) {
			return "";
		}
		return mSavedLoginUserInfo.get(0).userName;
	}
	
	public String getPassword(String userName) {
		if ( null == userName ) {
			return null;
		}
		
		if ( 0 == mSavedLoginUserInfo.size() ) {
			return null;
		}
		
		for ( SavedLoginUserInfo userInfo : mSavedLoginUserInfo ) {
			if ( userName.equals(userInfo.userName) ) {
				return userInfo.passWord;
			}
		}
		
		return null;
	}
	
	public List<String> getAllUserNames() {
		List<String> userNames = new ArrayList<String>();
		for ( SavedLoginUserInfo userInfo : mSavedLoginUserInfo ) {
			userNames.add(userInfo.userName);
		}
		return userNames;
	}
	
	public void saveLoginInfo(String userName, String passWord) {
		if ( null == userName || null == passWord ) {
			return;
		}
		
		SavedLoginUserInfo existUserInfo = null;
		for ( SavedLoginUserInfo userInfo : mSavedLoginUserInfo ) {
			if ( userName.equals(userInfo.userName) ) {
				existUserInfo = userInfo;
				break;
			}
		}
		
		if ( null != existUserInfo ) {
			mSavedLoginUserInfo.remove(existUserInfo);
			existUserInfo.passWord = passWord;
		} else {
			existUserInfo = new SavedLoginUserInfo();
			existUserInfo.userName = userName;
			existUserInfo.passWord = passWord;
		}
		
		existUserInfo.loginDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date());
		
		mSavedLoginUserInfo.add(0, existUserInfo);
		
		save();
	}
	
	private String encodeLoginUserInfo() {
		try {
			JSONArray jsonArray = new JSONArray();
			
			for ( SavedLoginUserInfo userInfo : mSavedLoginUserInfo ) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("username", userInfo.userName);
				jsonObj.put("password", userInfo.passWord);
				jsonObj.put("logindate", userInfo.loginDate);
				jsonArray.put(jsonObj);
			}
			
			return Base64.encodeToString(jsonArray.toString().getBytes(), Base64.DEFAULT);
		} catch (Exception e) {
			
		}
		return "";
	}
	
	private void load() {
		// 导入上次登录成功的账号/密码
		try {
			String path = FunPath.getLoginHistoryPath();
			String encStr = FileUtils.readFromFile(path);
			String desStr = new String(Base64.decode(encStr, Base64.DEFAULT));
			
			JSONArray jsonArray = new JSONArray(desStr);
			for ( int i = 0; i < jsonArray.length(); i ++ ) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				
				String userName = jsonObj.getString("username");
				String passWord = jsonObj.getString("password");
				String loginDate = jsonObj.getString("logindate");
				
				if ( userName.length() > 0 && passWord.length() > 0 ) {
					SavedLoginUserInfo userInfo = new SavedLoginUserInfo();
					userInfo.userName = userName;
					userInfo.passWord = passWord;
					userInfo.loginDate = loginDate;
					mSavedLoginUserInfo.add(userInfo);
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	private void save() {
		try {
			String path = FunPath.getLoginHistoryPath();
			FileUtils.saveToFile(path, encodeLoginUserInfo());
		} catch (Exception e) {
			
		}
	}
}
