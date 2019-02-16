package com.example.funsdkdemo.devices;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.PowerSocketSensitive;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

/*
 * 插座的敏感度设置
 * 
 */
public class ActivityGuideDevicesSocketSensitiveSetting extends ActivityDemo
		implements OnFunDeviceOptListener, OnClickListener {

	private FunDevice mFunDevice;
	private ImageButton mBtnSave = null;
	private ImageButton mBtnBack = null;
	private TextView mTextTitle = null;
	/**
	 * 低级
	 */
	public static int LEVEL_LOW = 1;
	/**
	 * 中级
	 */
	public static int LEVEL_INTERMEDIATE = 2;
	/**
	 * 高级
	 */
	public static int LEVEL_ADVANCED = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_devices_sensitive);
		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);

		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);

		mTextTitle.setText(this.getResources().getString(
				R.string.devices_socket_sensitivity_test));
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);
		if (null == mFunDevice) {
			finish();
			return;
		}
		mBtnSave = (ImageButton) setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		initLayout();
		initData();

	}

	@Override
	public void onDestroy() {

		FunSupport.getInstance().removeOnFunDeviceOptListener(this);

		super.onDestroy();

	}

	private void initLayout() {
		InitSpinnerText(R.id.sensitive_level_sp,
				new String[] { "高", "中", "低" }, new int[] { LEVEL_LOW - 1,
						LEVEL_INTERMEDIATE - 1, LEVEL_ADVANCED - 1 });

	}

	private void initData() {
		FunSupport.getInstance().requestDeviceConfig(mFunDevice,
				PowerSocketSensitive.CONFIG_NAME);
	}

	public int GetIntValue(int id) {
		View v = this.findViewById(id);
		if (v == null) {
			return 0;
		}
		if (v instanceof EditText) {
			EditText v0 = (EditText) v;
			return Integer.valueOf(v0.getText().toString());
		} else if (v instanceof CheckBox) {
			CheckBox v0 = (CheckBox) v;
			return v0.isChecked() ? 1 : 0;
		} else if (v instanceof SeekBar) {
			SeekBar v0 = (SeekBar) v;
			return v0.getProgress();
		} else if (v instanceof ImageView) {
			Object iv = v.getTag();
		} else if (v instanceof Spinner) {
			Spinner sp = (Spinner) v;
			Object iv = v.getTag();
			if (iv != null && iv instanceof int[]) {
				int[] values = (int[]) iv;
				int i = sp.getSelectedItemPosition();
				if (values != null && values.length > i) {
					return values[i];
				}
				return 0;
			}
		} else {
			System.err.println("GetIntValue:" + id);
		}
		return 0;
	}

	// @Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.backBtnInTopLayout:
			finish();
			break;
		case R.id.btnSave:
			PowerSocketSensitive mSocketSensitive = (PowerSocketSensitive) mFunDevice
					.checkConfig(PowerSocketSensitive.CONFIG_NAME);
			mSocketSensitive.setSensitive(GetIntValue(R.id.sensitive_level_sp));
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
					mSocketSensitive);
			Toast.makeText(
					this,
					v.getContext().getResources()
							.getString(R.string.devices_socket_save_success),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
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
		if (PowerSocketSensitive.CONFIG_NAME.equals(configName)) {
			PowerSocketSensitive mSocketSensitive = (PowerSocketSensitive) mFunDevice
					.checkConfig(PowerSocketSensitive.CONFIG_NAME);

			if (null != mSocketSensitive) {
				LEVEL_INTERMEDIATE = mSocketSensitive.getSensitive();

				final Spinner spinner = ((Spinner) findViewById(R.id.sensitive_level_sp));
				spinner.setSelection(LEVEL_INTERMEDIATE);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String sel = spinner.getSelectedItem().toString();
						Log.e("PowerSocketSensitive", sel + "");
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

			}

		}

	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		Toast.makeText(
				this,
				this.getResources().getString(
						R.string.devices_socket_save_failure),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		if (PowerSocketSensitive.CONFIG_NAME.equals(configName)) {
			PowerSocketSensitive mSocketSensitive = (PowerSocketSensitive) mFunDevice
					.checkConfig(PowerSocketSensitive.CONFIG_NAME);
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
					mSocketSensitive);
			Log.e("PowerSocketSensitive", mSocketSensitive + "");
		}
	}

	public int InitSpinnerText(int id, String[] texts, int[] values) {
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, R.layout.right_spinner_item, texts);
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
