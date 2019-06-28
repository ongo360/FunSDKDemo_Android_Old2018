package com.example.funsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.lib.FunSDK;
import com.lib.funsdk.support.FunDevicePassword;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceListener;
import com.lib.funsdk.support.models.FunDevStatus;
import com.lib.funsdk.support.models.FunDevType;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunLoginType;

public class ActivityGuideDeviceSNLogin extends ActivityDemo
		implements OnClickListener, OnFunDeviceListener, OnItemSelectedListener {

	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;

	private Spinner mSpinnerDevType = null;
	private Spinner mSpinnerDevIpType = null;
	private EditText mEditDevSN;
	private EditText mEditDevLoginName;
	private EditText mEditDevLoginPasswd;
	private Button mBtnDevLogin = null;

	private EditText mEditDevIP;
	private EditText meditDevicePort;
	private EditText mEditDevIpLoginName;
	private EditText mEditDevIpLoginPasswd;
	private Button mBtnDevIpLogin = null;

	private FunDevice mFunDevice = null;
	private String mCurrDevSn = null;
	private FunDevType mCurrDevType = null;
	private FunDevType mCurrDevIpType = null;

	private ImageButton mBtnScanQrCode = null;

	private final int MESSAGE_DELAY_FINISH = 0x100;

	// 定义当前支持通过序列号登录的设备类型
	// 如果是设备类型特定的话,固定一个就可以了
	private final FunDevType[] mSupportDevTypes = { FunDevType.EE_DEV_NORMAL_MONITOR,
			FunDevType.EE_DEV_INTELLIGENTSOCKET, FunDevType.EE_DEV_SMALLEYE };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_device_sn_login);

		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);

		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);

		// 初始化设备类型选择器
		mSpinnerDevType = (Spinner) findViewById(R.id.spinnerDeviceType);
		mSpinnerDevIpType = (Spinner) findViewById(R.id.spinnerDeviceIpType);
		String[] spinnerStrs = new String[mSupportDevTypes.length];
		for (int i = 0; i < mSupportDevTypes.length; i++) {
			spinnerStrs[i] = getResources().getString(mSupportDevTypes[i].getTypeStrId());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				spinnerStrs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDevType.setAdapter(adapter);
		mSpinnerDevType.setSelection(0);
		mCurrDevType = mSupportDevTypes[0];
		mSpinnerDevType.setOnItemSelectedListener(this);
		mSpinnerDevIpType.setAdapter(adapter);
		mSpinnerDevIpType.setSelection(0);
		mCurrDevIpType = mSupportDevTypes[0];
		mSpinnerDevIpType.setOnItemSelectedListener(this);

		mEditDevSN = (EditText) findViewById(R.id.editDeviceSN);
		mEditDevLoginName = (EditText) findViewById(R.id.editDeviceLoginName);
		mEditDevLoginPasswd = (EditText) findViewById(R.id.editDeviceLoginPasswd);
		mBtnDevLogin = (Button) findViewById(R.id.devLoginBtn);
		mBtnDevLogin.setOnClickListener(this);

		mEditDevIP = (EditText) findViewById(R.id.editDeviceIP);
		meditDevicePort = (EditText) findViewById(R.id.editDevicePort);
		mEditDevIpLoginName = (EditText) findViewById(R.id.editDeviceIpLoginName);
		mEditDevIpLoginPasswd = (EditText) findViewById(R.id.editDeviceIpLoginPasswd);
		mBtnDevIpLogin = (Button) findViewById(R.id.devLoginBtnIP);
		mBtnDevIpLogin.setOnClickListener(this);

		mBtnScanQrCode = (ImageButton) findViewById(R.id.btnScanCode);
		mBtnScanQrCode.setOnClickListener(this);

		mTextTitle.setText(R.string.guide_module_title_device_sn);

		// 设置登录方式为本地登录
		FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_LOCAL);

		// 监听设备类事件
		FunSupport.getInstance().registerOnFunDeviceListener(this);

		mEditDevSN.setText("");
		mEditDevLoginName.setText("");
		mEditDevLoginPasswd.setText("");
		mEditDevIP.setText("");
		mEditDevIpLoginName.setText("");
		mEditDevIpLoginPasswd.setText("");
	}

	@Override
	protected void onDestroy() {

		// 注销设备事件监听
		FunSupport.getInstance().removeOnFunDeviceListener(this);

		if (null != mHandler) {
			mHandler.removeCallbacksAndMessages(null);
		}

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
		case R.id.devLoginBtn: {
			requestDeviceStatus();
		}
			break;
		case R.id.devLoginBtnIP: {
			requestDeviceIpStatus();
		}
			break;
		case R.id.btnScanCode: {
			startScanQrCode();
		}
			break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_DELAY_FINISH: {
				hideWaitDialog();

				// 启动/打开设备操作界面
				if (null != mFunDevice) {

					// 传入用户名/密码
					mFunDevice.loginName = mEditDevLoginName.getText().toString().trim();
					if (mFunDevice.loginName.length() == 0) {
						// 用户名默认是:admin
						mFunDevice.loginName = "admin";
					}
					mFunDevice.loginPsw = mEditDevLoginPasswd.getText().toString().trim();

                    //Save the password to local file
                    FunDevicePassword.getInstance().saveDevicePassword(mFunDevice.getDevSn(), mEditDevLoginPasswd.getText().toString().trim());
                    FunSDK.DevSetLocalPwd(mFunDevice.getDevSn(), "admin", mEditDevLoginPasswd.getText().toString().trim());

					DeviceActivitys.startDeviceActivity(ActivityGuideDeviceSNLogin.this, mFunDevice);
				}

				mFunDevice = null;
				finish();
			}
				break;
			}
		}

	};

	// 扫描二维码
	private void startScanQrCode() {
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		startActivityForResult(intent, 1);
	}

	// 设备登录
	private void requestDeviceStatus() {
		String devSN = mEditDevSN.getText().toString().trim();

		if (devSN.length() == 0) {
			showToast(R.string.device_login_error_sn);
			return;
		}

		mFunDevice = null;
		mCurrDevSn = devSN;

		showWaitDialog(R.string.device_stauts_unknown);

		FunSupport.getInstance().requestDeviceStatus(mCurrDevType, devSN);
	}

	// 设备登录
	private void requestDeviceIpStatus() {
		String devIP = mEditDevIP.getText().toString().trim();
		String devport = meditDevicePort.getText().toString().trim();
		if (devIP.length() == 0) {
			showToast(R.string.device_login_error_sn);
			return;
		}
		if (devport.length() == 0) {
			devport = "34567";
		}
		FunDevType devType = null;
		String devMac = null;
		String dev = null;
		dev = devIP + ":" + devport;
		mFunDevice = FunSupport.getInstance().buildTempDeivce(devType, devMac);
		mCurrDevSn = devIP;
		mFunDevice.devType = mCurrDevIpType;
		mFunDevice.devIp = devIP;
		mFunDevice.tcpPort = Integer.parseInt(devport);
		mFunDevice.devSn = dev;
		// 传入用户名/密码
		mFunDevice.loginName = mEditDevIpLoginName.getText().toString().trim();
		if (mFunDevice.loginName.length() == 0) {
			// 用户名默认是:admin
			mFunDevice.loginName = "admin";
		}
		mFunDevice.loginPsw = mEditDevIpLoginPasswd.getText().toString().trim();
		DeviceActivitys.startDeviceActivity(ActivityGuideDeviceSNLogin.this, mFunDevice);

	}

	@Override
	public void onDeviceListChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceStatusChanged(FunDevice funDevice) {
		// 设备状态变化,如果是当前登录的设备查询之后是在线的,打开设备操作界面
		if (null != mCurrDevSn && mCurrDevSn.equals(funDevice.getDevSn())) {

			mFunDevice = funDevice;

			showToast(R.string.device_get_status_success);

			hideWaitDialog();

			if (funDevice.devStatus == FunDevStatus.STATUS_ONLINE) {
				// 如果设备在线,获取设备信息
				if ((funDevice.devType == null || funDevice.devType == FunDevType.EE_DEV_UNKNOWN)) {
					funDevice.devType = mCurrDevType;
				}

				if (null != mHandler) {
					mHandler.removeMessages(MESSAGE_DELAY_FINISH);
					mHandler.sendEmptyMessageDelayed(MESSAGE_DELAY_FINISH, 1000);
				}
			} else {
				// 设备不在线
				showToast(R.string.device_stauts_offline);
			}
		}
	}

	@Override
	public void onDeviceAddedSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceAddedFailed(Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceRemovedSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceRemovedFailed(Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAPDeviceListChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLanDeviceListChanged() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (position >= 0 && position < mSupportDevTypes.length) {
			mCurrDevType = mSupportDevTypes[position];
			mCurrDevIpType = mSupportDevTypes[position];
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		if (requestCode == 1 && responseCode == RESULT_OK) {
			// Demo, 扫描二维码的结果
			if (null != data) {
				String deviceSn = data.getStringExtra("result");
				if (null != deviceSn && null != mEditDevSN) {
					mEditDevSN.setText(deviceSn);
				}
			}
		}

	}

}
