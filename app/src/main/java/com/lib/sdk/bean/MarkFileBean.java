package com.lib.sdk.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class MarkFileBean {
	@JSONField(name = "FileNum")
	private int fileNum;
	@JSONField(name = "FileNameList")
	private List<MarkFileNameBean> fileNameList;
	@JSONField(name = "FailFileList")
	private String[] failFileList;

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public List<MarkFileNameBean> getFileNameList() {
		return fileNameList;
	}

	public void setFileNameList(List<MarkFileNameBean> fileNameList) {
		this.fileNameList = fileNameList;
	}

	public String[] getFailFileList() {
		return failFileList;
	}

	public void setFailFileList(String[] failFileList) {
		this.failFileList = failFileList;
	}
}
