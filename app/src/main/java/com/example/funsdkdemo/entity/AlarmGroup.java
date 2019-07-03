package com.example.funsdkdemo.entity;

import java.util.ArrayList;
import java.util.List;

public class AlarmGroup {
	private String date;
	private List<AlarmInfo> infoList;

	public AlarmGroup() {
		this.infoList = new ArrayList<AlarmInfo>();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<AlarmInfo> getInfoList() {
		return this.infoList;
	}

	public void setInfoList(List<AlarmInfo> infoList) {
		this.infoList = infoList;
	}

}
