package com.lib.funsdk.support.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.funsdk.support.FunLog;

public class NetWorkAlarmServer extends BaseConfig{

	/**
	 *  以下CONFIG_NAME的定义必须要有,是为了保持所有的配置可以统一解析 
	 */
	public static final String CONFIG_NAME = "NetWork.AlarmServer";
	
	public Bean[] beans = null;
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	@Override
	public boolean onParse(String json) {
		if ( !super.onParse(json) ) {
			return false;
		}
		
		try {
			JSONArray c_jsonobj = mJsonObj.getJSONArray(getConfigName()); 
			beans = new Bean[c_jsonobj.length()];
			for(int i = 0; i < beans.length; i++){
				beans[i] = new Bean();
			}
			for (int i = 0; i < c_jsonobj.length(); i++) {  
			    JSONObject temp = (JSONObject) c_jsonobj.get(i);  			
			    onParse(temp, beans[i]);
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean onParse(JSONObject obj, Bean bean) throws JSONException {
		if (null == obj) {
			return false;
		}
		bean.Log = obj.optBoolean("Log");
		bean.Enable = obj.optBoolean("Enable");
		JSONObject serverObj= obj.getJSONObject("Server");
		bean.server.Name=serverObj.optString("Name");
		bean.server.UserName=serverObj.optString("UserName");
		bean.server.Port=serverObj.optInt("Port");
		bean.server.Anonymity=serverObj.optBoolean("Anonymity");
		bean.server.Password=serverObj.optString("Password");
		bean.server.Address=serverObj.optString("Address");
		bean.Protocol=obj.optString("Protocol");
		bean.Alarm = obj.optBoolean("Alarm");
		return true;
	}

	/* (non-Javadoc)
	 * @see com.lib.funsdk.support.config.BaseConfig#getSendMsg()
	 */
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigName());
			mJsonObj.put("SessionID", "0x00001234");
			JSONArray netArray= new JSONArray();
			for(int i=0;i<beans.length;i++){
				Bean bean = beans[i];				
				JSONObject c_json=new JSONObject();
				JSONObject serjson=new JSONObject();
				c_json.put("Alarm", bean.Alarm);
				c_json.put("Enable", bean.Enable);
				c_json.put("Log", bean.Log);
				c_json.put("Protocol", bean.Protocol);
				c_json.put("Server", serjson);
				serjson.put("Address", bean.server.Address);
				serjson.put("Anonymity", bean.server.Anonymity);
				serjson.put("Name", bean.server.Name);
				serjson.put("Password", bean.server.Password);
				serjson.put("Port", bean.server.Port);
				serjson.put("UserName", bean.server.UserName);
				netArray.put(c_json);
			}
			
			
			mJsonObj.put(getConfigName(), netArray);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		FunLog.d(getConfigName(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
	
	public class Bean{
		public boolean Alarm = false;
		public boolean Enable = false;
		public boolean Log = false;
		public String Protocol = new String();	
		public Server server = new Server();
	
	}
	
	public class Server{
		public String Name = new String();
		public String UserName = new String();
		public String Password = new String();
		public int Port = -1;
		public String Address = new String();
		public boolean Anonymity = false;			
	}
	
	public void setAlarm(boolean mAlarm){
		beans[0].Alarm=mAlarm;
	}
	
	public boolean getAlarm(){
		return beans[0].Alarm;
	}

	public void setEnable(boolean mEnable){
		beans[0].Enable=mEnable;
	}
	
	public boolean getEnable(){
		return beans[0].Enable;
	}
	
	public void setLog(boolean mLog){
		beans[0].Log=mLog;
	}
	
	public boolean getLog(){
		return beans[0].Log;
	}
	
	public void setProtocol(String mProtocol){
		beans[0].Protocol=mProtocol;
	}
	
	public String getProtocol(){
		return beans[0].Protocol;
	}
	
	public void setPort(int mPort){
		beans[0].server.Port=mPort;
	}
	
	public int getPort(){
		return beans[0].server.Port;
	}
	
	public void setName(String mName){
		beans[0].server.Name=mName;
	}
	
	public String getName(){
		return beans[0].server.Name;
	}
}
