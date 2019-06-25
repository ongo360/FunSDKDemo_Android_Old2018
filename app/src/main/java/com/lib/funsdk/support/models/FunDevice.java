package com.lib.funsdk.support.models;


import android.text.TextUtils;

import com.basic.G;
import com.lib.funsdk.support.config.BaseConfig;
import com.lib.funsdk.support.config.DeviceGetJson;
import com.lib.funsdk.support.config.SystemInfo;
import com.lib.funsdk.support.utils.Define;
import com.lib.sdk.struct.SDBDeviceInfo;
import com.lib.sdk.struct.SDK_CONFIG_NET_COMMON_V2;
import com.lib.sdk.struct.SDK_ChannelNameConfigAll;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunDevice {
	public String devSn;		// 设备序列号
	public String devMac;		// 设备MAC
	public String devName;		// 设备名称
	public String devIp;		// 设备IP
	public String loginName;	// 设备登录名
	public String loginPsw;		// 设备登录密码
	public FunDevType devType;	// 设备类型
	public int tcpPort;
	
	public SDK_ChannelNameConfigAll channel;
	public int CurrChannel = 0;		//当前播放通道号,默认为零
	
	public boolean isRemote;	// 是否是一个远程设备
	
	public FunDevStatus devStatus;	// 是否在线(状态)
	public int devStatusValue;		// 设备状态值,记录了不同协议P2P,DSS等的状态信息
	
	protected Map<String, BaseConfig> configMap = new HashMap<String, BaseConfig>();

    public List<FunFileData> mDatas;
    
    private int nNetConnnectType = -1; // 0: p2p 1:转发 2IP直连
    
    // 设备是否已经登录标志
    private boolean mHasLogin = false;
    // 是否已验证funDevice密码的正确性
    private boolean servicepsd = false;
    
    // 保存设备连接状态, 记录的是：EUIMSG.DEV_ON_RECONNECT和EUIMSG.DEV_ON_DISCONNECT
    private boolean mHasConnected = false;	

    //云存储状态
    private int cloudState;

    private long cloudExpired;//到期时间 单位秒
	public FunDevice() {
		
    }
	
	// 新的设备,来自用户
	public void initWith(SDBDeviceInfo devInfo) {
		this.devMac = G.ToString(devInfo.st_0_Devmac);
		this.devSn = this.devMac;
		this.devName = G.ToString(devInfo.st_1_Devname);
		if ( null != this.devName ) {
			this.devName = G.UnescapeHtml3(this.devName);
		}
		this.devIp = G.ToString(devInfo.st_2_Devip);
		this.loginName = G.ToString(devInfo.st_4_loginName);
		this.loginPsw = G.ToString(devInfo.st_5_loginPsw);
		this.tcpPort = devInfo.st_6_nDMZTcpPort;
		this.devType = FunDevType.getType(devInfo.st_7_nType);
		this.isRemote = true;
		this.devStatus = FunDevStatus.STATUS_UNKNOWN;
	}
	
	public void initWith(SDK_CONFIG_NET_COMMON_V2 comm) {
		this.devSn = G.ToString(comm.st_14_sSn);
		this.devMac = G.ToString(comm.st_13_sMac);
		this.devName = G.ToString(comm.st_00_HostName);
		if ( null != this.devName ) {
			this.devName = G.UnescapeHtml3(this.devName);
		}
		this.devIp = comm.st_01_HostIP.getIp();
		this.loginName = "admin";
		this.loginPsw = "";
		this.tcpPort = comm.st_05_TCPPort;
		this.devType = FunDevType.getType(comm.st_15_DeviceType);
		this.isRemote = true;
		this.devStatus = FunDevStatus.STATUS_UNKNOWN;
	}
	
	public SDBDeviceInfo toSDBDeviceInfo() {
		SDBDeviceInfo devInfo = new SDBDeviceInfo();
		
		G.SetValue(devInfo.st_0_Devmac, this.devMac);
		G.SetValue(devInfo.st_1_Devname, this.devName);
		G.SetValue(devInfo.st_2_Devip, this.devIp);
		G.SetValue(devInfo.st_4_loginName, this.loginName);
		G.SetValue(devInfo.st_5_loginPsw, this.loginPsw);
		devInfo.st_6_nDMZTcpPort = this.tcpPort;
		devInfo.st_7_nType = this.devType.getDevIndex();
		
		return devInfo;
	}
	
	// 新的设备,来自附近AP
	public void initWith(FunDevType devType, String ssid, String bssid) {
//		this.devMac = bssid;
//		if ( ssid.contains("_") ) {
//			this.devName = ssid.substring(ssid.lastIndexOf("_") + 1);
//		} else {
			this.devName = ssid;
//		}
		if ( FunDevType.EE_DEV_INTELLIGENTSOCKET == devType
				|| FunDevType.EE_DEV_SCENELAMP == devType
				|| FunDevType.EE_DEV_LAMPHOLDER == devType ) {
			this.devMac = "172.16.10.1:9001";
			this.devIp = "172.16.10.1";
			this.tcpPort = 9001;
		} else {
			this.devMac = "192.168.10.1:34567";
			this.devIp = "192.168.10.1";
			this.tcpPort = 34567;
		}
		
		this.devSn = this.devMac;
		this.loginName = "admin";
		this.loginPsw = "";
		this.devType = devType;
		this.isRemote = false;
		this.devStatus = FunDevStatus.STATUS_ONLINE;
	}
	
	// 从JSONObject解析
	public void initWith(JSONObject jsonObj) {
		try {
			this.devMac = jsonObj.getString("devMac");
			this.devSn = jsonObj.getString("devSn");
			this.devName = jsonObj.getString("devName");
			this.devIp = jsonObj.getString("devIp");
			this.loginName = jsonObj.getString("loginName");
			this.loginPsw = jsonObj.getString("loginPsw");
			this.devType = FunDevType.getType(jsonObj.getInt("devType"));
			this.tcpPort = jsonObj.getInt("tcpPort");
			this.isRemote = jsonObj.getBoolean("isRemote");
			this.devStatus = FunDevStatus.getStatus(jsonObj.getInt("devStatus"));
		} catch (Exception e) {
			
		}
	}

	public JSONObject toJson() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("devMac", devMac);
			jsonObj.put("devSn", devSn);
			jsonObj.put("devName", devName);
			jsonObj.put("devIp", devIp);
			jsonObj.put("loginName", loginName);
			jsonObj.put("loginPsw", loginPsw);
			jsonObj.put("devType", devType.getDevIndex());
			jsonObj.put("tcpPort", tcpPort);
			jsonObj.put("isRemote", isRemote);
			jsonObj.put("devStatus", devStatus.getSatusId());
			return jsonObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "FunDevice[type=" + this.devType.getDevIndex() 
				+ "][mac=" + this.devMac + "]"
				+ "][sn=" + this.devSn + "]"
				+ "][name=" + this.devName + "]"
				+ "][ip=" + this.devIp + "]"
				+ "][port=" + this.tcpPort + "]";
	}
	
	public String getDevMac() {
		return this.devMac;
	}
	
	public String getDevSn() {
		if ( null != this.devSn ) {
			return this.devSn;
		}
		
		SystemInfo systemInfo = (SystemInfo)getConfig(SystemInfo.CONFIG_NAME);
		if ( null != systemInfo ) {
			this.devSn = systemInfo.getSerialNo();
			if ( null != this.devSn ) {
				return this.devSn;
			}
		}
		
		// 没有sn,返回mac作为sn
		return this.devMac;
	}
	
	public String getDevIP() {
		return this.devIp;
	}
	
	public FunDevType getDevType()
	{
		return this.devType;
	}
	
	@Override
	public int hashCode() {
		// 以MAC和设备名称,作为唯一标识,同时适合在线远程设备和局域网设备
		if ( null != this.devSn ) {
			return (this.devSn + this.devName).hashCode();
		}
		
		return super.hashCode();
	}
	
	public int getId() {
		return this.hashCode();
	}
	
//	private int getDefaultPort() {
//		if ( FunDevType.EE_DEV_INTELLIGENTSOCKET == devType
//				|| FunDevType.EE_DEV_SCENELAMP == devType
//				|| FunDevType.EE_DEV_LAMPHOLDER == devType ) {
//			return 9001;
//		}
//		return 34567;
//	}
	
	
	public boolean setConfig(BaseConfig cfg) {
		if ( null == cfg || null == configMap ) {
			return false;
		}
		
		configMap.put(cfg.getConfigName(), cfg);
		return true;
	}
	
	public void setChannel(SDK_ChannelNameConfigAll channel) {
		this.channel = channel;
	}
	
	/**
	 * 获取参数配置
	 * @param configName
	 * @return
	 */
	public BaseConfig getConfig(String configName) {
		if ( null == configName) {
			return null;
		}
		
		return configMap.get(configName);
	}
	
	/**
	 * 获取参数配置(如果不存在就创建之)
	 * @param configName
	 * @return
	 */
	public BaseConfig checkConfig(String configName) {
		if ( null == configName) {
			return null;
		}
		
		BaseConfig config = configMap.get(configName);
		if ( null == config ) {
			config = DeviceGetJson.buildConfig(configName);
			setConfig(config);
		}
		
		return config;
	}
	
	/**
	 * 将制定的参数配置去除(使无效,重新搜索/更新时调用)
	 * @param configName
	 */
	public void invalidConfig(String configName) {
		if ( null != configName
				&& configMap.containsKey(configName) ) {
			configMap.remove(configName);
		}
	}
	
	public boolean hasGotConfig(String configName) {
		return (null != getConfig(configName));
	}
	
	/**
	 * 获取设备序列号
	 * @return
	 */
	public String getSerialNo() {

        if (TextUtils.isEmpty(devSn)) {
            SystemInfo sysInfo = (SystemInfo) getConfig(SystemInfo.CONFIG_NAME);
            if (null == sysInfo) {
                return null;
            }
            return sysInfo.getSerialNo();
        } else {
            return devSn;
        }

    }

    /**
	 * 获取设备名称
	 * @return
	 */
	public String getDevName() {
		if ( null == this.devName ) {
			return this.devMac;
		}
		
		return this.devName;
	}
	
	/**
	 * 是否支持云台控制
	 * @return
	 */
	public boolean isSupportPTZ() {
		return Define.IsSupportPTZ(devType.getDevIndex());
	}
	
	/**
	 * 是否支持监控预览
	 * @return
	 */
	public boolean isSupportMonitor() {
		return Define.IsSupportMonitor(devType.getDevIndex());
	}
	
	/**
	 * 是否支持推送功能
	 * @return
	 */
	public boolean isSupportPushMsg() {
		return Define.IsSupportPushMsg(devType.getDevIndex());
	}
	
	/**
	 * 是否支持对讲功能
	 * @return
	 */
	public boolean isSupportTalk() {
		return Define.IsSupportTalk(devType.getDevIndex());
	}
	
	/**
	 * 是否支持SD卡
	 * @return
	 */
	public boolean isSupportSDCard() {
		SystemInfo sysInfo = (SystemInfo)getConfig(SystemInfo.CONFIG_NAME);
		if ( null == sysInfo ) {
			return false;
		}
		
		return Define.IsSupportSDCard(devType.getDevIndex(),
				sysInfo.getDevExpandType());
	}
	
	public void setStatusValue(int value) {
		devStatusValue = value;
	}
	
	/**
	 * 查询指定的状态类型是否在线
	 * @param statusType
	 * @return
	 */
	public boolean getStatus(FunDevStatusType statusType) {
		return ((devStatusValue >> statusType.getStatusId()) & 0x01) > 0;
	}
	
	/**
	 * 获取详细状态信息
	 * @return
	 */
	public String getStatusMore() {
		String str = "";
		for ( FunDevStatusType type : FunDevStatusType.values() ) {
			if ( getStatus(type) ) {
				str += "[" + type.getStatusName() + "] ";
			}
		}
		
		return str;
	}
	
	public void setNetConnectType(int type) {
		nNetConnnectType = type;
	}
	
	public int getNetConnectType() {
		return nNetConnnectType;
	}
	
	public boolean hasLogin() {
		return mHasLogin;
	}
	
	public void setHasLogin(boolean hasLogin) {
		mHasLogin = true;
	}
	
	public boolean servicepsd() {
		return servicepsd;
	}
	
	public void setServicepsd(boolean Servicepsd) {
		servicepsd = Servicepsd;
	}
	
	public boolean hasConnected() {
		return mHasConnected;
	}
	
	public void setConnected(boolean connected) {
		mHasConnected = connected;
	}

	public int getCloudState() {
		return cloudState;
	}

	public void setCloudState(int cloudState) {
		this.cloudState = cloudState;
	}

	public long getCloudExpired() {
		return cloudExpired;
	}

	public void setCloudExpired(long cloudExpired) {
		this.cloudExpired = cloudExpired;
	}
}
