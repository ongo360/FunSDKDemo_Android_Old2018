package com.lib.funsdk.support.config;

import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.utils.MyUtils;

import android.nfc.cardemulation.OffHostApduService;

import org.json.JSONException;
import org.json.JSONObject;

public class CameraParam extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.CAMERA_PARAM;

	public int AeSensitivity;
	public int Day_nfLevel;			// 降噪(白天)
	public int DncThr;
	public int ElecLevel;
	public int IRCUTMode;
	public int IrcutSwap;
	public int Night_nfLevel;		// 降噪(晚上),0:关,2:低级,4:高级
	public String ApertureMode;
	public String BLCMode;			// 背光补偿
	public String DayNightColor;
	public String EsShutter;
	public String PictureFlip;		// 图像上下翻转
	public String PictureMirror;	// 图像做鱼翻转
	public String RejectFlicker;
	public String WhiteBalance;
	
	public ExposureParam exposureParam = new ExposureParam();
	public GainParam gainParam = new GainParam();
	
	public class ExposureParam{
		public String LeastTime;
		public int Level;
		public String MostTime;
	}
	
	public class GainParam {
		public int AutoGain;
		public int Gain;
	}
			
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	public String getConfigNameOfChannel(){
		return Config_Name_ofchannel;
	}
	
	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			Config_Name_ofchannel = mJsonObj.getString("Name");
			JSONObject c_jsonobj = mJsonObj.getJSONObject(mJsonObj.getString("Name"));
			return onParse(c_jsonobj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj) {
			return false;
		}
		
		AeSensitivity = obj.optInt("AeSensitivity");
		Day_nfLevel = obj.optInt("Day_nfLevel");
		DncThr = obj.optInt("DncThr");
		ElecLevel = obj.optInt("ElecLevel");
		IRCUTMode = obj.optInt("IRCUTMode");
		IrcutSwap = obj.optInt("IrcutSwap");
		Night_nfLevel = obj.optInt("Night_nfLevel");
		
		ApertureMode = obj.optString("ApertureMode");
		BLCMode = obj.optString("BLCMode");
		DayNightColor = obj.optString("DayNightColor");
		EsShutter = obj.optString("EsShutter");
		PictureFlip = obj.optString("PictureFlip");
		PictureMirror = obj.optString("PictureMirror");
		RejectFlicker = obj.optString("RejectFlicker");
		WhiteBalance = obj.optString("WhiteBalance");
		
		if ( obj.has("ExposureParam") ) {
			JSONObject expObj = obj.getJSONObject("ExposureParam");
			this.exposureParam.LeastTime = expObj.optString("LeastTime");
			this.exposureParam.Level = expObj.optInt("Level");
			this.exposureParam.MostTime = expObj.optString("MostTime");
		}
		
		if ( obj.has("GainParam") ) {
			JSONObject gaObj = obj.getJSONObject("GainParam");
			this.gainParam.AutoGain = gaObj.getInt("AutoGain");
			this.gainParam.Gain = gaObj.getInt("Gain");
		}
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigNameOfChannel());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONObject c_json = null;
			if ( mJsonObj.isNull(getConfigNameOfChannel()) ) {
				c_json = new JSONObject();
			} else {
				c_json = mJsonObj.getJSONObject(getConfigNameOfChannel());
			}
			
			c_json.put("AeSensitivity", AeSensitivity);
			c_json.put("Day_nfLevel", Day_nfLevel);
			c_json.put("DncThr", DncThr);
			c_json.put("ElecLevel", ElecLevel);
			c_json.put("IRCUTMode", IRCUTMode);
			c_json.put("IrcutSwap", IrcutSwap);
			c_json.put("Night_nfLevel", Night_nfLevel);
			c_json.put("ApertureMode", ApertureMode);
			c_json.put("BLCMode", BLCMode);
			c_json.put("DayNightColor", DayNightColor);
			c_json.put("EsShutter", EsShutter);
			c_json.put("PictureFlip", PictureFlip);
			c_json.put("PictureMirror", PictureMirror);
			c_json.put("RejectFlicker", RejectFlicker);
			c_json.put("WhiteBalance", WhiteBalance);
			
			JSONObject expObj = new JSONObject();
			expObj.put("LeastTime", exposureParam.LeastTime);
			expObj.put("Level", exposureParam.Level);
			expObj.put("MostTime", exposureParam.MostTime);
			c_json.put("ExposureParam", expObj);
			
			JSONObject gaObj = new JSONObject();
			gaObj.put("AutoGain", gainParam.AutoGain);
			gaObj.put("Gain", gainParam.Gain);
			c_json.put("GainParam", gaObj);
			
			mJsonObj.put(getConfigNameOfChannel(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	public boolean getPictureFlip() {
		return MyUtils.getIntFromHex(PictureFlip) != 0;
	}
	
	public void setPictureFlip(boolean enable) {
		if ( enable ) {
			PictureFlip = "0x1";
		} else {
			PictureFlip = "0x0";
		}
	}
	
	public boolean getPictureMirror() {
		return MyUtils.getIntFromHex(PictureMirror) != 0;
	}
	
	public void setPictureMirror(boolean enable) {
		if ( enable ) {
			PictureMirror = "0x1";
		} else {
			PictureMirror = "0x0";
		}
	}
	
	public boolean getBLCMode() {
		return MyUtils.getIntFromHex(BLCMode) != 0;
	}
	
	public void setBLCMode(boolean enable) {
		if ( enable ) {
			BLCMode = "0x1";
		} else {
			BLCMode = "0x0";
		}
	}
}
