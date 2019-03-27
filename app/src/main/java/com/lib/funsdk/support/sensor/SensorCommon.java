package com.lib.funsdk.support.sensor;

import com.lib.funsdk.support.utils.DeviceWifiManager;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.InquiryStatusBean;

/**
 * 公共实现,判断智联设备type来返回对应的实现类的getTips
 * @author ccy
 *
 */
public class SensorCommon implements ISensorTips {
	
	private ISensorTips s;

	@Override
	public String getTips(String jsonStr) {
		try {
			HandleConfigData data = new HandleConfigData();
			if (data.getDataObj(jsonStr, InquiryStatusBean.class)) {
				InquiryStatusBean bean = (InquiryStatusBean) data.getObj();
				if (bean != null) {
					switch (bean.getDevType()) {
						case DeviceWifiManager.SENSOR_TYPE.LINKCENTER_BODY_INFRARED:
							s = new SensorBodyInfrared();
							break;
						case DeviceWifiManager.SENSOR_TYPE.LINKCENTER_ENVIRONMENT:
							s = new SensorEnvironment();
							break;
						case DeviceWifiManager.SENSOR_TYPE.LINKCENTER_FUEL_GAS:
							s= new SensorFuelGas();
							break;
						case DeviceWifiManager.SENSOR_TYPE.LINKCENTER_MAGNETOMETER:
							s = new SensorMagnetometer();
							break;
						case DeviceWifiManager.SENSOR_TYPE.LINKCENTER_SMOKE:
							s = new SensorSmoke();
							break;
						case DeviceWifiManager.SENSOR_TYPE.LINKCENTER_WATER_IMMERSION:
							s = new SensorWaterImmersion();
							break;
						default:
							break;
					}
				}
				return s == null? null : s.getTips(jsonStr);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
