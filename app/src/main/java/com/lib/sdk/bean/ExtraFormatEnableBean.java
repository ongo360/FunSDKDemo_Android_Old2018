package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ExtraFormatEnableBean {
	@JSONField(name = "AudioEnable")
	private boolean audioEnable;

	public boolean isAudioEnable() {
		return audioEnable;
	}

	public void setAudioEnable(boolean audioEnable) {
		this.audioEnable = audioEnable;
	}
}
