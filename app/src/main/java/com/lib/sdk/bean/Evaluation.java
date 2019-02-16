package com.lib.sdk.bean;

import com.lib.FunSDK;

public class Evaluation {
	String content;
	String hostIp;
	String updateTime;
	int videoId;
	String userName;
	String video;

	public Evaluation() {

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if(userName == null || userName.length() == 0)
			userName = FunSDK.TS("Tourist");
		this.userName = userName;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
}
