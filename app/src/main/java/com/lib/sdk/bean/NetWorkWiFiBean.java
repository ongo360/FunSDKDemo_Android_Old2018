package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class NetWorkWiFiBean {
	@JSONField(name = "Auth")
	private String auth;// "OPEN" "CLOSE"
	@JSONField(name = "Channel")
	private int channel;
	@JSONField(name = "Enable")
	private boolean enable;
	@JSONField(name = "EncrypType")
	private String encrypType;
	@JSONField(name = "GateWay")
	private String gateWay;// 16进制 从右到左
	@JSONField(name = "HostIP")
	private String hostIP;
	@JSONField(name = "KeyType")
	private int keyType;
	@JSONField(name = "Keys")
	private String keys;
	@JSONField(name = "NetType")
	private String netType;
	@JSONField(name = "SSID")
	private String ssid;
	@JSONField(name = "Submask")
	private String submask;// 16进制 从右到左

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getEncrypType() {
		return encrypType;
	}

	public void setEncrypType(String encrypType) {
		this.encrypType = encrypType;
	}

	/**
	 * 
	 * @Title: getGateWay
	 * @Description: TODO(16进制 从右到左 如：0x010AA8C0 =》192.168.10.1)
	 */
	public String getGateWay() {
		return gateWay;
	}

	/**
	 * 
	 * @Title: setGateWay
	 * @Description: TODO(16进制 从右到左 如：0x010AA8C0 =》192.168.10.1)
	 */
	public void setGateWay(String gateWay) {
		this.gateWay = gateWay;
	}

	/**
	 * 
	 * @Title: getHostIP
	 * @Description: (16进制 从右到左 如：0x010AA8C0 =》192.168.10.1))
	 */
	public String getHostIP() {
		return hostIP;
	}

	/**
	 * 
	 * @Title: setHostIP
	 * @Description: (16进制 从右到左 如：0x010AA8C0 =》192.168.10.1)
	 */
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public int getKeyType() {
		return keyType;
	}

	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	/**
	 * 
	 * @Title: getSubmask
	 * @Description: TODO(16进制 从右到左 如：0x010AA8C0 =》192.168.10.1)
	 */
	public String getSubmask() {
		return submask;
	}

	/**
	 * 
	 * @Title: setSubmask
	 * @Description: TODO(16进制 从右到左 如：0x010AA8C0 =》192.168.10.1)
	 */
	public void setSubmask(String submask) {
		this.submask = submask;
	}

}
