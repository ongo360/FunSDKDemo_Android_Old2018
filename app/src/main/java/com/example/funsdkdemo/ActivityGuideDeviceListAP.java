package com.example.funsdkdemo;


import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.basic.G;
import com.example.funsdkdemo.ListAdapterFunDevice.OnFunDeviceItemClickListener;
import com.example.funsdkdemo.devices.ActivityGuideDeviceTransCom;
import com.example.funsdkdemo.devices.lowpower.LowPowerDevActivity;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnAddSubDeviceResultListener;
import com.lib.funsdk.support.OnFunDeviceListener;
import com.lib.funsdk.support.config.JsonConfig;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunLoginType;
import com.lib.funsdk.support.utils.APAutomaticSwitch;
import com.lib.funsdk.support.utils.WifiStateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo: 显示附近的打开WiFi热点的设备列表
 * 1. 切换访问模式为AP模式  - FunSDK.SysInitAsAPModle()
 * 2. 注册设备列表更新监听  - FunSupport.registerOnFunDeviceListener()
 * 3. 请求扫描附近的WiFi列表,找到SSID包含雄迈特征字符的WiFi,认为是可连接的设备
 * 4. 退出并注销监听
 */
public class ActivityGuideDeviceListAP extends ActivityDemo implements OnClickListener, OnFunDeviceListener, OnFunDeviceItemClickListener, WifiStateListener, OnAddSubDeviceResultListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private ImageButton mBtnRefresh = null;
	
	private ExpandableListView mListView = null;
	private ListAdapterFunDevice mAdapter = null;
	private List<FunDevice> mAPDeviceList = new ArrayList<FunDevice>();
	
	private FunDevice mCurrSelectedDevice = null;
	
	private APAutomaticSwitch mAPSwitcher = null;
	private String mSubDeviceSN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_list);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		mTextTitle.setText(R.string.device_list_nearby);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		View layoutRefresh = setNavagateRightButton(R.layout.imagebutton_refresh);
		mBtnRefresh = (ImageButton)layoutRefresh.findViewById(R.id.btnRefresh);
		mBtnRefresh.setOnClickListener(this);
		
		mListView = (ExpandableListView)findViewById(R.id.listViewDevice);
		mAdapter = new ListAdapterFunDevice(this, mAPDeviceList);
		// AP设备不允许删除和重命名,需要连接AP
		mAdapter.setCanRemoved(false);
		mAdapter.setCanRenamed(false);
		mAdapter.setNeedConnectAP(true);
		mAdapter.setOnFunDeviceItemClickListener(this);
		mListView.setAdapter(mAdapter);
		
		// 刷新设备列表
		refreshAPDeviceList();

		// Demo，如果是进入设备列表就切换到AP模式,退出时切换回NET模式
		FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_AP);
		
		// 监听设备类事件
		FunSupport.getInstance().registerOnFunDeviceListener(this);
		FunSupport.getInstance().registerOnAddSubDeviceResultListener(this);
		
		// 打开之后进行一次搜索
		requestToGetAPDeviceList();
	}
	

	@Override
	protected void onDestroy() {
		
		if ( null != mAPSwitcher ) {
			mAPSwitcher.onRelease();
			mAPSwitcher = null;
		}
		
		// 注销设备事件监听
		FunSupport.getInstance().removeOnFunDeviceListener(this);
		
		// 切换回网络访问
		FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_INTENTT);
		
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
		case R.id.btnRefresh:
			{
				// 刷新设备列表
				requestToGetAPDeviceList();
			}
			break;
		}
	}
	

	@Override
	protected void onPause() {
		mCurrSelectedDevice = null;
		super.onPause();
	}


	private void requestToGetAPDeviceList() {
		if ( !FunSupport.getInstance().requestAPDeviceList() ) {
			showToast(R.string.guide_message_error_call);
		} else {
			showWaitDialog();
		}
	}
	
	private void refreshAPDeviceList() {
		hideWaitDialog();
		
		mAPDeviceList.clear();
		
		mAPDeviceList.addAll(FunSupport.getInstance().getAPDeviceList());
		
		mAdapter.notifyDataSetChanged();
	}
	
	private void autoSwitchWifi(FunDevice funDevice) {
		if ( null == mAPSwitcher ) {
			mAPSwitcher = new APAutomaticSwitch(this, 
					FunSupport.getInstance().getDeviceWifiManager());
			
			mAPSwitcher.setWifiStateListener(this);
		}
		
		if ( !FunSupport.getInstance().isAPDeviceConnected(funDevice) ) {
			mCurrSelectedDevice = funDevice;
			mAPSwitcher.RouterToDevAP(funDevice.devName, false);
		} else {
			mAPSwitcher.DevAPToRouter();
		}
	}

	private void tryOpenDevice(FunDevice funDevice) {
		if ( FunSupport.getInstance().isAPDeviceConnected(funDevice) ) {
			// 已经连接到了AP,跳转到对应的设备控制界面
			DeviceActivitys.startDeviceActivity(this, funDevice);
		} else {
			// 设备未连接
			showToast(R.string.device_ap_disconnect);
		}
	}
	
	/**
	 * 以下函数实现来自OnFunDeviceListener()，监听设备列表变化
	 */
	@Override
	public void onDeviceListChanged() {
	}

	@Override
	public void onDeviceStatusChanged(final FunDevice funDevice) {
	}
	
	@Override
	public void onDeviceAddedSuccess() {
		
	}


	@Override
	public void onDeviceAddedFailed(Integer errCode) {
		
	}


	@Override
	public void onDeviceRemovedSuccess() {
		
	}


	@Override
	public void onDeviceRemovedFailed(Integer errCode) {
		
	}
	
	@Override
	public void onAPDeviceListChanged() {
//		if ( null != mListView ) {
//			// 所有展开的列收缩回去
//			for ( int i = 0; i < mListView.getChildCount(); i ++ ) {
//				if ( mListView.isGroupExpanded(i) ) {
//					mListView.collapseGroup(i);
//				}
//			}
//		}
		refreshAPDeviceList();
	}
	
	@Override
	public void onLanDeviceListChanged() {
		
	}


	/**
	 * 以下4个函数来自OnFunDeviceItemClickListener()，监听设备中的操作按钮选择
	 */
	@Override
	public void onFunDeviceRenameClicked(FunDevice funDevice) {
		// 设备重命名
		
	}


	@Override
	public void onFunDeviceConnectClicked(FunDevice funDevice) {
		// 打开设备/连接设备
		autoSwitchWifi(funDevice);
	}


	@Override
	public void onFunDeviceControlClicked(FunDevice funDevice) {
		// 浏览设备中的文件
		tryOpenDevice(funDevice);
	}
	
	@Override
	public void onFunDeviceAlarmClicked(FunDevice funDevice) {
		// 设备报警
//		if ( null != funDevice ) {
//			Intent intent = new Intent();
//			intent.setClass(this, ActivityGuideDeviceAlarmResult.class);
//			intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//		}
	}
	
	@Override
	public void onFunDeviceTransComClicked(FunDevice funDevice) {
		if ( null != funDevice ) {
			Intent intent = new Intent();
			intent.setClass(this, ActivityGuideDeviceTransCom.class);
			intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	@Override
	public void onFunDeviceRemoveClicked(FunDevice funDevice) {
		// 删除设备
		
	}



	/* (non-Javadoc)
	 * @see com.example.funsdkdemo.ListAdapterFunDevice.OnFunDeviceItemClickListener#onFunDevice433Control(com.lib.funsdk.support.models.FunDevice)
	 */
	@Override
	public void onFunDevice433Control(FunDevice funDevice) {
		// TODO Auto-generated method stub
		//        if (null != mSubDeviceSN && mSubDeviceSN != "") {
//            showWaitDialog();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("RFDeviceSN", mSubDeviceSN);
		jsonObject.put("DeviceType", "Relay");
		jsonObject.put("Operate", "Close");
		jsonObject.put("Freq", "433");
		jsonObject.put("BaudRate", "100");
		FunSupport.getInstance().requestControlSubDevice(funDevice, JsonConfig.ControlSubDevice, jsonObject.toJSONString());
//        }else{
//            Toast.makeText(this, "子设备序列号为空", Toast.LENGTH_SHORT).show();
//        }
	}

	@Override
	public void onFunDeviceWakeUp(FunDevice funDevice) {
		Intent intent = new Intent();
		intent.setClass(this, LowPowerDevActivity.class);
		intent.putExtra("FUN_DEVICE_ID", funDevice.getId());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onFunDeviceCloud(FunDevice funDevice) {

	}


	/* (non-Javadoc)
	 * @see com.example.funsdkdemo.ListAdapterFunDevice.OnFunDeviceItemClickListener#onFunDevice433AddSub(com.lib.funsdk.support.models.FunDevice)
	 */
	@Override
	public void onFunDevice433AddSub(FunDevice funDevice) {
		// TODO Auto-generated method stub
		showWaitDialog();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("DeviceType", "Relay");
		jsonObject.put("DeviceMold", "Control");
		jsonObject.put("Operate", "Close");
		jsonObject.put("Freq", "433");
		jsonObject.put("BaudRate", "100");
		FunSupport.getInstance().requestDeviceAddSubDev(funDevice, JsonConfig.AddSubDevice, jsonObject.toJSONString());
	}


	@Override
	public void onAddSubDeviceFailed(FunDevice funDevice, MsgContent msgContent) {
		if (msgContent.str.equals(JsonConfig.AddSubDevice)) {
			Toast.makeText(this, "添加子设备失败", Toast.LENGTH_SHORT).show();

		}
		hideWaitDialog();
	}

	@Override
	public void onAddSubDeviceSuccess(FunDevice funDevice, MsgContent msgContent) {
		if (msgContent.str.equals(JsonConfig.AddSubDevice)) {
			JSONObject jsonObject;
			jsonObject = (JSONObject) JSONObject.parse(G.ToString(msgContent.pData));
			mSubDeviceSN = (String) jsonObject.get("RFDeviceSN");
			System.out.println("zyy------------mSubDeviceSN    " + mSubDeviceSN);
			Toast.makeText(this, "添加子设备成功", Toast.LENGTH_SHORT).show();
		}
		hideWaitDialog();
	}

	@Override
	public void onNetWorkState(NetworkInfo.DetailedState state, int type, String ssid) {
		Log.d("test", "onNetWorkState : " + state + ", " + type + ", " + ssid);

		if ( state == NetworkInfo.DetailedState.CONNECTED ) {

			FunDevice funDev = FunSupport.getInstance().findAPDevice(ssid);
			if ( null != funDev ) {
				funDev.devIp = FunSupport.getInstance().getDeviceWifiManager().getGatewayIp();
			}

			if ( null != mCurrSelectedDevice
					&& ssid.equals(mCurrSelectedDevice.devName )) {
				tryOpenDevice(mCurrSelectedDevice);
			}
		}

		if ( null != mAdapter ) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onIsWiFiAvailable(boolean isWiFiAvailable) {

	}

	@Override
	public void onNetWorkChange(NetworkInfo.DetailedState state, int type, String SSid) {

	}
}
