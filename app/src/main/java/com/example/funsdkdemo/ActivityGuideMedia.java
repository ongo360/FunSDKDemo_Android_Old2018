package com.example.funsdkdemo;

import com.example.funsdkdemo.devices.ActivityGuideDeviceRecordList;
import com.example.funsdkdemo.devices.monitor.ActivityGuideMediaRealPlay;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideMedia extends ActivityGuide {

	private static List<DemoModule> mGuideModules = new ArrayList<DemoModule>();
	
	static {
	    
	    // 3.1 播放实时视频
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_play_live, 
 				-1,
				ActivityGuideMediaRealPlay.class));
 		
 		// 3.2 播放远程录像
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_play_record, 
 				R.string.guide_module_media_remote_video,
				ActivityGuideDeviceRecordList.class));
 		
 		// 3.4 播放本地录像
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_play_local, 
 				-1, 
 				null));
 		
 		// 3.5 云播放录像
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_play_cloud, 
 				-1, 
 				null));
 		
 		// 3.6 播放控制(暂停/继续等)
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_play_control, 
 				-1, 
 				null));
 		
 		// 3.7 录像控制
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_record_control, 
 				-1, 
 				null));
 		
 		// 3.8 视频抓图
 		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_media_capture, 
 				-1, 
 				null));
	}
	
	@Override
	protected List<DemoModule> getGuideModules() {
		return mGuideModules;
	}

	
}
