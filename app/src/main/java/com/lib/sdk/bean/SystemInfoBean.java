package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @ClassName: SystemInfoBean
 * @Description: TODO(SystemInfo)
 * @author xxy
 * @date 2016年3月19日 下午5:17:41
 * 
 */

public class SystemInfoBean {
	@JSONField(name = "DigChannel")
	private int digChannel;
	@JSONField(name = "ExtraChannel")
	private int extraChannel;
	@JSONField(name = "VideoInChannel")
	private int videoInChannel;
	@JSONField(name = "TalkInChannel")
	private int talkInChannel;
	@JSONField(name = "AlarmInChannel")
	private int alarmInChannel;
	@JSONField(name = "AlarmOutChannel")
	private int alarmOutChannel;
	@JSONField(name = "CombineSwitch")
	private int combineSwitch;
	@JSONField(name = "VideoOutChannel")
	private int videoOutChannel;
	@JSONField(name = "AudioInChannel")
	private int audioInChannel;
	@JSONField(name = "TalkOutChannel")
	private int talkOutChannel;
	@JSONField(name = "UpdataTime")
	private String updataTime;
	@JSONField(name = "SerialNo")
	private String serialNo;
	@JSONField(name = "EncryptVersion")
	private String encryptVersion;
	@JSONField(name = "UpdataType")
	private String updataType;

	public String getUpdataType() {
		return updataType;
	}

	public void setUpdataType(String updataType) {
		this.updataType = updataType;
	}

	@JSONField(name = "BuildTime")
	private String buildTime;
	@JSONField(name = "HardWare")
	private String hardWare;
	@JSONField(name = "DeviceRunTime")
	private String deviceRunTime;
	@JSONField(name = "HardWareVersion")
	private String hardWareVersion;
	@JSONField(name = "SoftWareVersion")
	private String softWareVersion;
	public int[] Status;

	public int getDigChannel() {
		return digChannel;
	}

	public void setDigChannel(int digChannel) {
		this.digChannel = digChannel;
	}

	public int getExtraChannel() {
		return extraChannel;
	}

	public void setExtraChannel(int extraChannel) {
		this.extraChannel = extraChannel;
	}

	public int getVideoInChannel() {
		return videoInChannel;
	}

	public void setVideoInChannel(int videoInChannel) {
		this.videoInChannel = videoInChannel;
	}

	public int getTalkInChannel() {
		return talkInChannel;
	}

	public void setTalkInChannel(int talkInChannel) {
		this.talkInChannel = talkInChannel;
	}

	public int getAlarmInChannel() {
		return alarmInChannel;
	}

	public void setAlarmInChannel(int alarmInChannel) {
		this.alarmInChannel = alarmInChannel;
	}

	public int getAlarmOutChannel() {
		return alarmOutChannel;
	}

	public void setAlarmOutChannel(int alarmOutChannel) {
		this.alarmOutChannel = alarmOutChannel;
	}

	public int getCombineSwitch() {
		return combineSwitch;
	}

	public void setCombineSwitch(int combineSwitch) {
		this.combineSwitch = combineSwitch;
	}

	public int getVideoOutChannel() {
		return videoOutChannel;
	}

	public void setVideoOutChannel(int videoOutChannel) {
		this.videoOutChannel = videoOutChannel;
	}

	public int getAudioInChannel() {
		return audioInChannel;
	}

	public void setAudioInChannel(int audioInChannel) {
		this.audioInChannel = audioInChannel;
	}

	public int getTalkOutChannel() {
		return talkOutChannel;
	}

	public void setTalkOutChannel(int talkOutChannel) {
		this.talkOutChannel = talkOutChannel;
	}

	public String getUpdataTime() {
		return updataTime;
	}

	public void setUpdataTime(String updataTime) {
		this.updataTime = updataTime;
	}

	public String getSerialNo() {
		return null != serialNo ? serialNo : "";
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getEncryptVersion() {
		return null != encryptVersion ? encryptVersion : "";
	}

	public void setEncryptVersion(String encryptVersion) {
		this.encryptVersion = encryptVersion;
	}

	public String getBuildTime() {
		return null != buildTime ? buildTime : "";
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public String getHardWare() {
		return null != hardWare ? hardWare : "";
	}

	public void setHardWare(String hardWare) {
		this.hardWare = hardWare;
	}

	public String getDeviceRunTime() {
		return null != deviceRunTime ? deviceRunTime : "";
	}

	public void setDeviceRunTime(String deviceRunTime) {
		this.deviceRunTime = deviceRunTime;
	}

	public String getHardWareVersion() {
		return null != hardWareVersion ? hardWareVersion : "";
	}

	public void setHardWareVersion(String hardWareVersion) {
		this.hardWareVersion = hardWareVersion;
	}

	public String getSoftWareVersion() {
		return null != softWareVersion ? softWareVersion : "";
	}

	public void setSoftWareVersion(String softWareVersion) {
		this.softWareVersion = softWareVersion;
	}

	public int getDevExpandType() {
		if (null != softWareVersion) {
			return ParseVersionUtils.getDevExpandType(softWareVersion);
		} else {
			return 0;
		}
	}

	public int GetChnCount() {
		System.out.println("ChanNum:" + videoInChannel + "digChannel:"
				+ digChannel);
		return videoInChannel + digChannel;
	}
}
