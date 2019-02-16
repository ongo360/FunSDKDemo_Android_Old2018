package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class OPPowerSocketGet extends BaseConfig {
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = "OPPowerSocketGet";
	
	
	private int switchPower;	// 插座电源,0表示关闭,1表示打开,其他值则忽略
	private int switchLight;	// 灯光,
	private int switchUSB;		// USB
	private int autoSwitch;		// 0 表示关闭自动功能,1表示开启自动功能,其他值被忽略
	private int autoLight;
	private int autoUSB;
	private int sensorLight;	// 自动感应(体感)
	private int sensorSwitch;
	private int sensorUsbPower;

	public OPPowerSocketGet() {
		
	}
	
	public OPPowerSocketGet(OPPowerSocketGet srcObj) {
		this.sensorLight = srcObj.sensorLight;
		this.switchPower = srcObj.switchPower;
		this.switchLight = srcObj.switchLight;
		this.switchUSB = srcObj.switchUSB;
		this.autoSwitch = srcObj.autoSwitch;
		this.autoLight = srcObj.autoLight;
		this.autoUSB = srcObj.autoUSB;
		this.sensorSwitch = srcObj.sensorSwitch;
		this.sensorUsbPower = srcObj.sensorUsbPower;
	}
	
	public void merge(OPPowerSocketGet srcObj) {
		// 只有0-1有效
		if ( srcObj.sensorLight == 0 || srcObj.sensorLight == 1 ) {
			this.sensorLight = srcObj.sensorLight;
		}
		if ( srcObj.switchPower == 0 || srcObj.switchPower == 1 ) {
			this.switchPower = srcObj.switchPower;
		}
		if ( srcObj.switchLight == 0 || srcObj.switchLight == 1 ) {
			this.switchLight = srcObj.switchLight;
		}
		if ( srcObj.switchUSB == 0 || srcObj.switchUSB == 1 ) {
			this.switchUSB = srcObj.switchUSB;
		}
		if ( srcObj.autoSwitch == 0 || srcObj.autoSwitch == 1 ) {
			this.autoSwitch = srcObj.autoSwitch;
		}
		if ( srcObj.autoLight == 0 || srcObj.autoLight == 1 ) {
			this.autoLight = srcObj.autoLight;
		}
		if ( srcObj.autoUSB == 0 || srcObj.autoUSB == 1 ) {
			this.autoUSB = srcObj.autoUSB;
		}
		if ( srcObj.sensorSwitch == 0 || srcObj.sensorSwitch == 1 ) {
			this.sensorSwitch = srcObj.sensorSwitch;
		}
		if ( srcObj.sensorUsbPower == 0 || srcObj.sensorUsbPower == 1 ) {
			this.sensorUsbPower = srcObj.sensorUsbPower;
		}
	}
	
	public int getSwitchPower() {
		return switchPower;
	}

	public int getSensorLight() {
		return sensorLight;
	}

	public void setSensorLight(int sensorLight) {
		this.sensorLight = sensorLight;
		this.switchPower = -1;
		this.switchLight = -1;
		this.switchUSB = -1;
		this.autoSwitch = -1;
		this.autoLight = 0;
		this.autoUSB = -1;
		this.sensorSwitch = -1;
		this.sensorUsbPower = -1;
	}

	public int getSensorSwitch() {
		return sensorSwitch;
	}

	public void setSensorSwitch(int sensorSwitch) {
		this.sensorSwitch = sensorSwitch;
		this.switchPower = -1;
		this.switchLight = -1;
		this.switchUSB = -1;
		this.autoSwitch = 0;
		this.autoLight = -1;
		this.autoUSB = -1;
		this.sensorLight = -1;
		this.sensorUsbPower = -1;
	}

	public int getSensorUsbPower() {
		return sensorUsbPower;
	}

	public void setSensorUsbPower(int sensorUsbPower) {
		this.sensorUsbPower = sensorUsbPower;
		this.switchPower = -1;
		this.switchLight = -1;
		this.switchUSB = -1;
		this.autoSwitch = -1;
		this.autoLight = -1;
		this.autoUSB = 0;
		this.sensorLight = -1;
		this.sensorSwitch = -1;
	}

	public void setSwitchPower(int switchPower) {
		this.switchPower = switchPower;
		this.switchLight = -1;
		this.switchUSB = -1;
		this.autoSwitch = 0;
		this.autoLight = -1;
		this.autoUSB = -1;
		this.sensorLight = -1;
		this.sensorSwitch = 0;
		this.sensorUsbPower = -1;
	}

	public int getSwitchLight() {
		return switchLight;

	}

	public void setSwitchLight(int switchLight) {
		this.switchLight = switchLight;
		this.switchPower = -1;
		this.switchUSB = -1;
		this.autoSwitch = -1;
		this.autoLight = 0;
		this.autoUSB = -1;
		this.sensorLight = 0;
		this.sensorSwitch = -1;
		this.sensorUsbPower = -1;
	}

	public int getSwitchUSB() {
		return switchUSB;
	}

	public void setSwitchUSB(int switchUSB) {
		this.switchUSB = switchUSB;
		this.switchPower = -1;
		this.switchLight = -1;
		this.autoSwitch = -1;
		this.autoLight = -1;
		this.autoUSB = 0;
		this.sensorLight = -1;
		this.sensorSwitch = -1;
		this.sensorUsbPower = 0;
	}

	public int getAutoSwitch() {
		return autoSwitch;
	}

	public void setAutoSwitch(int autoSwitch) {
		this.autoSwitch = autoSwitch;
		this.switchUSB = -1;
		this.switchPower = -1;
		this.switchLight = -1;
		this.autoLight = -1;
		this.autoUSB = -1;
		this.sensorLight = -1;
		this.sensorSwitch = 0;
		this.sensorUsbPower = -1;
	}

	public int getAutoLight() {
		return autoLight;
	}

	public void setAutoLight(int autoLight) {
		this.autoLight = autoLight;
		this.autoSwitch = -1;
		this.switchUSB = -1;
		this.switchPower = -1;
		this.switchLight = -1;
		this.autoUSB = -1;
		this.sensorLight = 0;
		this.sensorSwitch = -1;
		this.sensorUsbPower = -1;
	}

	public int getAutoUSB() {
		return autoUSB;
	}

	public void setAutoUSB(int autoUSB) {
		this.autoUSB = autoUSB;
		this.autoLight = -1;
		this.autoSwitch = -1;
		this.switchUSB = -1;
		this.switchPower = -1;
		this.switchLight = -1;
		this.sensorLight = -1;
		this.sensorSwitch = -1;
		this.sensorUsbPower = 0;
	}

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			JSONObject c_jsonobj = mJsonObj.getJSONObject(CONFIG_NAME);
			return onParse(c_jsonobj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj)
			return false;
		switchPower = obj.optInt("Switch");
		switchLight = obj.optInt("Light");
		switchUSB = obj.optInt("UsbPower");
		autoSwitch = obj.optInt("AutoSwitch");
		autoLight = obj.optInt("AutoLight");
		autoUSB = obj.optInt("AutoUsbPower");
		sensorSwitch = obj.optInt("SensorSwitch");
		sensorLight = obj.optInt("SensorLight");
		sensorUsbPower = obj.optInt("SensorUsbPower");
		return true;
	}

	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", CONFIG_NAME);
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_jsonObj = null;
			if ( mJsonObj.isNull(CONFIG_NAME) ) {
				c_jsonObj = new JSONObject();
			} else {
				c_jsonObj = mJsonObj.getJSONObject(CONFIG_NAME);
			}
			
			c_jsonObj.put("Switch", switchPower);
			c_jsonObj.put("Light", switchLight);
			c_jsonObj.put("UsbPower", switchUSB);
			c_jsonObj.put("AutoSwitch", autoSwitch);
			c_jsonObj.put("AutoLight", autoLight);
			c_jsonObj.put("AutoUsbPower", autoUSB);
			c_jsonObj.put("SensorSwitch", sensorSwitch);
			c_jsonObj.put("SensorLight", sensorLight);
			c_jsonObj.put("SensorUsbPower", sensorUsbPower);
			
			mJsonObj.put(CONFIG_NAME, c_jsonObj);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(CONFIG_NAME, "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	
}
