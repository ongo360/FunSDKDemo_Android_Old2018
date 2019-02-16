package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class StorageGlobalBean {
	@JSONField(name = "KeyOverWrite")
	private boolean keyOverWrite;

	public boolean isKeyOverWrite() {
		return keyOverWrite;
	}

	public void setKeyOverWrite(boolean keyOverWrite) {
		this.keyOverWrite = keyOverWrite;
	}

}
