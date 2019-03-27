package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class OPConsumerProCmdBean {
	public static final String JSON_NAME = "OPConsumerProCmd";
	public static final int JSON_ID = 2046;
	public String Cmd;
	public String Arg1;
	public String Arg2;

	public AlarmInfoBean ConsSensorAlarm;



	@JSONField(name = "Cmd")
	public String getCmd() {
		return Cmd;
	}

	public void setCmd(String cmd) {
		this.Cmd = cmd;
	}

	@JSONField(name = "Arg1")
	public String getArg1() {
		return Arg1;
	}

	public void setArg1(String arg1) {
		this.Arg1 = arg1;
	}

	@JSONField(name = "Arg2")
	public String getArg2() {
		return Arg2;
	}

	public void setArg2(String arg2) {
		Arg2 = arg2;
	}

	@JSONField(serialize = false)
	public static String getCmdJson(String cmd,String arg1,String arg2) {
		OPConsumerProCmdBean bean = new OPConsumerProCmdBean();
		bean.Cmd = cmd;
		bean.Arg1 = arg1;
		bean.Arg2 = arg2;
		return HandleConfigData.getSendData(JSON_NAME, "0x01", bean);
	}

}
