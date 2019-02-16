package com.lib.sdk.bean;

import com.basic.BaseJson;


public class AVEncEncodeBean extends BaseJson {
	public EncodeExAudio Audio;
	public EncodeExVideo Video;
	public boolean AudioEnable;
	public boolean VideoEnable;

	public class EncodeExAudio {
		public int BitRate;
		public int Bitrate;
		public int MaxVolume;
		public int SampleRate;
	}

	public class EncodeExVideo {
		public int BitRate;
		public String BitRateControl;
		public String Compression;
		public int FPS;
		public int GOP;
		public int Quality;
		public String Resolution;
	}
}
