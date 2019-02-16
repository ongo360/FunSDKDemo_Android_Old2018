package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class BroadTrendsEnableBean {
	@JSONField(name = "AutoGain")
	private boolean autoGain;

	public boolean isAutoGain() {
		return autoGain;
	}

	public void setAutoGain(boolean autoGain) {
		this.autoGain = autoGain;
	}
}
