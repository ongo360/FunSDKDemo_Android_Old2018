package com.example.funsdkdemo.devices;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.ListAdapterDeviceComComands;
import com.example.funsdkdemo.ListAdapterDeviceComComands.ComCommand;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.OnFunDeviceSerialListener;
import com.lib.funsdk.support.config.PowerSocketBulb;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;


/**
 * Demo:情景灯泡
 * 1. 注册监听
 * 2. 登入设备  - 设备操作都需要先登录设备
 * 3. 获取当前灯泡信息
 * 4. 灯泡控制
 * 5. 注销监听
 * 6. 登出设备并退出
 * 
 */
public class ActivityGuideDeviceBulb extends ActivityDemo implements OnClickListener, OnFunDeviceOptListener, OnSeekBarChangeListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private ImageButton mImgBulb = null;
	private SeekBar mSeekBarRed = null;		// 红色
	private SeekBar mSeekBarGreen = null;	// 绿色
	private SeekBar mSeekBarBlue = null;	// 蓝色
	private SeekBar mSeekBarLuma = null;		// 亮度
	
	private FunDevice mFunDevice = null;
	
	private final int MESSAGE_MODIFY_POWERSOCKET_BLUB = 0x100;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_bulb);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		setNavagateRightButton(R.layout.imagebutton_settings);
		
		mImgBulb = (ImageButton)findViewById(R.id.imgBulb);
		mImgBulb.setOnClickListener(this);
		
		mSeekBarRed = (SeekBar)findViewById(R.id.seekColorRed);
		mSeekBarGreen = (SeekBar)findViewById(R.id.seekColorGreen);
		mSeekBarBlue = (SeekBar)findViewById(R.id.seekColorBlue);
		mSeekBarLuma = (SeekBar)findViewById(R.id.seekLuma);
		mSeekBarRed.setMax(100);
		mSeekBarGreen.setMax(100);
		mSeekBarBlue.setMax(100);
		mSeekBarLuma.setMax(100);
		mSeekBarRed.setOnSeekBarChangeListener(this);
		mSeekBarGreen.setOnSeekBarChangeListener(this);
		mSeekBarBlue.setOnSeekBarChangeListener(this);
		mSeekBarLuma.setOnSeekBarChangeListener(this);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mFunDevice = funDevice;
		
		mTextTitle.setText(mFunDevice.getDevName());
		
		// 1. 注册监听
		// 注册设备操作类监听(设备登录时需要)
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		// 2. 登入设备
		loginDevice();
	}
	

	@Override
	protected void onDestroy() {
		
		// 6. 注销监听
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);
		
		if ( null != mFunDevice ) {
			// 5. 关闭串口
			FunSupport.getInstance().transportSerialClose(mFunDevice);
			
			// 7. 登出设备并退出
			FunSupport.getInstance().requestDeviceLogout(mFunDevice);
		}
		
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
		case R.id.imgBulb:
			{
				mImgBulb.setSelected(!mImgBulb.isSelected());
				resetModifyInterval();
			}
			break;
		}
	}
	
	private void loginDevice() {
		showWaitDialog();
		
		FunSupport.getInstance().requestDeviceLogin(mFunDevice);
	}
	
	private void refreshBulbInfo() {
		if ( null != mFunDevice ) {
			PowerSocketBulb bulbInfo = (PowerSocketBulb)mFunDevice.getConfig(PowerSocketBulb.CONFIG_NAME);
			if ( null != bulbInfo ) {
				
				if ( bulbInfo.enable() ) {
					// 开
					mImgBulb.setSelected(true);
				} else {
					// 关
					mImgBulb.setSelected(false);
				}
				
				// R/G/B三色
				mSeekBarRed.setProgress(bulbInfo.getRed());
				mSeekBarGreen.setProgress(bulbInfo.getGreen());
				mSeekBarBlue.setProgress(bulbInfo.getBlue());
				mSeekBarLuma.setProgress(bulbInfo.getLuma());
			}
		}
	}
	
	/**
	 * 重置定时器,200毫秒之后再提交修改
	 */
	private void resetModifyInterval() {
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_MODIFY_POWERSOCKET_BLUB);
			mHandler.sendEmptyMessageDelayed(MESSAGE_MODIFY_POWERSOCKET_BLUB, 200);
		}
	}
	
	/**
	 * 提交灯泡参数修改
	 */
	private void requestModifyPowersocketBulb() {
		if ( null != mFunDevice ) {
			showWaitDialog();
			
			PowerSocketBulb bulbInfo = (PowerSocketBulb)mFunDevice.checkConfig(
					PowerSocketBulb.CONFIG_NAME);
			bulbInfo.setRed(mSeekBarRed.getProgress());
			bulbInfo.setGreen(mSeekBarGreen.getProgress());
			bulbInfo.setBlue(mSeekBarBlue.getProgress());
			bulbInfo.setLuma(mSeekBarLuma.getProgress());
			bulbInfo.setEnable(mImgBulb.isSelected());
			
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, bulbInfo);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_MODIFY_POWERSOCKET_BLUB:
				{
					requestModifyPowersocketBulb();
				}
				break;
			}
		}
		
	};
	
	@Override
	public void onDeviceLoginSuccess(FunDevice funDevice) {
		// 3. 打开串口  - 在设备登录成功之后才能打开串口
		hideWaitDialog();
		
		// 获取当前灯泡参数
		FunSupport.getInstance().requestDeviceConfig(
				mFunDevice, PowerSocketBulb.CONFIG_NAME);
	}


	@Override
	public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceGetConfigSuccess(FunDevice funDevice,
			String configName, int nSeq) {
		if ( null != mFunDevice 
				&& null != funDevice
				&& mFunDevice.getId() == funDevice.getId() ) {
			if ( PowerSocketBulb.CONFIG_NAME.equals(configName) ) {
				hideWaitDialog();
				
				refreshBulbInfo();
			}
		}
	}


	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceSetConfigSuccess(final FunDevice funDevice, 
			final String configName) {
		hideWaitDialog();
	}


	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
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
	public void onDeviceOptionSuccess(final FunDevice funDevice, final String option) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDeviceOptionFailed(final FunDevice funDevice, final String option, final Integer errCode) {
		showToast(FunError.getErrorStr(errCode) + "[" + option + "]");
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
	public void onProgressChanged(SeekBar seekBar, int progress,  
            boolean fromUser) {
		if ( fromUser ) {
			if ( seekBar.equals(mSeekBarRed)
					|| seekBar.equals(mSeekBarGreen)
					|| seekBar.equals(mSeekBarBlue)
					|| seekBar.equals(mSeekBarLuma) ) {
				resetModifyInterval();	// 需要提交修改
			}
		}
	}


	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

}
