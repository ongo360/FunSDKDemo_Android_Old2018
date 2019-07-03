package com.example.funsdkdemo.alarm;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.ListAdapterDeviceAlarmInfo;
import com.example.funsdkdemo.R;
import com.example.funsdkdemo.devices.ActivityGuideDeviceLanAlarm;
import com.example.funsdkdemo.devices.settings.alarm.ActivityGuideDeviceSetupAlarm;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.Mps.MpsClient;
import com.lib.Mps.XPMS_SEARCH_ALARMINFO_REQ;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunAlarmNotification;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceAlarmListener;
import com.lib.funsdk.support.config.AlarmInfo;
import com.lib.funsdk.support.models.FunDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ActivityGuideDeviceAlarmResult extends ActivityDemo implements OnClickListener,
		OnFunDeviceAlarmListener, OnItemClickListener,IFunSDKResult {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private Button mBtnAlarmConfig = null;
	private Button mBtnLanAlarmTest = null;
	private ImageButton mSwitchAlarmNotification = null;
	
	private ListView mListView = null;
	private ListAdapterDeviceAlarmInfo mAdapter = null;
	
	private Button mBtnDateSelect = null;
	
	private FunDevice mFunDevice = null;
	
	private String mCurrDate = null;

	private int userId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_alarm_result);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_opt_alarm);
		
		mBtnAlarmConfig = (Button)findViewById(R.id.btnAlarmConfig);
		mBtnAlarmConfig.setOnClickListener(this);
		
		mBtnLanAlarmTest = (Button)findViewById(R.id.btnLanAlarmTest);
		mBtnLanAlarmTest.setOnClickListener(this);
		
		mSwitchAlarmNotification = (ImageButton)findViewById(R.id.btnSwitchAlarmNotify);
		mSwitchAlarmNotification.setOnClickListener(this);
		
		mListView = (ListView)findViewById(R.id.listAlarmInfo);
		mAdapter = new ListAdapterDeviceAlarmInfo(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
		mBtnDateSelect = (Button)findViewById(R.id.btnSelectDate);
		mBtnDateSelect.setOnClickListener(this);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mFunDevice = funDevice;
		
		refreshAlarmNotificationEnable();
		
		// 注册设备报警消息监听
		FunSupport.getInstance().registerOnFunDeviceAlarmListener(this);
		
		// 搜索当天的报警消息
		trySearchAlarm(null);

		userId = FunSDK.GetId(userId,this);
	}
	

	@Override
	protected void onDestroy() {
		
		// 注销监听
		FunSupport.getInstance().removeOnFunDeviceAlarmListener(this);
		
		// 释放资源
		if ( null != mAdapter ) {
			mAdapter.release();
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
		case R.id.btnAlarmConfig:
			{
				startAlarmConfig();
			}
			break;
		case R.id.btnLanAlarmTest:
			{
				startLanAlarmTest();
			}
			break;
		case R.id.btnSelectDate:
			{
				showDatePicker();
			}
			break;
		case R.id.btnSwitchAlarmNotify:
			{
				boolean enable = !mSwitchAlarmNotification.isSelected();
				mSwitchAlarmNotification.setSelected(enable);
				trySetAlarmNotificationEnable(enable);
			}
			break;
		}
	}
	
	private void startAlarmConfig() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityGuideDeviceSetupAlarm.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("FUN_DEVICE_ID", mFunDevice.getId());
		startActivity(intent);
	}
	
	private void startLanAlarmTest() {
		Intent intent = new Intent();
		intent.setClass(this, ActivityGuideDeviceLanAlarm.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("FUN_DEVICE_ID", mFunDevice.getId());
		startActivity(intent);
	}
	
	private void showDatePicker() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				Date date = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, month);
				c.set(Calendar.DAY_OF_MONTH, day);
				trySearchAlarm(c.getTime());
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}
	
	private void refreshAlarmNotificationEnable() {
		boolean enable = FunAlarmNotification.getInstance().getDeviceAlarmNotification(mFunDevice.getDevSn());
		mSwitchAlarmNotification.setSelected(enable);
	}

	private void initSearchInfo(XPMS_SEARCH_ALARMINFO_REQ info, Date date,
			int channel) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		info.st_02_StarTime.st_0_year = c.get(Calendar.YEAR);
		info.st_02_StarTime.st_1_month = c.get(Calendar.MONTH) + 1;
		info.st_02_StarTime.st_2_day = c.get(Calendar.DATE);
		info.st_02_StarTime.st_4_hour = 0;
		info.st_02_StarTime.st_5_minute = 0;
		info.st_02_StarTime.st_6_second = 0;
		info.st_03_EndTime.st_0_year = c.get(Calendar.YEAR);
		info.st_03_EndTime.st_1_month = c.get(Calendar.MONTH) + 1;
		info.st_03_EndTime.st_2_day = c.get(Calendar.DATE);
		info.st_03_EndTime.st_4_hour = 23;
		info.st_03_EndTime.st_5_minute = 59;
		info.st_03_EndTime.st_6_second = 59;
		
		// for test 05-10
//		info.st_02_StarTime.st_1_month = 5;
//		info.st_02_StarTime.st_2_day = 9;
//		info.st_03_EndTime.st_1_month = 5;
//		info.st_03_EndTime.st_2_day = 9;

		info.st_04_Channel = channel;
	}
	
	private void trySearchAlarm(Date date) {
		XPMS_SEARCH_ALARMINFO_REQ info = new XPMS_SEARCH_ALARMINFO_REQ();
		G.SetValue(info.st_00_Uuid, mFunDevice.getDevSn());
		
		if ( null == date ) {
			date = new Date();
		}
		
		initSearchInfo(info, date, -1);
		info.st_06_Number = 128;
		
		mCurrDate = String.format("%04d-%02d-%02d", 
				info.st_02_StarTime.st_0_year,
				info.st_02_StarTime.st_1_month,
				info.st_02_StarTime.st_2_day);
		
		mBtnDateSelect.setText(mCurrDate);
		
		
		showWaitDialog();
		
		FunSupport.getInstance().requestDeviceSearchAlarmInfo(mFunDevice, info);
	}
	
	private void trySetAlarmNotificationEnable(boolean enable) {
		// 保存设备报警通知状态(本地操作,不分设备类型)
		FunAlarmNotification.getInstance().setDeviceAlarmNotification(
				mFunDevice.getDevSn(), enable);
	}

	@Override
	public void onDeviceAlarmReceived(FunDevice funDevice, AlarmInfo alarmInfo) {

	}

	@Override
	public void onDeviceAlarmSearchSuccess(FunDevice funDevice,
			List<AlarmInfo> infos) {
		hideWaitDialog();
//		showToast(Integer.toString(infos.size()));
		if ( null != mAdapter ) {
			// 临时处理,现在取到的报警消息有错误的情况,先在这里做二次过滤,BUG修复后可以注释掉
			List<AlarmInfo> tmpInfos = new ArrayList<AlarmInfo>();
			for ( AlarmInfo alarmInfo : infos ) {
				Log.d("test", "alarmInfo.getDate() = [" + alarmInfo.getDate() + "] - [" + mCurrDate + "]");
				if ( alarmInfo.getDate().equals(mCurrDate) ) {
					tmpInfos.add(alarmInfo);
				}
			}
			
			if ( tmpInfos.size() == 0 ) {
				showToast(R.string.device_alarm_message_empty);
			}
			
			mAdapter.updateAlarmInfos(tmpInfos);
		}
	}


	@Override
	public void onDeviceAlarmSearchFailed(FunDevice funDevice, int errCode) {
		showToast(FunError.getErrorStr(errCode));
		hideWaitDialog();
	}

	@Override
	public void onDeviceLanAlarmReceived(FunDevice funDevice,
			AlarmInfo alarmInfo) {
		// TODO Auto-generated method stub
		
	}
    

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if ( arg0 == mListView && null != mAdapter ) {
			AlarmInfo alarmInfo = mAdapter.getAlarmInfo(position);
			if ( null != alarmInfo ) {
				String savePicPath = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg";
				MpsClient.DownloadAlarmImage(userId
						,mFunDevice.getDevSn()
						,savePicPath
						,alarmInfo.getOriginJson()
						,0
						,0
						,position);

				//目前云存储上的图片没有url地址
				String picUrl = alarmInfo.getPic();
				if ( null == picUrl ) {
					picUrl = "";
				}
				showToast(picUrl);
				Log.i("", "------picurl = " + picUrl);
			}
		}
	}


	@Override
	public int OnFunSDKResult(Message message, MsgContent msgContent) {
		switch (message.what) {
			case EUIMSG.MC_SearchAlarmPic:
				if (message.arg1 >= 0) {
					showToast(R.string.download_s);
				}else {
					showToast(R.string.download_f);
				}
				break;
		}
		return 0;
	}
}
