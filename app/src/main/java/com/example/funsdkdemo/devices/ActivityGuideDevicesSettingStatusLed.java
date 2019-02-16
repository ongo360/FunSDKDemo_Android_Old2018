package com.example.funsdkdemo.devices;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.PowerSocketBanLed;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunDeviceSocket;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

public class ActivityGuideDevicesSettingStatusLed extends ActivityDemo implements
		OnClickListener, OnFunDeviceOptListener, OnItemSelectedListener {
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	private ImageButton mbtnSwitchBanLed = null;
	private ImageButton mBtnSave = null;
	private FunDevice mFunDevice = null;
	private int mCharge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_device_setup_banled);

		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);

		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);

		mTextTitle.setText(this.getResources().getString(R.string.devices_socket_status_light));

		mbtnSwitchBanLed = (ImageButton) findViewById(R.id.btnSwitchBanLed);
		mbtnSwitchBanLed.setOnClickListener(this);
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);
		if (null == mFunDevice) {
			finish();
			return;
		}
		mBtnSave = (ImageButton) setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		initData();
	}

	private void initData() {

		FunSupport.getInstance().requestDeviceConfig(mFunDevice,
				PowerSocketBanLed.CONFIG_NAME);
	}

	@Override
	protected void onDestroy() {

		// 注销监听
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtnInTopLayout: {
			// 返回/退出
			finish();
		}
			break;
		case R.id.btnSwitchBanLed: {
			v.setSelected(!v.isSelected());
		}

			break;

		case R.id.btnSave: // 保存
		{
			trySaveLedConfig();
		}
			break;
		}
	}

	private void trySaveLedConfig() {
		// 插座类
		boolean enable = mbtnSwitchBanLed.isSelected();
		mCharge = enable ? 1 : 0;

		PowerSocketBanLed mSocketImage = (PowerSocketBanLed) mFunDevice
				.checkConfig(PowerSocketBanLed.CONFIG_NAME);

		if (mCharge != mSocketImage.getBanstatus()) {
			mSocketImage.setBanstatus(enable ? 1 : 0);

			// showWaitDialog();

			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
					mSocketImage);
		}
	}

	private void refreshLedConfig() {
		// 插座类
		PowerSocketBanLed mSocketBanLed = ((FunDeviceSocket) mFunDevice)
				.getPowerSocketBanLed();
		if (null != mSocketBanLed) {
			mbtnSwitchBanLed.setSelected(mSocketBanLed.getBanstatus() > 0);
		}
//		FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
//				mSocketBanLed);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceLoginSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceGetConfigSuccess(FunDevice funDevice,
			String configName, int nSeq) {
		// TODO Auto-generated method stub
		if (PowerSocketBanLed.CONFIG_NAME.equals(configName)) {
//			PowerSocketBanLed mSocketBanLed = (PowerSocketBanLed) mFunDevice
//					.getConfig(PowerSocketBanLed.CONFIG_NAME);
//			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
//					mSocketBanLed);
			refreshLedConfig();
		}
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		if (PowerSocketBanLed.CONFIG_NAME.equals(configName)) {
			PowerSocketBanLed mSocketBanLed = (PowerSocketBanLed) mFunDevice
					.getConfig(PowerSocketBanLed.CONFIG_NAME);
			mCharge = mSocketBanLed.getBanstatus();
//			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
//					mSocketBanLed);
			refreshLedConfig();
		}

	}

	@Override
	public void onDeviceSetConfigFailed(FunDevice funDevice, String configName,
			Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceChangeInfoSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceOptionSuccess(FunDevice funDevice, String option) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceOptionFailed(FunDevice funDevice, String option,
			Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice,
			H264_DVR_FILE_DATA[] datas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

}
