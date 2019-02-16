package com.example.funsdkdemo;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideTranscode extends ActivityGuide {

	private static List<DemoModule> mGuideModules = new ArrayList<DemoModule>();
	
	static {
		
		// 4.1 JPEG转MP4
		mGuideModules.add(new DemoModule(-1, 
 				R.string.guide_module_title_trancode_jpeg2mp4, 
 				-1, 
 				null));
		
	}
	
	@Override
	protected List<DemoModule> getGuideModules() {
		return mGuideModules;
	}

	
}
