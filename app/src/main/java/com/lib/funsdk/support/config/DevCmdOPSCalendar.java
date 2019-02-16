package com.lib.funsdk.support.config;

import com.lib.EDEV_JSON_ID;
import com.lib.funsdk.support.FunLog;
import com.lib.sdk.struct.SDK_SYSTEM_TIME;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DevCmdOPSCalendar extends DevCmdGeneral {
	public static final String CONFIG_NAME = "OPSCalendar";
	public static final int JSON_ID = EDEV_JSON_ID.CALENDAR_YEAR_REQ;
	
	@Override
	public int getJsonID() {
		return JSON_ID;
	}
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	private String event = "";
	private String fileType = "";// h264、jpg
	private int month;
	private String rev = "";
	private int year;
	private int ret;
	private ArrayList<SameDayPicInfo> mList = new ArrayList<SameDayPicInfo>();

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
		this.month = 0;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public ArrayList<SameDayPicInfo> getData() {
		return mList;
	}
	
	public SameDayPicInfo getDayData(SDK_SYSTEM_TIME tm) {
		if ( null != mList && mList.size() > 0 ) {
			for ( SameDayPicInfo info : mList ) {
				SDK_SYSTEM_TIME infoTm = info.getTime();
				if ( infoTm.st_0_year == tm.st_0_year
						&& infoTm.st_1_month == tm.st_1_month
						&& infoTm.st_2_day == tm.st_2_day ) {
					return info;
				}
			}
		}
		return null;
	}

	public void setRecordMap(ArrayList<SameDayPicInfo> dataList) {
		this.mList = dataList;
	}

	public String getSendMsg() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("Name", CONFIG_NAME);
			jsonObj.put("SessionID", "0x00000001");
			JSONObject c_jsonObj = new JSONObject();
			jsonObj.put(CONFIG_NAME, c_jsonObj);
			c_jsonObj.put("Event", getEvent());
			c_jsonObj.put("FileType", getFileType());
			c_jsonObj.put("Month", getMonth());
			c_jsonObj.put("Rev", getRev());
			c_jsonObj.put("Year", getYear());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		FunLog.d(CONFIG_NAME, "json:" + jsonObj.toString());
		return jsonObj.toString();
	}

	public boolean onParse(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			Object c_obj = new JSONObject(json).getJSONObject(CONFIG_NAME)
					.getJSONArray("Mask");
			if (c_obj instanceof JSONArray) {
				mList.clear();
				for (int i = 0; i < ((JSONArray) c_obj).length(); ++i) {
					int mask = ((JSONArray) c_obj).getInt(i);
					month = i + 1;
					if (mask > 0) {
						onParseMask(mask);
					}
				}
				
				// 排序 
				Collections.sort(mList, new SameDayPicInfoComparator());
			}
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
				SameDayPicInfo info = new SameDayPicInfo();
				info.setTime(year, month, i + 1);
				mList.add(info);
			}
		}
	}
	
	private class SameDayPicInfoComparator implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			SameDayPicInfo s0 = (SameDayPicInfo)arg0;
			SameDayPicInfo s1 = (SameDayPicInfo)arg1;
			
			return s1.getDispDate().compareTo(s0.getDispDate());
		}
		
	}
}
