package com.lib.funsdk.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lib.SDKCONST.NetWorkType;

public class CheckNetWork {
	public static int NetWorkUseful(Context context) {
		String net_type;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return NetWorkType.No_NetWork;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return NetWorkType.No_NetWork;
		}
		net_type = networkinfo.getTypeName();
		if (net_type.equalsIgnoreCase("WIFI"))
			return NetWorkType.Wifi;
		else if (net_type.equalsIgnoreCase("MOBILE"))
			return NetWorkType.Other_NetWork;
		return NetWorkType.No_NetWork;
	}
}
