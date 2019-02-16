package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CameraFishEyeBean {

	public int AppType;

	public int Duty;

	public LightOnSec LightOnSec;

	public int WorkMode;

	public int Secene;

	public CameraFishEyeBean() {

	}
	@JSONField(name = "AppType") 
	public int getAppType() {
		return AppType;
	}

	public void setAppType(int appType) {
		AppType = appType;
	}
	@JSONField(name = "Duty")
	public int getDuty() {
		return Duty;
	}

	public void setDuty(int duty) {
		Duty = duty;
	}
	@JSONField(name = "WorkMode")
	public int getWorkMode() {
		return WorkMode;
	}

	public void setWorkMode(int workMode) {
		WorkMode = workMode;
	}
	@JSONField(name = "Secene")
	public int getSecene() {
		return Secene;
	}

	public void setSecene(int secene) {
		Secene = secene;
	}

	public  class LightOnSec {

		public int EHour;

		public int EMinute;

		public int Enable;

		public int SHour;

		public int SMinute;

		public LightOnSec() {

		}
		@JSONField(name = "EHour")
		public int getEHour() {
			return EHour;
		}

		public void setEHour(int eHour) {
			EHour = eHour;
		}
		@JSONField(name = "EMinute")
		public int getEMinute() {
			return EMinute;
		}

		public void setEMinute(int eMinute) {
			EMinute = eMinute;
		}
		@JSONField(name = "Enable")
		public int getEnable() {
			return Enable;
		}

		public void setEnable(int enable) {
			Enable = enable;
		}
		@JSONField(name = "SHour")
		public int getSHour() {
			return SHour;
		}

		public void setSHour(int sHour) {
			SHour = sHour;
		}
		@JSONField(name = "SMinute")
		public int getSMinute() {
			return SMinute;
		}

		public void setSMinute(int sMinute) {
			SMinute = sMinute;
		}

	}

}
