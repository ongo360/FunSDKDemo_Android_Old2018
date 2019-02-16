package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class MainFormatEnableBean {
	@JSONField(name = "AudioEnable")
	private boolean audioEnable;
	@JSONField(name = "Video")
	private VideoEnableBean video;

	public VideoEnableBean getVideo() {
		return video;
	}

	public void setVideo(VideoEnableBean video) {
		this.video = video;
	}

	@JSONField()
	public boolean isAudioEnable() {
		return audioEnable;
	}

	public void setAudioEnable(boolean audioEnable) {
		this.audioEnable = audioEnable;
	}
}
