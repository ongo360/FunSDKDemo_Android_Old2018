package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CloudStorageBean {
	@JSONField(name = "AlarmRecTypeMsk")
	private int alarmRecTypeMsk;
	@JSONField(name = "EnableMsk")
	private int enableMsk;
	@JSONField(name = "StreamType")
	private int streamType;
	@JSONField(name = "TimeSection")
	private Object[][] timeSection;

	public int getAlarmRecTypeMsk() {
		return alarmRecTypeMsk;
	}

	public void setAlarmRecTypeMsk(int alarmRecTypeMsk) {
		this.alarmRecTypeMsk = alarmRecTypeMsk;
	}

	public int getEnableMsk() {
		return enableMsk;
	}

	public void setEnableMsk(int enableMsk) {
		this.enableMsk = enableMsk;
	}

	public int getStreamType() {
		return streamType;
	}

	public void setStreamType(int streamType) {
		this.streamType = streamType;
	}

	public Object[][] getTimeSection() {
		return timeSection;
	}

	public void setTimeSection(Object[][] timeSection) {
		this.timeSection = timeSection;
	}

}
