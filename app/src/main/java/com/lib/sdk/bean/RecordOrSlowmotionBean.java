package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class RecordOrSlowmotionBean {
	@JSONField(name = "Type")
	private int type;
	@JSONField(name = "Value")
	private int value;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
