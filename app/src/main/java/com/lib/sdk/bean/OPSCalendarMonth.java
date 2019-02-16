package com.lib.sdk.bean;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class OPSCalendarMonth {
	public static final String CLASSNAME = "OPSCalendar";
	private String event = "";
	private String fileType = "";// h264、jpg
	private int month;
	private String rev = "";
	private int year;
	private int mask;
	private int ret;
	private HashMap<Object, Boolean> recordMap = new HashMap<Object, Boolean>();

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
		onParseMask(mask);
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public HashMap<Object, Boolean> getRecordMap() {
		return recordMap;
	}

	public void setRecordMap(HashMap<Object, Boolean> recordMap) {
		this.recordMap = recordMap;
	}

	public String getSendMsg() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("Name", CLASSNAME);
			jsonObj.put("SessionID", "0x00000001");
			JSONObject c_jsonObj = new JSONObject();
			jsonObj.put(CLASSNAME, c_jsonObj);
			c_jsonObj.put("Event", getEvent());
			c_jsonObj.put("FileType", getFileType());
			c_jsonObj.put("Month", getMonth());
			c_jsonObj.put("Rev", getRev());
			c_jsonObj.put("Year", getYear());
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
		return jsonObj.toString();
	}

	public boolean onParse(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			JSONObject c_obj = obj.getJSONObject(CLASSNAME);
			setMask(c_obj.getInt("Mask"));
			setRet(obj.getInt("Ret"));
			return ret == 100;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void onParseMask(int mask) {
		for (int i = 0; i < 31; ++i) {
			if (0 != (mask & (1 << i))) {
				recordMap
						.put(String.format("%04d%02d%02d", year, month, i + 1),
								true);
			}
		}
	}
}
