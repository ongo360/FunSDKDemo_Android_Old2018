package com.lib.funsdk.support.models;

/**
 * 设备不同端口/协议的状态
 * @author Administrator
 *
 */
public enum FunDevStatusType {

	DevStatus_P2P(0, "P2P"),
	DevStatus_TPS_V0(1, "TPS_V0"),
	DevStatus_TPS(2, "TPS"),
	DevStatus_DSS(3, "DSS"),
	DevStatus_CSS(4, "CSS"),
	DevStatus_P2P_V0(5, "P2P_V0"),
	DevStatus_IP(6, "IP"),
	DevStatus_RPS(7, "RPS");

	private String mStatusName;
	private int mStatusId;
	
	FunDevStatusType(int id, String name) {
		mStatusId = id;
		mStatusName = name;
	}
	
	public int getStatusId() {
		return mStatusId;
	}
	
	public String getStatusName() {
		return mStatusName;
	}
}
