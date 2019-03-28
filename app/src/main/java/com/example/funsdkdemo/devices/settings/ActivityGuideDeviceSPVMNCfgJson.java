package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.config.SPVMNCfgJson;
import com.lib.funsdk.support.models.FunDevice;

import static com.example.funsdkdemo.R.id.img_setup_motion_move_btn;
import static com.example.funsdkdemo.R.id.img_setup_outalarm_open_btn;
import static com.example.funsdkdemo.R.id.img_setup_screen_occlusion_btn;
import static com.example.funsdkdemo.R.id.img_setup_server_open_btn;
import static com.example.funsdkdemo.R.id.img_setup_system_abnormality_btn;
import static com.example.funsdkdemo.R.id.img_setup_user_connect_btn;
import static com.example.funsdkdemo.R.id.img_setup_video_dismiss_btn;

/**
 * Created by zzq on 2017-07-13.
 */

public class ActivityGuideDeviceSPVMNCfgJson extends ActivityDemo implements View.OnClickListener, IFunSDKResult {

	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	private ImageButton mBtnSave = null;
	private int mHandler;
	private static final String TAG = "ActivityGuideDeviceSetupSPVMNCfgJson";

	FunDevice mFunDevice = null;
	SPVMNCfgJson CfgJson;

	private ImageButton mServerOpenImgBtn;
	private EditText mOutAlarmOpenImgBtn;
	private EditText mVideoDismissImgBtn;
	private EditText mMotionMoveImgBtn;
	private EditText mScreenOcclusionImgBtn;
	private EditText mSystemAbnormalityImgBtn;
	private EditText mUserConnectImgBtn;

	private EditText mServerAddressEdt;
	private EditText mServerPortEdt;
	private EditText mServerUdpPortEdt;
	private EditText mServerNumberEdt;
	private EditText mAlaemNumberEdt;
	private EditText mServerDomainEdt;
	private EditText mDeviceNumberEdt;
	private EditText mDeviceRegisterPwdEdt;
	private EditText mHeartbeatIntervalTimeEdt;
	private EditText mRegisterDataEdt;
	private EditText mPortNumber;
	private EditText mCamreaLevelEdt;
	private EditText mAlarmLevelEdt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_setup_spvmn_cfg_json);

		int devID = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devID);
		if (null == mFunDevice) {
			finish();
			return;
		}
		CfgJson = new SPVMNCfgJson();
		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
		mTextTitle.setText(R.string.device_setup_spvmn_cfg_json);
		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		mBtnSave = (ImageButton) setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);

		initLayout();
		mHandler = FunSDK.RegUser(this);
		showWaitDialog();
		//获取GB配置
		FunSDK.DevGetConfigByJson(mHandler, mFunDevice.devSn,
				CfgJson.CONFIG_NAME, 4096, -1, 10000, mFunDevice.getId());
	}

	private void initLayout() {
		mServerOpenImgBtn = (ImageButton) findViewById(img_setup_server_open_btn);
		mServerOpenImgBtn.setOnClickListener(this);

		mOutAlarmOpenImgBtn = (EditText) findViewById(img_setup_outalarm_open_btn);
		mVideoDismissImgBtn = (EditText) findViewById(img_setup_video_dismiss_btn);
		mMotionMoveImgBtn = (EditText) findViewById(img_setup_motion_move_btn);
		mScreenOcclusionImgBtn = (EditText) findViewById(img_setup_screen_occlusion_btn);
		mSystemAbnormalityImgBtn = (EditText) findViewById(img_setup_system_abnormality_btn);
		mUserConnectImgBtn = (EditText) findViewById(img_setup_user_connect_btn);
		mServerAddressEdt = (EditText) findViewById(R.id.edit_setup_server_address);
		mServerPortEdt = (EditText) findViewById(R.id.edit_setup_server_port);
		mServerUdpPortEdt = (EditText) findViewById(R.id.edit_setup_server_udp_port);
		mServerNumberEdt = (EditText) findViewById(R.id.edit_setup_server_number);
		mAlaemNumberEdt = (EditText) findViewById(R.id.edit_setup_alarm_number);
		mServerDomainEdt = (EditText) findViewById(R.id.edit_setup_server_domain);
		mDeviceNumberEdt = (EditText) findViewById(R.id.edit_setup_device_number);
		mDeviceRegisterPwdEdt = (EditText) findViewById(R.id.edit_setup_device_register_pwd);
		mHeartbeatIntervalTimeEdt = (EditText) findViewById(R.id.edit_setup_heartbeat_interval_time);
		mRegisterDataEdt = (EditText) findViewById(R.id.edit_setup_register_data);
		mPortNumber = (EditText) findViewById(R.id.edit_setup_port_number);
		mCamreaLevelEdt = (EditText) findViewById(R.id.edit_setup_camrea_level);
		mAlarmLevelEdt = (EditText) findViewById(R.id.edit_setup_alarm_level);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FunSDK.UnRegUser(mHandler);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.backBtnInTopLayout:

				finish();

				break;
			case R.id.img_setup_server_open_btn: {
				v.setSelected(!v.isSelected());
			}
			break;
			case R.id.btnSave: // 保存
			{
				trySaveConfig();
			}
			break;
			default:
				break;
		}
	}

	private void trySaveConfig() {
		CfgJson.setBCsEnable(mServerOpenImgBtn.isSelected());

		CfgJson.setUiAlarmStateGpinEnable(mOutAlarmOpenImgBtn.getText().toString());
		CfgJson.setUiAlarmStateLoseEnable(mVideoDismissImgBtn.getText().toString());
		CfgJson.setUiAlarmStateMotionEnable(mMotionMoveImgBtn.getText().toString());
		CfgJson.setUiAlarmStateBlindEnable(mScreenOcclusionImgBtn.getText().toString());
		CfgJson.setUiAlarmStatePerformanceEnable(mSystemAbnormalityImgBtn.getText().toString());
		CfgJson.setUiAlarmStateConnectEnable(mUserConnectImgBtn.getText().toString());

		CfgJson.setSzCsIP(mServerAddressEdt.getText().toString());
		CfgJson.setSCsPort(Integer.parseInt(mServerPortEdt.getText().toString()));
		CfgJson.setSUdpPort(Integer.parseInt(mServerUdpPortEdt.getText().toString()));
		CfgJson.setSzServerNo(mServerNumberEdt.getText().toString());
		CfgJson.setAlarmid(mAlaemNumberEdt.getText().toString());
		CfgJson.setSzServerDn(mServerDomainEdt.getText().toString());
		CfgJson.setSzDeviceNO(mDeviceNumberEdt.getText().toString());
		CfgJson.setSzConnPass(mDeviceRegisterPwdEdt.getText().toString());
		CfgJson.setIHsIntervalTime(Integer.parseInt(mHeartbeatIntervalTimeEdt.getText().toString()));
		CfgJson.setIRsAgedTime(Integer.parseInt(mRegisterDataEdt.getText().toString()));
		CfgJson.setCamreaid(mPortNumber.getText().toString());
		CfgJson.setCamreaLevel(Integer.parseInt(mCamreaLevelEdt.getText().toString()));
		CfgJson.setAlarmLevel(Integer.parseInt(mAlarmLevelEdt.getText().toString()));

		FunSDK.DevSetConfigByJson(mHandler, mFunDevice.getDevSn(), CfgJson.CONFIG_NAME, CfgJson.getSendMsg(), -1, 60000, mFunDevice.getId());
	}

	private void initdata() {
		mServerOpenImgBtn.setSelected(CfgJson.isBCsEnable());

		mOutAlarmOpenImgBtn.setText(CfgJson.getUiAlarmStateGpinEnable());
		mVideoDismissImgBtn.setText(CfgJson.getUiAlarmStateLoseEnable());
		mMotionMoveImgBtn.setText(CfgJson.getUiAlarmStateMotionEnable());
		mScreenOcclusionImgBtn.setText(CfgJson.getUiAlarmStateBlindEnable());
		mSystemAbnormalityImgBtn.setText(CfgJson.getUiAlarmStatePerformanceEnable());
		mUserConnectImgBtn.setText(CfgJson.getUiAlarmStateConnectEnable());

		mServerAddressEdt.setText(CfgJson.getSzCsIP());
		mServerPortEdt.setText(String.valueOf(CfgJson.getSCsPort()));
		mServerUdpPortEdt.setText(String.valueOf(CfgJson.getSUdpPort()));
		mServerNumberEdt.setText(CfgJson.getSzServerNo());
		mAlaemNumberEdt.setText(CfgJson.getAlarmid());
		mServerDomainEdt.setText(CfgJson.getSzServerDn());
		mDeviceNumberEdt.setText(CfgJson.getSzDeviceNO());
		mDeviceRegisterPwdEdt.setText(CfgJson.getSzConnPass());
		mHeartbeatIntervalTimeEdt.setText(String.valueOf(CfgJson.getIHsIntervalTime()));
		mRegisterDataEdt.setText(String.valueOf(CfgJson.getIRsAgedTime()));
		mPortNumber.setText(CfgJson.getCamreaid());
		mCamreaLevelEdt.setText(String.valueOf(CfgJson.getCamreaLevel()));
		mAlarmLevelEdt.setText(String.valueOf(CfgJson.getAlarmLevel()));


	}

	@Override
	public int OnFunSDKResult(Message message, MsgContent msgContent) {
		// TODO Auto-generated method stub
		FunLog.d(TAG, "msg.what : " + message.what);
		FunLog.d(TAG, "msg.arg1 : " + message.arg1 + " [" + FunError.getErrorStr(message.arg1) + "]");
		FunLog.d(TAG, "msg.arg2 : " + message.arg2);
		if (null != msgContent) {
			FunLog.d(TAG, "msgContent.sender : " + msgContent.sender);
			FunLog.d(TAG, "msgContent.seq : " + msgContent.seq);
			FunLog.d(TAG, "msgContent.str : " + msgContent.str);
			FunLog.d(TAG, "msgContent.arg3 : " + msgContent.arg3);
			FunLog.d(TAG, "msgContent.pData : " + msgContent.pData);
		}

		switch (message.what) {

			case EUIMSG.DEV_GET_JSON:
				FunLog.i(TAG, "EUIMSG.DEV_GET_JSON");
				FunLog.i(TAG, "JSON__" + G.ToString(msgContent.pData));

				if (msgContent.seq == mFunDevice.getId()) {

					if (message.arg1 < 0) {
						getconfigfailed(message.arg1);
					} else if (msgContent.pData != null) {
						hideWaitDialog();
						if (!CfgJson.onParse(G.ToString(msgContent.pData))) {
							Toast.makeText(this, R.string.EE_DVR_DEV_VER_NOMATCH, Toast.LENGTH_SHORT).show();
							break;
						}
						initdata();
					}
				}
				break;
			case EUIMSG.DEV_SET_JSON:
				if (msgContent.seq == mFunDevice.getId()) {

					if (message.arg1 < 0) {
						getconfigfailed(message.arg1);
					} else if (msgContent.pData != null) {
						hideWaitDialog();
						Toast.makeText(this, R.string.devices_socket_save_success, Toast.LENGTH_SHORT).show();
					}
				}
			default:
				break;
		}
		return 0;
	}

	private void getconfigfailed(int ErrCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(ErrCode));
	}
}
