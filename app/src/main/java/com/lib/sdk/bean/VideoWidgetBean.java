package com.lib.sdk.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class VideoWidgetBean {
	@JSONField(name = "ChannelTitle")
	private ChannelTitle channelTitle;
	@JSONField(name = "ChannelTitleAttribute")
	private Color channelTitleAttribute;
	@JSONField(name = "Covers")
	private List<Color> covers;
	@JSONField(name = "CoversNum")
	private int coversNum;
	@JSONField(name = "TimeTitleAttribute")
	private Color timeTitleAttribute;

	public ChannelTitle getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(ChannelTitle channelTitle) {
		this.channelTitle = channelTitle;
	}

	public Color getChannelTitleAttribute() {
		return channelTitleAttribute;
	}

	public void setChannelTitleAttribute(Color channelTitleAttribute) {
		this.channelTitleAttribute = channelTitleAttribute;
	}

	public List<Color> getCovers() {
		return covers;
	}

	public void setCovers(List<Color> covers) {
		this.covers = covers;
	}

	public int getCoversNum() {
		return coversNum;
	}

	public void setCoversNum(int coversNum) {
		this.coversNum = coversNum;
	}

	public Color getTimeTitleAttribute() {
		return timeTitleAttribute;
	}

	public void setTimeTitleAttribute(Color timeTitleAttribute) {
		this.timeTitleAttribute = timeTitleAttribute;
	}

	public class ChannelTitle {
		@JSONField(name = "Name")
		private String name;
		@JSONField(name = "SerialNo")
		private String serialNo;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSerialNo() {
			return serialNo;
		}

		public void setSerialNo(String serialNo) {
			this.serialNo = serialNo;
		}

	}

	public class Color {
		@JSONField(name = "BackColor")
		private String backColor;// 16进制
		@JSONField(name = "EncodeBlend")
		private boolean encodeBlend;
		@JSONField(name = "FrontColor")
		private String frontColor;// 16进制
		@JSONField(name = "PreviewBlend")
		private boolean previewBlend;
		@JSONField(name = "RelativePos")
		private int[] relativePos;

		public String getBackColor() {
			return backColor;
		}

		public void setBackColor(String backColor) {
			this.backColor = backColor;
		}

		public boolean isEncodeBlend() {
			return encodeBlend;
		}

		public void setEncodeBlend(boolean encodeBlend) {
			this.encodeBlend = encodeBlend;
		}

		public String getFrontColor() {
			return frontColor;
		}

		public void setFrontColor(String frontColor) {
			this.frontColor = frontColor;
		}

		public boolean isPreviewBlend() {
			return previewBlend;
		}

		public void setPreviewBlend(boolean previewBlend) {
			this.previewBlend = previewBlend;
		}

		public int[] getRelativePos() {
			return relativePos;
		}

		public void setRelativePos(int[] relativePos) {
			this.relativePos = relativePos;
		}

	}
}
