package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class VideoWidgetEnableBean {
	@JSONField(name = "ChannelTitle")
	private ChannelTitleEnableBean channelTitle;
	@JSONField(name = "TimeTitleAttribute")
	private TimeTitleAttributeEnableBean timeTitleAttribute;

	public ChannelTitleEnableBean getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(ChannelTitleEnableBean channelTitle) {
		this.channelTitle = channelTitle;
	}

	public TimeTitleAttributeEnableBean getTimeTitleAttribute() {
		return timeTitleAttribute;
	}

	public void setTimeTitleAttribute(
			TimeTitleAttributeEnableBean timeTitleAttribute) {
		this.timeTitleAttribute = timeTitleAttribute;
	}
}
