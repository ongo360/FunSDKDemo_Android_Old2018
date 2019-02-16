package com.example.funsdkdemo;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideDevice extends ActivityGuide {

	private static List<DemoModule> mGuideModules = new ArrayList<DemoModule>();
	
	static {
		// 2.1 连接设备(通过序列号连接)
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_sn, 
				-1, 
				ActivityGuideDeviceSNLogin.class));
		
		// 2.2 连接设备(附近AP直连)
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_ap, 
				-1, 
				ActivityGuideDeviceListAP.class));
		
		// 2.3 连接设备(局域网内)
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_lan, 
				-1, 
				ActivityGuideDeviceListLan.class));
		
		// 2.4 用户添加设备
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_add, 
				-1, 
				ActivityGuideDeviceAddByUser.class));
		
		// 2.5 用户移除设备
//		mGuideModules.add(new DemoModule(-1, 
//				R.string.guide_module_title_device_remove, 
//				-1, 
//				ActivityGuideDeviceList.class));
		
		// 2.6 设备参数
//		mGuideModules.add(new DemoModule(-1, 
//				R.string.guide_module_title_device_config, 
//				-1, 
//				ActivityGuideDeviceList.class));
		
		// 2.7 连接用户设备(远程)
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_connect2, 
				-1, 
				ActivityGuideDeviceList.class));
		
		// 2.8 浏览设备文件
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_browse, 
				-1, 
				null));
		
		// 2.9 设备报警
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_alarm, 
				-1, 
				null));
		
		// 2.10 快速配置WiFi
		mGuideModules.add(new DemoModule(-1, 
				R.string.guide_module_title_device_setwifi, 
				-1, 
				ActivityGuideDeviceWifiConfig.class));
	}
	
	@Override
	protected List<DemoModule> getGuideModules() {
		return mGuideModules;
	}
	
}
