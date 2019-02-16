package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CameraParamExEnableBean {
	@JSONField(name = "AeMeansure")
	private boolean aeMeansure;
	@JSONField(name = "BroadTrends")
	private BroadTrendsEnableBean broadTrends;
	@JSONField(name = "Dis")
	private boolean dis;
	@JSONField(name = "Ldc")
	private boolean ldc;
	@JSONField(name = "LowLuxMode")
	private boolean lowLuxMode;

	public boolean isAeMeansure() {
		return aeMeansure;
	}

	public void setAeMeansure(boolean aeMeansure) {
		this.aeMeansure = aeMeansure;
	}

	public BroadTrendsEnableBean getBroadTrends() {
		return broadTrends;
	}

	public void setBroadTrends(BroadTrendsEnableBean broadTrends) {
		this.broadTrends = broadTrends;
	}

	public boolean isDis() {
		return dis;
	}

	public void setDis(boolean dis) {
		this.dis = dis;
	}

	public boolean isLdc() {
		return ldc;
	}

	public void setLdc(boolean ldc) {
		this.ldc = ldc;
	}

	public boolean isLowLuxMode() {
		return lowLuxMode;
	}

	public void setLowLuxMode(boolean lowLuxMode) {
		this.lowLuxMode = lowLuxMode;
	}
}
