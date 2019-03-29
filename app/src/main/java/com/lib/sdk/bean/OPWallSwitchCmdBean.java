package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class OPWallSwitchCmdBean {
	public static final String JSON_NAME = "OPConsumerProCmd";
	public static final String CMD_NAME = "ChangeSwitchState";
	public static final int JSON_ID = 2046;
	@JSONField(name = "Cmd")
	private String cmd = CMD_NAME;
	@JSONField(name = "Arg1")
	private String arg1;
	@JSONField(name = "SensorState")
	private SensorState sensorState;

	public SensorState getSensorState() {
		return sensorState;
	}

	public void setSensorState(SensorState sensorState) {
		this.sensorState = sensorState;
	}

	class SensorState {
		@JSONField(name = "ControlMask")
		private int controlMask;
		@JSONField(name = "IgnoreMask")
		private int ignoreMask;

		public int getControlMask() {
			return controlMask;
		}

		public void setControlMask(int controlMask) {
			this.controlMask = controlMask;
		}

		public int getIgnoreMask() {
			return ignoreMask;
		}

		public void setIgnoreMask(int ignoreMask) {
			this.ignoreMask = ignoreMask;
		}
	}

	@JSONField(serialize = false)
	public static String getCmdJson(String devId,int ctrlMask,int ignoreMask) {
		OPWallSwitchCmdBean bean = new OPWallSwitchCmdBean();
		SensorState sensorState = bean.new SensorState();
		sensorState.setControlMask(ctrlMask);
		sensorState.setIgnoreMask(ignoreMask);
		bean.setSensorState(sensorState);
		bean.setArg1(devId);
		return HandleConfigData.getSendData(JSON_NAME, "0x01", bean);
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getArg1() {
		return arg1;
	}

	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
}
