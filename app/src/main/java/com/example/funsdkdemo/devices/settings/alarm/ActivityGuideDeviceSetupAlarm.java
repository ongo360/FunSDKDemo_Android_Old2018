package com.example.funsdkdemo.devices.settings.alarm;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.G;
import com.example.common.DeviceConfigType;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.dialog.AlarmPeriodDlg;
import com.example.funsdkdemo.entity.TimeItem;
import com.lib.DevSDK;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.AlarmOut;
import com.lib.funsdk.support.config.AlarmOut.AlarmOutInfo;
import com.lib.funsdk.support.config.DetectBlind;
import com.lib.funsdk.support.config.DetectMotion;
import com.lib.funsdk.support.config.LocalAlarm;
import com.lib.funsdk.support.config.PowerSocketArm;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunDeviceSocket;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.bean.JsonConfig;
import com.lib.sdk.bean.NetworkPmsBean;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.xm.ui.widget.ListSelectItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.funsdkdemo.dialog.AlarmPeriodDlg.REQUEST_CODE_SETTING_ALARM_TIME;


/**
 * Demo: 报警配置
 *
 */
public class ActivityGuideDeviceSetupAlarm extends ActivityDemo implements OnClickListener, OnFunDeviceOptListener,
		OnItemSelectedListener,IFunSDKResult,SeekBar.OnSeekBarChangeListener {
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private ImageButton mBtnSwitchMotion = null;
	private ImageButton mBtnSwitchMotionRecord = null;
	private ImageButton mBtnSwitchMotionCapture = null;
	private ImageButton mBtnSwitchMotionPushMsg = null;
	private Spinner mSpinnerDetectionLevel = null;
	private ImageButton mBtnSwitchBlock = null;
	private ImageButton mBtnSwitchBlockRecord = null;
	private ImageButton mBtnSwitchBlockCapture = null;
	private ImageButton mBtnSwitchBlockPushMsg = null;
	private ImageButton mBtnSwitchLocalIO = null;
	private ImageButton mBtnSwitchLocalIORecord = null;
	private ImageButton mBtnSwitchLocalIOCapture = null;
	private ImageButton mBtnSwitchLocalIOPushMsg = null;
	private ImageButton mBtnSwitchAlarmOutStatus = null;
	private Spinner mSpinnerAlarmOutType = null;
	private RelativeLayout mRlAlarmTime = null;
	private ListSelectItem mLsiAlarmInterval = null;//报警间隔
	private SeekBar mSbAlarmInterval = null;
	private String[] mDetectionAlarmLevels = null;
	
	private ImageButton mBtnSave = null;
	
	private FunDevice mFunDevice = null;
	
	// 以下Layout在插座的报警配置时隐藏
	private LinearLayout mLayoutOthers = null;

	private int mUserId;

	private NetworkPmsBean mNetworkPms;

	/**
	 * 本界面需要获取到的设备配置信息(插座类)
	 */
	private final String[] DEV_CONFIGS_FOR_SOCKET = {
			// 插座类报警
			PowerSocketArm.CONFIG_NAME
	};
	
	/**
	 * 本界面需要获取到的设备配置信息(监控类)
	 */
	private final String[] DEV_CONFIGS_FOR_CAMERA = {
			// 移动侦测
			DetectMotion.CONFIG_NAME,
			
			// 视频遮掉
			DetectBlind.CONFIG_NAME,
			
			// 报警输入
			LocalAlarm.CONFIG_NAME,

			//报警输出
			AlarmOut.CONFIG_NAME,
	};
	
	private final String[] DEV_CONFIGS_FOR_CHANNELS = {
			//移动侦测
			DetectMotion.CONFIG_NAME,
			
			//视频遮挡
			DetectBlind.CONFIG_NAME,
			AlarmOut.CONFIG_NAME
	};
	
	private String[] DEV_CONFIGS = null;
	
	// 设置配置信息的时候,由于有多个,通过下面的列表来判断是否所有的配置都设置完成了
	private List<String> mSettingConfigs = new ArrayList<String>();

	private AlarmPeriodDlg alarmPeriodDlg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_setup_alarm);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_opt_alarm_config);
		
		mBtnSwitchMotion = (ImageButton)findViewById(R.id.btnSwitchMotionDetection);
		mBtnSwitchMotion.setOnClickListener(this);
		mBtnSwitchMotionRecord = (ImageButton)findViewById(R.id.btnSwitchMotionDetectionAlarmRecord);
		mBtnSwitchMotionRecord.setOnClickListener(this);
		mBtnSwitchMotionCapture = (ImageButton)findViewById(R.id.btnSwitchMotionDetectionAlarmCapture);
		mBtnSwitchMotionCapture.setOnClickListener(this);
		mBtnSwitchMotionPushMsg = (ImageButton)findViewById(R.id.btnSwitchMotionDetectionAlarmPushMsg);
		mBtnSwitchMotionPushMsg.setOnClickListener(this);
		mSpinnerDetectionLevel = (Spinner)findViewById(R.id.spinnerMotionDetectionAlarmLevel);
		String[] alarmLevels = getResources().getStringArray(R.array.device_setup_alarm_level);
		mDetectionAlarmLevels = alarmLevels;
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.right_spinner_item, alarmLevels);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDetectionLevel.setAdapter(adapter);
		mSpinnerDetectionLevel.setOnItemSelectedListener(this);
		
		mBtnSwitchBlock = (ImageButton)findViewById(R.id.btnSwitchVideoBlock);
		mBtnSwitchBlock.setOnClickListener(this);
		mBtnSwitchBlockRecord = (ImageButton)findViewById(R.id.btnSwitchVideoBlockAlarmRecord);
		mBtnSwitchBlockRecord.setOnClickListener(this);
		mBtnSwitchBlockCapture = (ImageButton)findViewById(R.id.btnSwitchVideoBlockAlarmCapture);
		mBtnSwitchBlockCapture.setOnClickListener(this);
		mBtnSwitchBlockPushMsg = (ImageButton)findViewById(R.id.btnSwitchVideoBlockAlarmPushMsg);
		mBtnSwitchBlockPushMsg.setOnClickListener(this);
		
		mBtnSwitchLocalIO = (ImageButton)findViewById(R.id.btnSwitchLocalIOAlarm);
		mBtnSwitchLocalIO.setOnClickListener(this);
		mBtnSwitchLocalIORecord = (ImageButton)findViewById(R.id.btnSwitchLocalIOAlarmRecord);
		mBtnSwitchLocalIORecord.setOnClickListener(this);
		mBtnSwitchLocalIOCapture = (ImageButton)findViewById(R.id.btnSwitchLocalIOAlarmCapture);
		mBtnSwitchLocalIOCapture.setOnClickListener(this);
		mBtnSwitchLocalIOPushMsg = (ImageButton)findViewById(R.id.btnSwitchLocalIOAlarmPushMsg);
		mBtnSwitchLocalIOPushMsg.setOnClickListener(this);
		
		mBtnSwitchAlarmOutStatus = (ImageButton) findViewById(R.id.btnSwitchAlarmOutStatus);

		mRlAlarmTime = findViewById(R.id.rl_alarm_time);
		mRlAlarmTime.setOnClickListener(this);

		mLsiAlarmInterval = findViewById(R.id.lsi_alarm_interval);
		mSbAlarmInterval = mLsiAlarmInterval.getExtraSeekbar();
		mSbAlarmInterval.setMax(1770);
		mSbAlarmInterval.setOnSeekBarChangeListener(this);
		mLsiAlarmInterval.setOnClickListener(this);

		//屏蔽该项设置，只能获取，不能设置
//		mBtnSwitchAlarmOutStatus.setOnClickListener(this);
		mSpinnerAlarmOutType = (Spinner) findViewById(R.id.spinnerAlarmOutType);
		String[] alarmouttypes = getResources().getStringArray(R.array.alarmouttype);
		ArrayAdapter<String> adapterofalarmout=new ArrayAdapter<String>(this, R.layout.right_spinner_item, alarmouttypes);
		adapterofalarmout.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerAlarmOutType.setAdapter(adapterofalarmout);
		mSpinnerAlarmOutType.setOnItemSelectedListener(this);
		
		mLayoutOthers = (LinearLayout)findViewById(R.id.layoutOthers);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mBtnSave = (ImageButton)setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);
		
		mFunDevice = funDevice;
		
		if ( mFunDevice instanceof FunDeviceSocket ) {
			// 插座类的设备报警
			DEV_CONFIGS = DEV_CONFIGS_FOR_SOCKET;
		} else {
			// 监控类的设备报警
			DEV_CONFIGS = DEV_CONFIGS_FOR_CHANNELS;
			
			//如果是单通道，设置以下配置
			if ((mFunDevice.channel != null) && (mFunDevice.channel.nChnCount == 1)) {
				findViewById(R.id.layoutAlarmInput).setVisibility(View.VISIBLE);
				DEV_CONFIGS = DEV_CONFIGS_FOR_CAMERA;
			}
		}
		
		// 如果是插座型的设备,隐藏多余的配置项
		if ( mFunDevice instanceof FunDeviceSocket ) {
			mLayoutOthers.setVisibility(View.GONE);
		}
		
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		// 获取报警配置信息
		tryGetAlarmConfig();

		alarmPeriodDlg = new AlarmPeriodDlg(this);
		alarmPeriodDlg.setFunDevice(funDevice);
	}
	

	@Override
	protected void onDestroy() {
		
		// 注销监听
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);
		
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
			
		case R.id.btnSwitchMotionDetection:				// 移动侦测
		case R.id.btnSwitchMotionDetectionAlarmRecord:
		case R.id.btnSwitchMotionDetectionAlarmCapture:
		case R.id.btnSwitchMotionDetectionAlarmPushMsg:
		case R.id.btnSwitchVideoBlock:					// 视频遮挡
		case R.id.btnSwitchVideoBlockAlarmRecord:
		case R.id.btnSwitchVideoBlockAlarmCapture:
		case R.id.btnSwitchVideoBlockAlarmPushMsg:
		case R.id.btnSwitchLocalIOAlarm:				// 报警输入
		case R.id.btnSwitchLocalIOAlarmRecord:
		case R.id.btnSwitchLocalIOAlarmCapture:
		case R.id.btnSwitchAlarmOutStatus:
		case R.id.btnSwitchLocalIOAlarmPushMsg:
			{
				v.setSelected(!v.isSelected());
			}
			break;
			
		case R.id.btnSave:	// 保存
			{
				trySaveAlarmConfig();
			}
			break;
		case R.id.rl_alarm_time:
			alarmPeriodDlg.show();
			break;
		case R.id.lsi_alarm_interval:
			mLsiAlarmInterval.toggleExtraView();
			break;
		}
	}
	
	/**
	 * Level报警灵敏度转换,界面低级/中级/高级(0,1,2),需要和实际级别做一个转换
	 * @param level
	 * @return
	 */
	private int changeLevelToUI(int level) {
		int uiLevel = (level == 0 ? 1 : (level % 2 + level / 2)) - 1;
		return Math.max(0, uiLevel);
	}
	
	private int changeLevelToDetect(int uiLevel) {
		return (uiLevel+1) * 2;
	}
	
	private void refreshAlarmConfig() {
		if ( mFunDevice instanceof FunDeviceSocket ) {
			// 插座类
			PowerSocketArm socketArm = ((FunDeviceSocket) mFunDevice).getPowerSocketArm();
			if ( null != socketArm ) {
				mBtnSwitchMotion.setSelected(socketArm.getGuard() > 0);
			}
		} else {
			// 监控类
			// 移动侦测
			DetectMotion detectMotion = (DetectMotion)mFunDevice.getConfig(DetectMotion.CONFIG_NAME);
			if ( null != detectMotion ) {
				mBtnSwitchMotion.setSelected(detectMotion.Enable);
				mBtnSwitchMotionRecord.setSelected(detectMotion.event.RecordEnable);
				mBtnSwitchMotionCapture.setSelected(detectMotion.event.SnapEnable);
				mBtnSwitchMotionPushMsg.setSelected(detectMotion.event.MessageEnable);
				mSpinnerDetectionLevel.setSelection(changeLevelToUI(detectMotion.Level));
			}
			
			// 视频遮挡
			DetectBlind detectBlind = (DetectBlind)mFunDevice.getConfig(DetectBlind.CONFIG_NAME);
			if ( null != detectBlind ) {
				mBtnSwitchBlock.setSelected(detectBlind.Enable);
				mBtnSwitchBlockRecord.setSelected(detectBlind.event.RecordEnable);
				mBtnSwitchBlockCapture.setSelected(detectBlind.event.SnapEnable);
				mBtnSwitchBlockPushMsg.setSelected(detectBlind.event.MessageEnable);
			}
			
			// 报警输入
			LocalAlarm localAlarm = (LocalAlarm)mFunDevice.getConfig(LocalAlarm.CONFIG_NAME);
			if ( null != localAlarm ) {
				mBtnSwitchLocalIO.setSelected(localAlarm.Enable);
				mBtnSwitchLocalIORecord.setSelected(localAlarm.event.RecordEnable);
				mBtnSwitchLocalIOCapture.setSelected(localAlarm.event.SnapEnable);
				mBtnSwitchLocalIOPushMsg.setSelected(localAlarm.event.MessageEnable);
			}
			
			// 报警输出
			AlarmOut alarmOut = (AlarmOut) mFunDevice.getConfig(AlarmOut.CONFIG_NAME);
			if (alarmOut != null && mFunDevice.CurrChannel < alarmOut.Alarms.size()) {
				AlarmOutInfo alarmOutInfo = alarmOut.Alarms.get(mFunDevice.CurrChannel);
				boolean b = alarmOutInfo.AlarmOutStatus.equals("OPEN");
				mBtnSwitchAlarmOutStatus.setSelected(b);
				mSpinnerAlarmOutType.setSelection(getPositionOfAlarmOutType(alarmOutInfo.AlarmOutType));
			}
		}
	}
	
	private int getPositionOfAlarmOutType(String string){
		if (string.equals("AUTO")) {
			return 0;
		}else if (string.equals("CLOSE")) {
			return 2;
		}else {
			return 1;
		}
	}
	
	private String getStringOfAlarmOutType(int i){
		switch (i) {
		case 0:
			return "AUTO";
		case 1:
			return "MANUAL";
		case 2:
			return "CLOSE";
		default:
			break;
		}
		return null;
	}
	
	private void tryGetAlarmConfig() {
		if ( null != mFunDevice ) {

			mUserId = FunSDK.GetId(mUserId,this);

			showWaitDialog();
			
			for ( String configName : DEV_CONFIGS ) {
				
				// 删除老的配置信息
				mFunDevice.invalidConfig(configName);
				
				// 重新搜索新的配置信息
				if (contains(DeviceConfigType.DeviceConfigCommon, configName)) {
					 FunSupport.getInstance().requestDeviceConfig(mFunDevice,
					 configName);
				}else if (contains(DeviceConfigType.DeviceConfigByChannel, configName)) {
					FunSupport.getInstance().requestDeviceConfig(mFunDevice, configName, mFunDevice.CurrChannel);
				}
			}

			//获取报警间隔
			FunSDK.DevCmdGeneral(mUserId, mFunDevice.getDevSn(), 1042,
					JsonConfig.CFG_PMS, -1, 5000, null, -1, 0);
		}
	}
	
	private boolean isCurrentUsefulConfig(String configName) {
		for ( int i = 0; i < DEV_CONFIGS.length; i ++ ) {
			if ( DEV_CONFIGS[i].equals(configName) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否所有需要的配置都获取到了
	 * @return
	 */
	private boolean isAllConfigGetted() {
		for ( String configName : DEV_CONFIGS ) {
			if ( null == mFunDevice.getConfig(configName) ) {
				return false;
			}
		}
		return true;
	}

	private void trySaveAlarmConfig() {
		if ( mFunDevice instanceof FunDeviceSocket ) {
			// 插座类
			boolean enable = mBtnSwitchMotion.isSelected();
			int nGuard = enable?1:0;
			
			PowerSocketArm socketArm = (PowerSocketArm)mFunDevice.checkConfig(PowerSocketArm.CONFIG_NAME);
			
			if ( nGuard != socketArm.getGuard() ) {
				socketArm.setGuard(enable?1:0);
				
				showWaitDialog();
				
				FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, socketArm);
			} else {
				showToast(R.string.device_alarm_no_change);
			}
		} else {
			// 监控类
			boolean beMotionChanged = false;
			boolean beBlockChanged = false;
			boolean beLocalIOChanged = false;
			boolean beAlarmOutChanged = false;
			
			DetectMotion detectMotion = (DetectMotion)mFunDevice.getConfig(DetectMotion.CONFIG_NAME);
			if ( null != detectMotion ) {
				if ( mBtnSwitchMotion.isSelected() != detectMotion.Enable ) {
					detectMotion.Enable = mBtnSwitchMotion.isSelected();
					beMotionChanged = true;
				}
				
				if ( mBtnSwitchMotionRecord.isSelected() != detectMotion.event.RecordEnable ) {
					detectMotion.event.RecordEnable = mBtnSwitchMotionRecord.isSelected();
					detectMotion.event.RecordMask = DevSDK.SetSelectHex(
							detectMotion.event.RecordMask, mFunDevice.CurrChannel,
							detectMotion.event.RecordEnable);
					beMotionChanged = true;
				}
				
				if ( mBtnSwitchMotionCapture.isSelected() != detectMotion.event.SnapEnable ) {
					detectMotion.event.SnapEnable = mBtnSwitchMotionCapture.isSelected();
					detectMotion.event.SnapShotMask = DevSDK.SetSelectHex(
							detectMotion.event.SnapShotMask, mFunDevice.CurrChannel,
							detectMotion.event.SnapEnable);
					beMotionChanged = true;
				}
				
				if ( mBtnSwitchMotionPushMsg.isSelected() != detectMotion.event.MessageEnable ) {
					detectMotion.event.MessageEnable = mBtnSwitchMotionPushMsg.isSelected();
					beMotionChanged = true;
				}
				
				if ( mSpinnerDetectionLevel.getSelectedItemPosition()
						!= changeLevelToUI(detectMotion.Level) ) {
					detectMotion.Level = changeLevelToDetect(
							mSpinnerDetectionLevel.getSelectedItemPosition());
					beMotionChanged = true;
				}

				if (mNetworkPms != null) {
					beMotionChanged = true;
					FunSDK.DevCmdGeneral(mUserId, mFunDevice.getDevSn(), 1040, JsonConfig.CFG_PMS, -1, 5000,
							HandleConfigData.getSendData(JsonConfig.CFG_PMS,"0x08", mNetworkPms).getBytes(), -1, 0);
				}
			}
			
			DetectBlind detectBlind = (DetectBlind)mFunDevice.getConfig(DetectBlind.CONFIG_NAME);
			if ( null != detectBlind ) {
				if ( mBtnSwitchBlock.isSelected() != detectBlind.Enable ) {
					detectBlind.Enable = mBtnSwitchBlock.isSelected();
					beBlockChanged = true;
				}
				
				if ( mBtnSwitchBlockRecord.isSelected() != detectBlind.event.RecordEnable ) {
					detectBlind.event.RecordEnable = mBtnSwitchBlockRecord.isSelected();
					beBlockChanged = true;
				}
				
				if ( mBtnSwitchBlockCapture.isSelected() != detectBlind.event.SnapEnable ) {
					detectBlind.event.SnapEnable = mBtnSwitchBlockCapture.isSelected();
					beBlockChanged = true;
				}
				
				if ( mBtnSwitchBlockPushMsg.isSelected() != detectBlind.event.MessageEnable ) {
					detectBlind.event.MessageEnable = mBtnSwitchBlockPushMsg.isSelected();
					beBlockChanged = true;
				}
			}
			
			LocalAlarm localAlarm = (LocalAlarm)mFunDevice.getConfig(LocalAlarm.CONFIG_NAME);
			if ( null != localAlarm ) {
				if ( mBtnSwitchLocalIO.isSelected() != localAlarm.Enable ) {
					localAlarm.Enable = mBtnSwitchLocalIO.isSelected();
					beLocalIOChanged = true;
				}
				
				if ( mBtnSwitchLocalIORecord.isSelected() != localAlarm.event.RecordEnable ) {
					localAlarm.event.RecordEnable = mBtnSwitchLocalIORecord.isSelected();
					localAlarm.event.RecordMask = DevSDK.SetSelectHex(
							localAlarm.event.RecordMask, mFunDevice.CurrChannel,
							localAlarm.event.RecordEnable);
					beLocalIOChanged = true;
				}
				
				if ( mBtnSwitchLocalIOCapture.isSelected() != localAlarm.event.SnapEnable ) {
					localAlarm.event.SnapEnable = mBtnSwitchLocalIOCapture.isSelected();
					localAlarm.event.SnapShotMask = DevSDK.SetSelectHex(localAlarm.event.SnapShotMask, mFunDevice.CurrChannel, 
							localAlarm.event.SnapEnable);
					beLocalIOChanged = true;
				}
				
				if ( mBtnSwitchLocalIOPushMsg.isSelected() != localAlarm.event.MessageEnable ) {
					localAlarm.event.MessageEnable = mBtnSwitchLocalIOPushMsg.isSelected();
					beLocalIOChanged = true;
				}
			}
			
			AlarmOut out = (AlarmOut) mFunDevice.getConfig(AlarmOut.CONFIG_NAME);
			if (out != null && mFunDevice.CurrChannel >= 0 && mFunDevice.CurrChannel < out.Alarms.size()) {
				AlarmOutInfo info = out.Alarms.get(mFunDevice.CurrChannel);
				info.AlarmOutStatus = mBtnSwitchAlarmOutStatus.isSelected() ? "OPEN" : "CLOSE";
				info.AlarmOutType = getStringOfAlarmOutType(mSpinnerAlarmOutType.getSelectedItemPosition());
				out.Alarms.remove(mFunDevice.CurrChannel);
				out.Alarms.add(mFunDevice.CurrChannel, info);
				beAlarmOutChanged = true;
			}
			mSettingConfigs.clear();
			
			if ( beBlockChanged 
					|| beMotionChanged
					|| beLocalIOChanged 
					|| beAlarmOutChanged) {
				showWaitDialog();
				
				if ( beMotionChanged ) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(detectMotion.getConfigName());
					}
					
					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, detectMotion);
				}
				
				if ( beBlockChanged ) {
					
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(detectBlind.getConfigName());
					}
					
					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, detectBlind);
				}
				
				if ( beLocalIOChanged ) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(localAlarm.getConfigName());
					}
					
					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, localAlarm);
				}
				if (beAlarmOutChanged) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(out.getConfigName());
					}
					
					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, out);
				}
			} else {
				showToast(R.string.device_alarm_no_change);
			}
		}
	}
	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		
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
		if ( null != mFunDevice 
				&& funDevice.getId() == mFunDevice.getId()
				&& isCurrentUsefulConfig(configName) ) {
			if ( isAllConfigGetted() ) {
				hideWaitDialog();
			}
			
			refreshAlarmConfig();
		}
	}


	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceSetConfigSuccess(final FunDevice funDevice, 
			final String configName) {
		if ( null != mFunDevice 
				&& funDevice.getId() == mFunDevice.getId() ) {
			synchronized (mSettingConfigs) {
				if ( mSettingConfigs.contains(configName) ) {
					mSettingConfigs.remove(configName);
				}
				
				if ( mSettingConfigs.size() == 0 ) {
					// 所有的设置修改都已经完成
					hideWaitDialog();
				}
			}
			
			refreshAlarmConfig();
		}
	}


	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}

	@Override
	public void onDeviceChangeInfoSuccess(final FunDevice funDevice) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceChangeInfoFailed(final FunDevice funDevice, final Integer errCode) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceOptionSuccess(final FunDevice funDevice, final String option) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceOptionFailed(final FunDevice funDevice, final String option, final Integer errCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {

    }


	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_CODE_SETTING_ALARM_TIME:
				if (resultCode == RESULT_OK) {
					TimeItem timeInfo = (TimeItem) data.getSerializableExtra("timeInfo");
					int position = data.getIntExtra("mPosition", -1);
					timeInfo.setTimeSection(position + 1, alarmPeriodDlg.mAlarmInfo.EventHandler.TimeSection);
					alarmPeriodDlg.update(position, timeInfo);
				}
				break;
		}
	}

	@Override
	public int OnFunSDKResult(Message message, MsgContent msgContent) {
		if (message.what == EUIMSG.DEV_CMD_EN) {
			if (JsonConfig.CFG_PMS.equals(msgContent.str)) {
				if (msgContent.pData != null) {
					HandleConfigData configData = new HandleConfigData();
					if (configData.getDataObj(G.ToString(msgContent.pData), NetworkPmsBean.class)) {
						mNetworkPms = (NetworkPmsBean) configData.getObj();
						if (mNetworkPms != null) {
							mSbAlarmInterval.setProgress(mNetworkPms.PushInterval);
							mLsiAlarmInterval.setRightText(mNetworkPms.PushInterval + getString(R.string.second));
						}
					}
				} else {
					Toast.makeText(this, R.string.get_config_f, Toast.LENGTH_LONG).show();
				}
			}
		}
		return 0;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		if (fromUser) {
			if (seekBar == mSbAlarmInterval) {
				mNetworkPms.PushInterval = progress;
				mLsiAlarmInterval.setRightText(mNetworkPms.PushInterval + getString(R.string.second));
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
