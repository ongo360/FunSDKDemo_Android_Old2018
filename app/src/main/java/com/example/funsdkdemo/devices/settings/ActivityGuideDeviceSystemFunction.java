package com.example.funsdkdemo.devices.settings;



import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.adapter.ListAdapterSystemFunction;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.SystemFunction;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;


/**
 * Demo: 获取系统功能列表
 */
public class ActivityGuideDeviceSystemFunction extends ActivityDemo 
	implements OnClickListener, OnFunDeviceOptListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	private ExpandableListView mExListView = null;
	private ListAdapterSystemFunction mAdapter = null;
	
	private FunDevice mFunDevice = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_system_function);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_setup_system_function);
		
		mExListView = (ExpandableListView)findViewById(R.id.listSystemFunction);
		mAdapter = new ListAdapterSystemFunction(this);
		mExListView.setAdapter(mAdapter);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == mFunDevice ) {
			finish();
			return;
		}
		
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		requestSystemFunction();
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
		}
	}
	
	private void refreshSystemFunction() {
		if ( null != mFunDevice ) {
			SystemFunction systemFunc = (SystemFunction)mFunDevice.getConfig(SystemFunction.CONFIG_NAME);
			if ( null != systemFunc && null != mAdapter ) {
				mAdapter.setSystemFunctions(systemFunc.getFunctionAttrs());
			}
		}
	}
	
	/**
	 * 请求获取系统功能列表
	 */
	private void requestSystemFunction() {
		showWaitDialog();
		
		// 获取系统功能列表
		FunSupport.getInstance().requestDeviceConfig(
				mFunDevice, SystemFunction.CONFIG_NAME);
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
				&& ( SystemFunction.CONFIG_NAME.equals(configName) ) ) {
			hideWaitDialog();
			refreshSystemFunction();
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
		
	}


	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
		
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
