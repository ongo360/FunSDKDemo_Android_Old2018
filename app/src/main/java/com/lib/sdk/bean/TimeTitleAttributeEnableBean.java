package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class TimeTitleAttributeEnableBean {
	@JSONField(name = "EncodeBlend")
	private boolean encodeBlend;

	public boolean isEncodeBlend() {
		return encodeBlend;
	}

	public void setEncodeBlend(boolean encodeBlend) {
		this.encodeBlend = encodeBlend;
	}
}
