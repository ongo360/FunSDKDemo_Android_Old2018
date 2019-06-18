/**
 * XMFamily
 * APAutomaticSwitch.java
 * Administrator
 * TODO
 * 2014-12-4
 */
package com.lib.funsdk.support.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.lib.SDKCONST.NetWorkType;
import com.lib.sdk.bean.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * XMFamily APAutomaticSwitch.java
 *
 */
public class APAutomaticSwitch {
    private static String MYLOG = APAutomaticSwitch.class.getClass()
            .getSimpleName();
    private HashMap<String, ScanResult> mEnableWifi = null;
    private Context mContext;
    private DeviceWifiManager mWifiManager;
    private IntentFilter mWifiFilter = null;
    private String mTryConnectSSID;
    private static final String TAG = "zyy----------";

    public APAutomaticSwitch(Context context, DeviceWifiManager wifiManager) {
        initData(context, wifiManager);
    }

    private void initData(Context context, DeviceWifiManager wifiManager) {
        this.mContext = context;
        this.mWifiManager = wifiManager;
        mEnableWifi = new HashMap<String, ScanResult>();
        synchronized (mWifiConnectReceiver) {
            if (mWifiFilter == null) {
                mWifiFilter = new IntentFilter();
                mWifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                mWifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                mWifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                mContext.registerReceiver(mWifiConnectReceiver, mWifiFilter);
            } else {
                onRelease();
            }
        }
        BaseThreadPool.getInstance().doTaskBySinglePool(new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mWifiManager.startScan(0, 1000);
                List<ScanResult> lists = mWifiManager.getWifiList();
                for (ScanResult result : lists) {
                    mEnableWifi.put(result.SSID, result);
                }
                super.run();
            }
        }, 4);
    }

    private BroadcastReceiver mWifiConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {// 监听是否连接上wifi
                if (null != mWifiStateLs && null != mWifiManager) {
                    WifiInfo wifiInfo = mWifiManager.getWifiInfo();
                    if (null == wifiInfo)
                        return;
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    // 为了获取IP 延时让APP与设备交互
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mWifiStateLs.onNetWorkState(info.getDetailedState(), info.getType(), wifiInfo.getSSID());
                    Log.d(TAG, "state  :" + info.toString());
                    Log.d(TAG, "state  :" + wifiInfo.getSSID());
                }

            } else if (intent.getAction() == WifiManager.NETWORK_STATE_CHANGED_ACTION) {// 网络状态变化
                if (null != mWifiStateLs && null != mWifiManager) {
                    WifiInfo wifiInfo = mWifiManager.getWifiInfo();
                    if (null == wifiInfo)
                        return;
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    mWifiStateLs.onNetWorkChange(info.getDetailedState(), info.getType(), wifiInfo.getSSID());
                }
            } else if (intent.getAction() == WifiManager.WIFI_STATE_CHANGED_ACTION) {// WIFI
                int wifiState = intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, 0);

                if (mWifiStateLs != null)
                    mWifiStateLs.onIsWiFiAvailable(wifiState == WifiManager.WIFI_STATE_ENABLED);
            }

        }

    };
    private WifiStateListener mWifiStateLs;

    public void setWifiStateListener(WifiStateListener ls) {
        this.mWifiStateLs = ls;
    }

    public int getXMDeviceWifiCount() {
        mWifiManager.startScan(1, 1000);
        List<ScanResult> wifiList = mWifiManager.getWifiList();
        int count = 0;
        for (int i = 0; i < wifiList.size(); i++) {
            if (DeviceWifiManager.isXMDeviceWifi(wifiList.get(i).SSID)) {
                count++;
            }
        }
        return count;
    }


    public int RouterToDevAP(boolean bScan) {
        int network_state = CheckNetWork.NetWorkUseful(mContext);
        if (network_state == NetWorkType.No_NetWork) {
            if (!mWifiManager.openWifi())
                return -4;
        }
        String router_ssid = mWifiManager.getSSID();

        if (!DeviceWifiManager.isXMDeviceWifi(router_ssid)) {
            if (!StringUtils.isStringNULL(router_ssid)
                    && !router_ssid.endsWith("0x")) {
                SPUtil.getInstance(mContext).setSettingParam(
                        Define.ROUTER_WIFI_SSID, router_ssid);
            }
            if (bScan) {
                mWifiManager.startScan(1, 1000);
            }
            final List<ScanResult> wifiList = mWifiManager.getWifiList();
            if (wifiList != null && wifiList.size() > 0) {
                Collections.sort(wifiList, new Comparator<ScanResult>() {

                    @Override
                    public int compare(ScanResult arg0, ScanResult arg1) {
                        // TODO Auto-generated method stub
                        return (arg0.level + "").compareTo(arg1.level + "");
                    }
                });

                mTryConnectSSID = wifiList.get(0).SSID;
                WifiConfiguration wcg = null;
                wcg = mWifiManager.createWifiInfo(mTryConnectSSID,
                        "1234567890", 3);
                if (null != wcg && mWifiManager.addNetwork(wcg)) {
                    return 1;
                } else
                    return 0;
            } else
                return -3;
        } else
            return -2;
    }


    public int RouterToDevAP(String ssid, boolean bScan) {
        mTryConnectSSID = ssid;
        int network_state = CheckNetWork.NetWorkUseful(mContext);
        if (network_state == NetWorkType.No_NetWork) {
            if (!mWifiManager.openWifi())
                return -4;
        }
        String router_ssid = mWifiManager.getSSID();

        if (!StringUtils.contrast(ssid, router_ssid) && DeviceWifiManager.isXMDeviceWifi(ssid)) {
            if (!StringUtils.isStringNULL(router_ssid) && !router_ssid.endsWith("0x")
                    && !DeviceWifiManager.isXMDeviceWifi(router_ssid)) {
                SPUtil.getInstance(mContext).setSettingParam(Define.ROUTER_WIFI_SSID, router_ssid);
            }
            if (mTryConnectSSID == null) {
                if (bScan) {
                    mWifiManager.startScan(1, 1000);
                }
                final List<ScanResult> wifiList = mWifiManager.getWifiList();
                if (wifiList != null && wifiList.size() > 0) {
                    Collections.sort(wifiList, new Comparator<ScanResult>() {

                        @Override
                        public int compare(ScanResult arg0, ScanResult arg1) {
                            // TODO Auto-generated method stub
                            return (arg0.level + "").compareTo(arg1.level + "");
                        }
                    });
                    mTryConnectSSID = wifiList.get(0).SSID;
                } else
                    return -3;
            }
            WifiConfiguration wcg = null;
            Log.d(TAG, "有密码WiFi");
            wcg = mWifiManager.createWifiInfo(mTryConnectSSID, "1234567890", 3);
            if (mWifiManager.addNetwork(wcg)) {
                return 1;
            } else {
                return 0;
            }
        } else
            return -2;
    }

    public int onReconnectAP() {
        String curSSID = mWifiManager.getSSID();
        if (DeviceWifiManager.isXMDeviceWifi(curSSID)) {
            mTryConnectSSID = mWifiManager.getSSID();
            mWifiManager.onRemoveNetWork(mTryConnectSSID);
        } else {
            mTryConnectSSID = SPUtil.getInstance(mContext).getSettingParam(
                    Define.XMJP_WIFI_SSID, null);
            if (!StringUtils.isStringNULL(curSSID) && !curSSID.endsWith("0x")) {
                SPUtil.getInstance(mContext).setSettingParam(
                        Define.ROUTER_WIFI_SSID, curSSID);
            }
        }
        if (null != mTryConnectSSID) {
            if (mWifiManager.enableNetwork(mTryConnectSSID)) {
                return 1;
            } else {
                return RouterToDevAP(mTryConnectSSID, false);
            }
        } else {
            return RouterToDevAP(false);
        }
    }


    public boolean DevAPToRouter() {
        int network_state = CheckNetWork.NetWorkUseful(mContext);
        boolean bret = false;
        if (network_state == NetWorkType.No_NetWork) {
            if (!mWifiManager.openWifi())
                return false;
        }
        String xmjp_ssid = SPUtil.getInstance(mContext).getSettingParam(
                Define.XMJP_WIFI_SSID, null);
        String router_ssid = SPUtil.getInstance(mContext).getSettingParam(
                Define.ROUTER_WIFI_SSID, null);

        String cur_ssid = mWifiManager.getSSID();
        if (DeviceWifiManager.isXMDeviceWifi(cur_ssid)) {
            List<WifiConfiguration> allList = mWifiManager.getConfiguration();
            List<WifiConfiguration> xmList = new ArrayList<WifiConfiguration>();
            if (allList == null) {
                mWifiManager.startScan(2, 1000);
                allList = mWifiManager.getConfiguration();
                if (allList == null)
                    return false;
            }
            HashMap<String, WifiConfiguration> mWifiEnable = new HashMap<String, WifiConfiguration>();
            for (WifiConfiguration config : allList) {
                if (DeviceWifiManager.isXMDeviceWifi(config.SSID)
                        || onCheckEnable(config.SSID)) {
                    continue;
                }
                xmList.add(config);
                mWifiEnable.put(config.SSID, config);
            }
            if (!StringUtils.isStringNULL(router_ssid)
                    && !router_ssid.equals("0x")) {
                if (mWifiEnable.containsKey(router_ssid)) {
                    bret = mWifiManager
                            .addNetwork(mWifiEnable.get(router_ssid));
                } else if (mWifiEnable.containsKey("\"" + router_ssid + "\"")) {
                    bret = mWifiManager.addNetwork(mWifiEnable.get("\""
                            + router_ssid + "\""));
                }
            } else if (!xmList.isEmpty()) {
                bret = mWifiManager.addNetwork(xmList.get(0));
            }
            mWifiManager.onRemoveNetWork(cur_ssid);
            return bret;
        } else {
            if (null != xmjp_ssid) {
                mWifiManager.onRemoveNetWork(xmjp_ssid);
            }
            return false;
        }
    }


    public boolean DevAPToRouter(String ssid, String password, int type) {
        if (ssid == null)
            return false;
        int network_state = CheckNetWork.NetWorkUseful(mContext);
        if (network_state == NetWorkType.No_NetWork) {
            if (!mWifiManager.openWifi())
                return false;
        }
        String cur_ssid = mWifiManager.getSSID();
        if (!cur_ssid.equals(ssid)) {
            WifiConfiguration wcg = mWifiManager.createWifiInfo(ssid, password,
                    type);
            if (mWifiManager.addNetwork(wcg)) {
                mWifiManager.onRemoveNetWork(cur_ssid);
                return true;
            } else {
                mWifiManager.onRemoveNetWork(cur_ssid);
                return false;
            }
        } else
            return false;
    }


    public boolean ToRecordRouter(boolean bRemoveAP) {
        int network_state = CheckNetWork.NetWorkUseful(mContext);
        boolean bret = false;
        if (network_state == NetWorkType.No_NetWork) {
            if (!mWifiManager.openWifi())
                return false;
        }
        if (!DeviceWifiManager.isXMDeviceWifi(mWifiManager.getSSID())) {
            return true;
        }
        String router_ssid = SPUtil.getInstance(mContext).getSettingParam(
                Define.ROUTER_WIFI_SSID, "");

        if (StringUtils.contrast(router_ssid, mWifiManager.getSSID())) {
            return true;
        }
        if (bRemoveAP) {
            mWifiManager.onRemoveNetWork(mWifiManager.getSSID());// 删除之前连接过的设备热点
        }
        List<WifiConfiguration> allList = mWifiManager.getConfiguration();
        List<WifiConfiguration> xmList = new ArrayList<WifiConfiguration>();
        if (null == allList) {
            mWifiManager.startScan(2, 1000);
            allList = mWifiManager.getConfiguration();
        }
        if (null == allList) {
            return false;
        }
        HashMap<String, WifiConfiguration> wifiEnable = new HashMap<String, WifiConfiguration>();
        for (WifiConfiguration config : allList) {
            if (DeviceWifiManager.isXMDeviceWifi(config.SSID)
                    || onCheckEnable(config.SSID)) {
                continue;
            }
            xmList.add(config);
            wifiEnable.put(config.SSID, config);
        }
        if (!DeviceWifiManager.isXMDeviceWifi(router_ssid)
                && !StringUtils.isStringNULL(router_ssid)
                && !router_ssid.equals("0x")) {
            if (wifiEnable.containsKey(router_ssid)) {
                bret = mWifiManager.addNetwork(wifiEnable.get(router_ssid));
            } else if (wifiEnable.containsKey("\"" + router_ssid + "\"")) {
                bret = mWifiManager.addNetwork(wifiEnable.get("\""
                        + router_ssid + "\""));
            }
        } else if (!xmList.isEmpty()) {
            bret = mWifiManager.addNetwork(xmList.get(0));
            if (bret) {
                SPUtil.getInstance(mContext).setSettingParam(
                        Define.ROUTER_WIFI_SSID, xmList.get(0).SSID);
            }
        }
        return bret;
    }

    /**
     * @param oldDevSSID 之前连接过的设备热点
     * @Title: ToRecordRouter
     * @Description: TODO(连接到历史路由器热点并且删掉之前连接过的设备热点)
     */
    public boolean ToRecordRouter(String oldDevSSID) {
        boolean bret = false;
        int network_state = CheckNetWork.NetWorkUseful(mContext);
        if (!StringUtils.isStringNULL(oldDevSSID)) {
            boolean bRemove = mWifiManager.onRemoveNetWork(oldDevSSID);// 删除之前连接过的设备热点

        }
        if (network_state == NetWorkType.No_NetWork) {
            if (!mWifiManager.openWifi())
                return false;
        }
        String router_ssid = SPUtil.getInstance(mContext).getSettingParam(
                Define.ROUTER_WIFI_SSID, null);
        List<WifiConfiguration> allList = mWifiManager.getConfiguration();
        List<WifiConfiguration> xmList = new ArrayList<WifiConfiguration>();
        if (allList == null) {
            mWifiManager.startScan(2, 1000);
            allList = mWifiManager.getConfiguration();
        }
        HashMap<String, WifiConfiguration> mWifiEnable = new HashMap<String, WifiConfiguration>();
        for (WifiConfiguration config : allList) {
            if (DeviceWifiManager.isXMDeviceWifi(config.SSID)
                    || onCheckEnable(config.SSID)) {
                continue;
            }
            xmList.add(config);
            mWifiEnable.put(config.SSID, config);
        }
        if (StringUtils.isStringNULL(router_ssid) && !router_ssid.equals("0x")) {
            if (mWifiEnable.containsKey(router_ssid)) {
                bret = mWifiManager.addNetwork(mWifiEnable.get(router_ssid));
            } else if (mWifiEnable.containsKey("\"" + router_ssid + "\"")) {
                bret = mWifiManager.addNetwork(mWifiEnable.get("\""
                        + router_ssid + "\""));
            }
        } else if (!xmList.isEmpty()) {
            bret = mWifiManager.addNetwork(xmList.get(0));
        }
        return bret;
    }

    private boolean onCheckEnable(String ssid) {
        return mEnableWifi.containsKey(ssid);
    }

    public void onRelease() {
        synchronized (mWifiConnectReceiver) {
            if (mWifiFilter != null) {
                mContext.unregisterReceiver(mWifiConnectReceiver);
                mWifiFilter = null;
            }
        }
    }

    public String getTryConnectSSID() {
        return mTryConnectSSID;
    }
}
