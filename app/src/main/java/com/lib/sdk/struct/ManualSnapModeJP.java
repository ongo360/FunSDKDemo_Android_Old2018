package com.lib.sdk.struct;

import com.basic.BaseJson;

import org.json.JSONException;
import org.json.JSONObject;

public class ManualSnapModeJP extends BaseJson {
	public static final String CLASSNAME = "ManualSnapMode";
	public static final int DELAY_CAPTURE = 0;
	public static final int COUNTDOWN = 1;
	public static final int CONTINUOUSCAPTURE = 2;
	public static final int CAPTURE = 3;
	public static final int RECORD = 4;
	public static final int UNATTENDED = 11;// 无人值守
	public static final int COMPRESS_VIDEO = 13;//缩时
	private int snapMode;// 0 表示延时拍；1 表示倒计时拍 ；2 表示连拍;3表示单拍;4 表示录像
	private double snapTime;// 延时拍，倒计时拍传过来的时间，单位为100毫秒
	private int snapNumber;// 连拍张数
	private boolean snapStatus;// 1 开,0 关 //开关状态只有录像，延时拍，慢动作才有
	private int value;//扩展值
	private String sessionID = "0x00000002";
	private long recordTime = 0;
	private boolean isSuccess = false;
	private int errorId;
	public boolean isSuccess() {
		return isSuccess;
	}


	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public int getErrorId() {
		return this.errorId;
	}
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}
	public long getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(long recordTime) {
		this.recordTime = recordTime;
	}

	public int getSnapMode() {
		return snapMode;
	}

	public void setSnapMode(int snapMode) {
		this.snapMode = snapMode;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String string) {
		this.sessionID = string;
	}

	public double getSnapTime() {
		return snapTime;
	}

	public void setSnapTime(double snapTime) {
		this.snapTime = snapTime;
	}

	public int getSnapNumber() {
		return snapNumber;
	}

	public void setSnapNumber(int snapNumber) {
		this.snapNumber = snapNumber;
	}

	public boolean isSnapStatus() {
		return snapStatus;
	}

	public void setSnapStatus(boolean snapStatus) {
		this.snapStatus = snapStatus;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getSendMsg() {
		super.getSendMsg();
		try {
			jsonObj.put("Name", CLASSNAME);
			jsonObj.put("SessionID", sessionID);
			JSONObject c_jsonObj = new JSONObject();
			c_jsonObj.put("SnapMode", snapMode);
			c_jsonObj.put("SnapTime", snapTime);
			c_jsonObj.put("SnapNumber", snapNumber);
			c_jsonObj.put("SnapStatus", snapStatus ? 1 : 0);
			c_jsonObj.put("Value", value);
			jsonObj.put(CLASSNAME, c_jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
		return jsonObj.toString();
	}

	public boolean onParse(String json) {
		if (super.onParse(json)) {
			return onParse(jsonObj);
		} else {
			return false;
		}
	}

	public boolean onParse(JSONObject obj) {
		try {
			setSnapMode(obj.getInt("SnapMode"));
			setSnapNumber(obj.getInt("SnapNumber"));
			setSnapTime(obj.getInt("SnapTime"));
			setSnapStatus(obj.getInt("SnapStatus") == 1);
			setValue(obj.getInt("Value"));
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 延时拍操作参数
	 * 
	 * @param time
	 *            单位 s
	 */
	public void delayCapture(double time, boolean snapStatus) {
		snapMode = DELAY_CAPTURE;
		snapTime = time * 10;
		snapNumber = 0;
		this.snapStatus = snapStatus;
	}

	/**
	 * 倒计时操作参数
	 * 
	 * @param time
	 *            单位 s
	 */
	public void countdown(int time) {
		snapMode = COUNTDOWN;
		snapTime = time * 10;
		snapNumber = 0;
		snapStatus = false;
	}

	/**
	 * 连拍操作参数
	 */
	public void continuousCapture(int number) {
		snapMode = CONTINUOUSCAPTURE;
		snapTime = 0;
		snapNumber = number;
		snapStatus = false;
	}

	/**
	 * 录像操作参数
	 */
	public void record(int value, boolean snapStatus) {
		snapMode = RECORD;
		snapTime = 0;
		snapNumber = 0;
		this.snapStatus = snapStatus;
		if (value == 0) {
			this.value = 1080;
		}else if(value == 1){
			this.value = 720;
		}else {
			this.value = value;
		}
	}
    public void compressVideo(String value, boolean snapStatus){
		snapMode = COMPRESS_VIDEO;
		snapNumber = 0;
		this.snapStatus = snapStatus;
		String[] ResolutionTime = value.split(":");
		snapTime = Integer.parseInt(ResolutionTime[1]);
		this.value = Integer.parseInt(ResolutionTime[0]);
	}
	public void untended(int value, boolean snapStatus) {
		snapMode = UNATTENDED;
		this.snapStatus = snapStatus;
		this.value = value;
	}
}
