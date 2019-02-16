package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class EncodeCapability extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析
	 */
	public static final String CONFIG_NAME = JsonConfig.ENCODE_CAPABILITY;

	public int MaxBitrate; // 支持的总码率大小
	public int MaxEncodePower; // 总最大编码能力
	public int ChannelMaxSetSync;	//是否需要同步分辨率
	public int MaxEncodePowerPerChannel;  //当前通道最大编码能力
	public int ImageSizePerChannel;			//当前通道图像质量（应该是分辨率能力集）
	public int ExImageSizePerChannel;		//辅码流的
	public JSONArray ExImageSizePerChannelEx;	//确定主码流的情况下，辅码流当前通道分辨率能力集

	public boolean Enable;			//是否支持该编码方式
	public boolean HaveAudio;		
	public String StreamType;		//码流类型
	public int CompressionMask; 	// 编码模式
	public long ResolutionMask; 	//该码流下 支持的分辨率

	public int Comb_CompressionMask;	//以下是组合编码信息（我也不懂）
	public long Comb_ResolutionMask;
	public boolean Comb_Enable;
	public boolean Comb_HaveAudio;
	public String Comb_StreamType;

	JSONObject obj;
	private JSONArray encodeInfo_Array;
	private JSONObject encodeInfo_Obj;
	private int mChannel;

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json, int channel) {		//通过对源xmeye的上下文观察，传进来的应该是通道号
		if (!super.onParse(json))
			return false;
		try {
			mChannel = channel;
			Object obj = mJsonObj.get(getConfigName());
			JSONObject c_jsonobj = null;
			if (obj instanceof JSONObject) {
				c_jsonobj = mJsonObj.getJSONObject(getConfigName());
			} else if (obj instanceof JSONArray) {
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
		this.obj = obj;
		MaxBitrate = obj.optInt("MaxBitrate");
		MaxEncodePower = obj.optInt("MaxEncodePower");

		if (obj.has("EncodeInfo")) {
			// getEncodeInfo(ENCODE_INFO);
			encodeInfo_Array = obj.optJSONArray("EncodeInfo");
			encodeInfo_Obj = encodeInfo_Array.optJSONObject(0);			//此处取主码流的能力集作为默认能力集
			for (int i = 0; i < 5; i++) {		/*这两行是从xmeye copy来的*/
				if (i == mChannel) {				/*本人觉得毫无意义*/
					Enable = encodeInfo_Obj.optBoolean("Enable");
					CompressionMask = getValues("CompressionMask");
					HaveAudio = encodeInfo_Obj.optBoolean("HaveAudio");
					StreamType = encodeInfo_Obj.optString("StreamType");
					ResolutionMask = getValue("ResolutionMask");

				}
			}
		}

		if (obj.has("CombEncodeInfo")) {
			// getEncodeInfo(COMB_ENCODE_INFO);
			encodeInfo_Array = obj.optJSONArray("CombEncodeInfo");
			encodeInfo_Obj = encodeInfo_Array.optJSONObject(0);
			for (int i = 0; i < 5; i++) {			//同上
				if (i == mChannel) {
					Comb_Enable = encodeInfo_Obj.optBoolean("Enable");
					Comb_CompressionMask = getValues("CompressionMask");
					Comb_HaveAudio = encodeInfo_Obj.optBoolean("HaveAudio");
					Comb_StreamType = encodeInfo_Obj.optString("StreamType");
					Comb_ResolutionMask = getValue("ResolutionMask");
				}
			}
		}
		ChannelMaxSetSync = obj.optInt("ChannelMaxSetSync");
		MaxEncodePowerPerChannel = getValue("MaxEncodePowerPerChannel", mChannel);
		ExImageSizePerChannel = getValue("ExImageSizePerChannel", mChannel);
		ExImageSizePerChannelEx = obj.optJSONArray("ExImageSizePerChannelEx").optJSONArray(mChannel);
		ImageSizePerChannel = getValue("ImageSizePerChannel", mChannel);

		Log.d("TTT", "TTT" + "ImageSizePerChannel: " + ImageSizePerChannel + " ResolutionMask: " + ResolutionMask);

		return true;
	}

	@Override
	public String getSendMsg() {
		return super.getSendMsg();

	}

	private int getValues(String space) {
		if (encodeInfo_Array == null)
			return 0;
		int value = 0;
		if (encodeInfo_Obj.has(space)) {
			String memoryparam = encodeInfo_Obj.optString(space);
			if (memoryparam != null && memoryparam.length() > 2) {
				value = Integer.parseInt(memoryparam.substring(2, memoryparam.length()), 16);
			} else
				value = encodeInfo_Obj.optInt(space);
		}
		return value;
	}

	private int getValue(String space, int channel) {
		if (obj == null)
			return 0;
		int value = 0;
		if (obj.has(space)) {
			String memoryparam = obj.optJSONArray(space).optString(channel);
			if (memoryparam != null && memoryparam.length() > 2) {
				value = Integer.parseInt(memoryparam.substring(2, memoryparam.length()), 16);
			} else
				value = obj.optInt(space);
		}
		return value;
	}

	private long getValue(String space) {
		if (encodeInfo_Array == null)
			return 0;
		long value = 0;
		if (encodeInfo_Obj.has(space)) {
			String memoryparam = encodeInfo_Obj.optString(space);
			if (memoryparam != null && memoryparam.length() > 2) {
				value = Long.parseLong(memoryparam.substring(2, memoryparam.length()), 16);
			} else
				value = encodeInfo_Obj.optInt(space);
		}
		return value;
	}

}
