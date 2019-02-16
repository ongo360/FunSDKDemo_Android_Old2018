
package com.lib.funsdk.support.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedParamMng{ 
	public final static String FUN_PARAM_DB = "xm_example";
	
	
	SharedPreferences mPreferences = null;
	Context mContext = null;
	
	public SharedParamMng(Context context)
	{	
		mContext = context;
		if(mContext != null)
		{
			mPreferences = context.getSharedPreferences(FUN_PARAM_DB, 
					Context.MODE_PRIVATE);
		}
	}
	
	public void setUserValue(String key, String value)
	{
		if(mPreferences != null)
		{
			Editor editor = mPreferences.edit();
			
			editor.putString(key, value);
			
			editor.commit();
		}
	}

	
	public String getUserValue(String key)
	{
		if(mPreferences != null)
		{
			String value = mPreferences.getString(key, "");
			
			return value;
		}
		
		return null;
	}

    public boolean removeUserValue(String key) {
        Log.i("test", "Remove: " + getUserValue(key));
        if (mPreferences != null) {
            Editor editor = mPreferences.edit();
            editor.remove(key);
            editor.commit();
            return true;
        }else{
            return false;
        }
    }

    public boolean getBooleanUserValue(String key, boolean defaultVal)
	{
		if(mPreferences != null)
		{
			return mPreferences.getBoolean(key, defaultVal);
		}
		
		return defaultVal;
	}
	
	public void setBooleanUserValue(String key, boolean value)
	{
		if(mPreferences != null)
		{
			Editor editor = mPreferences.edit();
			
			editor.putBoolean(key, value);
			
			editor.commit();
		}
		
	}
}