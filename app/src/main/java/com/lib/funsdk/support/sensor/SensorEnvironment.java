package com.lib.funsdk.support.sensor;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lib.FunSDK;
import com.lib.funsdk.support.utils.DeviceWifiManager;

/**
 * 环境  
 * 注：环境有两条状态，拼成一个Str
 * @author CCY
 *
 */
public class SensorEnvironment implements ISensorTips {

	private SensorEnvironmentBean bean;

	@Override
	public String getTips(String jsonStr) {
		StringBuilder sb = new StringBuilder();
		bean = parseJson(jsonStr);
		if (bean == null) {
			Log.d("ccy", "环境传感器，json解析错误");
			return null;
		}
		if(bean.DevType != DeviceWifiManager.SENSOR_TYPE.LINKCENTER_ENVIRONMENT){
			throw new RuntimeException("传入的智联设备不匹配");
		}

		if (bean.DevTemp != -1 || bean.DevWet != -1 || bean.DevBright != -1) {
			sb.append(ts("Temperature")).append(bean.DevTemp / 10.0f +"").append("\u2103").append(ts("humidity"))
					.append(bean.DevWet +"").append("%").append(ts("Bright")).append(bean.DevBright +"").append("\n");
		}
		if (bean.DevStatus != -1) {
			if ((bean.DevStatus & 0x40) == 0x40) {
				sb.append(ts("Water_immersion_line_is_removed"));
			} else {
				if ((bean.DevStatus & 0x01) == 1) {
					sb.append(ts("Encounter_Water"));
				} else {
					sb.append(ts("normal"));
				}
			}
		}
		return sb.toString();
	}

	private String ts(String toast) {
		return FunSDK.TS(toast);
	}

	private SensorEnvironmentBean parseJson(String jsonStr) {
		SensorEnvironmentBean b = new SensorEnvironmentBean();
		try {

			JSONObject obj = JSON.parseObject(jsonStr).getJSONObject("InquiryStatus");
			Log.d("ccy", "环境 json= "+obj);
			b = JSON.parseObject(obj.toJSONString(), SensorEnvironmentBean.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return b;
	}

	
}
