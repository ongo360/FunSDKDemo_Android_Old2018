package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class GSensorConfigEnableBean {
	@JSONField(name = "GSenFunction")
	private boolean gSenFunction;
	@JSONField(name = "GSenStatus")
	private boolean gSenStatus;
	@JSONField(name = "GSenThreshold")
	private boolean gSenThreshold;

	public boolean isgSenFunction() {
		return gSenFunction;
	}

	public void setgSenFunction(boolean gSenFunction) {
		this.gSenFunction = gSenFunction;
	}

	public boolean isgSenStatus() {
		return gSenStatus;
	}

	public void setgSenStatus(boolean gSenStatus) {
		this.gSenStatus = gSenStatus;
	}

	public boolean isgSenThreshold() {
		return gSenThreshold;
	}

	public void setgSenThreshold(boolean gSenThreshold) {
		this.gSenThreshold = gSenThreshold;
	}
}
