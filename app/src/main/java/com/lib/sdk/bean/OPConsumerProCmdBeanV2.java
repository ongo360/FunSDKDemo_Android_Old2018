package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class OPConsumerProCmdBeanV2 {
	public static final String JSON_NAME = "OPConsumerProCmd";
	public static final int JSON_ID = 2046;
	public String Cmd;
	public String Arg1;
	public Arg2 Arg2;

	public class Arg2 {
		public String sceneId;
		public int status;
	}

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
	public Arg2 getArg2() {
		return Arg2;
	}

	public void setArg2(Arg2 arg2) {
		Arg2 = arg2;
	}

	@JSONField(serialize = false)
	public static String getCmdJson(String cmd,String arg1,String arg2,int status) {
		OPConsumerProCmdBeanV2 bean = new OPConsumerProCmdBeanV2();
		bean.Cmd = cmd;
		bean.Arg1 = arg1;
		bean.Arg2 = bean.new Arg2();
		bean.Arg2.sceneId = arg2;
		bean.Arg2.status = status;
		return HandleConfigData.getSendData(JSON_NAME, "0x01", bean);
	}
	
}
