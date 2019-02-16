package com.lib.sdk.bean;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.G;

public class SquareVideoJsonParse {
	public static List<Evaluation> getEvaFromJSON(String str) {
		List<Evaluation> dataList = new ArrayList<Evaluation>();
		JSONArray jsa;
		JSONObject jso;
		try {
			if (StringUtils.isStringNULL(str))
				return null;
			jso = new JSONObject(G.UnescapeHtml3(str));
			if (jso.getInt("code") != 10001)
				return null;
			if (jso.isNull("data"))
				return null;
			String jsonArray = jso.getString("data");
			jsa = new JSONArray(jsonArray);
			Evaluation eva;
			for (int i = 0; i < jsa.length(); i++) {
				eva = new Evaluation();
				JSONObject temp = (JSONObject) jsa.get(i);
				eva.setContent(temp.optString("content"));
				eva.setHostIp(temp.optString("hostIp"));
				eva.setUpdateTime(temp.optString("updateTime"));
				eva.setUserName(temp.optString("userName"));
				eva.setVideo(temp.optString("video"));
				eva.setVideoId(temp.optInt("videoId"));
				dataList.add(eva);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	public static SquareVideo getLiveVideoInfo(String jsonStr) {
		if (StringUtils.isStringNULL(jsonStr))
			return null;
		try {
			JSONObject jsonObj = new JSONObject(
					G.UnescapeHtml3(jsonStr));
			if (jsonObj.getInt("code") != 10001 || jsonObj.isNull("data"))
				return null;
			JSONArray jsonArray = new JSONArray(jsonObj.getString("data"));
			if (jsonArray == null || jsonArray.length() < 1)
				return null;
			Object _obj = jsonArray.get(0);
			if (!(_obj instanceof JSONObject)) {
				return null;
			}
			SquareVideo mLiveVideo = new SquareVideo();
			JSONObject obj = (JSONObject) _obj;
			mLiveVideo.setTitle(obj.optString("title"));
			mLiveVideo.setUpdateTime(obj.optString("updateTime"));
			mLiveVideo.setLocation(obj.optString("location"));
			mLiveVideo.setImageUrl(obj.optString("imageUrl"));
			mLiveVideo.setDescription(obj.optString("description"));
			mLiveVideo.setReportQuantity(obj.optInt("reportQuantity"));
			mLiveVideo.setPraiseQuantity(obj.optInt("praiseQuantity"));
			mLiveVideo.setPlayQuantity(obj.optInt("playQuantity"));
			mLiveVideo.setReviewQuantity(obj.optInt("reviewQuantity"));
			mLiveVideo.setContext(obj.optString("context"));
			mLiveVideo.setUserName(obj.optString("userName"));
			mLiveVideo.setUrl(obj.optString("url"));
			mLiveVideo.setStyle(obj.optInt("style"));
			return mLiveVideo;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<SquareVideo> getSquareVideoList(String jsonStr) {
		if (StringUtils.isStringNULL(jsonStr))
			return null;
		try {
			JSONObject jsonObj = new JSONObject(
					G.UnescapeHtml3(jsonStr));
			if (jsonObj.getInt("code") != 10001 || jsonObj.isNull("data"))
				return null;
			JSONArray jsonArray = new JSONArray(jsonObj.getString("data"));
			if (jsonArray == null || jsonArray.length() < 1)
				return null;
			ArrayList<SquareVideo> mDataList = new ArrayList<SquareVideo>();
			SquareVideo mLiveVideo;
			for (int i = 0; i < jsonArray.length(); i++) {
				Object _obj = jsonArray.get(i);
				if (!(_obj instanceof JSONObject)) {
					return null;
				}
				mLiveVideo = new SquareVideo();
				JSONObject obj = (JSONObject) _obj;
				mLiveVideo.setTitle(obj.optString("title"));
				mLiveVideo.setUpdateTime(obj.optString("updateTime"));
				mLiveVideo.setLocation(obj.optString("location"));
				mLiveVideo.setImageUrl(obj.optString("imageUrl"));
				mLiveVideo.setDescription(obj.optString("description"));
				mLiveVideo.setReportQuantity(obj.optInt("reportQuantity"));
				mLiveVideo.setPraiseQuantity(obj.optInt("praiseQuantity"));
				mLiveVideo.setPlayQuantity(obj.optInt("playQuantity"));
				mLiveVideo.setReviewQuantity(obj.optInt("reviewQuantity"));
				mLiveVideo.setContext(obj.optString("context"));
				mLiveVideo.setUserName(obj.optString("userName"));
				mLiveVideo.setUrl(obj.optString("url"));
				mLiveVideo.setStyle(obj.optInt("style"));
				mLiveVideo.setId(obj.optInt("id"));
				mDataList.add(mLiveVideo);
			}
			return mDataList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
