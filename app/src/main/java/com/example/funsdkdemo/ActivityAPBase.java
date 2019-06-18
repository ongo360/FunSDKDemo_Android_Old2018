/**
 * FutureFamily
 * APBaseActivity.java
 * Administrator
 * TODO
 * 2015-6-23
 */
package com.example.funsdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lib.funsdk.support.utils.APAutomaticSwitch;
import com.lib.funsdk.support.utils.DeviceWifiManager;
import com.lib.funsdk.support.utils.WifiStateListener;


/**
 * FutureFamily APBaseActivity.java
 *
 * @author huangwanshui TODO 2015-6-23
 */
public abstract class ActivityAPBase extends ActivityDemo implements WifiStateListener {
    protected DeviceWifiManager mDevWifiManager;
    private APAutomaticSwitch mAPAutoSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mDevWifiManager = DeviceWifiManager.getInstance(getApplicationContext());
        initAPAutoSwitch();
        super.onCreate(savedInstanceState);
    }

    private void initAPAutoSwitch() {
        if (null != mDevWifiManager) {
            mAPAutoSwitch = new APAutomaticSwitch(this, mDevWifiManager);
        }
    }

    protected void initWifiStateListener() {
        if (null != mAPAutoSwitch)
            mAPAutoSwitch.setWifiStateListener(this);
    }

    public APAutomaticSwitch getAPSwitch() {
        if (null == mAPAutoSwitch) {
            initAPAutoSwitch();
        }
        return mAPAutoSwitch;
    }

    protected boolean isAPSwitchEnable() {
        synchronized (this) {
            return null == mAPAutoSwitch ? false : true;
        }
    }

    protected DeviceWifiManager getWiFiManager() {
        return mDevWifiManager == null ? DeviceWifiManager
                .getInstance(getApplicationContext()) : mDevWifiManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobile.myeye.base.BaseActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (null != mAPAutoSwitch) {
            mAPAutoSwitch.onRelease();
            mAPAutoSwitch = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

}
