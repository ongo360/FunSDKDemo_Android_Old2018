package com.example.funsdkdemo.devices;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.common.UIFactory;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.OPPowerSocketGet;
import com.lib.funsdk.support.config.PowerSocketArm;
import com.lib.funsdk.support.config.PowerSocketAutoLight;
import com.lib.funsdk.support.config.PowerSocketAutoSwitch;
import com.lib.funsdk.support.config.PowerSocketAutoUsb;
import com.lib.funsdk.support.config.PowerSocketBanLed;
import com.lib.funsdk.support.config.PowerSocketImage;
import com.lib.funsdk.support.config.PowerSocketSensitive;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunDeviceSocket;
import com.lib.funsdk.support.utils.FileUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;


public class ActivityGuideDeviceSocket extends ActivityDemo implements OnClickListener, OnFunDeviceOptListener, OnPageChangeListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private ViewPager mViewPager = null;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private SocketViewPagerAdapter mViewPagerAdapter = null;
	
	private Button mBtnPageLeft;
	private Button mBtnPageRight;
	private ImageButton mBtnSetting;
	
	private FunDeviceSocket mFunDevice = null;
	
	private final int MESSAGE_DEVICE_CFG_UPDATED = 0x100;
	
	private String mFragmentName[];
	private String mFragmentShortName[];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_socket);
		
		mFragmentName = getResources().getStringArray(R.array.device_socket_fragment_name);
		mFragmentShortName = getResources().getStringArray(R.array.device_socket_fragment_short_name);
		
		mBtnPageLeft = (Button)findViewById(R.id.btnSocketPageLeft);
		mBtnPageRight = (Button)findViewById(R.id.btnSocketPageRight);
		UIFactory.setLeftDrawable(this, mBtnPageLeft, R.drawable.icon_arrow_left, 8, 10);
		UIFactory.setRightDrawable(this, mBtnPageRight, R.drawable.icon_arrow_right, 8, 10);
		mBtnPageLeft.setOnClickListener(this);
		mBtnPageRight.setOnClickListener(this);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			mFunDevice = null;
		} else {
			try {
				if ( funDevice instanceof FunDeviceSocket ) {
					mFunDevice = (FunDeviceSocket)funDevice;
				} else {
					// 传入的参数不是插座类的,不适合用这个Activity打开
					mFunDevice = null;
				}
			} catch (Exception e) {
				mFunDevice = null;
				e.printStackTrace();
			}
		}
		
		if ( null == mFunDevice ) {
			finish();
			return;
		}
		
		View v =setNavagateRightButton(R.layout.imagebutton_settings);
		mBtnSetting = (ImageButton)v.findViewById(R.id.btnSettings);
		mViewPager = (ViewPager)findViewById(R.id.socketViewPager);
		mViewPagerAdapter = new SocketViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		mBtnSetting.setOnClickListener(this);
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		mTextTitle.setText(mFunDevice.devName);
		
		initLayoutByDevice();
		
		loginDevice();
		//toGetAuthority();
	}
	

	@Override
	protected void onDestroy() {
		
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);
		
		if ( null != mFunDevice ) {
			FunSupport.getInstance().requestDeviceLogout(mFunDevice);
		}
		
		if ( null != mHandler ) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}

        //清除抓拍缓存
        FileUtils.cleanFolder(FunPath.PATH_CAPTURE_TEMP);

        super.onDestroy();
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSettings:
			{
				if ( null != mFunDevice ) {
					Intent intent=new Intent(this, ActivityGuideDeviceSocketSetting.class);
					intent.putExtra("FUN_DEVICE_ID", mFunDevice.getId());
					startActivity(intent);
				}
			}
			break;
		case R.id.backBtnInTopLayout:
			{
				// 返回/退出
				finish();
			}
			break;
		case R.id.btnSocketPageLeft:
			{
				if ( mViewPager.getCurrentItem() > 0 ) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1, true);
				}
			}
			break;
		case R.id.btnSocketPageRight:
			{
				if ( mViewPager.getCurrentItem() < mFragmentList.size() ) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
				}
			}
			break;
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}


	@Override
	public void onPageSelected(int position) {
		refreshTitle(position);
	}
	public FunDevice getFunDevice() {
		return mFunDevice;
	}
	private void refreshTitle(int position) {
		mTextTitle.setText(mFragmentName[position%mFragmentName.length]);
		if ( position > 0 ) {
			mBtnPageLeft.setText(mFragmentShortName[position-1]);
			mBtnPageLeft.setVisibility(View.VISIBLE);
		} else {
			mBtnPageLeft.setVisibility(View.INVISIBLE);
		}
		
		if ( position < mFragmentShortName.length - 1 ) {
			mBtnPageRight.setText(mFragmentShortName[position+1]);
			mBtnPageRight.setVisibility(View.VISIBLE);
		} else {
			mBtnPageRight.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initLayoutByDevice() {
		mFragmentList.clear();
		
		mFragmentList.add(new FragmentSocketPower());
		mFragmentList.add(new FragmentSocketUsb());
		mFragmentList.add(new FragmentSocketLight());
		mFragmentList.add(new FragmentSocketCapture());
		
		refreshLayout();
		
		refreshTitle(0);
		
		mViewPagerAdapter.notifyDataSetChanged();
	}
	
	private void refreshLayout() {
		for ( int i = 0; i < mFragmentList.size(); i ++ ) {
			((FragmentSocketBase)mFragmentList.get(i)).updateDevice(mFunDevice);
		}
	}
	
	private void mergeChangedConfig() {
		for ( int i = 0; i < mFragmentList.size(); i ++ ) {
			((FragmentSocketBase)mFragmentList.get(i)).mergeChangedConfig();
		}
	}
	
	private void cleanChangedConfig() {
		for ( int i = 0; i < mFragmentList.size(); i ++ ) {
			((FragmentSocketBase)mFragmentList.get(i)).cleanChangedConfig();
		}
	}
	
	void loginDevice() {
		showWaitDialog();
		
		FunSupport.getInstance().requestDeviceLogin(mFunDevice);
	}
	
	private void tryGetAuthority() {
		if ( !mFunDevice.hasGotConfig(OPPowerSocketGet.CONFIG_NAME) ) {
			showWaitDialog();
			
			// FunSupport.getInstance().requestDeviceConfig(mFunDevice);
		} else {
			
			hideWaitDialog();
		}
		// 不论是否获取过状态，在打开的时候都重新获取一次最新的
		FunSupport.getInstance().requestDeviceConfig(mFunDevice);
		
		FunSupport.getInstance().requestDeviceConfig(mFunDevice, OPPowerSocketGet.CONFIG_NAME);
		
		// 在这里获取有需要的配置（不需要的可以不获取）
		if ( !mFunDevice.hasGotConfig(PowerSocketAutoSwitch.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketAutoSwitch.CONFIG_NAME);
		}
		if ( !mFunDevice.hasGotConfig(PowerSocketAutoUsb.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketAutoUsb.CONFIG_NAME);
		}
		if ( !mFunDevice.hasGotConfig(PowerSocketAutoLight.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketAutoLight.CONFIG_NAME);
		}
		
		if ( !mFunDevice.hasGotConfig(PowerSocketImage.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketImage.CONFIG_NAME);
		}
		if ( !mFunDevice.hasGotConfig(PowerSocketSensitive.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketSensitive.CONFIG_NAME);
		}
		if ( !mFunDevice.hasGotConfig(PowerSocketArm.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketArm.CONFIG_NAME);
		}
		if ( !mFunDevice.hasGotConfig(PowerSocketBanLed.CONFIG_NAME) ) {
			FunSupport.getInstance().requestDeviceConfig(mFunDevice, PowerSocketBanLed.CONFIG_NAME);
		}
	}
	
	class SocketViewPagerAdapter extends FragmentPagerAdapter {
		public SocketViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragmentList == null ? 0 : mFragmentList.size();
		}
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_DEVICE_CFG_UPDATED:
				{
					refreshLayout();
				}
				break;
			}
		}
		
	};

	@Override
	public void onDeviceLoginSuccess(final FunDevice funDevice) {
		if ( null != mFunDevice && null != funDevice ) {
			if ( mFunDevice.getId() == funDevice.getId() ) {
				// 登录成功后尝试读取配置信息
				tryGetAuthority();
			}
		}
	}
	
	@Override
	public void onDeviceLoginFailed(final FunDevice funDevice, final Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceGetConfigSuccess(final FunDevice funDevice, final String configName, final int nSeq) {
		hideWaitDialog();
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_DEVICE_CFG_UPDATED);
			mHandler.sendEmptyMessageDelayed(MESSAGE_DEVICE_CFG_UPDATED, 100);
		}
	}


	@Override
	public void onDeviceGetConfigFailed(final FunDevice funDevice, final Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceSetConfigSuccess(final FunDevice funDevice,
			final String configName) {
		hideWaitDialog();
		mergeChangedConfig();
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_DEVICE_CFG_UPDATED);
			mHandler.sendEmptyMessageDelayed(MESSAGE_DEVICE_CFG_UPDATED, 100);
		}
	}


	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
		hideWaitDialog();
		cleanChangedConfig();
		showToast(FunError.getErrorStr(errCode));
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_DEVICE_CFG_UPDATED);
			mHandler.sendEmptyMessageDelayed(MESSAGE_DEVICE_CFG_UPDATED, 100);
		}
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
