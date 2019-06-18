package com.example.funsdkdemo.devices.settings;


import android.app.Activity;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.basic.G;
import com.example.funsdkdemo.ActivityAPBase;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.WifiListAdapter;
import com.example.funsdkdemo.utils.XUtils;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.BaseThreadPool;
import com.lib.funsdk.support.utils.Define;
import com.lib.funsdk.support.utils.DeviceWifiManager;
import com.lib.funsdk.support.utils.SPUtil;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.JsonConfig;
import com.lib.sdk.bean.NetWorkWiFiBean;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideDeviceAPToWiFi extends ActivityAPBase
	implements OnClickListener, IFunSDKResult,AdapterView.OnItemClickListener{

	private final String TAG = "ActivityGuideDeviceAPToWiFi";
	protected ListView mListView;
	private DeviceWifiManager mWifiManager;
	private WifiListAdapter mAdapter;
	private Handler mHandler;
	protected ImageView mRefreshWifi;
	protected TextView mWifiName;
	protected EditText mWifiPassword;
	protected Button mShowPassword;
	protected CheckBox mAPSetting;
	protected CheckBox mRouterSetting;
	protected Button mSave;
	private ScanResult mScanResult;
	protected TextView mCurrentModel;
	protected LinearLayout mRouterLayout;
	protected TextView mAPModelTip;
	private boolean mLinkDeviceByAP = false;
	private NetWorkWiFiBean mNetworkWifi;
	private List<ScanResult> mWifiList;
	private FunDevice mFunDevice = null;
	private static final int mRequestCode = 0x100;
	private int mUserId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_ap_to_wifi);
		findViewById(R.id.backBtnInTopLayout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		mListView = (ListView) findViewById(R.id.wifi_list);
		mRefreshWifi = (ImageView) findViewById(R.id.wifi_refresh);
		mWifiName = (TextView) findViewById(R.id.wifi);
		mWifiPassword = (EditText) findViewById(R.id.wifi_psd);
		mShowPassword = (Button) findViewById(R.id.psd_show);
		mAPSetting = (CheckBox) findViewById(R.id.ap_setting);
		mRouterSetting = (CheckBox) findViewById(R.id.router_setting);
		mCurrentModel = (TextView) findViewById(R.id.current_model);
		mSave = (Button) findViewById(R.id.save);
		mRouterLayout = (LinearLayout) findViewById(R.id.router_layout);
		mAPModelTip = (TextView) findViewById(R.id.AP_model_tip);
		mAPSetting.setOnClickListener(this);
		mRefreshWifi.setOnClickListener(this);
		mWifiManager = DeviceWifiManager.getInstance(this);
		mAdapter = new WifiListAdapter(mWifiList, this);
		mListView.setAdapter(mAdapter);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						//更新WiFi列表
						mAdapter.updateData(mWifiList);
						break;
					case 1:
						if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
						break;
				}
			}
		};
		mListView.setOnItemClickListener(this);
		mShowPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mWifiPassword.getTransformationMethod() == null) {
					mWifiPassword.setTransformationMethod(new PasswordTransformationMethod());
				} else {
					mWifiPassword.setTransformationMethod(null);
				}
			}
		});
		mSave.setOnClickListener(this);
		mAPSetting.setOnClickListener(this);
		mRouterSetting.setOnClickListener(this);
		if (DeviceWifiManager.isXMDeviceWifi(mWifiManager.getSSID())) {
			mCurrentModel.setText(R.string.Current_link_model_ap);
			mRouterLayout.setVisibility(View.GONE);
			mAPModelTip.setVisibility(View.VISIBLE);
			mAPSetting.setChecked(true);
			mLinkDeviceByAP = true;
		} else {
			mCurrentModel.setText(R.string.Current_link_model_router);
			mRouterSetting.setChecked(true);
			mRouterLayout.setVisibility(View.VISIBLE);
			mAPModelTip.setVisibility(View.GONE);
			mLinkDeviceByAP = false;
		}

		mUserId = FunSDK.GetId(mUserId,this);
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);

		FunSDK.DevGetConfigByJson(mUserId,mFunDevice.getDevSn(), JsonConfig.NETWORK_WIFI, 1024, -1, 5000, 0);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.wifi_refresh:
				scanWifi(DeviceWifiManager.WIFI_TYPE.ROUTER);
				break;
			case R.id.save: {
				if (mAPSetting.isChecked()) {
					showWaitDialog();
					JSONObject jsonObject1 = new JSONObject();
					jsonObject1.put("Action", "ToAP");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("Name", "OPNetModeSwitch");
					jsonObject.put("OPNetModeSwitch", jsonObject1);
					FunSDK.DevCmdGeneral(mUserId, mFunDevice.getDevSn(), 1450, "OPNetModeSwitch", 0, 5000, jsonObject.toJSONString().getBytes(), 0, 0);
				} else if (mRouterSetting.isChecked()) {
					if (!mWifiName.getText().equals("")) {
							String ssid = mWifiName.getText().toString();
							String wifiPwd = mWifiPassword.getText().toString();
							mScanResult = mWifiManager.getCurScanResult(ssid);
							if (null != mScanResult) {
								int pwdType = XUtils.getEncrypPasswordType(mScanResult.capabilities);
								if (pwdType == 3 && (wifiPwd.length() == 10 || wifiPwd.length() == 26)) {
									wifiPwd = XUtils.asciiToString(wifiPwd);
								}
							}
							if (mScanResult != null && (mScanResult.frequency > 4900 && mScanResult.frequency < 5900)) {
								showToast(R.string.Frequency_support);
								return;
							}
							showWaitDialog();
							if (mLinkDeviceByAP) {
								FunSDK.DevSetLocalPwd("192.168.10.1", "admin", FunSDK.DevGetLocalPwd(mFunDevice.getDevSn()));//AP配网成功后设置IP对应的密码为空
								FunSDK.DevStartWifiConfigByAPLogin(mUserId, "192.168.10.1", ssid, wifiPwd, -1);
							}else {
								FunSDK.DevStartWifiConfig(mUserId, mFunDevice.getDevSn(), ssid, wifiPwd, -1);
							}

					} else {
						showToast(R.string.Enter_link_wifi);
					}
				} else {
					showToast(R.string.Select_switch_model);
				}
				break;
			}
			case R.id.ap_setting:
				if (mAPSetting.isChecked()) {
					mRouterSetting.setChecked(false);
					mRouterLayout.setVisibility(View.GONE);
					mAPModelTip.setVisibility(View.VISIBLE);
				} else {
					mRouterSetting.setChecked(true);
					mRouterLayout.setVisibility(View.VISIBLE);
					mAPModelTip.setVisibility(View.GONE);
				}
				break;
			case R.id.router_setting:
				if (mRouterSetting.isChecked()) {
					mAPSetting.setChecked(false);
					mRouterLayout.setVisibility(View.VISIBLE);
					mAPModelTip.setVisibility(View.GONE);
				} else {
					mAPSetting.setChecked(true);
					mRouterLayout.setVisibility(View.GONE);
					mAPModelTip.setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (DeviceWifiManager.isXMDeviceWifi(mWifiManager.getSSID())) {
			mCurrentModel.setText(R.string.Current_link_model_ap);
		} else {
			mCurrentModel.setText(R.string.Current_link_model_router);
		}
		scanWifi(DeviceWifiManager.WIFI_TYPE.ROUTER);
	}

	public void scanWifi(final int wifiType) {
		if (!getWiFiManager().isWiFiEnabled()) {
			getWiFiManager().openWifi();
		}
		showWaitDialog();
		BaseThreadPool.getInstance().doTaskBySinglePool(new Thread() {
			@Override
			public void run() {
				super.run();
				getWiFiManager().startScan(wifiType, 3000);
				ArrayList<ScanResult> list = (ArrayList<ScanResult>) getWiFiManager().getWifiList();
				if (list.size() == 0) {
					if (getWiFiManager().getWifiNumber() == 0) {
						mHandler.sendEmptyMessage(1);//WiFi个数为0
						return;
					}
				}
				mWifiList = new ArrayList<ScanResult>();
				for (ScanResult sR : list) {
					if (sR.frequency > 4900 && sR.frequency < 5900) {
						// 5G, not support
						continue;
					} else {
						mWifiList.add(sR);
					}
				}
				mHandler.sendEmptyMessage(0);
			}
		}, 4);
	}

	@Override
	public int OnFunSDKResult(Message msg, MsgContent ex) {
		if (msg.arg1 < 0) {
			hideWaitDialog();
			showToast(getString(R.string.set_config_f));
			return 0;
		}
		switch (msg.what) {
			case EUIMSG.DEV_CMD_EN: {
				{
					showToast(getString(R.string.set_config_s));
					finish();
				}
				break;
			}
			case EUIMSG.DEV_SET_WIFI_CFG: {
				hideWaitDialog();
				if (msg.arg1 == 1) {
					FunSDK.DevStopWifiConfig();
					showToast(getString(R.string.set_config_s));
					finish();
				} else if (msg.arg1 < 0) {
					showToast(getString(R.string.set_config_f));
				}
				break;
			}
			case EUIMSG.DEV_GET_JSON: {
				hideWaitDialog();
				if (JsonConfig.NETWORK_WIFI.equals(ex.str)) {
					if (ex.pData != null && ex.pData.length > 0) {
						HandleConfigData configData = new HandleConfigData();
						if (configData.getDataObj(G.ToString(ex.pData), NetWorkWiFiBean.class)) {
							mNetworkWifi = (NetWorkWiFiBean) configData.getObj();
							mWifiName.setText(mNetworkWifi.getSsid());
							mWifiPassword.setText(mNetworkWifi.getKeys());
						}
					}
				}
				break;
			}
		}
		return 0;
	}

	@Override
	public void onNetWorkState(NetworkInfo.DetailedState state, int type, String ssid) {

	}

	@Override
	public void onIsWiFiAvailable(boolean isWiFiAvailable) {

	}

	@Override
	public void onNetWorkChange(NetworkInfo.DetailedState state, int type, String SSid) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == R.id.wifi_list) {
			if (DeviceWifiManager.isXMDeviceWifi(((ScanResult) mAdapter.getItem(position)).SSID)) {
				getAPSwitch().RouterToDevAP(((ScanResult) mAdapter.getItem(position)).SSID, false);
			} else {
				mWifiName.setText(((ScanResult) mAdapter.getItem(position)).SSID);
				mWifiPassword.setText(SPUtil.getInstance(this).getSettingParam(Define.WIFI_PASSWORD_PREFIX + mWifiName.getText().toString(), ""));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == this.mRequestCode) {
			if (resultCode == Activity.RESULT_OK) {
				finish();
			} else if (resultCode == Activity.RESULT_CANCELED) {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
