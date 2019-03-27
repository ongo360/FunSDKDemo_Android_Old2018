package com.lib.funsdk.support.sensor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lib.FunSDK;
import com.lib.funsdk.support.utils.DeviceWifiManager;

/**
 * 燃气
 * @author ccy
 *
 */
public class SensorFuelGas implements ISensorTips {

	@Override
	public String getTips(String jsonStr) {
		String tips = FunSDK.TS("normal");
		try{
			JSONObject jobj = JSON.parseObject(jsonStr).getJSONObject("InquiryStatus");
			if(jobj.getIntValue("DevType") != DeviceWifiManager.SENSOR_TYPE.LINKCENTER_FUEL_GAS){
				throw new RuntimeException("传入的智联设备不匹配");
			}
			int status = jobj.getIntValue("DevStatus");
			if((status & 0x01) ==1){
				tips = FunSDK.TS("Gas_leakage");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
				
		return tips;
	}
	
}
