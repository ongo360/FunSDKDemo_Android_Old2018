package com.lib.funsdk.support.sensor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lib.FunSDK;
import com.lib.funsdk.support.utils.DeviceWifiManager;

/**
 * 门磁
 * @author ccy
 *
 */ 
public class SensorMagnetometer implements ISensorTips {

	@Override
	public String getTips(String jsonStr) {
		String tips =  FunSDK.TS("normal");
		try{
			JSONObject jobj = JSON.parseObject(jsonStr).getJSONObject("InquiryStatus");
			if(jobj.getIntValue("DevType") != DeviceWifiManager.SENSOR_TYPE.LINKCENTER_MAGNETOMETER){
				throw new RuntimeException("传入的智联设备不匹配");
			}
			int status = jobj.getIntValue("DevStatus"); 
			if((status & 0x40) == 0x40 ){
				tips = FunSDK.TS("lose_door");
			}else{
				if((status & 0x01) == 1){
					tips = FunSDK.TS("Open_the_door");
				}else{
					tips = FunSDK.TS("Close_the_door");
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return tips;
	}

}
