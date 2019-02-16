package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class MangageTipsEnableBean {
	@JSONField(name = "PhotoTipsEnable")
	private boolean photoTipsEnable;
	@JSONField(name = "RecordTipsEnable")
	private boolean recordTipsEnable;
	@JSONField(name = "Tipsvolume")
	private boolean tipsvolume;
	@JSONField(name = "WarningTipsEnable")
	private boolean warningTipsEnable;

	public boolean isPhotoTipsEnable() {
		return photoTipsEnable;
	}

	public void setPhotoTipsEnable(boolean photoTipsEnable) {
		this.photoTipsEnable = photoTipsEnable;
	}

	public boolean isRecordTipsEnable() {
		return recordTipsEnable;
	}

	public void setRecordTipsEnable(boolean recordTipsEnable) {
		this.recordTipsEnable = recordTipsEnable;
	}

	public boolean isTipsvolume() {
		return tipsvolume;
	}

	public void setTipsvolume(boolean tipsvolume) {
		this.tipsvolume = tipsvolume;
	}

	public boolean isWarningTipsEnable() {
		return warningTipsEnable;
	}

	public void setWarningTipsEnable(boolean warningTipsEnable) {
		this.warningTipsEnable = warningTipsEnable;
	}
}
