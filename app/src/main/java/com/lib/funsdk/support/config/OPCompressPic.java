package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class OPCompressPic {
	public static final String CLASSNAME = "OPCompressPic";
	private int width;
	private int height;
	private int isGeo;
	private String picName;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getIsGeo() {
		return isGeo;
	}

	public void setIsGeo(int isGeo) {
		this.isGeo = isGeo;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getSendMsg() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("Name", CLASSNAME);
			jsonObj.put("SessionID", "0x00001234");
			JSONObject c_jsonObj = new JSONObject();
			c_jsonObj.put("Width", width);
			c_jsonObj.put("Height", height);
			c_jsonObj.put("IsGeo", isGeo);
			c_jsonObj.put("PicName", picName);
			jsonObj.put(CLASSNAME, c_jsonObj);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		FunLog.d(CLASSNAME, "json:" + jsonObj.toString());
		return jsonObj.toString();
	}
}