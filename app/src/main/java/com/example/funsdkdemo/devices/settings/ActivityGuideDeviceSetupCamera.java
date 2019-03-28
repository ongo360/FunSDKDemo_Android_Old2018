package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.basic.G;
import com.example.common.DeviceConfigType;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.AVEncVideoWidget;
import com.lib.funsdk.support.config.CameraParam;
import com.lib.funsdk.support.config.CameraParamEx;
import com.lib.funsdk.support.config.ChannelTitle;
import com.lib.funsdk.support.config.FVideoOsdLogo;
import com.lib.funsdk.support.config.PowerSocketImage;
import com.lib.funsdk.support.config.SimplifyEncode;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunDeviceSocket;
import com.lib.funsdk.support.utils.MyUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.SDK_TitleDot;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo: 图像配置
 *
 */
public class ActivityGuideDeviceSetupCamera extends ActivityDemo
		implements OnClickListener, OnFunDeviceOptListener, OnItemSelectedListener {

	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;

	// 清晰度
	private Spinner mSpinnerDefinition = null;
	// 上下翻转
	private ImageButton mBtnSwitchFlip = null;
	// 左右翻转
	private ImageButton mBtnSwitchMirror = null;
	// OSD水印开关
	private ImageButton mBtnSwitchOSD = null;
	// OSD时间开关
	private ImageButton mBtnTimeSwitchOSD = null;
	// OSD(水印内容)
	private EditText mEditOSDContent = null;
	// 背光补偿
	private ImageButton mBtnSwitchBLCMode = null;
	// 宽动态
	private ImageButton mBtnSwitchWideDynamic = null;
	// 降噪
	private Spinner mSpinnerNoiseReduction = null;
	// 电子防抖
	private ImageButton mBtnSwitchAntiShake = null;
	// 测光模式
	private Spinner mSpinnerMetering = null;

	private LinearLayout mLayoutOthers = null;
	private LinearLayout mLayoutOthera = null;

	private ImageButton mBtnSave = null;

	private FunDevice mFunDevice = null;
	private int mFlip;
	/**
	 * 本界面需要的获取的设备配置信息（插座类）
	 * 
	 */
	private final String[] DEV_CONFIGS_FOR_SOCKET = { PowerSocketImage.CONFIG_NAME, };

	/**
	 * 本界面需要获取到的设备配置信息（监控类）
	 */
	private final String[] DEV_CONFIGS_FOR_CAMERA = {
			// 获取参数:SimplifyEncode -> 清晰度
			SimplifyEncode.CONFIG_NAME,

			// 获取参数:FVideoOsdLogo 
			FVideoOsdLogo.CONFIG_NAME,

			// 获取参数:CameraParam -> 图像上下翻转/图像左右翻转/背光补偿/降噪
			CameraParam.CONFIG_NAME,

			// 获取参数:CameraParamEx -> 电子防抖/测光模式/宽动态

			CameraParamEx.CONFIG_NAME,

			// OSD水印内容
//			 ChannelTitle.CONFIG_NAME,
			// ChannelTitle
			AVEncVideoWidget.CONFIG_NAME };
	private String[] DEV_CONFIGS = null;
	// 设置配置信息的时候,由于有多个,通过下面的列表来判断是否所有的配置都设置完成了
	private List<String> mSettingConfigs = new ArrayList<String>();
	
	private ChannelTitle mSetChannelTitle = null;
	private SDK_TitleDot mTitleDot = null;
	private TextView mTvOSD = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_device_setup_camera);

		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);

		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);

		mTextTitle.setText(R.string.device_setup_image);

		mSpinnerDefinition = (Spinner) findViewById(R.id.spinnerCameraDefinition);
		String[] definitions = getResources().getStringArray(R.array.device_setup_camera_definition_values);
		ArrayAdapter<String> adapterDefinition = new ArrayAdapter<String>(this, R.layout.right_spinner_item,
				definitions);
		adapterDefinition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDefinition.setAdapter(adapterDefinition);
		Integer[] defValues = { 6, 5, 4, 3, 2, 1 };
		mSpinnerDefinition.setTag(defValues);
		mSpinnerDefinition.setOnItemSelectedListener(this);

		mBtnSwitchFlip = (ImageButton) findViewById(R.id.btnSwitchCameraFlip);
		mBtnSwitchFlip.setOnClickListener(this);
		mBtnSwitchMirror = (ImageButton) findViewById(R.id.btnSwitchCameraMirror);
		mBtnSwitchMirror.setOnClickListener(this);
		mBtnSwitchOSD = (ImageButton) findViewById(R.id.btnSwitchCameraOSD);
		mBtnSwitchOSD.setOnClickListener(this);
		mBtnTimeSwitchOSD = (ImageButton) findViewById(R.id.btntimeSwitchCameraOSD);
		mBtnTimeSwitchOSD.setOnClickListener(this);
		mEditOSDContent = (EditText) findViewById(R.id.editCameraOSDContent);
		mBtnSwitchBLCMode = (ImageButton) findViewById(R.id.btnSwitchCameraBLCMode);
		mBtnSwitchBLCMode.setOnClickListener(this);
		mBtnSwitchWideDynamic = (ImageButton) findViewById(R.id.btnSwitchCameraWideDynamic);
		mBtnSwitchWideDynamic.setOnClickListener(this);
		
		mTvOSD = (TextView)findViewById(R.id.osd_tv);

		mSpinnerNoiseReduction = (Spinner) findViewById(R.id.spinnerCameraNoiseReduction);
		String[] noiseReds = getResources().getStringArray(R.array.device_setup_camera_noise_reduction_values);
		ArrayAdapter<String> adapterNoise = new ArrayAdapter<String>(this, R.layout.right_spinner_item, noiseReds);
		adapterNoise.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerNoiseReduction.setAdapter(adapterNoise);
		Integer[] noiseValues = { 0, 2, 4 };
		mSpinnerNoiseReduction.setTag(noiseValues);
		mSpinnerNoiseReduction.setOnItemSelectedListener(this);

		mBtnSwitchAntiShake = (ImageButton) findViewById(R.id.btnSwitchCameraAntiShake);
		mBtnSwitchAntiShake.setOnClickListener(this);

		mSpinnerMetering = (Spinner) findViewById(R.id.spinnerCameraMetering);
		String[] meterings = getResources().getStringArray(R.array.device_setup_camera_metering_values);
		ArrayAdapter<String> adapterMetering = new ArrayAdapter<String>(this, R.layout.right_spinner_item, meterings);
		adapterMetering.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerMetering.setAdapter(adapterMetering);
		Integer[] meteringValues = { 0, 1, 2 };
		mSpinnerMetering.setTag(meteringValues);
		mSpinnerMetering.setOnItemSelectedListener(this);
		mLayoutOthera = (LinearLayout) findViewById(R.id.layoutOther);
		mLayoutOthers = (LinearLayout) findViewById(R.id.layoutOthers);
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);
		if (null == mFunDevice) {
			finish();
			return;
		}
		mBtnSave = (ImageButton) setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);
		if (mFunDevice instanceof FunDeviceSocket) {
			// 插座类的设备报警
			DEV_CONFIGS = DEV_CONFIGS_FOR_SOCKET;
		} else {
			// 监控类的设备报警
			DEV_CONFIGS = DEV_CONFIGS_FOR_CAMERA;
		}
		// 如果是插座型的设备,隐藏多余的配置项
		if (mFunDevice instanceof FunDeviceSocket) {
			mLayoutOthers.setVisibility(View.GONE);
			mLayoutOthera.setVisibility(View.GONE);
		}
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);

		// 获取报警配置信息
		tryGetCameraConfig();
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

		case R.id.btnSwitchCameraFlip:
		case R.id.btnSwitchCameraMirror:
		case R.id.btnSwitchCameraOSD:
		case R.id.btntimeSwitchCameraOSD:
		case R.id.btnSwitchCameraAntiShake:
		case R.id.btnSwitchCameraBLCMode:
		case R.id.btnSwitchCameraWideDynamic: {
			v.setSelected(!v.isSelected());
		}
			break;

		case R.id.btnSave: // 保存
		{
			trySaveCameraConfig();
		}
			break;
		}
	}

	private int getSpinnerValue(Spinner spinner) {
		Integer[] values = (Integer[]) spinner.getTag();
		int position = spinner.getSelectedItemPosition();
		if (position >= 0 && position < values.length) {
			return values[position];
		}
		return 0;
	}
	
	private int getSpinnerPosition(Spinner spinner, int value) {
		Integer[] values = (Integer[]) spinner.getTag();
		for (int i = 0; i < values.length; i++) {
			if (values[i] == value) {
				return i;
			}
		}
		return 0;
	}

	private boolean isCurrentUsefulConfig(String configName) {
		for (int i = 0; i < DEV_CONFIGS.length; i++) {
			if (DEV_CONFIGS[i].equals(configName)) {
				return true;
			}
		}
		return false;
	}

	private void refreshCameraConfig() {
		if (mFunDevice instanceof FunDeviceSocket) {
			PowerSocketImage mSocketImage = ((FunDeviceSocket) mFunDevice).getPowerSocketImage();
			if (null != mSocketImage) {
				mBtnSwitchFlip.setSelected(mSocketImage.getFlip() > 0);
			}

		} else {
			SimplifyEncode simplifyEncode = (SimplifyEncode) mFunDevice.getConfig(SimplifyEncode.CONFIG_NAME);
			if (null != simplifyEncode) {
				// 清晰度
				mSpinnerDefinition
						.setSelection(getSpinnerPosition(mSpinnerDefinition, simplifyEncode.mainFormat.video.Quality));
			}

			CameraParam cameraParam = (CameraParam) mFunDevice.getConfig(CameraParam.CONFIG_NAME);
			if (null != cameraParam) {
				// 图像上下翻转
				mBtnSwitchFlip.setSelected(cameraParam.getPictureFlip());

				// 图像左右翻转
				mBtnSwitchMirror.setSelected(cameraParam.getPictureMirror());

				// 背光补偿
				mBtnSwitchBLCMode.setSelected(cameraParam.getBLCMode());

				// 降噪
				mSpinnerNoiseReduction
						.setSelection(getSpinnerPosition(mSpinnerNoiseReduction, cameraParam.Night_nfLevel / 2 * 2));
			}

			CameraParamEx cameraParamEx = (CameraParamEx) mFunDevice.getConfig(CameraParamEx.CONFIG_NAME);
			if (null != cameraParamEx) {
				// 电子防抖
				mBtnSwitchAntiShake.setSelected(cameraParamEx.getDis());

				// 测光模式
				mSpinnerMetering.setSelection(getSpinnerPosition(mSpinnerMetering, cameraParamEx.AeMeansure));

				// 宽动态
				mBtnSwitchWideDynamic.setSelected(cameraParamEx.getWideDynamic());
			}

			FVideoOsdLogo fVideoOsd = (FVideoOsdLogo) mFunDevice.getConfig(FVideoOsdLogo.CONFIG_NAME);
			AVEncVideoWidget avEnc = (AVEncVideoWidget) mFunDevice.getConfig(AVEncVideoWidget.CONFIG_NAME);
			if (null != avEnc) {
				// 水印开关
				mBtnSwitchOSD.setSelected(avEnc.channelTitleAttribute.EncodeBlend);
				// 时间开关
				mBtnTimeSwitchOSD.setSelected(avEnc.timeTitleAttribute.EncodeBlend);
			}

			if (null != avEnc) {
				mEditOSDContent.setText(avEnc.getChannelTitle());
//				setOSDName(avEnc.getChannelTitle());
			}
		}
	}

	private void tryGetCameraConfig() {
		if (null != mFunDevice) {

			showWaitDialog();

			for (String configName : DEV_CONFIGS) {

				// 删除老的配置信息
				mFunDevice.invalidConfig(configName);

				//根据是否需要传通道号 重新搜索新的配置信息
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
	 * 
	 * @return
	 */
	private boolean isAllConfigGetted() {
		for (String configName : DEV_CONFIGS) {
			if (null == mFunDevice.getConfig(configName)) {
				return false;
			}
		}
		return true;
	}

	private void trySaveCameraConfig() {
		mSetChannelTitle = null;
		
		if (mFunDevice instanceof FunDeviceSocket) {
			boolean enable = mBtnSwitchFlip.isSelected();
			mFlip = enable ? 1 : 0;

			PowerSocketImage mSocketImage = (PowerSocketImage) mFunDevice.checkConfig(PowerSocketImage.CONFIG_NAME);

			if (mFlip != mSocketImage.getFlip()) {
				mSocketImage.setFlip(enable ? 1 : 0);

				showWaitDialog();

				FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, mSocketImage);
			}
		} else {
			boolean beSimplifyEncodeChanged = false;
			SimplifyEncode simplifyEncode = (SimplifyEncode) mFunDevice.getConfig(SimplifyEncode.CONFIG_NAME);
			if (null != simplifyEncode) {
				// 清晰度
				if (simplifyEncode.mainFormat.video.Quality != getSpinnerValue(mSpinnerDefinition)) {
					simplifyEncode.mainFormat.video.Quality = getSpinnerValue(mSpinnerDefinition);
					beSimplifyEncodeChanged = true;
				}
			}

			boolean beCameraParamChanged = false;
			CameraParam cameraParam = (CameraParam) mFunDevice.getConfig(CameraParam.CONFIG_NAME);
			if (null != cameraParam) {
				// 图像上下翻转
				if (mBtnSwitchFlip.isSelected() != cameraParam.getPictureFlip()) {
					cameraParam.setPictureFlip(mBtnSwitchFlip.isSelected());
					beCameraParamChanged = true;
				}

				// 图像左右翻转
				if (mBtnSwitchMirror.isSelected() != cameraParam.getPictureMirror()) {
					cameraParam.setPictureMirror(mBtnSwitchMirror.isSelected());
					beCameraParamChanged = true;
				}

				// 背光补偿
				if (mBtnSwitchBLCMode.isSelected() != cameraParam.getBLCMode()) {
					cameraParam.setBLCMode(mBtnSwitchBLCMode.isSelected());
					beCameraParamChanged = true;
				}

				// 降噪
				if (cameraParam.Night_nfLevel != getSpinnerValue(mSpinnerNoiseReduction)) {
					cameraParam.Night_nfLevel = getSpinnerValue(mSpinnerNoiseReduction);
					beCameraParamChanged = true;
				}

			}

			boolean beCameraParamExChanged = false;
			CameraParamEx cameraParamEx = (CameraParamEx) mFunDevice.getConfig(CameraParamEx.CONFIG_NAME);
			if (null != cameraParamEx) {
				// 电子防抖
				if (mBtnSwitchAntiShake.isSelected() != cameraParamEx.getDis()) {
					cameraParamEx.setDis(mBtnSwitchAntiShake.isSelected());
					beCameraParamExChanged = true;
				}

				// 测光模式
				if (cameraParamEx.AeMeansure != getSpinnerValue(mSpinnerMetering)) {
					cameraParamEx.AeMeansure = getSpinnerValue(mSpinnerMetering);
					beCameraParamExChanged = true;
				}

				// 宽动态
				if (mBtnSwitchWideDynamic.isSelected() != cameraParamEx.getWideDynamic()) {
					cameraParamEx.setWideDynamic(mBtnSwitchWideDynamic.isSelected());
					beCameraParamExChanged = true;
				}
			}

			boolean beFVideoOsdLogoChanged = false;
			FVideoOsdLogo fVideoOsd = (FVideoOsdLogo) mFunDevice.getConfig(FVideoOsdLogo.CONFIG_NAME);
			if (null != fVideoOsd) {
				
			}

			AVEncVideoWidget avEnc = (AVEncVideoWidget) mFunDevice.getConfig(AVEncVideoWidget.CONFIG_NAME);
			boolean beChannelTitleChanged = false;
			if (null != avEnc) {
				//Switch of OSD
				if (mBtnSwitchOSD.isSelected() != avEnc.channelTitleAttribute.EncodeBlend) {
					avEnc.channelTitleAttribute.EncodeBlend = mBtnSwitchOSD.isSelected();
					beChannelTitleChanged = true;
				}
				// switch of time 
				if (mBtnTimeSwitchOSD.isSelected() != avEnc.timeTitleAttribute.EncodeBlend) {
					avEnc.timeTitleAttribute.EncodeBlend = mBtnTimeSwitchOSD.isSelected();
					beChannelTitleChanged = true;
				}
				//text of OSD
				String osdContent = mEditOSDContent.getText().toString().trim();
				if (!osdContent.equals(avEnc.getChannelTitle())) {
					avEnc.setChannelTitle(osdContent);
					setOSDName(osdContent);
					beChannelTitleChanged = true;
				}
			}

			mSettingConfigs.clear();

			if (beSimplifyEncodeChanged || beCameraParamChanged || beCameraParamExChanged || beFVideoOsdLogoChanged
					|| beChannelTitleChanged) {
				showWaitDialog();

				// 保存SimplifyEncode
				if (beSimplifyEncodeChanged) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(simplifyEncode.getConfigName());
					}

					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, simplifyEncode);
				}

				// 保存CameraParam
				if (beCameraParamChanged) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(cameraParam.getConfigName());
					}

					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, cameraParam);
				}

				// 保存CameraParamEx
				if (beCameraParamExChanged) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(cameraParamEx.getConfigName());
					}

					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, cameraParamEx);
				}

				// 保存FVideoOsdLogo
				if (beFVideoOsdLogoChanged) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(fVideoOsd.getConfigName());
					}

					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, fVideoOsd);
				}

				// 保存ChannelTitle
				if (beChannelTitleChanged) {
					synchronized (mSettingConfigs) {
						mSettingConfigs.add(avEnc.getConfigName());
						
						mSetChannelTitle = new ChannelTitle();
						mSetChannelTitle.setChannelTitle(avEnc.getChannelTitle());
						
						// 最后需要设置一个点阵信息
						//mSettingConfigs.add(ChannelTitleDotSet.CONFIG_NAME);
					}
					
					FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, avEnc);
					
					if ( null != mSetChannelTitle ) {
						FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, mSetChannelTitle);
					}
				}
			} else {
				showToast(R.string.device_alarm_no_change);
			}
		}
	}
	
	private void setOSDName(String name) {
		mTvOSD.setText(name);
		float fontWidth = mTvOSD.getPaint().measureText(name);
		int reach = (int) fontWidth % 8;
		if (reach != 0) {
			mTvOSD.setWidth((int) (fontWidth + 8 - reach));
		} else {
			mTvOSD.setWidth((int) fontWidth);
		}
//		findViewById(R.id.osd_tv).requestLayout();
	}
	
	private void setChannelTitleDot() {
		byte[] pixels = MyUtils.getPixelsToDevice(mTvOSD);
		if (null == pixels) {
			return;
		}
		if (null == mTitleDot) {
			mTitleDot = new SDK_TitleDot(mTvOSD.getWidth(),
					mTvOSD.getHeight());
		}
		
		G.SetValue(mTitleDot.st_3_pDotBuf, pixels);
		mTitleDot.st_0_width = (short) mTvOSD.getWidth();
		mTitleDot.st_1_height = (short) mTvOSD.getHeight();
		FunSupport.getInstance().requestDeviceTitleDot(mFunDevice, mTitleDot);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

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
	public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {
		if (null != mFunDevice 
				&& funDevice.getId() == mFunDevice.getId() 
				&& isCurrentUsefulConfig(configName)) {
			if (isAllConfigGetted()) {
				hideWaitDialog();
			}

			refreshCameraConfig();
		}
		
		if ( ChannelTitle.CONFIG_NAME.equals(configName) ) {
			if ( null != mSetChannelTitle ) {
				setChannelTitleDot();
			}
		}
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}

	@Override
	public void onDeviceSetConfigSuccess(final FunDevice funDevice, final String configName) {
		if (null != mFunDevice && funDevice.getId() == mFunDevice.getId()) {
			synchronized (mSettingConfigs) {
				if (mSettingConfigs.contains(configName)) {
					mSettingConfigs.remove(configName);
				}

				if (mSettingConfigs.size() == 0) {
					hideWaitDialog();
				}
			}

			refreshCameraConfig();
		}
	}

	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, final String configName, final Integer errCode) {
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