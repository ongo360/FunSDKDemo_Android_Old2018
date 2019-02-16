package com.lib.sdk.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public class HandleConfigData<T> {
	public static final String TAG = "HandleConfigData";
	String seeionsid;
	String name;
	int ret;
	T obj;
	HashMap<String, String> mHashMap = new HashMap<String, String>();

	public String getSeeionsid() {
		return seeionsid;
	}

	public void setSeeionsid(String seeionsid) {
		this.seeionsid = seeionsid;
	}

	public int getRet() {
		return ret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	// 通过jsonobject 获得数据对象
	@SuppressWarnings("unchecked")
	public boolean getDataObj(String json, Class<?> cla) {
		JSONObject jObj;
		try {
			jObj = JSON.parseObject(json);
			if (null == jObj) {
				return false;
			}
			ret = jObj.getIntValue("Ret");
			if (ret >= 0) {
				name = jObj.getString("Name");
				seeionsid = jObj.getString("SessionID");

				if (null != name) {
					mHashMap.put(name, seeionsid);
					Object _obj = jObj.get(name);
					if (null == _obj) {
						return false;
					}
					if ((_obj.getClass().getName())
							.equals(JsonConfig.FAST_JSON_ARRAY)) {
						obj = (T) JSON.parseArray(jObj.getString(name), cla);
					} else if ((_obj.getClass().getName())
							.equals(JsonConfig.FAST_JSON_OBJECT)) {
						obj = (T) JSON.parseObject(jObj.getString(name), cla);
					} else {
						obj = (T) jObj.getString(name);
					}
					return true;
				} else {
					return false;
				}
			} else {
				obj = null;
				return false;
			}
		} catch (JSONException e) {
			obj = null;
			e.printStackTrace();
			return false;
		}
	}

	public boolean getDataObj(String json, String name) {
		JSONObject jObj;
		try {
			jObj = JSON.parseObject(json);
			if (null == jObj) {
				return false;
			}
			ret = jObj.getIntValue("Ret");
			if (ret >= 0) {
				seeionsid = jObj.getString("SessionID");
				obj = (T) jObj.getString(name);
				return true;
			} else {
				obj = null;
				return false;
			}
		} catch (JSONException e) {
			obj = null;
			e.printStackTrace();
			return false;
		}
	}

	public static boolean getDataRet(String json) {
		JSONObject jObj;
		jObj = JSON.parseObject(json);
		int ret = jObj.getIntValue("Ret");
		return ret == 100;
	}

	public String getSendData(String name, Object obj) {
		JSONObject sendObj = new JSONObject();
		sendObj.put("Name", name);
		sendObj.put("SessionID", mHashMap.get(name));
		sendObj.put(name, obj);
		System.out.println("TTT------------>" + JSON.toJSONString(sendObj));
		return JSON.toJSONString(sendObj);
	}

	public static String getSendData(String name, String sessionId, Object obj) {
		System.out.print("TTTT----->>" + obj.toString());
		JSONObject sendObj = new JSONObject();
		sendObj.put("Name", name);
		sendObj.put("SessionID", sessionId);
		sendObj.put(name, obj);
		System.out.println("TTT------------>" + JSON.toJSONString(sendObj));
		return JSON.toJSONString(sendObj);
	}

	public static String getFullName(String jsonName, int chnId) {
		if (chnId >= 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(jsonName).append(".").append("[").append(chnId)
					.append("]");
			return sb.toString();
		} else {
			return jsonName;
		}
	}

	public static int getIntFromHex(String s) {
		int value = 0;
		if (s != null && s.length() > 2 && s.startsWith("0x")) {
			value = Integer.parseInt((s.substring(2, s.length())), 16);
		} else if (s != null && s.isEmpty()) {

		} else if (s != null) {
			value = Integer.parseInt(s);
		}
		return value;
	}

	public static long getLongFromHex(String s) {
		long value = 0;
		if (s != null && s.length() > 2 && s.startsWith("0x")) {
			value = Long.parseLong((s.substring(2, s.length())), 16);
		} else if (s != null && s.isEmpty()) {

		} else if (s != null) {
			value = Long.parseLong(s);
		}
		return value;
	}

	public static String getHexFromInt(int i) {
		return "0x" + Integer.toHexString(i);
	}

	public static String getHexFromLong(long l) {
		return "0x" + Long.toHexString(l);
	}
}
