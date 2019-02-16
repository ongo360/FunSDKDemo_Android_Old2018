package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class SimplifyEncode extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.SIMPLIFY_ENCODE;

	public VideoFormat mainFormat = new VideoFormat();
	public VideoFormat extraFormat = new VideoFormat();

	public class VideoFormat {
		public boolean VideoEnable;			//视频
		public boolean AudioEnable;			//音频
		public Video video = new Video();
		
		public void parse(JSONObject jsonObj) {
			VideoEnable = jsonObj.optBoolean("VideoEnable");
			AudioEnable = jsonObj.optBoolean("AudioEnable");
			if ( jsonObj.has("Video") ) {
				video.parse(jsonObj.optJSONObject("Video"));
			}
		}
		
		public JSONObject toJson() {
			JSONObject jsonObj = new JSONObject();
			
			try {
				jsonObj.put("VideoEnable", VideoEnable);
				jsonObj.put("AudioEnable", AudioEnable);
				jsonObj.put("Video", video.toJson());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return jsonObj;
		}
	}
			
	public class Video {
		public int Quality;				// 画质,6/5/4/3/2/1
		public int FPS;					//帧数
		public int GOP;
		public int BitRate;				//比特率
		public String Resolution;		// 分辨率
		public String Compression;
		public String BitRateControl;
		
		public void parse(JSONObject jsonObj) {
			Quality = jsonObj.optInt("Quality");
			FPS = jsonObj.optInt("FPS");
			GOP = jsonObj.optInt("GOP");
			BitRate = jsonObj.optInt("BitRate");
			Resolution = jsonObj.optString("Resolution");
			Compression = jsonObj.optString("Compression");
			BitRateControl = jsonObj.optString("BitRateControl");
		}
		
		public JSONObject toJson() {
			JSONObject jsonObj = new JSONObject();
			
			try {
				jsonObj.put("Quality", Quality);
				jsonObj.put("FPS", FPS);
				jsonObj.put("GOP", GOP);
				jsonObj.put("BitRate", BitRate);
				jsonObj.put("Resolution", Resolution);
				jsonObj.put("Compression", Compression);
				jsonObj.put("BitRateControl", BitRateControl);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return jsonObj;
		}
	}
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
			return false;
		try {
			Object obj = mJsonObj.get(getConfigName());
			
			JSONObject c_jsonobj = null;
			if ( obj instanceof JSONObject ) {
				c_jsonobj = mJsonObj.getJSONObject(getConfigName());
			} else if (obj instanceof JSONArray){
				c_jsonobj = mJsonObj.getJSONArray(getConfigName()).getJSONObject(0);
			}
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
		
		if ( obj.has("MainFormat") ) {
			mainFormat.parse(obj.optJSONObject("MainFormat"));
		}
		
		if ( obj.has("ExtraFormat") ) {
			extraFormat.parse(obj.optJSONObject("ExtraFormat"));
		}
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigName());
			mJsonObj.put("SessionID", "0x00001234");
			
			JSONArray c_jsonArray = new JSONArray();
			JSONObject c_json = new JSONObject();
			
			c_json.put("MainFormat", mainFormat.toJson());
			c_json.put("ExtraFormat", extraFormat.toJson());
			
			c_jsonArray.put(c_json);
			
			mJsonObj.put(getConfigName(), c_jsonArray);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

}
