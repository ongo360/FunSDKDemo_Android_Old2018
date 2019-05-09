package com.example.funsdkdemo.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceAlarmListener;
import com.lib.funsdk.support.OnFunDeviceConnectListener;
import com.lib.funsdk.support.OnFunDeviceListener;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.AlarmInfo;
import com.lib.funsdk.support.models.FunDevStatus;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.util.List;


/**
 * Demo: 局域网报警(P2P点对点报警)功能
 * 局域网报警,需要设备登录之后才能接收到报警消息,所有必须保证设备处于登录状态
 *
 */
public class ServiceGuideLanAlarmNotification extends Service implements OnFunDeviceAlarmListener, OnFunDeviceOptListener, OnFunDeviceListener, OnFunDeviceConnectListener {

	private static final String TAG = "FunSupport.LanAlarm";
	
	private NotificationManager mNotifyManager = null;
	
	
	// 每隔10秒钟检查一下设备是否已经登录
	private final int CHECK_INTERVAL = 10000;
	
	private final int MESSAGE_CHECK_DEVICE_LOGIN_STATUS = 0x100;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mNotifyManager = (NotificationManager) getSystemService(
				Context.NOTIFICATION_SERVICE);
		
		// 启动局域网报警监听(SDK)
		startLanAlarmListener();
		
		// 启动报警消息接收监听(这个只是当前代码方便,统一在FunSupport中处理了报警消息,只要在此接收结果
		FunSupport.getInstance().registerOnFunDeviceAlarmListener(this);
		
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		FunSupport.getInstance().registerOnFunDeviceListener(this);
		
		FunSupport.getInstance().registerOnFunDeviceConnectListener(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
		resetCheckInterval();
	}

	@Override
	public void onDestroy() {
		
		// 注销监听
		FunSupport.getInstance().removeOnFunDeviceAlarmListener(this);
		
		super.onDestroy();
	}
	
	private void checkAllDeviceLogin() {
		
		// 登录所有的在线设备
		loginAllOnLineDevice();
		
		// 重置定时消息
		resetCheckInterval();
	}
	
	
	/**
	 * 重置定时器
	 */
	private void resetCheckInterval() {
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_CHECK_DEVICE_LOGIN_STATUS);
			mHandler.sendEmptyMessageDelayed(MESSAGE_CHECK_DEVICE_LOGIN_STATUS, CHECK_INTERVAL);
		}
	}

	private void notifyDeviceAlarm(FunDevice funDev,AlarmInfo alarmInfo) {
		//mNotifyManager.cancel(funDev.getId());
		if ( null == funDev ) {
			return;
		}
		
		FunLog.i(TAG, "notifyDeviceAlarm : " + funDev.getDevSn() + ", " + funDev.getId());

		String title = getResources().getString(R.string.device_alarm_notification);
		if (alarmInfo != null) {
			title = alarmInfo.getEvent();
		}
		
		Intent newIntent = new Intent(this, ActivityGuideDeviceAlarmResult.class);
		newIntent.putExtra("FUN_DEVICE_ID", funDev.getId());
		newIntent.putExtra("FUN_DEVICE_SN", funDev.getDevSn());
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 
				funDev.getId(), newIntent, Intent.FLAG_ACTIVITY_NEW_TASK);


        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo_app)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(funDev.getDevSn())
                .setContentIntent(pendingIntent)
                .setNumber(1);
        Notification notification = builder.build();

//		Notification notification = new Notification.Builder(this)
//				.setSmallIcon(R.drawable.logo_app)
//				.setTicker(title)
//				.setContentTitle(title)
//				.setContentText(funDev.getDevSn())
//				.setContentIntent(pendingIntent)
//				.setNumber(1)
//				.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
		mNotifyManager.notify(funDev.getId(), notification);
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_CHECK_DEVICE_LOGIN_STATUS:
				{
					checkAllDeviceLogin();
				}
				break;
			}
		}
		
	};
	
	/******************************************************************
	 * for Lan Alarm
	 * 如果需要所有设备接收局域网报警信息,请参考以下函数的调用
	 * loginDevice();
	 * startLanAlarmListener();
	 * setAlarmEnable();
	 */
	// 登录所有已经在线的设备
	private void loginAllOnLineDevice() {
		List<FunDevice> devices = FunSupport.getInstance().getDeviceList();
		for ( FunDevice device : devices ) {
			if ( !device.hasLogin() && ( device.devStatus == FunDevStatus.STATUS_ONLINE || device.hasConnected() ) ) {
				// 设备在线的,并且还没有登录的,重新登录
				loginDevice(device);
			} else if (device.devStatus == FunDevStatus.STATUS_UNKNOWN) {
				// 获取设备状态未知,请求获取设备状态
				requestDeviceStauts(device);
			}
		}
	}
	
	// 登录设备
	private void loginDevice(FunDevice funDevice) {
		if ( null != funDevice ) {
			FunSupport.getInstance().requestDeviceLogin(funDevice);
		}
	}
	
	// 请求获取设备状态
	private void requestDeviceStauts(FunDevice funDevice) {
		if ( null != funDevice ) {
			FunSupport.getInstance().requestDeviceStatus(funDevice);
		}
	}
	
	// 启动局域网报警监听,设置一次即可,如果不设置,接收不到局域网报警信息
	private void startLanAlarmListener() {
		FunSupport.getInstance().startDeviceLanAlarmListener();
	}
	
	// 打开/关闭设备的局域网报警功能
	private void setAlarmEnable(FunDevice funDevice, 
			boolean enable) {
		if ( null != funDevice ) {
			FunLog.i(TAG, "setAlarmEnable : " + funDevice.getDevSn() + " -> " + enable);
			FunSupport.getInstance().requestDeviceLanAlarmEnable(
					funDevice.getDevSn(), enable);
		}
	}
	
	/**
	 * ****************************************************************
	 */

	@Override
	public void onDeviceAlarmReceived(FunDevice funDevice, AlarmInfo alarmInfo) {

	}

	@Override
	public void onDeviceAlarmSearchSuccess(FunDevice funDevice,
			List<AlarmInfo> infos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceAlarmSearchFailed(FunDevice funDevice, int errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceLanAlarmReceived(FunDevice funDevice,
			AlarmInfo alarmInfo) {
		// 收到设备报警
		notifyDeviceAlarm(funDevice,alarmInfo);
	}

	@Override
	public void onDeviceLoginSuccess(FunDevice funDevice) {
		/*****************************************************************
		 * for Lan Alarm
		 * 以下代码,如果需要局域网报警功能的时候调用
		 */
		if ( null != funDevice ) {
			setAlarmEnable(funDevice, true);
		}
		/**
		 * ****************************************************************
		 */
	}

	@Override
	public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceGetConfigSuccess(FunDevice funDevice,
			String configName, int nSeq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		// TODO Auto-generated method stub
		
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
	public void onDeviceListChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceStatusChanged(FunDevice funDevice) {
		/*****************************************************************
		 * for Lan Alarm
		 * 以下代码,如果需要局域网报警功能的时候调用
		 */
		// 如果设备在线,先登录设备,登录成功之后启动设备局域网报警功能
		if ( null != funDevice 
				&& funDevice.devStatus == FunDevStatus.STATUS_ONLINE ) {
			loginDevice(funDevice);
		}
		/**
		 * ****************************************************************
		 */

	}

	@Override
	public void onDeviceAddedSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceAddedFailed(Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceRemovedSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceRemovedFailed(Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAPDeviceListChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLanDeviceListChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceReconnected(FunDevice funDevice) {
		if ( null != funDevice ) {
			setAlarmEnable(funDevice, true);
		}
	}

	@Override
	public void onDeviceDisconnected(FunDevice funDevice) {
		if ( null != funDevice ) {
			funDevice.devStatus = FunDevStatus.STATUS_OFFLINE;
			requestDeviceStauts(funDevice);
		}
	}

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}
	
}
