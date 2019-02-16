package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzq on 2017-07-13.
 */

public class SPVMNCfgJson extends BaseConfig{

	public static final String CONFIG_NAME = "NetWork.SPVMN";

	private String Camreaid;
	private int CamreaLevel;
	private String Alarmid;
	private int AlarmLevel;

	private boolean bCsEnable;
	private int iHsIntervalTime;
	private int iRsAgedTime;
	private int sCsPort;
	private int sUdpPort;
	private String szConnPass;
	private String szCsIP;
	private String szDeviceNO;
	private String szServerDn;
	private String szServerNo;
	private String uiAlarmStateBlindEnable;
	private String uiAlarmStateConnectEnable;
	private String uiAlarmStateGpinEnable;
	private String uiAlarmStateLoseEnable;
	private String uiAlarmStateMotionEnable;
	private String uiAlarmStatePerformanceEnable;

	private JSONObject jsonObject;

	public String getCamreaid() {
		return Camreaid;
	}

	public void setCamreaid(String Camreaid) {
		this.Camreaid = Camreaid;
	}

	public int getCamreaLevel() {
		return CamreaLevel;
	}

	public void setCamreaLevel(int CamreaLevel) {
		this.CamreaLevel = CamreaLevel;
	}

	public String getAlarmid() {
		return Alarmid;
	}

	public void setAlarmid(String Alarmid) {
		this.Alarmid = Alarmid;
	}

	public int getAlarmLevel() {
		return AlarmLevel;
	}

	public void setAlarmLevel(int AlarmLevel) {
		this.AlarmLevel = AlarmLevel;
	}

	public boolean isBCsEnable() {
		return bCsEnable;
	}

	public void setBCsEnable(boolean bCsEnable) {
		this.bCsEnable = bCsEnable;
	}

	public int getIHsIntervalTime() {
		return iHsIntervalTime;
	}

	public void setIHsIntervalTime(int iHsIntervalTime) {
		this.iHsIntervalTime = iHsIntervalTime;
	}

	public int getIRsAgedTime() {
		return iRsAgedTime;
	}

	public void setIRsAgedTime(int iRsAgedTime) {
		this.iRsAgedTime = iRsAgedTime;
	}

	public int getSCsPort() {
		return sCsPort;
	}

	public void setSCsPort(int sCsPort) {
		this.sCsPort = sCsPort;
	}

	public int getSUdpPort() {
		return sUdpPort;
	}

	public void setSUdpPort(int sUdpPort) {
		this.sUdpPort = sUdpPort;
	}

	public String getSzConnPass() {
		return szConnPass;
	}

	public void setSzConnPass(String szConnPass) {
		this.szConnPass = szConnPass;
	}

	public String getSzCsIP() {
		return szCsIP;
	}

	public void setSzCsIP(String szCsIP) {
		this.szCsIP = szCsIP;
	}

	public String getSzDeviceNO() {
		return szDeviceNO;
	}

	public void setSzDeviceNO(String szDeviceNO) {
		this.szDeviceNO = szDeviceNO;
	}

	public String getSzServerDn() {
		return szServerDn;
	}

	public void setSzServerDn(String szServerDn) {
		this.szServerDn = szServerDn;
	}

	public String getSzServerNo() {
		return szServerNo;
	}

	public void setSzServerNo(String szServerNo) {
		this.szServerNo = szServerNo;
	}

	public String getUiAlarmStateBlindEnable() {
		return uiAlarmStateBlindEnable;
	}

	public void setUiAlarmStateBlindEnable(String uiAlarmStateBlindEnable) {
		this.uiAlarmStateBlindEnable = uiAlarmStateBlindEnable;
	}

	public String getUiAlarmStateConnectEnable() {
		return uiAlarmStateConnectEnable;
	}

	public void setUiAlarmStateConnectEnable(String uiAlarmStateConnectEnable) {
		this.uiAlarmStateConnectEnable = uiAlarmStateConnectEnable;
	}

	public String getUiAlarmStateGpinEnable() {
		return uiAlarmStateGpinEnable;
	}

	public void setUiAlarmStateGpinEnable(String uiAlarmStateGpinEnable) {
		this.uiAlarmStateGpinEnable = uiAlarmStateGpinEnable;
	}

	public String getUiAlarmStateLoseEnable() {
		return uiAlarmStateLoseEnable;
	}

	public void setUiAlarmStateLoseEnable(String uiAlarmStateLoseEnable) {
		this.uiAlarmStateLoseEnable = uiAlarmStateLoseEnable;
	}

	public String getUiAlarmStateMotionEnable() {
		return uiAlarmStateMotionEnable;
	}

	public void setUiAlarmStateMotionEnable(String uiAlarmStateMotionEnable) {
		this.uiAlarmStateMotionEnable = uiAlarmStateMotionEnable;
	}

	public String getUiAlarmStatePerformanceEnable() {
		return uiAlarmStatePerformanceEnable;
	}

	public void setUiAlarmStatePerformanceEnable(String uiAlarmStatePerformanceEnable) {
		this.uiAlarmStatePerformanceEnable = uiAlarmStatePerformanceEnable;
	}
	public boolean onParse(String json) {
		try {
			jsonObject = new JSONObject(json);
			JSONObject spvmnJson = jsonObject.getJSONObject("NetWork.SPVMN");
			bCsEnable = spvmnJson.getBoolean("bCsEnable");
			iHsIntervalTime	= spvmnJson.getInt("iHsIntervalTime");
			iRsAgedTime	= spvmnJson.getInt("iRsAgedTime");
			sCsPort	= spvmnJson.getInt("sCsPort");
			sUdpPort = spvmnJson.getInt("sUdpPort");
			szConnPass = spvmnJson.getString("szConnPass");
			szCsIP = spvmnJson.getString("szCsIP");
			szDeviceNO = spvmnJson.getString("szDeviceNO");
			szServerDn = spvmnJson.getString("szServerDn");
			szServerNo = spvmnJson.getString("szServerNo");
			uiAlarmStateBlindEnable = spvmnJson.getString("uiAlarmStateBlindEnable");
			uiAlarmStateConnectEnable = spvmnJson.getString("uiAlarmStateConnectEnable");
			uiAlarmStateGpinEnable = spvmnJson.getString("uiAlarmStateGpinEnable");
			uiAlarmStateLoseEnable = spvmnJson.getString("uiAlarmStateLoseEnable");
			uiAlarmStateMotionEnable = spvmnJson.getString("uiAlarmStateMotionEnable");
			uiAlarmStatePerformanceEnable = spvmnJson.getString("uiAlarmStatePerformanceEnable");
			JSONArray AlarmLevelJson = spvmnJson.getJSONArray("AlarmLevel");
			AlarmLevel = AlarmLevelJson.getInt(0);
			JSONArray AlarmidJson = spvmnJson.getJSONArray("Alarmid");
			Alarmid = AlarmidJson.getString(0);
			JSONArray CamreaLevelJson = spvmnJson.getJSONArray("CamreaLevel");
			CamreaLevel = CamreaLevelJson.getInt(0);
			JSONArray CamreaidJson = spvmnJson.getJSONArray("Camreaid");
			Camreaid = CamreaidJson.getString(0);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	public String getSendMsg() {

		try {
			JSONObject c_jsonObj = new JSONObject();
			JSONArray AlarmLevelArr = new JSONArray();
			AlarmLevelArr.put(0,AlarmLevel);
			JSONArray AlarmidArr = new JSONArray();
			AlarmidArr.put(0,Alarmid);
			JSONArray CamreaLevelArr = new JSONArray();
			CamreaLevelArr.put(0,CamreaLevel);
			JSONArray CamreaidArr = new JSONArray();
			CamreaidArr.put(0,Camreaid);
			c_jsonObj.put("AlarmLevel",AlarmLevelArr);
			c_jsonObj.put("Alarmid",AlarmidArr);
			c_jsonObj.put("CamreaLevel",CamreaLevelArr);
			c_jsonObj.put("Camreaid",CamreaidArr);
			c_jsonObj.put("bCsEnable", bCsEnable);
			c_jsonObj.put("iHsIntervalTime", iHsIntervalTime);
			c_jsonObj.put("iRsAgedTime", iRsAgedTime);
			c_jsonObj.put("sCsPort", sCsPort);
			c_jsonObj.put("sUdpPort", sUdpPort);
			c_jsonObj.put("szConnPass", szConnPass);
			c_jsonObj.put("szCsIP", szCsIP);
			c_jsonObj.put("szDeviceNO", szDeviceNO);
			c_jsonObj.put("szServerDn", szServerDn);
			c_jsonObj.put("szServerNo", szServerNo);
			c_jsonObj.put("uiAlarmStateBlindEnable", uiAlarmStateBlindEnable);
			c_jsonObj.put("uiAlarmStateConnectEnable", uiAlarmStateConnectEnable);
			c_jsonObj.put("uiAlarmStateGpinEnable", uiAlarmStateGpinEnable);
			c_jsonObj.put("uiAlarmStateLoseEnable", uiAlarmStateLoseEnable);
			c_jsonObj.put("uiAlarmStateMotionEnable", uiAlarmStateMotionEnable);
			c_jsonObj.put("uiAlarmStatePerformanceEnable", uiAlarmStatePerformanceEnable);
			jsonObject.put(CONFIG_NAME, c_jsonObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
		return jsonObject.toString();
	}
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
}
