package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.SDKCONST.SDK_CAPTURE_SIZE_t;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.EncodeCapability;
import com.lib.funsdk.support.config.SimplifyEncode;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/*Demo:编码配置*/

public class ActivityGuideDeviceSetupEncode extends ActivityDemo
		implements OnClickListener, OnFunDeviceOptListener, OnItemSelectedListener ,OnTouchListener{

	// 主码流清晰度
	private Spinner mSpinner_main_Definition = null;
	// 辅码流清晰度
	private Spinner mSpinner_sub_definition = null;
	// 主码流分辨率
	private Spinner mSpinner_main_Resolution = null;
	// 辅码流分辨率
	private Spinner mSpinner_sub_Resolution = null;
	// 主码流帧数
	private Spinner mSpinner_main_FPS = null;
	// 辅码流帧数
	private Spinner mSpinner_sub_FPS = null;
	// 主码流音频
	private ImageView mBtnSwitch_main_Audio = null;
	// 辅码流音频
	private ImageView mBtnSwitch_sub_Audio = null;
	// 辅码流视频
	private ImageView mBtnSwitch_sub_Video = null;

	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	private ImageButton mBtnSave = null;

	private FunDevice mFunDevice = null;

	int _nMaxRate = 25;
	int _nMinSubRes = -1;
	private int mMaxEncodePowerPerChannel;
	private int mImageSizePerChannel;
	private long mResolutionMask;
	private long mComb_ResolutionMask;
	int _nLastRes = 0, _nLastRate = 0;
	private JSONArray mExImageSizePerChannelEx_Array;

	// 本界面需要获取到的界面配置信息
	private final String[] DEV_CONFIGS = {
			// 获取参数，分辨率，帧数，清晰度,音频，视频
			SimplifyEncode.CONFIG_NAME,
			// 编码能力集
			EncodeCapability.CONFIG_NAME };

	// 设置配置信息的时候,由于有多个,通过下面的列表来判断是否所有的配置都设置完成了
	private List<String> mSettingConfigs = new ArrayList<String>();

	final static int s_fps[] = { 704 * 576, // D1
			704 * 288, // HD1
			352 * 576, // BCIF
			352 * 288, // CIF
			176 * 144, // QCIF
			640 * 480, // VGA
			320 * 240, // QVGA
			480 * 480, // SVCD
			160 * 128, // QQVGA
			240 * 192, // ND1
			928 * 576, // 650TVL
			1280 * 720, // 720P
			1280 * 960, // 1_3M
			1600 * 1200, // UXGA
			1920 * 1080, // 1080P
			1920 * 1200, // WUXGA
			1872 * 1408, // 2_5M
			2048 * 1536, // 3M
			3744 * 1408, // 5M
			960 * 1080, // 1080N
	};

	public static final String s_res_str_0[] = { "D1", "HD1", // /< 352*576(PAL)
			// 352*480(NTSC)
			"BCIF", // /< 720*288(PAL) 720*240(NTSC)
			"CIF", // /< 352*288(PAL) 352*240(NTSC)
			"QCIF", // /< 176*144(PAL) 176*120(NTSC)
			"VGA", // /< 640*480(PAL) 640*480(NTSC)
			"QVGA", // /< 320*240(PAL) 320*240(NTSC)
			"SVCD", // /< 480*480(PAL) 480*480(NTSC)
			"QQVGA", // /< 160*128(PAL) 160*128(NTSC)
			"ND1", // /< 240*192
			"960H", // /< 926*576
			"720P", // /< 1280*720
			"960", // /< 1280*960
			"UXGA ", // /< 1600*1200
			"1080P", // /< 1920*1080
			"WUXGA", // /< 1920*1200
			"2_5M", // /< 1872*1408
			"3M", // /< 2048*1536
			"5M", "1080N" };

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_device_setup_encode);

		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
		// 返回
		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);

		mTextTitle.setText(R.string.device_setup_encode);

		mSpinner_main_Definition = (Spinner) findViewById(R.id.sp_main_definition);
		mSpinner_sub_definition = (Spinner) findViewById(R.id.sp_sub_definition);
		String[] definitions = getResources().getStringArray(R.array.device_setup_camera_definition_values);
		ArrayAdapter<String> adapterDefinition = new ArrayAdapter<String>(this, R.layout.right_spinner_item,
				definitions);
		adapterDefinition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner_main_Definition.setAdapter(adapterDefinition);
		mSpinner_sub_definition.setAdapter(adapterDefinition);
		int[] defValues = { 6, 5, 4, 3, 2, 1 };
		mSpinner_main_Definition.setTag(defValues);
		mSpinner_sub_definition.setTag(defValues);
		mSpinner_main_Definition.setOnItemSelectedListener(this);
		mSpinner_sub_definition.setOnItemSelectedListener(this);

		mSpinner_main_Resolution = (Spinner) findViewById(R.id.sp_main_resolution);
		mSpinner_sub_Resolution = (Spinner) findViewById(R.id.sp_sub_resolution);
		mSpinner_main_FPS = (Spinner) findViewById(R.id.sp_main_frame);
		mSpinner_sub_FPS = (Spinner) findViewById(R.id.sp_sub_frame);

		int sps[] = { R.id.sp_main_resolution, R.id.sp_main_frame, R.id.sp_sub_resolution, R.id.sp_sub_frame, };
		for (int i = 0; i < sps.length; ++i) {
			Spinner sp = (Spinner) this.findViewById(sps[i]);
			sp.setOnTouchListener(this);
			if (i < 3) {
				sp.setOnItemSelectedListener(this);
			}
		}

		mBtnSwitch_main_Audio = (ImageView) findViewById(R.id.ck_main_voice);
		mBtnSwitch_main_Audio.setOnClickListener(this);
		mBtnSwitch_sub_Audio = (ImageView) findViewById(R.id.ck_sub_voice);
		mBtnSwitch_sub_Audio.setOnClickListener(this);
		mBtnSwitch_sub_Video = (ImageView) findViewById(R.id.ck_sub_video);
		mBtnSwitch_sub_Video.setOnClickListener(this);

		mBtnSave = (ImageButton) setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);

		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if (null == funDevice) {
			finish();
			return;
		}

		mFunDevice = funDevice;

		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);

		if (mFunDevice != null) {
			showWaitDialog();
			for (String configName : DEV_CONFIGS) {
				// 删除老的配置信息
				mFunDevice.invalidConfig(configName);

				// 重新搜索新的配置信息
				FunSupport.getInstance().requestDeviceConfig(funDevice, configName);
			}
		}

	}

	int InitMinSubRes() {
		int nMinResSize = 0;
		int nMainMark = GetMainResMark();
		for (int j = 0; j < N_RESOLUTION_COUNT; ++j) {
			if (0 != (nMainMark & (0x1 << j))) {
				int nSubResMark = GetSubResMark(j);
				for (int i = 0; i < N_RESOLUTION_COUNT; ++i) {
					if (0 != (nSubResMark & (0x1 << i))) {
						if (nMinResSize == 0 || nMinResSize > GetResolutionSize(i)) {
							nMinResSize = GetResolutionSize(i);
							_nMinSubRes = i;
						}
					}
				}
			}
		}
		return 0;
	}

	int GetResolutionSize(int nResIndex) {
		if (nResIndex >= 0 && nResIndex <= SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_4M) {
			return s_fps[nResIndex];
		}
		return s_fps[0];
	}

	int GetResMark(int nLastAbility, int nRate, int nSupportResMark) {
		int nRetMark = 0;
		int nGetRes = 0;
		for (int i = 0; i <= SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_4M; ++i) {
			if (0 != (nSupportResMark & (1 << i))) {
				nGetRes = GetResolutionSize(i);
				if (nGetRes * nRate <= nLastAbility) {
					nRetMark |= (0x1 << i);
				}
			}
		}
		return nRetMark;
	}

	int GetSubResMark(int nMainRes) {
		int nRetMark = 0;
		if (mExImageSizePerChannelEx_Array != null) {
			String memoryparam = mExImageSizePerChannelEx_Array.optString(nMainRes);
			if (memoryparam != null && memoryparam.length() > 2) {
				nRetMark = Integer.parseInt(memoryparam.substring(2, memoryparam.length()), 16);
			}
		}
		// nRetMark = mExImageSizePerChannelEx_Array.optInt(nMainRes);
		if (nRetMark == 0) {
			nRetMark = (int) mComb_ResolutionMask;
		}
		return nRetMark;

		// nRetMark = _eab.st_8_ExImageSizePerChannelEx[_chnIndex][nMainRes];
		// if (nRetMark == 0) {
		// nRetMark = _eab.st_6_vCombEncInfo[_chnIndex].st_5_uiResolution;
		// }
		// return nRetMark;
	}

	public int N_RESOLUTION_COUNT = SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_4M;

	int GetMainResMark() {
		int nRetMark = 0;
		nRetMark = mImageSizePerChannel;
		if (nRetMark == 0) {
			nRetMark = (int) mResolutionMask;
		}

		return nRetMark;
	}

	/**
	 * 判断是否所有需要的配置都获取到了
	 * 
	 * @return
	 */
	private boolean isAllConfigGetted() {
		for (String configName : DEV_CONFIGS) {
			if (null == mFunDevice.getConfig(configName)) {
				System.out.println("TTTTT------>> configname = " + configName);
				return false;
			}
		}
		return true;
	}

	private boolean isCurrentUsefulConfig(String configName) {
		for (int i = 0; i < DEV_CONFIGS.length; i++) {
			if (DEV_CONFIGS[i].equals(configName)) {
				return true;
			}
		}
		return false;
	}

	public int GetIntValue(int id) {
		View v = this.findViewById(id);
		if (v == null) {
			return 0;
		}
		// if (v instanceof EditText) {
		// EditText v0 = (EditText) v;
		// return Integer.valueOf(v0.getText().toString());
		// } else if (v instanceof CheckBox) {
		// CheckBox v0 = (CheckBox) v;
		// return v0.isChecked() ? 1 : 0;
		// } else if (v instanceof SeekBar) {
		// SeekBar v0 = (SeekBar) v;
		// return v0.getProgress();
		// } else
		if (v instanceof ImageView) {
			ImageView iv = (ImageView) v;
			return iv.isSelected() ? 1 : 0;
		} else if (v instanceof Spinner) {
			Spinner sp = (Spinner) v;
			Object iv = v.getTag();
			if (iv != null && iv instanceof int[]) {
				int[] values = (int[]) iv;
				int i = sp.getSelectedItemPosition();
				if (i >= 0 && i < values.length) {
					return values[i];
				}
				return 0;
			}
		} else {
			System.err.println("GetIntValue:" + id);
		}
		return 0;
	}
	
	int GetIndex(String resolution) {
		final String D1 = "D1";
		final String HD1 = "HD1";
		final String BCIF = "BCIF";
		final String CIF = "CIF";
		final String QCIF = "QCIF";
		final String VGA = "VGA";
		final String QVGA = "QVGA";
		final String SVCD = "SVCD";
		final String QQVGA = "QQVGA";
		final String ND1 = "ND1";
		final String _960H = "960H";
		final String _720P = "720P";
		final String _960 = "960";
		final String UXGA = "UXGA";
		final String _1080P = "1080P";
		final String WUXGA = "WUXGA";
		final String _2_5M = "2_5M";
		final String _3M = "3M";
		final String _5M = "5M";
		final String _1080N = "1080N";

		final String _650TVL = "650TVL";
		final String _1_3M = "1_3M";

		if (resolution.equals(D1)) {
			return 0;
		} else if (resolution.equals(HD1)) {
			return 1;
		} else if (resolution.equals(BCIF)) {
			return 2;
		} else if (resolution.equals(CIF)) {
			return 3;
		} else if (resolution.equals(QCIF)) {
			return 4;
		} else if (resolution.equals(VGA)) {
			return 5;
		} else if (resolution.equals(QVGA)) {
			return 6;
		} else if (resolution.equals(SVCD)) {
			return 7;
		} else if (resolution.equals(QQVGA)) {
			return 8;
		} else if (resolution.equals(ND1)) {
			return 9;
		} else if (resolution.equals(_650TVL)) {
			return 10;
		} else if (resolution.equals(_720P)) {
			return 11;
		} else if (resolution.equals(_1_3M)) {
			return 12;
		} else if (resolution.equals(UXGA)) {
			return 13;
		} else if (resolution.equals(_1080P)) {
			return 14;
		} else if (resolution.equals(WUXGA)) {
			return 15;
		} else if (resolution.equals(_2_5M)) {
			return 16;
		} else if (resolution.equals(_3M)) {
			return 17;
		} else if (resolution.equals(_5M)) {
			return 18;
		} else if (resolution.equals(_1080N)) {
			return 19;
		} else {
			return -1;
		}
	}

	String GetString(int i) {
		final String D1 = "D1";
		final String HD1 = "HD1";
		final String BCIF = "BCIF";
		final String CIF = "CIF";
		final String QCIF = "QCIF";
		final String VGA = "VGA";
		final String QVGA = "QVGA";
		final String SVCD = "SVCD";
		final String QQVGA = "QQVGA";
		final String ND1 = "ND1";
		final String _960H = "960H";
		final String _720P = "720P";
		final String _960 = "960";
		final String UXGA = "UXGA";
		final String _1080P = "1080P";
		final String WUXGA = "WUXGA";
		final String _2_5M = "2_5M";
		final String _3M = "3M";
		final String _5M = "5M";
		final String _1080N = "1080N";

		final String _650TVL = "650TVL";
		final String _1_3M = "1_3M";

		switch (i) {
		case 0:
			return D1;
		case 1:
			return HD1;
		case 2:
			return BCIF;
		case 3:
			return CIF;
		case 4:
			return QCIF;
		case 5:
			return VGA;
		case 6:
			return QVGA;
		case 7:
			return SVCD;
		case 8:
			return QQVGA;
		case 9:
			return ND1;
		case 10:
			return _650TVL;
		case 11:
			return _720P;
		case 12:
			return _1_3M;
		case 13:
			return UXGA;
		case 14:
			return _1080P;
		case 15:
			return WUXGA;
		case 16:
			return _2_5M;
		case 17:
			return _3M;
		case 18:
			return _5M;
		case 19:
			return _1080N;
		default:
			return D1;
		}
	}

	String GetResFromIndex(int index) {
		if (index >= 0 && index <= SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_4M) {
			return s_res_str_0[index];
		}
		return "";
	}

	public int InitSpinnerText(int id, String[] texts, int[] values) {
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,
				texts);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner sp = (Spinner) this.findViewById(id);
		sp.setAdapter(adapter);
		if (values == null) {
			values = new int[texts.length];
			for (int i = 0; i < texts.length; ++i) {
				values[i] = i;
			}
		}
		sp.setTag(values);
		return 0;
	}

	@Override
	protected void onDestroy() {

		FunSupport.getInstance().removeOnFunDeviceOptListener(this);

		super.onDestroy();
	}

	public int SetValue(int id, int value) {
		View v = this.findViewById(id);
		if (v == null) {
			return 0;
		}
		if (v instanceof ImageView) {
			ImageView iv = (ImageView) v;
			iv.setSelected(toBoolean(value));
		} else if (v instanceof Spinner) {
			Spinner sp = (Spinner) v;
			Object iv = v.getTag();
			if (iv != null && iv instanceof int[]) {
				int values[] = (int[]) iv;
				for (int i = 0; i < values.length; ++i) {
					if (value == values[i]) {
						sp.setSelection(i);
						break;
					}
				}
			}
		} else {
			System.err.println("SetIntValue:" + id);
			return -1;
		}
		return 0;
	}

	// 保存设置
	private void tryTosaveConfig() {
		
		if (_nMinSubRes == -1) {
			InitMinSubRes();
		}
		
		boolean beSimplifyEncodeChanged = false;
		SimplifyEncode simplifyEncode = (SimplifyEncode) mFunDevice.getConfig(SimplifyEncode.CONFIG_NAME);
		if (null != simplifyEncode) {
			// 主码流
			// 清晰度
			System.out.println("TTTT------->>> getselectedItemposition = " + mSpinner_main_Definition.getSelectedItemPosition());
			if (simplifyEncode.mainFormat.video.Quality != GetIntValue(R.id.sp_main_definition)) {
				simplifyEncode.mainFormat.video.Quality = GetIntValue(R.id.sp_main_definition);
			}
			// 分辨率
			if (!simplifyEncode.mainFormat.video.Resolution
					.equals(GetString(GetIntValue(R.id.sp_main_resolution)))) {
				simplifyEncode.mainFormat.video.Resolution = GetString(GetIntValue(R.id.sp_main_resolution));
			}
			// 帧数
			if (simplifyEncode.mainFormat.video.FPS != GetIntValue(R.id.sp_main_frame)) {
				simplifyEncode.mainFormat.video.FPS = GetIntValue(R.id.sp_main_frame);
			}
			// 音频
			simplifyEncode.mainFormat.AudioEnable = mBtnSwitch_main_Audio.isSelected();

			// 辅码流
			// 清晰度
			if (simplifyEncode.extraFormat.video.Quality != GetIntValue(R.id.sp_sub_definition)) {
				simplifyEncode.extraFormat.video.Quality = GetIntValue(R.id.sp_sub_definition);
			}
			// 分辨率
			if (!simplifyEncode.extraFormat.video.Resolution
					.equals(GetString(GetIntValue(R.id.sp_sub_resolution)))) {
				simplifyEncode.extraFormat.video.Resolution = GetString(GetIntValue(R.id.sp_sub_resolution));
			}
			// 帧数
			if (simplifyEncode.extraFormat.video.FPS !=  GetIntValue(R.id.sp_sub_frame)) {
				simplifyEncode.extraFormat.video.FPS = GetIntValue(R.id.sp_sub_frame);
			}
			// 音频,视频
			simplifyEncode.extraFormat.AudioEnable = mBtnSwitch_sub_Audio.isSelected();
			simplifyEncode.extraFormat.VideoEnable = mBtnSwitch_sub_Video.isSelected();

			beSimplifyEncodeChanged = true;
		}
		
//		System.out.println("TTT-----mainframe = " + simplifyEncode.mainFormat.video.FPS);
//		System.out.println("TTT-----mainframe = " + simplifyEncode.extraFormat.video.FPS);
		
		mSettingConfigs.clear();

		if (beSimplifyEncodeChanged) {
			showWaitDialog();

			// 保存SimplifyEncode
			if (beSimplifyEncodeChanged) {
				synchronized (mSettingConfigs) {
					mSettingConfigs.add(simplifyEncode.getConfigName());
				}

				FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, simplifyEncode);
			}

		}else {
			showToast(R.string.device_alarm_no_change);
		}
	}

	// 更新UI
	private void refreshLayout() {

		if (_nMinSubRes == -1) {
			InitMinSubRes();
		}

		SimplifyEncode simpEnc = (SimplifyEncode) mFunDevice.getConfig(SimplifyEncode.CONFIG_NAME);
		EncodeCapability encodeCap = (EncodeCapability) mFunDevice.getConfig(EncodeCapability.CONFIG_NAME);

		if (null != simpEnc && encodeCap != null) {
			mImageSizePerChannel = encodeCap.ImageSizePerChannel;
			mResolutionMask = encodeCap.ResolutionMask;
			mComb_ResolutionMask = encodeCap.Comb_ResolutionMask;
			mExImageSizePerChannelEx_Array = encodeCap.ExImageSizePerChannelEx;
			mMaxEncodePowerPerChannel = encodeCap.MaxEncodePowerPerChannel;

			Log.d("TTT", "Resolution: " + simpEnc.mainFormat.video.Resolution + "Index: "
					+ GetIndex(simpEnc.mainFormat.video.Resolution));
			_nLastRes = GetIndex(simpEnc.mainFormat.video.Resolution);
			_nLastRate = simpEnc.mainFormat.video.FPS > _nMaxRate ? _nMaxRate : simpEnc.mainFormat.video.FPS;
			String mainRes[] = { GetResFromIndex(GetIndex(simpEnc.mainFormat.video.Resolution)) };
			int mainResValue[] = { GetIndex(simpEnc.mainFormat.video.Resolution) };
			InitSpinnerText(R.id.sp_main_resolution, mainRes, mainResValue);

			String subRes[] = { GetResFromIndex(GetIndex(simpEnc.extraFormat.video.Resolution)) };
			int subResValue[] = { GetIndex(simpEnc.extraFormat.video.Resolution) };
			InitSpinnerText(R.id.sp_sub_resolution, subRes, subResValue);

			String sInt[] = {
					"" + (simpEnc.mainFormat.video.FPS > _nMaxRate ? _nMaxRate : simpEnc.mainFormat.video.FPS) };
			int vInt[] = { simpEnc.mainFormat.video.FPS > _nMaxRate ? _nMaxRate : simpEnc.mainFormat.video.FPS };
			InitSpinnerText(R.id.sp_main_frame, sInt, vInt);

			String sInt1[] = {
					"" + (simpEnc.extraFormat.video.FPS > _nMaxRate ? _nMaxRate : simpEnc.extraFormat.video.FPS) };
			int vInt1[] = { simpEnc.extraFormat.video.FPS > _nMaxRate ? _nMaxRate : simpEnc.extraFormat.video.FPS };
			InitSpinnerText(R.id.sp_sub_frame, sInt1, vInt1);

			SetValue(R.id.sp_main_resolution, _nLastRes);
			SetValue(R.id.sp_main_frame, _nLastRate);
			SetValue(R.id.sp_main_definition, simpEnc.mainFormat.video.Quality);
			SetValue(R.id.ck_main_voice, toInt(simpEnc.mainFormat.AudioEnable));

			SetValue(R.id.sp_sub_resolution, GetIndex(simpEnc.extraFormat.video.Resolution));
			SetValue(R.id.sp_sub_frame, simpEnc.extraFormat.video.FPS);
			SetValue(R.id.sp_sub_definition, simpEnc.extraFormat.video.Quality);
			SetValue(R.id.ck_sub_voice, toInt(simpEnc.extraFormat.AudioEnable));
			SetValue(R.id.ck_sub_video, toInt(simpEnc.extraFormat.VideoEnable));
		}
		if (_nMinSubRes == -1) {
			InitMinSubRes();
		}
	}

	public boolean toBoolean(int i) {
		return i == 1;
	}

	public int toInt(boolean boo) {
		if (boo) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> av, View view, int position, long id) {
		// TODO Auto-generated method stub
		SimplifyEncode sim = (SimplifyEncode) mFunDevice.getConfig(SimplifyEncode.CONFIG_NAME);
		EncodeCapability encode = (EncodeCapability) mFunDevice.getConfig(EncodeCapability.CONFIG_NAME);
		switch (av.getId()) {
		case R.id.sp_main_resolution:
		case R.id.sp_main_frame: {
			int nMyRes = GetIntValue(R.id.sp_main_resolution);
			int nMyRate = GetIntValue(R.id.sp_main_frame);

			if (_nLastRes != nMyRes | nMyRate != _nLastRate) {
				_nLastRes = nMyRes;
				_nLastRate = nMyRate;
				int nLastAbility = GetLastAbility(mMaxEncodePowerPerChannel, nMyRes, nMyRate);
				int nSupportResMark = GetSubResMark(nMyRes);
				int nSubRes = GetIntValue(R.id.sp_sub_resolution);
				int nSubRate = GetIntValue(R.id.sp_sub_frame);
				int nRetMark = GetResMark(nLastAbility, nSubRate, nSupportResMark);

				if (0 != (nRetMark & nSubRes)) {
					return;
				}

				nRetMark = GetResMark(nLastAbility, 1, nSupportResMark);
				UpdateResSpanner(R.id.sp_sub_resolution, nRetMark);

				for (int i = 0; i < N_RESOLUTION_COUNT; ++i) {
					nRetMark = GetResMark(nLastAbility, nSubRate, nSupportResMark);
					if (0 != (nRetMark & (0x1 << i))) {
						this.SetValue(R.id.sp_sub_resolution, i);
						return;
					}
				}

				for (int j = _nMaxRate; j > 0; --j) {
					nRetMark = GetResMark(nLastAbility, j, nSupportResMark);
					for (int i = 0; i < N_RESOLUTION_COUNT; ++i) {
						if (0 != (nRetMark & (0x1 << i))) {
							this.SetValue(R.id.sp_sub_resolution, i);

							UpdateRateSpanner(R.id.sp_sub_frame, j);
							this.SetValue(R.id.sp_sub_frame, j);
							return;
						}
					}
				}
			}
		}
			break;
		case R.id.sp_sub_resolution: {
			int nMyRes = GetIntValue(R.id.sp_main_resolution);
			int nMyRate = GetIntValue(R.id.sp_main_frame);
			int nLastAbility = GetLastAbility(encode.MaxEncodePowerPerChannel, nMyRes, nMyRate);
			int nSupportResMark = GetSubResMark(nMyRes);
			int nSubRes = GetIntValue(R.id.sp_sub_resolution);
			int nSubRate = GetIntValue(R.id.sp_sub_frame);

			int nRetMark = GetResMark(nLastAbility, nSubRate, nSupportResMark);
			if (0 != (nRetMark & (0x1 << nSubRes))) {
				break;
			}

			for (int j = 25; j > 0; --j) {
				nRetMark = GetResMark(nLastAbility, j, nSupportResMark);
				if (0 != (nRetMark & (0x1 << nSubRes))) {
					UpdateRateSpanner(R.id.sp_sub_frame, j);
					this.SetValue(R.id.sp_sub_frame, j);
					break;
				}
			}
		}
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
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
		// 获取编码配置成功, 刷新布局
		if (null != mFunDevice && funDevice.getId() == mFunDevice.getId() && isCurrentUsefulConfig(configName)) {
			if (isAllConfigGetted()) {
				System.out.println("TTTTTTT----->>我的天");
				hideWaitDialog();
			}
			refreshLayout();
		}
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		if (null != mFunDevice && funDevice.getId() == mFunDevice.getId()) {
			synchronized (mSettingConfigs) {
				if (mSettingConfigs.contains(configName)) {
					mSettingConfigs.remove(configName);
				}

				if (mSettingConfigs.size() == 0) {
					hideWaitDialog();
				}
			}

			refreshLayout();
		}
	}

	@Override
	public void onDeviceSetConfigFailed(FunDevice funDevice, String configName, Integer errCode) {
		// TODO Auto-generated method stub
		showToast(FunError.getErrorStr(errCode));
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
	public void onDeviceOptionFailed(FunDevice funDevice, String option, Integer errCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.backBtnInTopLayout: {
			finish();
			// 返回
		}
			break;
		case R.id.ck_main_voice:
		case R.id.ck_sub_video:
		case R.id.ck_sub_voice:
			v.setSelected(!v.isSelected());
			break;
		case R.id.btnSave:
			tryTosaveConfig();
			break;
		default:
			break;
		}
	}

	int GetLastAbility(int nMaxPower, int nResIndex, int nRate) {
		return nMaxPower - GetResolutionSize(nResIndex) * nRate;
	}

	void UpdateResSpanner(int id, int nMark) {
		int nCount = 0;
		for (int i = 0; i <= SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_4M; ++i) {
			if (0 != (nMark & (1 << i))) {
				nCount++;
			}
		}
		String sv[] = new String[nCount];
		int vs[] = new int[nCount];
		int j = 0;
		for (int i = 0; i <= SDK_CAPTURE_SIZE_t.SDK_CAPTURE_SIZE_4M; ++i) {
			if (0 != (nMark & (1 << i))) {
				sv[j] = GetResFromIndex(i);
				vs[j] = i;
				j++;
			}
		}
		int old = GetIntValue(id);
		InitSpinnerText(id, sv, vs);
		this.SetValue(id, old);
	}

	int GetMaxRate(int nLastAbility, int nResIndex) {
		int nResSize = GetResolutionSize(nResIndex);
		int i = _nMaxRate;
		for (; i > -1; --i) {
			if (nResSize * i <= nLastAbility) {
				break;
			}
		}
		return i;
	}

	void UpdateRateSpanner(int id, int nMaxRate) {
		String sv[] = new String[nMaxRate];
		int vs[] = new int[nMaxRate];
		for (int i = 0; i < nMaxRate; ++i) {
			sv[i] = "" + (i + 1);
			vs[i] = i + 1;
		}

		int old = GetIntValue(id);
		InitSpinnerText(id, sv, vs);
		this.SetValue(id, old);
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		// TODO Auto-generated method stub
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.sp_main_resolution: {
				int nLastAbility = this.GetLastAbility(mMaxEncodePowerPerChannel, _nMinSubRes, 1);
				/*
				 * 以前结构体的配置方法
				 */
				// int nLastAbility = this.GetLastAbility(
				// _eab.st_2_nMaxPowerPerChannel[this._chnIndex], _nMinSubRes,
				// 1);
				int nSupportResMark = this.GetMainResMark();
				int nRate = this.GetIntValue(R.id.sp_main_frame);
				int nRetMark = this.GetResMark(nLastAbility, nRate, nSupportResMark);
				Log.d("TTT", "TTT　nRetMark：" + nRetMark);
				UpdateResSpanner(R.id.sp_main_resolution, nRetMark);
			}
				break;
			case R.id.sp_main_frame: {
				int nLastAbility = GetLastAbility(mMaxEncodePowerPerChannel, _nMinSubRes, 1);
				int nRes = this.GetIntValue(R.id.sp_main_resolution);
				int nMaxRate = GetMaxRate(nLastAbility, nRes);
				UpdateRateSpanner(R.id.sp_main_frame, nMaxRate);
			}
				break;
			case R.id.sp_sub_resolution: {
				int nMyRes = this.GetIntValue(R.id.sp_main_resolution);
				int nMyRate = GetIntValue(R.id.sp_main_frame);
				int nLastAbility = GetLastAbility(mMaxEncodePowerPerChannel, nMyRes, nMyRate);
				int nSupportResMark = GetSubResMark(nMyRes);
				int nSubRate = 1;// GetIntValue(R.id.sp_sub_frame);
				int nRetMark = GetResMark(nLastAbility, nSubRate, nSupportResMark);
				Log.d("TTT", "TTT" + "nSupportResMark:" + nSupportResMark);
				UpdateResSpanner(R.id.sp_sub_resolution, nRetMark);
			}
				break;
			case R.id.sp_sub_frame: {
				int nMyRes = this.GetIntValue(R.id.sp_main_resolution);
				int nMyRate = GetIntValue(R.id.sp_main_frame);
				int nLastAbility = GetLastAbility(mMaxEncodePowerPerChannel, nMyRes, nMyRate);
				int nSubRes = GetIntValue(R.id.sp_sub_resolution);
				int nMaxRate = GetMaxRate(nLastAbility, nSubRes);
				UpdateRateSpanner(R.id.sp_sub_frame, nMaxRate);
			}
				break;
			}
		}
		return false;
	}

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}
}
