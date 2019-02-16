package com.example.funsdkdemo;

import com.lib.funsdk.support.models.FunDevice;

import android.content.Context;
import android.content.Intent;

public class DemoModule {
	Integer iconResId;		// 图标资源ID
	Integer titleStrId;		// 标题字符串ID
	Integer descStrId;		// 说明字符串ID
	Class<?> clsActivity;	// 要跳转的下级界面
	
	public DemoModule(Integer icon, Integer title, Integer desc, Class<?> clsType) {
		this.iconResId = icon;
		this.titleStrId = title;
		this.descStrId = desc;
		this.clsActivity = clsType;
	}
	
	public boolean startModule(Context context) {
		if ( null != clsActivity ) {
			Intent intent = new Intent();
			intent.setClass(context, clsActivity);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("TITLE", context.getResources().getString(titleStrId));
			intent.putExtra("CAN_BACK", true);
			context.startActivity(intent);
			return true;
		}
		
		return false;
	}
	
	public boolean startModule(Context context, FunDevice funDevice) {
		if ( null != clsActivity ) {
			Intent intent = new Intent();
			intent.setClass(context, clsActivity);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("TITLE", context.getResources().getString(titleStrId));
			intent.putExtra("CAN_BACK", true);
			if ( null != funDevice ) {
				intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
			}
			context.startActivity(intent);
			return true;
		}
		
		return false;
	}
}
