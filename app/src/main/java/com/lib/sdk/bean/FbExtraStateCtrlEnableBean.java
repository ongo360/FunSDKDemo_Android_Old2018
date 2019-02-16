package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class FbExtraStateCtrlEnableBean {
	@JSONField(name = "ison")
	private boolean ison;

	public boolean isIson() {
		return ison;
	}

	public void setIson(boolean ison) {
		this.ison = ison;
	}
}
