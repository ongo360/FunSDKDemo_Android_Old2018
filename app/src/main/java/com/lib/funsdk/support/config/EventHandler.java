package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 报警联动参数
 *
 */
public class EventHandler {
	public int AlarmOutLatch;
	public int EventLatch;
	public int RecordLatch;
	public boolean MailEnable;
	public boolean MessageEnable;	// push massege to phone
	public boolean MsgtoNetEnable;
	public boolean BeepEnable;
	public boolean LogEnable;
	public boolean ShowInfo;
	public boolean ShortMsgEnable;
	public boolean MultimediaMsgEnable;
	public boolean VoiceEnable;
	public boolean AlarmOutEnable;
	public boolean MatrixEnable;
	public boolean TipEnable;
	public boolean FTPEnable;
	public boolean PtzEnable;
	public boolean TourEnable;
	public boolean RecordEnable;	// motiondetect record
	public boolean SnapEnable;		// motiondetect snap
	public String SnapShotMask;		// the mask of SnapEnable, you should change this if you want to change snapEnable
	public String TourMask;
	public String AlarmOutMask;
	public String RecordMask;		// the mask of recordenable, you should change this if you want to change recordEnable
	public String ShowInfoMask;
	public String MatrixMask;
	public String AlarmInfo;
	public Object[][] PtzLink;
	public String[][] TimeSection;
	
	public void parse(JSONObject evnObj) {
		try {
			AlarmInfo = evnObj.optString("AlarmInfo");
			AlarmOutEnable = evnObj.optBoolean("AlarmOutEnable");
			AlarmOutLatch = evnObj.optInt("AlarmOutLatch");
			AlarmOutMask = evnObj.optString("AlarmOutMask");
			BeepEnable = evnObj.optBoolean("BeepEnable");
			EventLatch = evnObj.optInt("EventLatch");
			FTPEnable = evnObj.optBoolean("FTPEnable");
			LogEnable = evnObj.optBoolean("LogEnable");
			ShowInfo = evnObj.optBoolean("ShowInfo");
			MailEnable = evnObj.optBoolean("MailEnable");
			MatrixEnable = evnObj.optBoolean("MatrixEnable");
			TipEnable = evnObj.optBoolean("TipEnable");
			MatrixMask = evnObj.optString("MatrixMask");			
			RecordEnable = evnObj.optBoolean("RecordEnable");
			RecordLatch = evnObj.optInt("RecordLatch");
			RecordMask = evnObj.optString("RecordMask");
			ShowInfoMask = evnObj.optString("ShowInfoMask");
			SnapEnable = evnObj.optBoolean("SnapEnable");
			SnapShotMask = evnObj.optString("SnapShotMask");
			TourMask = evnObj.optString("TourMask");
			MessageEnable = evnObj.optBoolean("MessageEnable");
			MsgtoNetEnable = evnObj.optBoolean("MsgtoNetEnable");
			MultimediaMsgEnable = evnObj.optBoolean("MultimediaMsgEnable");
			VoiceEnable = evnObj.optBoolean("VoiceEnable");
			PtzEnable = evnObj.optBoolean("PtzEnable");
			TourEnable = evnObj.optBoolean("TourEnable");
			ShortMsgEnable = evnObj.optBoolean("ShortMsgEnable");
			
			JSONArray PtzLinkArray = evnObj.getJSONArray("PtzLink");
			if ( null != PtzLinkArray ) {
				PtzLink = new Object[PtzLinkArray.length()][];
				
				for ( int i = 0; i < PtzLinkArray.length(); i ++ ) {
					JSONArray pArray = PtzLinkArray.getJSONArray(i);
					if ( pArray.length() > 0 ) {
						Object[] ptzLink = new Object[pArray.length()];
						for ( int j = 0; j < pArray.length(); j ++ ) {
							ptzLink[j] = pArray.get(j);
						}
						PtzLink[i] = ptzLink;
					}
				}
			}
			
			JSONArray TimeSectionArray = evnObj.getJSONArray("TimeSection");
			if ( null != TimeSectionArray ) {
				TimeSection = new String[TimeSectionArray.length()][];
				
				for ( int i = 0; i < TimeSectionArray.length(); i ++ ) {
					JSONArray pArray = TimeSectionArray.getJSONArray(i);
					if ( pArray.length() > 0 ) {
						String[] tmSecs = new String[pArray.length()];
						for ( int j = 0; j < pArray.length(); j ++ ) {
							tmSecs[j] = pArray.getString(j);
						}
						TimeSection[i] = tmSecs;
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	public JSONObject toJson() {
		try {
			JSONObject evnObj = new JSONObject();
			evnObj.put("AlarmInfo", AlarmInfo);
			evnObj.put("AlarmOutEnable", AlarmOutEnable);
			evnObj.put("AlarmOutLatch", AlarmOutLatch);
			evnObj.put("AlarmOutMask", AlarmOutMask);
			evnObj.put("BeepEnable", BeepEnable);
			evnObj.put("EventLatch", EventLatch);
			evnObj.put("FTPEnable", FTPEnable);
			evnObj.put("LogEnable", LogEnable);
			evnObj.put("MailEnable", MailEnable);
			evnObj.put("MatrixEnable", MatrixEnable);
			evnObj.put("MatrixMask", MatrixMask);
			evnObj.put("MessageEnable", MessageEnable);
			evnObj.put("MsgtoNetEnable", MsgtoNetEnable);
			evnObj.put("MultimediaMsgEnable", MultimediaMsgEnable);
			evnObj.put("PtzEnable", PtzEnable);
			
			JSONArray PtzLinkArray = new JSONArray();
			if ( null != PtzLink ) {
				for ( int i = 0; i < PtzLink.length; i ++ ) {
					if ( null != PtzLink[i] && PtzLink[i].length > 0 ) {
						JSONArray pArray = new JSONArray();
						for ( int j = 0; j < PtzLink[i].length; j ++ ) {
							pArray.put(PtzLink[i][j]);
						}
						PtzLinkArray.put(pArray);
					}
				}
			}
			evnObj.put("PtzLink", PtzLinkArray);
			
			evnObj.put("RecordEnable", RecordEnable);
			evnObj.put("RecordLatch", RecordLatch);
			evnObj.put("RecordMask", RecordMask);
			evnObj.put("ShortMsgEnable", ShortMsgEnable);
			evnObj.put("ShowInfo", ShowInfo);
			evnObj.put("ShowInfoMask", ShowInfoMask);
			evnObj.put("SnapEnable", SnapEnable);
			evnObj.put("SnapShotMask", SnapShotMask);
			
			JSONArray TimeSectionArray = new JSONArray();
			if ( null != TimeSection ) {
				for ( int i = 0; i < TimeSection.length; i ++ ) {
					if ( null != TimeSection[i] && TimeSection[i].length > 0 ) {
						JSONArray pArray = new JSONArray();
						for ( int j = 0; j < TimeSection[i].length; j ++ ) {
							pArray.put(TimeSection[i][j]);
						}
						TimeSectionArray.put(pArray);
					}
				}
			}
			evnObj.put("TimeSection", TimeSectionArray);
			
			evnObj.put("TipEnable", TipEnable);
			evnObj.put("TourEnable", TourEnable);
			evnObj.put("TourMask", TourMask);
			evnObj.put("VoiceEnable", VoiceEnable);
			
			return evnObj;
		} catch (Exception e) {
			
		}
		
		return null;
	}
}
