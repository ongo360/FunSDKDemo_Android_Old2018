package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class OPStorageManagerBean {
	@JSONField(name = "Action")
	private String action;// Clear;
	@JSONField(name = "PartNo")
	private int partNo;
	@JSONField(name = "SerialNo")
	private int serialNo;
	@JSONField(name = "Type")
	private String type;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPartNo() {
		return partNo;
	}

	public void setPartNo(int partNo) {
		this.partNo = partNo;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
