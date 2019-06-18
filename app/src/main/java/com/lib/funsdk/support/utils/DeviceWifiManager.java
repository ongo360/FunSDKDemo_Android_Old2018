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
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.basic.G;
import com.lib.sdk.bean.StringUtils;
import com.lib.sdk.struct.SDBDeviceInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.lib.SDKCONST.DEVICE_TYPE.BEYE;
import static com.lib.SDKCONST.DEVICE_TYPE.BOB;
import static com.lib.SDKCONST.DEVICE_TYPE.BULB;
import static com.lib.SDKCONST.DEVICE_TYPE.BULB_SOCKET;
import static com.lib.SDKCONST.DEVICE_TYPE.CAR;
import static com.lib.SDKCONST.DEVICE_TYPE.DASH_CAMERA;
import static com.lib.SDKCONST.DEVICE_TYPE.DRIVE_BEYE;
import static com.lib.SDKCONST.DEVICE_TYPE.FBULB;
import static com.lib.SDKCONST.DEVICE_TYPE.FEYE;
import static com.lib.SDKCONST.DEVICE_TYPE.IDR;
import static com.lib.SDKCONST.DEVICE_TYPE.MONITOR;
import static com.lib.SDKCONST.DEVICE_TYPE.MOV;
import static com.lib.SDKCONST.DEVICE_TYPE.MUSIC_BOX;
import static com.lib.SDKCONST.DEVICE_TYPE.ROBOT;
import static com.lib.SDKCONST.DEVICE_TYPE.SEYE;
import static com.lib.SDKCONST.DEVICE_TYPE.SOCKET;
import static com.lib.SDKCONST.DEVICE_TYPE.SPEAKER;

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


    private int wifiNumber = 0;

    private DeviceWifiManager(Context context) {
        if (null != context) {
            mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        return mWifiManager == null ? false : mWifiManager.isWifiEnabled();
    }

    public boolean openWifi() {
        return mWifiManager.setWifiEnabled(true);
    }

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
        enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }


    public void startScan(int type, int timeout) {
        boolean scan = mWifiManager == null ? false : mWifiManager.startScan();
        if (!scan)
            return;
        SystemClock.sleep(timeout);
        wifiNumber = 0;
        List<ScanResult> wifiList = mWifiManager.getScanResults();
        if (wifiList != null) {
            wifiNumber = wifiList.size();
        }
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
        if (mWifiList != null && wifiList != null) {
            mWifiList.clear();
            if (type == WIFI_TYPE.DEV_AP) {
                for (ScanResult result : wifiList) {

                    if (isXMDeviceWifi(result.SSID)) {
                        mWifiList.add(result);

                    }
                }
            } else if (type == WIFI_TYPE.ROUTER) {
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

    public ScanResult getCurScanResult(String ssid) {
        wifiNumber = 0;
        mWifiManager.startScan();
        List<ScanResult> wifiList = mWifiManager.getScanResults();
        if (wifiList != null) {
            wifiNumber = wifiList.size();
        }
        ScanResult scanResult = null;
        if (wifiList != null && ssid != null) {
            for (ScanResult result : wifiList) {
                String tmpSSID = result.SSID.trim();
                // 如果出现前后引号的情况,先去掉引号
                if (tmpSSID.startsWith("\"") && tmpSSID.endsWith("\"")) {
                    tmpSSID = tmpSSID.substring(1, tmpSSID.length() - 1);
                }
                if (StringUtils.contrast(tmpSSID,ssid)) {
                    if (result.frequency > 4900 && result.frequency < 5900) {
                        //在双频合一的情况下，同名WiFi可能是5GHZ，记录下遍历整个列表，如果没有2.4GHZ，则返回5G进行提示
                        scanResult = result;
                        continue;
                    } else {
                        return result;
                    }
                }
            }
        }
        return scanResult;
    }

    public void getCurScanResult(final String ssid, final OnCurScanResultListener listener) {
        wifiNumber = 0;
        mWifiManager.startScan();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ScanResult> wifiList = mWifiManager.getScanResults();
                if (wifiList != null) {
                    wifiNumber = wifiList.size();
                }
                ScanResult scanResult = null;
                if (wifiList != null && ssid != null) {
                    for (ScanResult result : wifiList) {
                        String tmpSSID = result.SSID.trim();
                        // 如果出现前后引号的情况,先去掉引号
                        if (tmpSSID.startsWith("\"") && tmpSSID.endsWith("\"")) {
                            tmpSSID = tmpSSID.substring(1, tmpSSID.length() - 1);
                        }
                        System.out.println("scanResult:" + tmpSSID + result.frequency);
                        if (StringUtils.contrast(tmpSSID,ssid)) {
                            if (result.frequency > 4900 && result.frequency < 5900) {
                                //在双频合一的情况下，同名WiFi可能是5GHZ，记录下遍历整个列表，如果没有2.4GHZ，则返回5G进行提示
                                scanResult = result;
                                continue;
                            } else {
                                scanResult = result;
                                break;
                            }
                        }
                    }
                }
                if (listener != null) {
                    listener.onResult(scanResult);
                }
            }
        },2000);
    }

    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    public boolean isWifiConnect() {
        NetworkInfo ni = connManager.getActiveNetworkInfo();
        return (ni != null && ni.isConnectedOrConnecting());
    }

    // �鿴ɨ����
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
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
        mWifiInfo = mWifiManager == null ? null : mWifiManager.getConnectionInfo();
        return mWifiInfo;
    }

    public boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = -1;
        if (null == wcg) {
            return false;
        }
        boolean benable = false;
//		mWifiManager.disconnect();
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
        benable = enableNetwork(wcgID, true);
//		mWifiManager.reconnect();
        System.out.println("wcgID--" + wcgID);
        System.out.println("benable--" + benable);
        return benable;
    }

    private static final int MAX_PRIORITY = 999999;

    /**
     * 通过反射出不同版本的connect方法来连接Wifi
     *
     * @param netId
     * @return
     * @author jiangping.li
     * @since MT 1.0
     */
    private boolean connectWifiByReflectMethod(int netId) {
        if (netId < 0) {
            return false;
        }
        Method connectMethod = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.i(TAG, "connectWifiByReflectMethod road 1");
            // 反射方法： connect(int, listener) , 4.2 <= phone's android version
            for (Method methodSub : mWifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connect".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(mWifiManager, netId, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return false;
                }
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            // 反射方法: connect(Channel c, int networkId, ActionListener listener)
            // 暂时不处理4.1的情况 , 4.1 == phone's android version
            Log.i(TAG, "connectWifiByReflectMethod road 2");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.i(TAG, "connectWifiByReflectMethod road 3");
            // 反射方法：connectNetwork(int networkId) ,
            // 4.0 <= phone's android version < 4.1
            for (Method methodSub : mWifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connectNetwork".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(mWifiManager, netId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return false;
                }
            }
        } else {
            // < android 4.0
            return false;
        }
        return true;
    }

    public boolean enableNetwork(int netId, boolean disableOthers) {
        return connectWifiByReflectMethod(netId) ? true : mWifiManager.enableNetwork(netId, disableOthers);
    }

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

                    state = enableNetwork(i.networkId, true);
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

    public WifiConfiguration createWifiInfo(String SSID, String Password) {
        return createWifiInfo(SSID, Password, getCipherType(SSID));
    }

    public WifiConfiguration createWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            // enableNetwork(tempConfig.networkId, false);
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
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    /**
     * �Ƴ����ӹ��XM�豸wifi�ȵ�
     *
     * @param ssid wifi�ȵ�
     * @return
     */
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
        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        if (existingConfigs == null)
            return null;
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (ssid.equals(existingConfig.SSID) || SSID.equals(existingConfig.SSID)) {
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

    // ��ȡ�Ѿ������ϵ�wifi��Ϣ
    public String getConfiguredNetwork() {
        return mWifiManager == null ? null : mWifiManager.getConnectionInfo()
                .getSSID();
    }

    /**
     * �ж��ǲ���xm�豸��wifi�ȵ�
     *
     * @param ssid
     * @return
     */
    public static boolean isXMDeviceWifi(String ssid) {
        if (StringUtils.isStringNULL(ssid))
            return false;
        if (isStartsWith(ssid, "robot_") || isStartsWith(ssid, "Robot_") || isStartsWith(ssid, "card_")
                || isStartsWith(ssid, "car_") || isStartsWith(ssid, "seye_") || isStartsWith(ssid, "NVR_")
                || isStartsWith(ssid, "DVR_") || isStartsWith(ssid, "beye_") || isStartsWith(ssid, "IPC_")
                || isStartsWith(ssid, "IPC") || isStartsWith(ssid, "Car") || isStartsWith(ssid, "BOB_")
                || isStartsWith(ssid, "socket_") || isStartsWith(ssid, "xmjp_") || isStartsWith(ssid, "feye_")
                || isStartsWith(ssid, "bullet_") || isStartsWith(ssid, "drum_") || isStartsWith(ssid, "camera_")
                || isStartsWith(ssid, "Camera_") || isStartsWith(ssid, "ipc") || isStartsWith(ssid,"dev_cz_idr_"))
            return true;
        else
            return false;
    }


    /**
     * ��ȡ�豸����
     *
     * @param
     * @return �����豸����ֵ
     */
    public static int getXMDeviceAPType(String ssid) {
        if (!isXMDeviceWifi(ssid))
            return -1;
        if (isStartsWith(ssid, "xmjp_idr") || isStartsWith(ssid, "idr")
                || isStartsWith(ssid,"dev_cz_idr_")) {
            return IDR;
        }
        if (isStartsWith(ssid, "robot_") || isStartsWith(ssid, "Robot_")
                || isStartsWith(ssid, "NVR_") || isStartsWith(ssid, "DVR_")
                || isStartsWith(ssid, "IPC_") || isStartsWith(ssid, "IPC")) {
            return MONITOR;
        }
        if (isStartsWith(ssid, "socket_") || isStartsWith(ssid, "xmjp_socket_")) {
            return SOCKET;
        }
        if (isStartsWith(ssid, "xmjp_bulb_")) {
            return BULB;
        }
        if (isStartsWith(ssid, "xmjp_bulbsocket_")) {
            return BULB_SOCKET;
        }
        if (isStartsWith(ssid, "car_") || isStartsWith(ssid, "xmjp_car_")) {
            return CAR;
        }
        if (isStartsWith(ssid, "beye_") || isStartsWith(ssid, "xmjp_beye_")) {
            return BEYE;
        }
        if (isStartsWith(ssid, "seye_") || isStartsWith(ssid, "xmjp_seye_")) {
            return SEYE;
        }
        if (isStartsWith(ssid, "xmjp_robot_")) {
            return ROBOT;
        }
        if (isStartsWith(ssid, "xmjp_mov_") || isStartsWith(ssid, "xmjp_spt_") || isStartsWith(ssid, "GripCam_")) {
            return MOV;
        }
        if (isStartsWith(ssid, "feye_") || isStartsWith(ssid, "xmjp_feye_")) {
            return FEYE;
        }
        if (isStartsWith(ssid, "xmjp_fbulb_")) {
            return FBULB;
        }
        if (isStartsWith(ssid, "xmjp_BOB_")) {
            return BOB;
        }
        if (isStartsWith(ssid, "xmjp_musicbox_")) {
            return MUSIC_BOX;
        }
        if (isStartsWith(ssid, "xmjp_speaker")) {
            return SPEAKER;
        }
        if (isStartsWith(ssid, "xmjp_dcam_")) {
            return DASH_CAMERA;
        }
        if (isStartsWith(ssid, "xmjp_m2g_") || isStartsWith(ssid, "xmjp_maf_")) {
            return DRIVE_BEYE;
        }
        return MONITOR;
    }

    public static boolean isStartsWith(String str1, String str2) {
        if (StringUtils.isStringNULL(str1))
            return false;
        return str1.startsWith(str2) | str1.startsWith(str2, 1);
    }

    // WIFI类型
    public interface WIFI_TYPE {
        public static final int ALL = 0;
        public static final int DEV_AP = 1;// 设备AP
        public static final int ROUTER = 2;// 路由器
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

    /**
     * 设备归类 0-雄迈精品设备 1-OEM设备
     */
    public interface DEVICE_CLASSIFY {
        public static final int CLASSIFY_XMJP = 0;
        public static final int CLASSIFY_OEM = 1;
    }

    public void setAPDeviceInfo(SDBDeviceInfo info, String ssid) {
        if (null == info)
            return;
        int devType = DeviceWifiManager.getXMDeviceAPType(ssid);
        switch (devType) {
            case SOCKET:
            case BULB:
            case BULB_SOCKET:
                G.SetValue(info.st_0_Devmac, "172.16.10.1:9001");
                G.SetValue(info.st_2_Devip, "172.16.10.1");
                break;
            default:
                G.SetValue(info.st_0_Devmac, "192.168.10.1:34567");
                G.SetValue(info.st_2_Devip, "192.168.10.1");
                break;
        }
        G.SetValue(info.st_1_Devname, ssid);
        G.SetValue(info.st_4_loginName, "admin");
        G.SetValue(info.st_5_loginPsw, "");
        info.st_7_nType = devType;
    }

    public int getCipherType(String ssid) {
        for (ScanResult scResult : mWifiList) {
            String _ssid = scResult.SSID;
            if (!TextUtils.isEmpty(_ssid)) {
                if (_ssid.replace("\"", "").equals(ssid)) {
                    String capabilities = scResult.capabilities;
                    if (!TextUtils.isEmpty(capabilities)) {
                        if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                            return TYPE_WPA;
                        } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                            return TYPE_WPA;
                        } else {
                            return TYPE_NO_PASSWD;
                        }
                    }
                }
            }
        }
        return 0;
    }

    public String getGatewayIp() {
        try {
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

    public int getWifiNumber() {
        return wifiNumber;
    }

    public void setWifiNumber(int wifiNumber) {
        this.wifiNumber = wifiNumber;
    }

    public interface OnCurScanResultListener {
        void onResult(ScanResult scanResult);
    }

}