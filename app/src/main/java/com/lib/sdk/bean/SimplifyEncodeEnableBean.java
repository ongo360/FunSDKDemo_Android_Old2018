package com.lib.sdk.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class SimplifyEncodeEnableBean {
	@JSONField(name = "ExtraFormat")
	private List<ExtraFormatEnableBean> extraFormat;
	@JSONField(name = "MainFormat")
	private List<MainFormatEnableBean> mainFormat;

	public List<ExtraFormatEnableBean> getExtraFormat() {
		return extraFormat;
	}

	public void setExtraFormat(List<ExtraFormatEnableBean> extraFormat) {
		this.extraFormat = extraFormat;
	}

	public List<MainFormatEnableBean> getMainFormat() {
		return mainFormat;
	}

	public void setMainFormat(List<MainFormatEnableBean> mainFormat) {
		this.mainFormat = mainFormat;
	}

}
