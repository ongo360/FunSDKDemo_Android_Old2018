package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * jsonName :GetAllDevList<br/>
 * 为{@link GetModeConfigBean}所使用<br/>
 *@see SensorDevCfgList
 */
public class GetAllDevListBean {

	public String DevAddr;
	public String DevID;
	public String DevName;
	public int DevType;
	@JSONField(name = "Status")
	private int alarmStatus;


	private int functionStatus;//功能状态 用于窗帘 墙壁开关等设备
	public int msgCount = 0; // 消息角标
	public String tips;
	public boolean isLinked = true;

	public int getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(int devStatus) {
		this.alarmStatus = devStatus;
	}

	public int getFunctionStatus() {
		return functionStatus;
	}

	public void setFunctionStatus(int functionStatus) {
		this.functionStatus = functionStatus;
	}
}
