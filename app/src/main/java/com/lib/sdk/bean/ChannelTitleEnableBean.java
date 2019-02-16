package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ChannelTitleEnableBean {
	@JSONField(name = "Name")
	private boolean name;
	@JSONField(name = "SerialNo")
	private boolean serialNo;

	public boolean isName() {
		return name;
	}

	public void setName(boolean name) {
		this.name = name;
	}

	public boolean isSerialNo() {
		return serialNo;
	}

	public void setSerialNo(boolean serialNo) {
		this.serialNo = serialNo;
	}

}
