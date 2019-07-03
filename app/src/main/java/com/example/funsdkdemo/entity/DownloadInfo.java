package com.example.funsdkdemo.entity;

import java.io.Serializable;

public class DownloadInfo<T> implements Serializable {

	private static final long serialVersionUID = 455813464894153372L;
	private String sn;
	private int position;
	private boolean isDownloading;
	private String fileName;
	private T obj;

	public DownloadInfo() {
		super();
		this.isDownloading = false;
	}

	public DownloadInfo(int position, String sn, T obj) {
		super();
		this.position = position;
		this.obj = obj;
		this.sn = sn;
		this.isDownloading = false;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public T getObj() {
		return obj;
	}
	
	public String GetStrSign(){
		return sn + "_" + obj.toString();
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

}
