package com.lib.funsdk.support.config;

import com.lib.EDEV_JSON_ID;
import com.lib.funsdk.support.FunLog;
import org.json.JSONException;
import org.json.JSONObject;

public class DevCmdSearchFileNumJP extends DevCmdGeneral {
	
	public static final String CONFIG_NAME = "SearchFileNum";
	public static final int JSON_ID = EDEV_JSON_ID.GET_FILE_NUM_REQ;

	private String mEvent = "";
	private String mFileType = "";
	private String mBeginTime = "";
	private String mEndTime = "";
	private String mStreamType = "0x00000002";
	private int mFileNum = 0;
	private String mSearchPath = "*";
	
	@Override
	public int getJsonID() {
		return JSON_ID;
	}

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	public String getEvent() {
		return mEvent;
	}

	public void setEvent(String event) {
		this.mEvent = event;
	}

	public String getFileType() {
		return mFileType;
	}

	public void setFileType(String fileType) {
		this.mFileType = fileType;
	}

	public String getBeginTime() {
		return mBeginTime;
	}

	public void setBeginTime(String beginTime) {
		this.mBeginTime = beginTime;
	}

	public String getEndTime() {
		return mEndTime;
	}

	public void setEndTime(String endTime) {
		this.mEndTime = endTime;
	}

	public String getStreamType() {
		return mStreamType;
	}

	public void setmStreamType(String streamType) {
		this.mStreamType = streamType;
	}

	public void setFileNum(int num) {
		this.mFileNum = num;
	}

	public int getFileNum() {
		return mFileNum;
	}

	public void setSearchPath(String path) {
		this.mSearchPath = path;
	}

	public String getSearchPath() {
		return mSearchPath;
	}

	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			JSONObject c_jsonObj;
			mJsonObj.put("Name", CONFIG_NAME);
			if (!mJsonObj.has(CONFIG_NAME)) {
				c_jsonObj = new JSONObject();
			} else {
				c_jsonObj = mJsonObj.getJSONObject(CONFIG_NAME);
			}
			c_jsonObj.put("Event", mEvent);
			c_jsonObj.put("FileType", mFileType);
			c_jsonObj.put("BeginTime", mBeginTime);
			c_jsonObj.put("EndTime", mEndTime);
			c_jsonObj.put("StreamType", mStreamType);
			c_jsonObj.put("SearchPath", mSearchPath);
			mJsonObj.put(CONFIG_NAME, c_jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
		FunLog.d(CONFIG_NAME, "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	@Override
	public boolean onParse(String json) {
		if (super.onParse(json)) {
			JSONObject c_jsonobj = null;
			try {
				c_jsonobj = mJsonObj.getJSONObject(CONFIG_NAME);
				return onParse(c_jsonobj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean onParse(JSONObject obj) {
		try {
			setFileNum(obj.getInt("FileNum"));
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

}
