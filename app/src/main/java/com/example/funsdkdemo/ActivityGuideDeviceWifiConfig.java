package com.example.funsdkdemo;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.FunWifiPassword;
import com.lib.funsdk.support.OnFunDeviceWiFiConfigListener;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.DeviceWifiManager;
import com.lib.funsdk.support.utils.MyUtils;
import com.lib.funsdk.support.utils.StringUtils;


public class ActivityGuideDeviceWifiConfig extends ActivityDemo implements OnClickListener, OnFunDeviceWiFiConfigListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private EditText mEditWifiSSID = null;
	private EditText mEditWifiPasswd = null;
	private Button mBtnSetting = null;
	
//	private ImageButton mBtnWifiList = null;
	
	// 下面字符串只是记录一下当前配置成功的设备,为了Demo方便,没有实际意义
	private String mSettedWifiDevSN = "";
	private TextView mTextSetted = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_set_wifi);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mEditWifiSSID = (EditText)findViewById(R.id.editWifiSSID);
		mEditWifiPasswd = (EditText)findViewById(R.id.editWifiPasswd);
		
//		mBtnWifiList = (ImageButton)findViewById(R.id.btnWifiList);
//		mBtnWifiList.setOnClickListener(this);
		
		mBtnSetting = (Button)findViewById(R.id.btnWifiQuickSetting);
		mBtnSetting.setOnClickListener(this);
		
		mTextTitle.setText(R.string.guide_module_title_device_setwifi);
		
		String currSSID = getConnectWifiSSID();
		mEditWifiSSID.setText(currSSID);
		mEditWifiPasswd.setText(FunWifiPassword.getInstance().getPassword(currSSID));
		
		mTextSetted = (TextView)findViewById(R.id.textWifiSettedDevices);
		
		FunSupport.getInstance().registerOnFunDeviceWiFiConfigListener(this);
	}
	

	@Override
	protected void onDestroy() {
		
		stopQuickSetting();
		
		if ( null != mHandler ) {
			mHandler.removeCallbacksAndMessages(null);
		}
		
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.backBtnInTopLayout:
			{
				// 返回/退出
				finish();
			}
			break;
		case R.id.btnWifiQuickSetting:
			{
				startQuickSetting();
			}
			break;
//		case R.id.btnWifiList:
//			{
//				showWifiScanResult();
//			}
//			break;
		}
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			}
		}
		
	};
	
	// 开始快速配置
	private void startQuickSetting() {
		
		try {
			WifiManager wifiManage = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManage.getConnectionInfo();
			DhcpInfo wifiDhcp = wifiManage.getDhcpInfo();
			
			if ( null == wifiInfo ) {
				showToast(R.string.device_opt_set_wifi_info_error);
				return;
			}
			
			String ssid = wifiInfo.getSSID().replace("\"", "");
			if ( StringUtils.isStringNULL(ssid) ) {
				showToast(R.string.device_opt_set_wifi_info_error);
				return;
			}
			
			ScanResult scanResult = DeviceWifiManager.getInstance(this).getCurScanResult(ssid);
			if ( null == scanResult ) {
				showToast(R.string.device_opt_set_wifi_info_error);
				return;
			}
			
			int pwdType = MyUtils.getEncrypPasswordType(scanResult.capabilities);
			String wifiPwd = mEditWifiPasswd.getText().toString().trim();
			
			if ( pwdType != 0 && StringUtils.isStringNULL(wifiPwd) ) {
				// 需要密码
				showToast(R.string.device_opt_set_wifi_info_error);
				return;
			}
			
			StringBuffer data = new StringBuffer();
			data.append("S:").append(ssid).append("P:").append(wifiPwd).append("T:").append(pwdType);
			
			String submask;
			if (wifiDhcp.netmask == 0) {
				submask = "255.255.255.0";
			} else {
				submask = MyUtils.formatIpAddress(wifiDhcp.netmask);
			}
			
			String mac = wifiInfo.getMacAddress();
			StringBuffer info = new StringBuffer();
			info.append("gateway:").append(MyUtils.formatIpAddress(wifiDhcp.gateway)).append(" ip:")
					.append(MyUtils.formatIpAddress(wifiDhcp.ipAddress)).append(" submask:").append(submask)
					.append(" dns1:").append(MyUtils.formatIpAddress(wifiDhcp.dns1)).append(" dns2:")
					.append(MyUtils.formatIpAddress(wifiDhcp.dns2)).append(" mac:").append(mac)
					.append(" ");
			
			showWaitDialog();
			
			FunSupport.getInstance().startWiFiQuickConfig(ssid, 
					data.toString(), info.toString(), 
					MyUtils.formatIpAddress(wifiDhcp.gateway), 
					pwdType, 0, mac, -1);
			
			FunWifiPassword.getInstance().saveWifiPassword(ssid, wifiPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopQuickSetting() {
		FunSupport.getInstance().stopWiFiQuickConfig();
	}

	private String getConnectWifiSSID() {
		try {
			WifiManager wifimanage=(WifiManager)getSystemService(Context.WIFI_SERVICE);
			return wifimanage.getConnectionInfo().getSSID().replace("\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

//	private void showWifiScanResult() {
//		
//		DeviceWifiManager.getInstance(this).updateWifiList(WIFI_TYPE.ROUTER);
//		
//		DialogWifiScanResult dialog = new DialogWifiScanResult(this,
//				DeviceWifiManager.getInstance(this).getWifiList(),
//				new OnWifiScanResultSelectListener() {
//					
//					@Override
//					public void onWifiScanResultSelected(ScanResult scanResult) {
//						if ( null != mEditWifiSSID ) {
//							mEditWifiSSID.setText(scanResult.SSID);
//							mEditWifiPasswd.setText("");
//						}
//					}
//				});
//		dialog.show();
//	}
	
	@Override
	public void onDeviceWiFiConfigSetted(FunDevice funDevice) {
		hideWaitDialog();
		
		if ( null != funDevice ) {
			showToast(String.format(
					getResources().getString(R.string.device_opt_set_wifi_success), 
					funDevice.getDevSn()));
			mSettedWifiDevSN += funDevice.getDevSn() + "\n";
			
			if ( null != mTextSetted ) {
				mTextSetted.setText(mSettedWifiDevSN);
			}
		}
	}
}
