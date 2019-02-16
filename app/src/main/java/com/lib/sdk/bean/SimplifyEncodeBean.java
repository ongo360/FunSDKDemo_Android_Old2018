package com.lib.sdk.bean;

/**
* 
* @ClassName: SimplifyEncodeBean 
* @Description: TODO(简单编码配置) 
* @author xxy 
* @date 2016年3月19日 下午4:48:13 
*
*/

public class SimplifyEncodeBean {

	public MainFormat MainFormat;

	public class MainFormat {
		public boolean VideoEnable;
		public boolean AudioEnable;
		public Video Video;

		public class Video {
			public int Quality;
			public int FPS;
			public int GOP;
			public int BitRate;
			public String Resolution;
			public String Compression;
			public String BitRateControl;
		}
	}

	public ExtraFormat ExtraFormat;

	public class ExtraFormat {
		public boolean VideoEnable;
		public boolean AudioEnable;
		public Video Video;

		public class Video {
			public int Quality;
			public int FPS;
			public int GOP;
			public int BitRate;
			public String BitRateControl;
			public String Compression;
			public String Resolution;
		}
	}
}
