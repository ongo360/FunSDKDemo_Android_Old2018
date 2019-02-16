package com.lib.funsdk.support.config;

import org.json.JSONException;
import org.json.JSONObject;

import com.lib.FunSDK;

public class ModifyPassword extends BaseConfig {

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = "ModifyPassword";

	public String NewPassWord;		// 设备新密码
	public String PassWord;			// 设备老密码
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public boolean onParse(String json) {
		// 只做设置,不获取,此处不解析
		return true;
	}

	@Override
	public String getSendMsg() {
		JSONObject pswJson = new JSONObject();
		
		// 修改密码,传输时需要加密处理
		try {
			String new_pwd = FunSDK.DevMD5Encrypt(NewPassWord);
			String old_pwd = FunSDK.DevMD5Encrypt(PassWord);
			pswJson.put("EncryptType", "MD5");
			pswJson.put("NewPassWord", new_pwd);
			pswJson.put("PassWord", old_pwd);
			pswJson.put("UserName", "admin");
			pswJson.put("SessionID", "0x6E472E78");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return pswJson.toString();
	}

}
