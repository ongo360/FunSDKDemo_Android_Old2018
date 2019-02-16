package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class UartPTZBean {
	@JSONField(name = "DeviceNo")
	private int deviceNo;
	@JSONField(name = "NumberInMatrixs")
	private int numberInMatrixs;
	@JSONField(name = "PortNo")
	private int portNo;
	@JSONField(name = "ProtocolName")
	private String protocolName;
	@JSONField(name = "Attribute")
	private Object[] attribute;

	public int getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(int deviceNo) {
		this.deviceNo = deviceNo;
	}

	public int getNumberInMatrixs() {
		return numberInMatrixs;
	}

	public void setNumberInMatrixs(int numberInMatrixs) {
		this.numberInMatrixs = numberInMatrixs;
	}

	public int getPortNo() {
		return portNo;
	}

	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public Object[] getAttribute() {
		return attribute;
	}

	public void setAttribute(Object[] attribute) {
		this.attribute = attribute;
	}

}
