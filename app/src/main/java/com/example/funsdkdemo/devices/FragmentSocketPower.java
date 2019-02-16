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
import com.lib.funsdk.support.config.PowerSocketAutoSwitch;
import com.lib.funsdk.support.models.FunDevice;

public class FragmentSocketPower extends FragmentSocketBase implements
		OnClickListener {
	ListView powerListView;
	SocketTaskAdapter powerTaskAdapter;

	@Override
	protected View MyOnCreate(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_socket_power, container,
				false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		setControllerBackground(getResources()
				.getColor(R.color.bg_socket_power));
		setRoundImage(R.drawable.socket_round_power);

		return view;
	}

	@Override
	protected void initLayout() {
		powerListView = (ListView) mLayout
				.findViewById(R.id.socket_power_task_lv);
		powerTaskAdapter = new SocketTaskAdapter(getActivity(), null);
		powerListView.setAdapter(powerTaskAdapter);
		powerListView.setOnItemClickListener(mOnItemClickListener);
		powerListView.setOnItemLongClickListener(mOnItemLongClickListener);
		mLayout.findViewById(R.id.add_iv).setOnClickListener(this);
		mRadioGroup.setVisibility(View.VISIBLE);
		mRadioBtnOn.setVisibility(View.VISIBLE);
		mRadioBtnOff.setVisibility(View.VISIBLE);
		mRadioBtnTiming.setVisibility(View.VISIBLE);
		mRadioBtnSensor.setVisibility(View.GONE);

	}

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
		PowerSocketAutoSwitch autoSwitch = (PowerSocketAutoSwitch) mFunDevice
				.getConfig(PowerSocketAutoSwitch.CONFIG_NAME);
		if (null != autoSwitch && position < autoSwitch.getAutoTimes().size()) {
			autoSwitch.getAutoTimes().remove(position);
			powerTaskAdapter.removeItem(position);
			powerTaskAdapter.notifyDataSetChanged();
			
			showWaitDialog();
			
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,
					autoSwitch);
		}
	}

	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FunDevice funDevice = ((ActivityGuideDeviceSocket) getActivity())
					.getFunDevice();
			Intent it = new Intent(getActivity(), ActivityGuideDevicesSocketSetTask.class);
			it.putExtra("FUN_DEVICE_ID", funDevice.getId());
			it.putExtra("FUNCTION_TYPE", ActivityGuideDevicesSocketSetTask.SOCKET_POWER);
			it.putExtra("FUNCTION_POSITION", position);
			it.putExtra("TITLE", 0);
			startActivityForResult(it, 0);
		}
	};

	private void startToAddAotuTime(int position) {
		FunDevice funDevice = ((ActivityGuideDeviceSocket) getActivity())
				.getFunDevice();
		Intent it = new Intent(getActivity(), ActivityGuideDevicesSocketSetTask.class);
		it.putExtra("FUN_DEVICE_ID", funDevice.getId());
		it.putExtra("FUNCTION_TYPE", ActivityGuideDevicesSocketSetTask.SOCKET_POWER);
		it.putExtra("FUNCTION_POSITION", position);
		startActivityForResult(it, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == -1) {
				powerTaskAdapter.notifyDataSetChanged();
			}
			break;
		case 2:
			if (resultCode == -1) {
				powerTaskAdapter.notifyDataSetChanged();
				break;
			}
		default:
			break;
		}
	}

	@Override
	protected void refreshLayout() {
		OPPowerSocketGet opps = getOPPowerSocketGet();
		if (null == opps || null == mRadioGroup) {
			return;
		}

		if (1 == opps.getAutoSwitch()) {
			// 定时打开
			mRadioBtnTiming.setChecked(true);
		} else if (1 == opps.getSwitchPower()) {
			// 电源开
			mRadioBtnOn.setChecked(true);
		} else {
			// 电源关
			mRadioBtnOff.setChecked(true);
		}

		setRoundImageSelected((1 == opps.getSwitchPower()));
		if (null != mFunDevice) {
			PowerSocketAutoSwitch autoSwitch = (PowerSocketAutoSwitch) mFunDevice
					.getConfig(PowerSocketAutoSwitch.CONFIG_NAME);
			if (null != autoSwitch) {
				powerTaskAdapter.setDataList(autoSwitch.getAutoTimes());
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		OPPowerSocketGet opps = getOPPowerSocketGet();
		if (null == opps || null == mRadioGroup) {
			return;
		}

		if (checkedId == mRadioBtnOn.getId()) {
			if (1 != opps.getSwitchPower() || 1 == opps.getAutoSwitch()) {
				setSwitchOn();
			}
		} else if (checkedId == mRadioBtnOff.getId()) {
			if (0 != opps.getSwitchPower() || 1 == opps.getAutoSwitch()) {
				setSwitchOff();
			}
		} else if (checkedId == mRadioBtnTiming.getId()) {
			if (1 != opps.getAutoSwitch()) {
				setSwitchTiming();
			}
		} else if (checkedId == mRadioBtnSensor.getId()) {

		} 
		
	}

	private void setSwitchOn() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if (null != opps) {
			opps.setSwitchPower(1);
			setOPPowerSocketGet(opps);
		}
	}

	private void setSwitchOff() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if (null != opps) {
			opps.setSwitchPower(0);
			setOPPowerSocketGet(opps);
		}
	}

	private void setSwitchTiming() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if (null != opps) {
			opps.setAutoSwitch(1);
			setOPPowerSocketGet(opps);
		}
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
		if ( PowerSocketAutoSwitch.CONFIG_NAME.equals(configName) ) {
			// 保存插座信息成功
			hideWaitDialog();
			Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();  
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
