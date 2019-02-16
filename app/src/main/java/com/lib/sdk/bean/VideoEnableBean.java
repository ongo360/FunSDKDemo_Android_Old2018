package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class VideoEnableBean {
	@JSONField(name = "Compression")
	private boolean compression;
	@JSONField(name = "FPS")
	private boolean fPS;
	@JSONField(name = "Quality")
	private boolean quality;
	@JSONField(name = "Resolution")
	private boolean resolution;

	public boolean isCompression() {
		return compression;
	}

	public void setCompression(boolean compression) {
		this.compression = compression;
	}

	public boolean isfPS() {
		return fPS;
	}

	public void setfPS(boolean fPS) {
		this.fPS = fPS;
	}

	public boolean isQuality() {
		return quality;
	}

	public void setQuality(boolean quality) {
		this.quality = quality;
	}

	public boolean isResolution() {
		return resolution;
	}

	public void setResolution(boolean resolution) {
		this.resolution = resolution;
	}
}
