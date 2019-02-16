package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class FbExtraStateCtrlBean {
	@JSONField(name = "ison")
	private int ison;

	public int getIson() {
		return ison;
	}

	public void setIson(int ison) {
		this.ison = ison;
	}

}
