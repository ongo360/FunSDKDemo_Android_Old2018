package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CameraParamEnableBean {
	@JSONField(name = "BLCMode")
	private boolean bLCMode;
	@JSONField(name = "DayNightColor")
	private boolean dayNightColor;
	@JSONField(name = "Day_nfLevel")
	private boolean day_nfLevel;
	@JSONField(name = "EsShutter")
	private boolean esShutter;
	@JSONField(name = "ExposureParam")
	private ExposureParamEnableBean exposureParam;
	@JSONField(name = "Night_nfLevel")
	private boolean night_nfLevel;
	@JSONField(name = "PictureFlip")
	private boolean pictureFlip;
	@JSONField(name = "PictureMirror")
	private boolean pictureMirror;
	@JSONField(name = "WhiteBalance")
	private boolean whiteBalance;

	public boolean isbLCMode() {
		return bLCMode;
	}

	public void setbLCMode(boolean bLCMode) {
		this.bLCMode = bLCMode;
	}

	public boolean isDayNightColor() {
		return dayNightColor;
	}

	public void setDayNightColor(boolean dayNightColor) {
		this.dayNightColor = dayNightColor;
	}

	public boolean isDay_nfLevel() {
		return day_nfLevel;
	}

	public void setDay_nfLevel(boolean day_nfLevel) {
		this.day_nfLevel = day_nfLevel;
	}

	public boolean isEsShutter() {
		return esShutter;
	}

	public void setEsShutter(boolean esShutter) {
		this.esShutter = esShutter;
	}

	public ExposureParamEnableBean getExposureParam() {
		return exposureParam;
	}

	public void setExposureParam(ExposureParamEnableBean exposureParam) {
		this.exposureParam = exposureParam;
	}

	public boolean isNight_nfLevel() {
		return night_nfLevel;
	}

	public void setNight_nfLevel(boolean night_nfLevel) {
		this.night_nfLevel = night_nfLevel;
	}

	public boolean isPictureFlip() {
		return pictureFlip;
	}

	public void setPictureFlip(boolean pictureFlip) {
		this.pictureFlip = pictureFlip;
	}

	public boolean isPictureMirror() {
		return pictureMirror;
	}

	public void setPictureMirror(boolean pictureMirror) {
		this.pictureMirror = pictureMirror;
	}

	public boolean isWhiteBalance() {
		return whiteBalance;
	}

	public void setWhiteBalance(boolean whiteBalance) {
		this.whiteBalance = whiteBalance;
	}
}
