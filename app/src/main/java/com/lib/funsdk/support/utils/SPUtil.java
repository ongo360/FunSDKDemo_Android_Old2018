package com.lib.funsdk.support.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	private static SPUtil instance;

	private SPUtil(Context context) {
		sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public static SPUtil getInstance(Context context) {
		if (null == instance) {
			instance = new SPUtil(context);
		}
		return instance;
	}

	public void setSettingParam(String key, boolean value) {
		if (null == editor) {
			editor = sp.edit();
		}
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void setSettingParam(String key, String value) {
		if (null == editor) {
			editor = sp.edit();
		}
		editor.putString(key, value);
		editor.commit();
	}

	public void setLongParam(String key, long value) {
		if (null == editor) {
			editor = sp.edit();
		}
		editor.putLong(key, value);
		editor.commit();
	}

	public void setSettingParam(String key, int value) {
		if (null == editor) {
			editor = sp.edit();
		}
		editor.putInt(key, value);
		editor.commit();
	}

	public void setSettingParam(String key, float value) {
		if (null == editor) {
			editor = sp.edit();
		}
		editor.putFloat(key, value);
		editor.commit();
	}

	public boolean getSettingParam(String key, boolean defValue) {
		try {
			return sp.getBoolean(key, defValue);
		} catch (Exception e) {
			// TODO: handle exception
			return defValue;
		}

	}

	public String getSettingParam(String key, String defValue) {
		try {
			return sp.getString(key, defValue);
		} catch (Exception e) {
			// TODO: handle exception
			return defValue;
		}

	}

	public float getSettingParam(String key, float defValue) {
		try {
			return sp.getFloat(key, defValue);
		} catch (Exception e) {
			// TODO: handle exception
			return defValue;
		}
	}

	public int getSettingParam(String key, int defValue) {
		try {
			return sp.getInt(key, defValue);
		} catch (Exception e) {
			// TODO: handle exception
			return defValue;
		}

	}

	public long getSettingParam(String key) {
		try {
			return sp.getLong(key, -1);
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}

	}
}
