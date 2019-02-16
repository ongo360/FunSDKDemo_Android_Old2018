package com.lib.funsdk.support.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设备能力集
 *
 */
public class SystemFunction extends BaseConfig {
	
	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = JsonConfig.SYSTEM_FUNCTION;
	
	public class FunctionItem {
		public String attrName;
		public Boolean isSupport;
		public FunctionItem(String name, Boolean support) {
			this.attrName = name;
			this.isSupport = support;
		}
	}
	
	// 定义一组功能集合
	public class FunctionAttr {
		public String name;	// 功能名称,如: AlarmFunction,CommFunction,EncodeFunction...
		public List<FunctionItem> funcs = new ArrayList<FunctionItem>();	// AlarmConfig : True
	}
	
	private List<FunctionAttr> mFunctions = new ArrayList<FunctionAttr>();
	
	
	public SystemFunction() {
		
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
			JSONObject c_jsonobj = mJsonObj.getJSONObject(CONFIG_NAME);
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
		
		mFunctions.clear();
		
		Iterator<String> keys = obj.keys();
		while ( keys.hasNext() ) {
			String funcName = keys.next();
			if ( funcName.length() > 0 ) {
				FunctionAttr funcAttr = new FunctionAttr();
				funcAttr.name = funcName;
				
				JSONObject funcObj = obj.getJSONObject(funcName);
				Iterator<String> attrs = funcObj.keys();
				while ( attrs.hasNext() ) {
					String attr = attrs.next();
					if ( attr.length() > 0 ) {
						try {
							funcAttr.funcs.add(new FunctionItem(attr, funcObj.optBoolean(attr)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				if ( funcAttr.funcs.size() > 0 ) {
					mFunctions.add(funcAttr);
				}
			}
		}
		
		return true;
	}

	@Override
	public String getSendMsg() {
		return super.getSendMsg();
	}

	public List<FunctionAttr> getFunctionAttrs() {
		return mFunctions;
	}
}
