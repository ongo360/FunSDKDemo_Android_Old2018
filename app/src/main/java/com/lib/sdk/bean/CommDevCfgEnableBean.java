package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CommDevCfgEnableBean {
	@JSONField(name = "CheckX1Remote")
	private boolean checkX1Remote;

	public boolean isCheckX1Remote() {
		return checkX1Remote;
	}

	public void setCheckX1Remote(boolean checkX1Remote) {
		this.checkX1Remote = checkX1Remote;
	}
}
