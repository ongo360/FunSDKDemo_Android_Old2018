package com.lib.funsdk.support.sensor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lib.FunSDK;
import com.lib.funsdk.support.utils.DeviceWifiManager;

/**
 * 烟雾
 * @author ccy
 *
 */
public class SensorSmoke implements ISensorTips {

	@Override
	public String getTips(String jsonStr) {
		String tips = FunSDK.TS("normal");
		try{
			JSONObject jobj = JSON.parseObject(jsonStr).getJSONObject("InquiryStatus");
			if(jobj.getIntValue("DevType") != DeviceWifiManager.SENSOR_TYPE.LINKCENTER_SMOKE){
				throw new RuntimeException("传入的智联设备不匹配");
			}
			int Status = jobj.getIntValue("DevStatus");
			if((Status & 0x01 ) == 1){
				tips = FunSDK.TS("Include_Smoke");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return tips;
	}

}
