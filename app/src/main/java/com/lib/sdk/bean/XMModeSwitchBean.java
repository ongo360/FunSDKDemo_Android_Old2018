package com.lib.sdk.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author hws 设备场景模式切换
 */
public class XMModeSwitchBean {
	public static String getConfig() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("Name", JsonConfig.CFG_XMMODE_SWITCH_GET);
			obj.put("SessionID", "0x00000001");
			return obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public String setConfig() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("Name", JsonConfig.CFG_XMMODE_SWITCH_SET);
			obj.put("SessionID", "0x00000001");
			obj.put("XMModeSwitch.ModeIndex", mode);
			return obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@JSONField(name = "ModeIndex")
	private int mode;
	@JSONField(name = "Enable")
	private ModeEnableBean modeEnable;

	public ModeEnableBean getModeEnable() {
		return modeEnable;
	}

	public void setModeEnable(ModeEnableBean modeEnable) {
		this.modeEnable = modeEnable;
	}

	public int getMode() {
		return mode;
	}

	/**
	 * 
	 * @param mode
	 *            行车记录仪为1，勇士为0
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

}
