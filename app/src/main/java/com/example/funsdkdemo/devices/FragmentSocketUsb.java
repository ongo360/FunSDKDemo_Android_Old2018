package com.example.funsdkdemo.devices;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.SocketTaskAdapter;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.config.OPPowerSocketGet;
import com.lib.funsdk.support.config.PowerSocketAutoUsb;
import com.lib.funsdk.support.models.FunDevice;

public class FragmentSocketUsb extends FragmentSocketBase implements OnClickListener {
	ListView usbTaskListView;
	SocketTaskAdapter usbTaskAdapter;

	@Override
	protected View MyOnCreate(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_socket_power, 
				container, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		setControllerBackground(getResources().getColor(R.color.bg_socket_usb));
		setRoundImage(R.drawable.socket_round_usb);
		return view;
	}

	@Override
	protected void initLayout() {
		usbTaskListView = (ListView) mLayout
				.findViewById(R.id.socket_power_task_lv);
		usbTaskAdapter = new SocketTaskAdapter(getActivity(), null);
		usbTaskListView.setAdapter(usbTaskAdapter);
		usbTaskListView.setOnItemClickListener(mOnItemClickListener);
		usbTaskListView.setOnItemLongClickListener(mOnItemLongClickListener);
		mLayout.findViewById(R.id.add_iv).setOnClickListener(this);
		mRadioGroup.setVisibility(View.VISIBLE);
		mRadioBtnOn.setVisibility(View.VISIBLE);
		mRadioBtnOff.setVisibility(View.VISIBLE);
		mRadioBtnTiming.setVisibility(View.VISIBLE);
		mRadioBtnSensor.setVisibility(View.VISIBLE);
	}

	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FunDevice funDevice = ((ActivityGuideDeviceSocket) getActivity())
					.getFunDevice();
			Intent it = new Intent(getActivity(), ActivityGuideDevicesSocketSetTask.class);
			it.putExtra("FUN_DEVICE_ID", funDevice.getId());
			it.putExtra("FUNCTION_TYPE", ActivityGuideDevicesSocketSetTask.SOCKET_USB);
			it.putExtra("FUNCTION_POSITION", position);
			it.putExtra("TITLE", 0);
			startActivityForResult(it, 2);
		}
	};

	OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("删除任务");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							onDelete(position);
						}
					});

			builder.setNegativeButton("取消", null);

			builder.show();

			return true;
		}
	};

	public void onDelete(int position) {

		PowerSocketAutoUsb autoUsb = (PowerSocketAutoUsb) mFunDevice
				.getConfig(PowerSocketAutoUsb.CONFIG_NAME);
		if (null != autoUsb && position < autoUsb.getAutoTimes().size()) {
			autoUsb.getAutoTimes().remove(position);
			usbTaskAdapter.removeItem(position);
			usbTaskAdapter.notifyDataSetChanged();
			showWaitDialog();
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
					autoUsb);
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == -1) {
				usbTaskAdapter.notifyDataSetChanged();
			}
			break;
		case 2:
			if (resultCode == -1) {
				usbTaskAdapter.notifyDataSetChanged();
				break;
			}
		default:
			break;
		}
	}

	@Override
	protected void refreshLayout() {
		OPPowerSocketGet opps = getOPPowerSocketGet();
		if ( null == opps || null == mRadioGroup ) {
			return;
		}
		
		if ( 1 == opps.getSensorUsbPower() ) {
			// 自动感应
			mRadioBtnSensor.setChecked(true);
		} else if ( 1 == opps.getAutoUSB() ) {
			// 定时打开
			mRadioBtnTiming.setChecked(true);
		} else if ( 1 == opps.getSwitchUSB() ) {
			// USB开
			mRadioBtnOn.setChecked(true);
		} else {
			// USB关
			mRadioBtnOff.setChecked(true);
		}
		
		setRoundImageSelected((1 == opps.getSwitchUSB()));
		if (null != mFunDevice) {
			PowerSocketAutoUsb autoUsb = (PowerSocketAutoUsb) mFunDevice
					.getConfig(PowerSocketAutoUsb.CONFIG_NAME);
			if (null != autoUsb) {
				usbTaskAdapter.setDataList(autoUsb.getAutoTimes());
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		OPPowerSocketGet opps = getOPPowerSocketGet();
		if ( null == opps || null == mRadioGroup ) {
			return;
		}
		
		if ( checkedId == mRadioBtnOn.getId() ) {
			if ( 1 != opps.getSwitchUSB()
					|| 1 == opps.getAutoUSB()
					|| 1 == opps.getSensorUsbPower() ) {
				setUsbOn();
			}
		} else if ( checkedId == mRadioBtnOff.getId() ) {
			if ( 0 != opps.getSwitchUSB()
					|| 1 == opps.getAutoUSB()
					|| 1 == opps.getSensorUsbPower() ) {
				setUsbOff();
			}
		} else if ( checkedId == mRadioBtnTiming.getId() ) {
			if ( 1 != opps.getAutoUSB() ) {
				setUsbTiming();
			}
		} else if ( checkedId == mRadioBtnSensor.getId() ) {
			if ( 1 != opps.getSensorUsbPower() ) {
				setUsbSensorOn();
			}
			}
		
	}
	
	private void setUsbOn() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setSwitchUSB(1);
			setOPPowerSocketGet(opps);
		}
	}
	
	private void setUsbOff() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setSwitchUSB(0);
			setOPPowerSocketGet(opps);
		}
	}
	
	private void setUsbTiming() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setAutoUSB(1);
			setOPPowerSocketGet(opps);
		}
	}
	
	private void setUsbSensorOn() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setSensorUsbPower(1);
			setOPPowerSocketGet(opps);
		}
	}


	private void startToAddAotuTime(int position) {
		FunDevice funDevice = ((ActivityGuideDeviceSocket) getActivity())
				.getFunDevice();
		Intent it = new Intent(getActivity(), ActivityGuideDevicesSocketSetTask.class);
		it.putExtra("FUN_DEVICE_ID", funDevice.getId());
		it.putExtra("FUNCTION_TYPE", ActivityGuideDevicesSocketSetTask.SOCKET_USB);
		it.putExtra("FUNCTION_POSITION", position);
		startActivityForResult(it, 2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_iv:
			startToAddAotuTime(-1);
			break;
		}
	}
	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		if ( PowerSocketAutoUsb.CONFIG_NAME.equals(configName) ) {
			// 保存插座信息成功
			Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();  
			hideWaitDialog();
		}
	}
	
	@Override
	public void onDeviceSetConfigFailed(FunDevice funDevice, String configName,
			Integer errCode) {
		// 保存插座信息失败
		showToast(FunError.getErrorStr(errCode));
	}

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}	
}
