package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ClearFogEnableBean {
	@JSONField(name = "Enable")
	private boolean enable;
	@JSONField(name = "Level")
	private boolean level;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isLevel() {
		return level;
	}

	public void setLevel(boolean level) {
		this.level = level;
	}
}
