package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 遥控按钮Bean
 */
public class RemoteCtrBean {
	@JSONField(name = "Action")	
	private int action;
	@JSONField(name = "Keymode")
	private int keymode;
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}

	public int getKeymode() {
		return keymode;
	}

	public void setKeymode(int keymode) {
		this.keymode = keymode;
	}

}
