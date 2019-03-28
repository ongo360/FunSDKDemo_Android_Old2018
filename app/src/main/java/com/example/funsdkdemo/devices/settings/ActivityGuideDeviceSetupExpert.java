package com.example.funsdkdemo.devices.settings;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.example.common.DeviceConfigType;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.CameraClearFog;
import com.lib.funsdk.support.config.CameraParam;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.MyUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;


public class ActivityGuideDeviceSetupExpert extends ActivityDemo implements OnClickListener, OnFunDeviceOptListener, OnItemSelectedListener {

	
	private TextView mTextTitle = null;
	
	private ImageButton mBtnBack = null;
	
	private Spinner mSpinnerExposureTime = null;
	private Spinner mSpinnerSceneMode = null;
	private Spinner mSpinnerElectronicSlowShutter =null;
	private Spinner mSpinnerColorMode = null;
	private Spinner mSpinnerDefogging = null;

	private ImageButton mBtnSave = null;
	
	private FunDevice mFunDevice = null;
	
	/**
	 * 本界面需要获取到的设备配置信息
	 */
	private final String[] DEV_CONFIGS = {

			CameraParam.CONFIG_NAME,

			//CameraParamEx.CONFIG_NAME,

			CameraClearFog.CONFIG_NAME
	};
	
	// 设置配置信息的时候,由于有多个,通过下面的列表来判断是否所有的配置都设置完成了
	private List<String> mSettingConfigs = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_setup_expert);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_setup_expert);
		
		mSpinnerExposureTime = (Spinner)findViewById(R.id.setupExposureTimeSpinner);
		String[] exposureTime = getResources().getStringArray(R.array.device_setup_exposure_time_values);
		ArrayAdapter<String> adapterExposureTime = new ArrayAdapter<String>(this, R.layout.right_spinner_item, exposureTime);
		adapterExposureTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerExposureTime.setAdapter(adapterExposureTime);
		Integer[] defValues1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		mSpinnerExposureTime.setTag(defValues1);
		mSpinnerExposureTime.setOnItemSelectedListener(this);
		
		mSpinnerSceneMode = (Spinner)findViewById(R.id.setupSceneModeSpinner);
		String[] scenceMode = getResources().getStringArray(R.array.device_setup_scene_mode_values);
		ArrayAdapter<String> adapterSceneMode = new ArrayAdapter<String>(this, R.layout.right_spinner_item, scenceMode);
		adapterSceneMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerSceneMode.setAdapter(adapterSceneMode);
		Integer[] defValues2 = { 0, 1, 2 };
		mSpinnerSceneMode.setTag(defValues2);
		mSpinnerSceneMode.setOnItemSelectedListener(this);
		
		mSpinnerElectronicSlowShutter = (Spinner)findViewById(R.id.setupElectronicSlowShutterSpinner);
		String[] electronicSlowShutter = getResources().getStringArray(R.array.device_setup_electronic_slow_shutter_values);
		ArrayAdapter<String> adapterElectronicSlowShutter = new ArrayAdapter<String>(this, R.layout.right_spinner_item, electronicSlowShutter);
		adapterElectronicSlowShutter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerElectronicSlowShutter.setAdapter(adapterElectronicSlowShutter);
		Integer[] defValues3 = { 0, 1, 2 };
		mSpinnerElectronicSlowShutter.setTag(defValues3);
		mSpinnerElectronicSlowShutter.setOnItemSelectedListener(this);
		
		mSpinnerColorMode = (Spinner)findViewById(R.id.setupColorModeSpinner);
		String[] colorMode = getResources().getStringArray(R.array.device_setup_color_mode_values);
		ArrayAdapter<String> adapterColorMode = new ArrayAdapter<String>(this, R.layout.right_spinner_item, colorMode);
		adapterColorMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerColorMode.setAdapter(adapterColorMode);
		Integer[] defValues4 = { 1, 2 };
		mSpinnerColorMode.setTag(defValues4);
		mSpinnerColorMode.setOnItemSelectedListener(this);
		
		mSpinnerDefogging = (Spinner)findViewById(R.id.setupDefoggingSpinner);
		String[] defogging = getResources().getStringArray(R.array.device_setup_defogging_values);
		ArrayAdapter<String> adapterDefogging = new ArrayAdapter<String>(this, R.layout.right_spinner_item, defogging);
		adapterDefogging.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDefogging.setAdapter(adapterDefogging);
		Integer[] defValues5 = { 0, 20, 50, 80 };
		mSpinnerDefogging.setTag(defValues5);
		mSpinnerDefogging.setOnItemSelectedListener(this);
		

		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mBtnSave = (ImageButton)setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);
		
		mFunDevice = funDevice;
		
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		// 获取报警配置信息
		tryGetExpertConfig();
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
			
		case R.id.btnSave:	// 保存
			{
				trySaveExpertConfig();
			}
			break;
		}
	}
	
	private int getSpinnerValue(Spinner spinner) {
		Integer[] values = (Integer[])spinner.getTag();
		int position = spinner.getSelectedItemPosition();
		if ( position >= 0 && position < values.length ) {
			return values[position];
		}
		return 0;
	}
	
	private int getSpinnerPosition(Spinner spinner, int value) {
		Integer[] values = (Integer[])spinner.getTag();
		for ( int i = 0; i < values.length; i ++ ) {
			if ( values[i] == value ) {
				return i;
			}
		}
		return 0;
	}
	
	private boolean isCurrentUsefulConfig(String configName) {
		for ( int i = 0; i < DEV_CONFIGS.length; i ++ ) {
			if ( DEV_CONFIGS[i].equals(configName) ) {
				return true;
			}
		}
		return false;
	}

	private int getDefoggingValue(CameraClearFog cfg) {
		if (null != cfg) {
			if (cfg.getClearFogEnable() == 0)
			{
				return 0;
			}
			else if (cfg.getClearFogLevel() <= 30)
			{
				return 20;
			}
			else if (cfg.getClearFogLevel() > 30 && cfg.getClearFogLevel() < 70)
			{
				return 50;
			}
			else if (cfg.getClearFogLevel() > 70)
			{
				return 80;
			}
		}
		return 0;
	}

	private int getEsShutterValue(CameraParam cfg)
	{
		int selectValue = 0;
		int esShutter = 0;
		
		if(cfg == null)
		{
			return 0;
		}
		esShutter = MyUtils.getIntFromHex(cfg.EsShutter);
		if(esShutter <= 0)
		{
			selectValue = 0;
		}
		else if(esShutter <= 3)
		{
			selectValue = 1;
		}
		else
		{
			selectValue = 2;
		}
		return selectValue;
	}
	
	private void refreshExpertConfig() {

		CameraParam cameraParam = (CameraParam)mFunDevice.getConfig(CameraParam.CONFIG_NAME);
		if ( null != cameraParam ) {

			mSpinnerExposureTime.setSelection(getSpinnerPosition(mSpinnerExposureTime, cameraParam.exposureParam.Level));
			int selectValue = 0;
			selectValue = getEsShutterValue(cameraParam);
			mSpinnerElectronicSlowShutter.setSelection(getSpinnerPosition(mSpinnerElectronicSlowShutter, selectValue));
			mSpinnerColorMode.setSelection(getSpinnerPosition(mSpinnerColorMode, MyUtils.getIntFromHex(cameraParam.DayNightColor)));
			mSpinnerSceneMode.setSelection(getSpinnerPosition(mSpinnerSceneMode, MyUtils.getIntFromHex(cameraParam.WhiteBalance)));
		}
		
		CameraClearFog cameraClearFog = (CameraClearFog)mFunDevice.getConfig(CameraClearFog.CONFIG_NAME);
		if ( null != cameraClearFog ) {

			mSpinnerDefogging.setSelection(getSpinnerPosition(mSpinnerDefogging, getDefoggingValue(cameraClearFog)));
		}
	}
	
	private void tryGetExpertConfig() {
		if ( null != mFunDevice ) {
			
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
		}
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

	private void trySaveExpertConfig() {

		boolean beCameraParamChanged = false;
		CameraParam cameraParam = (CameraParam)mFunDevice.getConfig(CameraParam.CONFIG_NAME);
		if ( null != cameraParam )
		{
			int value = getSpinnerValue(mSpinnerExposureTime);
			if(value != cameraParam.exposureParam.Level)
			{
				cameraParam.exposureParam.Level = value;
				beCameraParamChanged = true;
			}

			value = getSpinnerValue(mSpinnerSceneMode);
			if(!MyUtils.getHexFromInt(value).equals(cameraParam.WhiteBalance))
			{
				cameraParam.WhiteBalance = MyUtils.getHexFromInt(value);
				beCameraParamChanged = true;
			}
			
			value = getSpinnerValue(mSpinnerColorMode);
			if(!MyUtils.getHexFromInt(value).equals(cameraParam.DayNightColor))
			{
				cameraParam.DayNightColor = MyUtils.getHexFromInt(value);
				beCameraParamChanged = true;
			}
			
			value = getSpinnerValue(mSpinnerElectronicSlowShutter);
			if(value != getEsShutterValue(cameraParam))
			{
				cameraParam.EsShutter = MyUtils.getHexFromInt(value * 3);
				beCameraParamChanged = true;
			}
		}
		
		boolean beCameraClearFogChanged = false;
		CameraClearFog cameraClearFog = (CameraClearFog)mFunDevice.getConfig(CameraClearFog.CONFIG_NAME);
		if ( null != cameraClearFog )
		{
			int value = getSpinnerValue(mSpinnerDefogging);
			if(value != getDefoggingValue(cameraClearFog))
			{
				if(value > 0)
				{
					cameraClearFog.enable = 1;
					cameraClearFog.level = value;
				}
				else
				{
					cameraClearFog.enable = 0;
				}
				beCameraClearFogChanged = true;
			}
		}

		if ( beCameraParamChanged 
				|| beCameraClearFogChanged ) {
			showWaitDialog();

			if ( beCameraParamChanged ) {
				synchronized (mSettingConfigs) {
					mSettingConfigs.add(cameraParam.getConfigName());
				}
				
				FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, cameraParam);
			}
			
			if ( beCameraClearFogChanged ) {
				synchronized (mSettingConfigs) {
					mSettingConfigs.add(cameraClearFog.getConfigName());
				}
				
				FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, cameraClearFog);
			}
		} else {
			showToast(R.string.device_alarm_no_change);
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
			
			refreshExpertConfig();
		}
	}


	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
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
					hideWaitDialog();
				}
			}
			
			//refreshRecordConfig();
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

}
