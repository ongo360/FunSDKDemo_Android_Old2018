package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class DefaultConfigBean {
	@JSONField(name = "General")
	private int general;
	@JSONField(name = "Encode")
	private int encode;
	@JSONField(name = "Record")
	private int record;
	@JSONField(name = "NetServer")
	private int netService;
	@JSONField(name = "NetCommon")
	private int netCommon;
	@JSONField(name = "Alarm")
	private int alarm;
	@JSONField(name = "CommPtz")
	private int ptzComm;
	@JSONField(name = "Account")
	private int account;
	@JSONField(name = "Preview")
	private int preview;
	@JSONField(name = "CameraPARAM")
	private int cameraPARAM;

	public int getGeneral() {
		return general;
	}

	public void setGeneral(int general) {
		this.general = general;
	}

	public int getEncode() {
		return encode;
	}

	public void setEncode(int encode) {
		this.encode = encode;
	}

	public int getRecord() {
		return record;
	}

	public void setRecord(int record) {
		this.record = record;
	}

	public int getNetService() {
		return netService;
	}

	public void setNetService(int netService) {
		this.netService = netService;
	}

	public int getNetCommon() {
		return netCommon;
	}

	public void setNetCommon(int netCommon) {
		this.netCommon = netCommon;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public int getPtzComm() {
		return ptzComm;
	}

	public void setPtzComm(int ptzComm) {
		this.ptzComm = ptzComm;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public int getPreview() {
		return preview;
	}

	public void setPreview(int preview) {
		this.preview = preview;
	}

	public int getCameraPARAM() {
		return cameraPARAM;
	}

	public void setCameraPARAM(int cameraPARAM) {
		this.cameraPARAM = cameraPARAM;
	}

	public void setAllConfig(int value) {
		this.general = value;
		this.encode = value;
		this.record = value;
		this.netService = value;
		this.netCommon = value;
		this.alarm = value;
		this.ptzComm = value;
		this.account = value;
		this.preview = value;
		this.cameraPARAM = value;
	}
}
