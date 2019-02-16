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
import com.lib.funsdk.support.config.PowerSocketAutoLight;
import com.lib.funsdk.support.models.FunDevice;

public class FragmentSocketLight extends FragmentSocketBase implements OnClickListener {
	ListView lightTaskListView;
	SocketTaskAdapter lightTaskAdapter;
	

	@Override
	protected View MyOnCreate(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_socket_power, 
				container, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  super.onCreateView(inflater, container, savedInstanceState);
		
		setControllerBackground(getResources().getColor(R.color.bg_socket_light));
		setRoundImage(R.drawable.socket_round_light);
		
		return view;
	}

	@Override
	protected void initLayout() {
		lightTaskListView=(ListView)mLayout.findViewById(R.id.socket_power_task_lv);
		lightTaskAdapter=new SocketTaskAdapter(getActivity(), null);
		lightTaskListView.setAdapter(lightTaskAdapter);
		lightTaskListView.setOnItemClickListener(mOnItemClickListener);
		lightTaskListView.setOnItemLongClickListener(mOnItemLongClickListener);
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
			it.putExtra("FUNCTION_TYPE", ActivityGuideDevicesSocketSetTask.SOCKET_LIGHT);
			it.putExtra("FUNCTION_POSITION", position);
			it.putExtra("TITLE", 0);
			startActivityForResult(it, 1);
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

		PowerSocketAutoLight autoLight = (PowerSocketAutoLight) mFunDevice
				.getConfig(PowerSocketAutoLight.CONFIG_NAME);
		if (null != autoLight && position < autoLight.getAutoTimes().size()) {
			autoLight.getAutoTimes().remove(position);
			lightTaskAdapter.removeItem(position);
			lightTaskAdapter.notifyDataSetChanged();
			showWaitDialog();
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice,autoLight);
		}


	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == -1) {
				lightTaskAdapter.notifyDataSetChanged();
			}
			break;
		case 2:
			if (resultCode == -1) {
				lightTaskAdapter.notifyDataSetChanged();
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
		
		if ( 1 == opps.getSensorLight() ) {
			// 自动感应
			mRadioBtnSensor.setChecked(true);
		} else if ( 1 == opps.getAutoLight() ) {
			// 定时打开
			mRadioBtnTiming.setChecked(true);
		} else if ( 1 == opps.getSwitchLight() ) {
			// 灯光开
			mRadioBtnOn.setChecked(true);
		} else {
			// 灯光关
			mRadioBtnOff.setChecked(true);
		}
		
		setRoundImageSelected((1 == opps.getSwitchLight()));
		if (null != mFunDevice) {
			PowerSocketAutoLight autoLight = (PowerSocketAutoLight) mFunDevice
					.getConfig(PowerSocketAutoLight.CONFIG_NAME);
			if (null != autoLight) {
				lightTaskAdapter.setDataList(autoLight.getAutoTimes());
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
			if ( 1 != opps.getSwitchLight()
					|| 1 == opps.getAutoLight()
					|| 1 == opps.getSensorLight() ) {
				setLightOn();
			}
		} else if ( checkedId == mRadioBtnOff.getId() ) {
			if ( 0 != opps.getSwitchLight()
					|| 1 == opps.getAutoLight()
					|| 1 == opps.getSensorLight() ) {
				setLightOff();
			}
		} else if ( checkedId == mRadioBtnTiming.getId() ) {
			if ( 1 != opps.getAutoLight() ) {
				setLightTiming();
			}
		} else if ( checkedId == mRadioBtnSensor.getId() ) {
			if ( 1 != opps.getSensorLight() ) {
				setLightSensorOn();
			}
		}
	}
	
	private void setLightOn() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setSwitchLight(1);
			setOPPowerSocketGet(opps);
		}
	}
	
	private void setLightOff() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setSwitchLight(0);
			setOPPowerSocketGet(opps);
		}
	}
	
	private void setLightTiming() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setAutoLight(1);
			setOPPowerSocketGet(opps);
		}
	}
	
	private void setLightSensorOn() {
		OPPowerSocketGet opps = copyOPPowerSocketGet();
		if ( null != opps ) {
			opps.setSensorLight(1);
			setOPPowerSocketGet(opps);
		}
	}

	private void startToAddAotuTime(int position) {
		FunDevice funDevice = ((ActivityGuideDeviceSocket) getActivity())
				.getFunDevice();
		Intent it = new Intent(getActivity(), ActivityGuideDevicesSocketSetTask.class);
		it.putExtra("FUN_DEVICE_ID", funDevice.getId());
		it.putExtra("FUNCTION_TYPE", ActivityGuideDevicesSocketSetTask.SOCKET_LIGHT);
		it.putExtra("FUNCTION_POSITION", position);
		startActivityForResult(it, 1);
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
		if ( PowerSocketAutoLight.CONFIG_NAME.equals(configName) ) {
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
