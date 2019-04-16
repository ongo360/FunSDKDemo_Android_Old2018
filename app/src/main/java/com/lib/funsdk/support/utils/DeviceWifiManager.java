package com.lib.funsdk.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.SystemClock;
import android.text.TextUtils;

import com.lib.funsdk.support.FunLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeviceWifiManager {

	public static final String TAG = DeviceWifiManager.class.getSimpleName();
	public static final int TYPE_NO_PASSWD = 1;
	public static final int TYPE_WEP = 2;
	public static final int TYPE_WPA = 3;
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiList = new ArrayList<ScanResult>();
	private List<WifiConfiguration> mWifiConfiguration;
	private WifiLock mWifiLock;
	private DhcpInfo dhcpInfo;
	private ConnectivityManager connManager;
	private static DeviceWifiManager mInstance;

	// private LogManager mLogManager = null;
	private DeviceWifiManager(Context context) {
		if (null != context) {
			mWifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			mWifiInfo = mWifiManager.getConnectionInfo();
		} else {
			mInstance = null;
		}
	}

	public static synchronized DeviceWifiManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DeviceWifiManager(context);
		}
		return mInstance;
	}

	public boolean isWiFiEnabled() {
		return mWifiManager != null && mWifiManager.isWifiEnabled();
	}

	// 打开WiFi
	public boolean openWifi() {
		return mWifiManager.setWifiEnabled(true);
	}

	// 关闭WiFi
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	public int checkState() {
		return mWifiManager.getWifiState();
	}

	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	public void connectConfiguration(int index) {
		if (index > mWifiConfiguration.size()) {
			return;
		}
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
	}

	/**
	 * 扫描WiFi
	 * @param type WiFi设备类型,参考WIFI_TYPE
	 * @param timeout 搜索时间,单位毫秒
	 */
	public void startScan(int type, int timeout) {
		boolean scan = mWifiManager != null && mWifiManager.startScan();
		if (!scan) {
			return;
		}
		
		SystemClock.sleep(timeout);
		updateWifiList(type);
	}
	
	/**
	 * 更新当前WIFI列表
	 * @param type
	 */
	public void updateWifiList(int type) {
		if ( null == mWifiManager ) {
			return;
		}
		
		List<ScanResult> wifiList = mWifiManager.getScanResults();
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
		if (mWifiList != null && wifiList != null) {
			mWifiList.clear();
			// //Log.i(TAG, "startScan result:" + wifiList.size());
			if (type == WIFI_TYPE.DEV_AP) {// AP,雄迈精品设备
				for (ScanResult result : wifiList) {
					//FunLog.d(TAG, "ssid1:" + result.SSID);
					if (isXMDeviceWifi(result.SSID)) {
						mWifiList.add(result);
						//FunLog.i(TAG, "ssid:" + result.SSID);
					}
				}
			} else if (type == WIFI_TYPE.ROUTER) {// ROUTER,路由器等其他设备
				for (ScanResult result : wifiList) {
					if (!isXMDeviceWifi(result.SSID) && !result.SSID.equals("")) {
						mWifiList.add(result);
					}
				}
			} else {
				mWifiList = wifiList;
			}
		}
		
	}

	public ScanResult getCurScanResult(String SSID) {
		mWifiManager.startScan();
		List<ScanResult> wifiList = mWifiManager.getScanResults();
		ScanResult scanResult = null;
		if (wifiList != null && SSID != null) {
			for (ScanResult result : wifiList) {
				if (result.SSID.contains(SSID)) {
					scanResult = result;
					return scanResult;
				}
			}
		}
		return scanResult;
	}

	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	public boolean isWifiConnect() {
		NetworkInfo ni = connManager.getActiveNetworkInfo();
		return (ni != null && ni.isConnectedOrConnecting());
	}

	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder
					.append("Index_" + Integer.toString(i + 1) + ":");
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	public String getMacAddress() {
		return (mWifiManager == null) ? "NULL" : mWifiManager
				.getConnectionInfo().getMacAddress();
	}

	public String getBSSID() {
		return (mWifiManager == null) ? "NULL" : mWifiManager
				.getConnectionInfo().getBSSID();
	}

	public String getSSID() {
		if (mWifiList == null)
			return null;
		String ssid = mWifiManager.getConnectionInfo().getSSID();
		//FunLog.e(TAG, "ssid:" + ssid);
		if (ssid != null && ssid.startsWith("\""))
			ssid = ssid.substring(1, ssid.length() - 1);
		return ssid;
	}

	public DhcpInfo getDhcpInfo() {
		return dhcpInfo = mWifiManager.getDhcpInfo();
	}

	public String getIPAddress() {
		int ipAddress = 0;
		String ip = null;
		if (mWifiManager != null) {
			ipAddress = mWifiManager.getConnectionInfo().getIpAddress();
			if (ipAddress == 0) {
				return null;
			} else {
				ip = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
						+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
				FunLog.e(TAG, "IP:" + ip);
				return ip;
			}
		} else {
			return null;
		}
	}

	public int getNetworkId() {
		return (mWifiManager == null) ? 0 : mWifiManager.getConnectionInfo()
				.getNetworkId();
	}

	public int getLinkSpeed() {
		return (mWifiManager == null) ? 0 : mWifiManager.getConnectionInfo()
				.getLinkSpeed();
	}

	public WifiInfo getWifiInfo() {
		mWifiInfo = mWifiManager == null ? null : mWifiManager
				.getConnectionInfo();
		return mWifiInfo;
	}

	public boolean addNetwork(WifiConfiguration wcg) {
		int wcgID = -1;
		if (null == wcg) {
			return false;
		}
		boolean benable = false;
		mWifiManager.disconnect();
		if (wcg.networkId >= 0) {
			wcgID = wcg.networkId;
		} else {
			wcgID = mWifiManager.addNetwork(wcg);
		}
		int newPri = 10000;// getMaxPriority() + 1;
		if (newPri >= MAX_PRIORITY) {
			// We have reached a rare situation.
			newPri = shiftPriorityAndSave();
		}
		wcg.priority = newPri;
		mWifiManager.updateNetwork(wcg);
		mWifiManager.saveConfiguration();
		benable = mWifiManager.enableNetwork(wcgID, true);
		mWifiManager.reconnect();
		System.out.println("wcgID--" + wcgID);
		System.out.println("benable--" + benable);
		return benable;
	}

	private static final int MAX_PRIORITY = 999999;

	/**
	 * Allow a previously configured network to be associated with.
	 */
	public boolean enableNetwork(String ssid) {
		boolean state = false;
		List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();

		if (list != null && list.size() > 0) {
			for (WifiConfiguration i : list) {
				if (i.SSID != null
						&& i.SSID.equals(convertToQuotedString(ssid))) {

					mWifiManager.disconnect();

					int newPri = getMaxPriority() + 1;
					if (newPri >= MAX_PRIORITY) {
						// We have reached a rare situation.
						newPri = shiftPriorityAndSave();
					}

					i.priority = newPri;
					mWifiManager.updateNetwork(i);
					mWifiManager.saveConfiguration();

					state = mWifiManager.enableNetwork(i.networkId, true);
					mWifiManager.reconnect();
					break;
				}
			}
		}

		return state;
	}

	private int getMaxPriority() {
		final List<WifiConfiguration> configurations = mWifiManager
				.getConfiguredNetworks();
		int pri = 0;
		for (final WifiConfiguration config : configurations) {
			if (config.priority > pri) {
				pri = config.priority;
			}
		}
		return pri;
	}

	private void sortByPriority(final List<WifiConfiguration> configurations) {
		Collections.sort(configurations, new Comparator<WifiConfiguration>() {
			@Override
			public int compare(WifiConfiguration object1,
					WifiConfiguration object2) {
				return object1.priority - object2.priority;
			}
		});
	}

	private int shiftPriorityAndSave() {
		final List<WifiConfiguration> configurations = mWifiManager
				.getConfiguredNetworks();
		sortByPriority(configurations);
		final int size = configurations.size();
		for (int i = 0; i < size; i++) {
			final WifiConfiguration config = configurations.get(i);
			config.priority = i;
			mWifiManager.updateNetwork(config);
		}
		mWifiManager.saveConfiguration();
		return size;
	}

	/**
	 * Add quotes to string if not already present.
	 * 
	 * @param string
	 * @return
	 */
	public static String convertToQuotedString(String string) {
		if (TextUtils.isEmpty(string)) {
			return "";
		}

		final int lastPos = string.length() - 1;
		if (lastPos > 0
				&& (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
			return string;
		}

		return "\"" + string + "\"";
	}

	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	public boolean disconnect() {
		boolean benable = mWifiManager.disconnect();
		System.out.println("disconnect--" + benable);
		return benable;
	}

	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
                                            int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			// mWifiManager.enableNetwork(tempConfig.networkId, false);
			return tempConfig;
		}
		if (Type == TYPE_NO_PASSWD) // WIFICIPHER_NOPASS
		{
			config.wepKeys[0] = "\"" + "\"";
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == TYPE_WEP) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";// \"ת���ַ���"
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == TYPE_WPA) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = false;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	public boolean onRemoveNetWork(String ssid) {
		WifiConfiguration tempConfig = this.IsExsits(ssid);
		if (tempConfig != null && isXMDeviceWifi(ssid)) {
			mWifiManager.removeNetwork(tempConfig.networkId);
			mWifiManager.saveConfiguration();
			return true;
		} else {
			return false;
		}
	}

	private WifiConfiguration IsExsits(String SSID) {
		String ssid = "\"" + SSID + "\"";
		FunLog.d(TAG, "ssid1:" + ssid);
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		if (existingConfigs == null)
			return null;
		for (WifiConfiguration existingConfig : existingConfigs) {
			FunLog.d(TAG, "ssid:" + existingConfig.SSID);
			if (existingConfig.SSID.equals(ssid)
					|| existingConfig.SSID.equals(SSID)) {
				FunLog.d(TAG, "ssid ok");
				return existingConfig;
			}
		}
		return null;
	}

	public boolean isSSIDExist(String ssid) {
		mWifiManager.startScan();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ScanResult result : mWifiManager.getScanResults()) {
			if (result.SSID.equals(ssid))
				return true;
		}
		return false;
	}

	public String getConfiguredNetwork() {
		return mWifiManager == null ? null : mWifiManager.getConnectionInfo()
				.getSSID();
	}
	
	public String getGatewayIp() {
		try {
			//String gatewayIp = intToIp(mWifiManager.getDhcpInfo().gateway);
			String gatewayIp = intToIp(mWifiManager.getDhcpInfo().serverAddress);
			return gatewayIp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String intToIp(int i)  {
		return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF );
	}
	
	/**
	 * 通过SSID判断WiFi是否是一个雄迈的设备
	 * 
	 * @param ssid
	 * @return
	 */
	public static boolean isXMDeviceWifi(String ssid) {
		if (StringUtils.isStringNULL(ssid))
			return false;
		return isStartsWith(ssid, "robot_") || isStartsWith(ssid, "Robot_")
				|| isStartsWith(ssid, "card_") || isStartsWith(ssid, "car_")
				|| isStartsWith(ssid, "seye_") || isStartsWith(ssid, "NVR_")
				|| isStartsWith(ssid, "DVR_") || isStartsWith(ssid, "beye_")
				|| isStartsWith(ssid, "IPC_") || isStartsWith(ssid, "IPC")
				|| isStartsWith(ssid, "Car") || isStartsWith(ssid, "BOB_")
				|| isStartsWith(ssid, "socket_") || isStartsWith(ssid, "xmjp_")
				|| isStartsWith(ssid, "feye_");
	}

	/**
	 * 通过SSID名称来判断设备类型
	 * @param ssid SSID
	 * @return 设备类型
	 */
	public static int getXMDeviceAPType(String ssid) {
		if (!isXMDeviceWifi(ssid))
			return -1;
		if (isStartsWith(ssid, "robot_") || isStartsWith(ssid, "Robot_")
				|| isStartsWith(ssid, "NVR_") || isStartsWith(ssid, "DVR_")
				|| isStartsWith(ssid, "IPC_") || isStartsWith(ssid, "IPC")
				|| isStartsWith(ssid, "camera_")) {
			return DEVICE_TYPE.MONITOR;
		}else if (isStartsWith(ssid, "socket_") || isStartsWith(ssid, "xmjp_socket_")) {
			return DEVICE_TYPE.SOCKET;
		}else if (isStartsWith(ssid, "xmjp_bulb_")) {
			return DEVICE_TYPE.BULB;
		} else if (isStartsWith(ssid, "xmjp_bulbsocket_")) {
			return DEVICE_TYPE.BULB_SOCKET;
		}else if (isStartsWith(ssid, "car_") || isStartsWith(ssid, "xmjp_car_")) {
			return DEVICE_TYPE.CAR;
		}else if (isStartsWith(ssid, "beye_") || isStartsWith(ssid, "xmjp_beye_")) {
			return DEVICE_TYPE.BEYE;
		}else if (isStartsWith(ssid, "seye_") || isStartsWith(ssid, "xmjp_seye_")) {
			return DEVICE_TYPE.SEYE;
		}else if (isStartsWith(ssid, "xmjp_robot_")) {
			return DEVICE_TYPE.ROBOT;
		}else if (isStartsWith(ssid, "xmjp_mov_") || isStartsWith(ssid, "xmjp_spt_")|| isStartsWith(ssid, "xmjp_dcam_")|| isStartsWith(ssid, "xmjp_maf_")) {
			return DEVICE_TYPE.MOV;
		}else if (isStartsWith(ssid, "feye_") || isStartsWith(ssid, "xmjp_feye_")) {
			return DEVICE_TYPE.FEYE;
		}else if (isStartsWith(ssid, "xmjp_fbulb_")) {
			return DEVICE_TYPE.FBULB;
		}else if (isStartsWith(ssid, "xmjp_BOB_")) {
			return DEVICE_TYPE.BOB;
		}else if (isStartsWith(ssid, "xmjp_musicbox_")) {
			return DEVICE_TYPE.MUSIC_BOX;
		}else if (isStartsWith(ssid, "xmjp_speaker")) {
			return DEVICE_TYPE.SPEAKER;
		}
		return DEVICE_TYPE.MONITOR;
	}

	public static boolean isStartsWith(String str1, String str2) {
		if (StringUtils.isStringNULL(str1))
			return false;
		return str1.startsWith(str2) | str1.startsWith(str2, 1);
	}

	// WIFI类型
	public interface WIFI_TYPE {
		int ALL = 0;
		int DEV_AP = 1;// 设备AP
		int ROUTER = 2;// 路由器
	}

	// 设备类型
	public interface DEVICE_TYPE {
		int MONITOR = 0;// 监控设备
		int SOCKET = 1;// 智能插座
		int BULB = 2;// 情景灯泡
		int BULB_SOCKET = 3;// 灯座
		int CAR = 4;// 汽车伴侣
		int BEYE = 5;// 大眼睛
		int SEYE = 6;// 小眼睛/小雨点
		int NSEYE = 601;// 直播小雨点
		int ROBOT = 7;// 雄迈摇头机
		int MOV = 8;// 运动摄像机
		int FEYE = 9;// 鱼眼小雨点
		int FBULB = 10;// 鱼眼灯泡/全景智能灯泡
		int BOB = 11;// 小黄人
		int MUSIC_BOX = 12;// wifi音乐盒
		int SPEAKER = 13;// wifi音响
	}

	/**
	 * 设备归类 0-雄迈精品设备 1-OEM设备
	 */
	public interface DEVICE_CLASSIFY {
		int CLASSIFY_XMJP = 0;
		int CLASSIFY_OEM = 1;
	}

	/**
	 * 感应器类型
	 *
	 * @author XMuser
	 */
	public interface SENSOR_TYPE {
		/**
		 * 433设备的移动侦测
		 */
		int DEV_433_DETECT = 0;
		/**
		 * 智联插座
		 */
		int SOCKET_TYPE = 0x64;
		/**
		 * 红外遥控

		 */
		int REMOTE_TYPE = 0x65;
		/**
		 * 墙壁开关
		 */
		int WALLSWITCH_TYPE = 0x66;
		/**
		 * //窗帘控制器
		 */
		int CURTAINS_TYPE = 0x67;
		/**
		 * //灯带控制器
		 */
		int LIGHT_TYPE = 0x68;
		/**
		 * 智联按钮
		 */
		int BUTTON_TYPE = 0x69;
		/**
		 * 门磁传感器
		 */
		int LINKCENTER_MAGNETOMETER = 0x6e;
		/**
		 * 人体红外传感器
		 */
		int LINKCENTER_BODY_INFRARED = 0x6f;
		/**
		 * 水浸传感器
		 */
		int LINKCENTER_WATER_IMMERSION = 0x70;
		/**
		 * 环境传感器
		 */
		int LINKCENTER_ENVIRONMENT = 0x71;
		/**
		 * 燃气传感器
		 */
		int LINKCENTER_FUEL_GAS = 0x72;
		/**
		 * 烟雾传感器
		 */
		int LINKCENTER_SMOKE = 0x73;
	}
}