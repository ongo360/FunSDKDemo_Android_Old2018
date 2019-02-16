package com.lib.funsdk.support.config;

import java.util.ArrayList;
import java.util.List;

import com.lib.funsdk.support.FunLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AVEncVideoWidget extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.CFG_WIDEOWIDGET;
	
	public ChannelTitle channelTitle = new ChannelTitle();	// OSD(水印叠加)信息
	public Cover channelTitleAttribute = new Cover();
	public Cover timeTitleAttribute = new Cover();
	public int CoversNum;
	public List<Cover> Covers = new ArrayList<Cover>();
	
	
	public class ChannelTitle {
		public String Name;			// OSD(水印叠加) 设备通道名称
		public String SerialNo;
		
		public void parse(JSONObject jsonObj) {
			Name = jsonObj.optString("Name");
			SerialNo = jsonObj.optString("SerialNo");
		}
		
		public JSONObject toJson() {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("Name", Name);
				jsonObj.put("SerialNo", SerialNo);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonObj;
		}
	}
	
	public class Cover {
		public String BackColor;
		public String FrontColor;
		public boolean EncodeBlend;
		public boolean PreviewBlend;
		public int[] RelativePos = new int[4];
		
		public void parse(JSONObject jsonObj) {
			BackColor = jsonObj.optString("BackColor");
			FrontColor = jsonObj.optString("FrontColor");
			EncodeBlend = jsonObj.optBoolean("EncodeBlend");
			PreviewBlend = jsonObj.optBoolean("PreviewBlend");
			JSONArray posArray = jsonObj.optJSONArray("RelativePos");
			if ( null != posArray ) {
				for ( int i =0; i < posArray.length() 
						&& i < RelativePos.length; 
						i ++ ) {
					RelativePos[i] = posArray.optInt(i);
				}
			}
		}
		
		public JSONObject toJson() {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("BackColor", BackColor);
				jsonObj.put("FrontColor", FrontColor);
				jsonObj.put("EncodeBlend", EncodeBlend);
				jsonObj.put("PreviewBlend", PreviewBlend);
				
				JSONArray posArray = new JSONArray();
				for ( int i = 0; i < RelativePos.length; i ++ ) {
					posArray.put(RelativePos[i]);
				}
				jsonObj.put("RelativePos", posArray);
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
		
		Covers.clear();
		
		channelTitle.parse(obj.optJSONObject("ChannelTitle"));
		channelTitleAttribute.parse(obj.optJSONObject("ChannelTitleAttribute"));
		timeTitleAttribute.parse(obj.optJSONObject("TimeTitleAttribute"));
		CoversNum = obj.optInt("CoversNum");
		JSONArray coversArray = obj.optJSONArray("Covers");
		for ( int i = 0; i < coversArray.length(); i ++ ) {
			Cover cover = new Cover();
			cover.parse(coversArray.optJSONObject(i));
			Covers.add(cover);
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
			
			c_json.put("ChannelTitle", channelTitle.toJson());
			c_json.put("ChannelTitleAttribute", channelTitleAttribute.toJson());
			c_json.put("TimeTitleAttribute", timeTitleAttribute.toJson());
			c_json.put("CoversNum", CoversNum);
			
			JSONArray coversArray = new JSONArray();
			for ( int i = 0; i < Covers.size(); i ++ ) {
				coversArray.put(Covers.get(i).toJson());
			}
			c_json.put("Covers", coversArray);
			
			mJsonObj.put(getConfigNameOfChannel(), c_json);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}

	/**
	 * 获取ChannelTitle
	 * @return
	 */
	public String getChannelTitle() {
		return channelTitle.Name;
	}
	
	/**
	 * 设置ChannelTitle
	 * @param title
	 */
	public void setChannelTitle(String title) {
		channelTitle.Name = title;
	}
}
