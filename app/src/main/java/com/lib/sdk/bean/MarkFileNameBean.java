package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class MarkFileNameBean {
	@JSONField(name = "FileName")
	private String fileName;
	@JSONField(name = "StrmType")
	private int strmType;
	@JSONField(name = "Flag")
	private boolean flag;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getStrmType() {
		return strmType;
	}

	public void setStrmType(int strmType) {
		this.strmType = strmType;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
