package com.lib.sdk.bean;

public class SquareVideo {
	int id;
	String title;
	String updateTime;
	String location;
	String imageUrl;
	String description;
	String context;
	String userName;
	String url;
	int praiseQuantity; // 点赞量
	int playQuantity;// 播放量
	int reviewQuantity;// 评论量
	int reportQuantity;// 举报量
	int style;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPraiseQuantity() {
		return praiseQuantity;
	}

	public void setPraiseQuantity(int praiseQuantity) {
		this.praiseQuantity = praiseQuantity;
	}

	public int getPlayQuantity() {
		return playQuantity;
	}

	public void setPlayQuantity(int playQuantity) {
		this.playQuantity = playQuantity;
	}

	public int getReviewQuantity() {
		return reviewQuantity;
	}

	public void setReviewQuantity(int reviewQuantity) {
		this.reviewQuantity = reviewQuantity;
	}

	public int getReportQuantity() {
		return reportQuantity;
	}

	public void setReportQuantity(int reportQuantity) {
		this.reportQuantity = reportQuantity;
	}

}
