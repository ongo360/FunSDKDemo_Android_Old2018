package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ChannelTitleBean {
	@JSONField(name = "ChannelTitle")
	private String channelTitle;

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

}
