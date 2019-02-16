package com.example.funsdkdemo.devices;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.PowerSocketWorkRecord;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.StringUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

/**
 * 关于插座的电量统计Demo
 * 
 */
public class ActivityGuideDevicesSocketAboutWork extends
		ActivityDemo implements OnClickListener,
		OnFunDeviceOptListener {
	private String jsonstr = null;
	String modifyDevPow = "";

	private FunDevice mFunDevice;
	public int TotalEnergy;
	public int TotalTime;
	public int EnergyOfThisMon;
	public int TimeOfThisMon;
	public int EnergyRecently;
	public int TimeRecently;
	public int DeviceType;
	public int DevicePower;
	private int pow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dev_config_about_record);
		findViewById(R.id.work_set_rl).setOnClickListener(this);
		findViewById(R.id.backBtnInTopLayout).setOnClickListener(this);
		SetTextView(R.id.textViewInTopLayout, this.getResources().getString(R.string.devices_socket_power_consumption));
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);

		FunSupport.getInstance().registerOnFunDeviceOptListener(this);

		initData();
	}

	@Override
	public void onDestroy() {

		FunSupport.getInstance().removeOnFunDeviceOptListener(this);

		super.onDestroy();

	}

	private void initData() {

		FunSupport.getInstance().requestDeviceConfig(mFunDevice,
				PowerSocketWorkRecord.CONFIG_NAME);
	}

	// 初始化数据
	private void initLayout() {
		System.out.println("json-->" + jsonstr);
		Log.d("Demo--->getConfig", TimeRecently + "");

		SetTextView(R.id.TotalEnergy,
				(String.format("%.4f", (double) TotalEnergy / 60000)) + this.getResources().getString(R.string.devices_socket_electric_quantity));
		SetTextView(R.id.TotalTime, SecondToTime(TotalTime));
		SetTextView(R.id.EnergyOfThisMon,
				(String.format("%.4f", (double) EnergyOfThisMon / 60000)) + this.getResources().getString(R.string.devices_socket_electric_quantity));
		SetTextView(R.id.TimeOfThisMon, SecondToTime(TimeOfThisMon));
		SetTextView(R.id.EnergyRecently,
				(String.format("%.4f", (double) EnergyRecently / 60000)) + this.getResources().getString(R.string.devices_socket_electric_quantity));
		SetTextView(R.id.TimeRecently, SecondToTime(TimeRecently));
		SetTextView(R.id.DeviceType, DeviceType + "");
		SetTextView(R.id.DevicePower, DevicePower + this.getResources().getString(R.string.devices_socket_electric_watt));

	}

	private String SecondToTime(int time) {
		System.out.println("seconds--->:" + time);
		String time_str;
		String[] time_arr = this.getResources().getStringArray(R.array.time);
		int d = time / (3600 * 24);
		int h = (time % (3600 * 24)) / 3600;
		int m = (time % 3600) / 60;
		int s = (time % 3600) % 60;
		time_str = d + time_arr[0] + h + time_arr[1] + m + time_arr[2] + s + time_arr[3];
		return time_str;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backBtnInTopLayout:
			finish();
			break;
		case R.id.work_set_rl:
			onPowerSet();
			break;
		default:
			break;
		}
	}
	public boolean SetTextView(int id, String text) {
	TextView et = (TextView) this.findViewById(id);
	if (et == null) {
		return false;
	}
	et.setText(text);
	return true;
}
	private void onPowerSet() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.activity_socket_input, null);
		final EditText edit = (EditText) view.findViewById(R.id.editText);

		new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.devices_socket_power_set))// 提示框标题
				.setView(view).setPositiveButton(this.getResources().getString(R.string.common_confirm),// 提示框的两个按钮
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String input = edit.getText().toString();
								if (StringUtils.contrast(input, "")) {
									Toast.makeText(
											ActivityGuideDevicesSocketAboutWork.this,
											view.getContext().getResources().getString(R.string.devices_socket_input_null) 
											+ input,
											Toast.LENGTH_SHORT).show();
								} else {
									modifyDevPow = edit.getText().toString();

									try {
										pow = Integer.parseInt(modifyDevPow);
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (pow < 1) {
										Toast.makeText(
												ActivityGuideDevicesSocketAboutWork.this,
												view.getContext().getResources().getString(R.string.devices_socket_input_one) ,
												Toast.LENGTH_SHORT)
												.show();
										return;
									}

									onPowerSet(pow + "");

								}
							}
						}).setNegativeButton(this.getResources().getString(R.string.device_sport_camera_record_success_cancel), null).create().show();

	}

	private void onPowerSet(String power) {
		PowerSocketWorkRecord mSocketWorkRecord = (PowerSocketWorkRecord) mFunDevice
				.getConfig(PowerSocketWorkRecord.CONFIG_NAME);
		mSocketWorkRecord.setDevicePower(pow);
		FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
				mSocketWorkRecord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lib.IFunSDKResult#OnFunSDKResult(android.os.Message,
	 * com.lib.MsgContent)
	 */

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
		if (PowerSocketWorkRecord.CONFIG_NAME.equals(configName)) {
			// 添加数据
			PowerSocketWorkRecord mSocketWorkRecord = (PowerSocketWorkRecord) mFunDevice
					.getConfig(PowerSocketWorkRecord.CONFIG_NAME);
			TotalEnergy = mSocketWorkRecord.getTotalEnergy();
			EnergyOfThisMon = mSocketWorkRecord.getEnergyOfThisMon();
			EnergyRecently = mSocketWorkRecord.getEnergyRecently();
			TotalTime = mSocketWorkRecord.getTotalTime();
			TimeOfThisMon = mSocketWorkRecord.getTimeOfThisMon();
			TimeRecently = mSocketWorkRecord.getTimeRecently();
			DeviceType = mSocketWorkRecord.getDeviceType();
			DevicePower = mSocketWorkRecord.getDevicePower();
			initLayout();
		}
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {

	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		if (PowerSocketWorkRecord.CONFIG_NAME.equals(configName)) {
			PowerSocketWorkRecord mSocketWorkRecord = (PowerSocketWorkRecord) mFunDevice
					.getConfig(PowerSocketWorkRecord.CONFIG_NAME);
			DevicePower = mSocketWorkRecord.getDevicePower();
			initLayout();
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
