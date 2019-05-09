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
import android.widget.Toast;

import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunAlarmNotification;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceAlarmListener;
import com.lib.funsdk.support.OnFunLoginListener;
import com.lib.funsdk.support.config.AlarmInfo;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.bean.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ServiceGuidePushAlarmNotification extends Service implements OnFunLoginListener, OnFunDeviceAlarmListener {

	private static final String TAG = "FunSupport.Alarm";
	
	
	private NotificationManager mNotifyManager = null;
	
	private boolean mHasStarted = false;
	
	private List<String> mLinkedDevSn = new ArrayList<String>();
	
	private final int MESSAGE_CHECK_ALARM_STATUS = 0x100;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mNotifyManager = (NotificationManager) getSystemService(
				Context.NOTIFICATION_SERVICE);
		
		// 添加用户登录监听
		FunSupport.getInstance().registerOnFunLoginListener(this);
		
		// 添加设备报警监听
		FunSupport.getInstance().registerOnFunDeviceAlarmListener(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
		startMps();
		
		resetCheckInterval();
	}

	@Override
	public void onDestroy() {
		
		FunSupport.getInstance().removeOnFunDeviceAlarmListener(this);
		
		FunSupport.getInstance().removeOnFunLoginListener(this);
		
		super.onDestroy();
	}

	/**
	 * 初始化消息推送
	 */
	private void startMps() {
		
		if ( !FunSupport.getInstance().hasLogin() ) {
			// 只有用户登录了之后才进行消息推送监听
			return ;
		}
		
		if ( mHasStarted ) {
			// 已经启动了,无需重复启动
			return;
		}
		
		checkLinkDevices();
		
		mHasStarted = true;
	}
	
	private void stopMps() {
		clearLinkDevices();
		
		mHasStarted = false;
	}
	
	private void checkMps() {
		
		if ( !mHasStarted
				&& FunSupport.getInstance().hasLogin() ) {
			startMps();
		} else if ( mHasStarted 
				&& !FunSupport.getInstance().hasLogin() ) {
			stopMps();
		} else if ( mHasStarted ) {
			checkLinkDevices();
		}
		
		resetCheckInterval();
	}
	
	/**
	 * 获取消息通知是否使能
	 * @param devMac
	 * @return
	 */
	private boolean enabelNotification(String devMac) {
		return FunAlarmNotification.getInstance().getDeviceAlarmNotification(devMac);
	}
	
	private void checkLinkDevices() {
		List<FunDevice> devList = new ArrayList<FunDevice>();
		devList.addAll(FunSupport.getInstance().getDeviceList());
		
		synchronized (mLinkedDevSn) {
			// Unlink已经不存在的设备
			for ( int i = mLinkedDevSn.size()-1; i >= 0; i -- ) {
				String devSn = mLinkedDevSn.get(i);
				if ( null == FunSupport.getInstance().findDeviceBySn(devSn)
						|| !enabelNotification(devSn) ) {
					
					FunLog.i(TAG, "unlink : " + devSn);
					
					// 取消设备报警(报警服务器)
					FunSupport.getInstance().mpsUnLinkDevice(devSn);
					
					// 从列表中移除
					mLinkedDevSn.remove(i);
				}
			}
			
			// 添加还没有的Linked的设备
			for ( FunDevice dev : devList ) {
				String devSn = dev.getDevSn();
				if ( !mLinkedDevSn.contains(devSn)
						&& enabelNotification(devSn) ) {
					
					FunLog.i(TAG, "link : " + devSn);

					// 添加设备报警(报警服务器支持)
					FunSupport.getInstance().mpsLinkDevice(dev);
					
					mLinkedDevSn.add(devSn);
				}
			}
		}
	}
	
	private void clearLinkDevices() {
		synchronized (mLinkedDevSn) {
			for ( String devSn : mLinkedDevSn ) {
				FunSupport.getInstance().mpsUnLinkDevice(devSn);
			}
			mLinkedDevSn.clear();
		}
	}
	
	private void resetCheckInterval() {
		if ( null != mHandler ) {
			mHandler.removeMessages(MESSAGE_CHECK_ALARM_STATUS);
			mHandler.sendEmptyMessageDelayed(MESSAGE_CHECK_ALARM_STATUS, 1000);
		}
	}
	
	private void notifyDeviceAlarm(FunDevice funDev,AlarmInfo alarmInfo) {
		//mNotifyManager.cancel(funDev.getId());
		if ( null == funDev) {
			return;
		}
		
		FunLog.i(TAG, "notifyDeviceAlarm : " + funDev.getDevSn() + ", " + funDev.getId());
		String title = getResources().getString(R.string.device_alarm_notification);
		if (alarmInfo.getLinkCenterExt() != null) {
			/**
			 * 智联中心推送
			 */
			if(StringUtils.contrast(alarmInfo.getEvent(),"433Alarm")
					|| StringUtils.contrast(alarmInfo.getEvent(),"ConsSensorAlarm")){ //普通433传感器推送
				title = alarmInfo.getEvent();
			} else if(StringUtils.contrast(alarmInfo.getEvent(),"DoorLockNotify")
					|| StringUtils.contrast(alarmInfo.getEvent(),"DoorLockAlarm")
					|| StringUtils.contrast(alarmInfo.getEvent(),"DoorLockCall")){ //门锁报警
				if(StringUtils.contrast(alarmInfo.getEvent(),"DoorLockNotify")
						|| StringUtils.contrast(alarmInfo.getEvent(),"DoorLockCall")) {
					title = alarmInfo.getEvent() + "(Notification message)";
				}else if (StringUtils.contrast(alarmInfo.getEvent(),"DoorLockAlarm")) {
					title = alarmInfo.getEvent() + "(Exception message)";
				}
			}
			title += alarmInfo.getLinkCenterExt().toString();
		}else {
			if (StringUtils.contrast(alarmInfo.getEvent(), "VideoMotion")) {
				title = "Motion Detection";
			} else if (StringUtils.contrast(alarmInfo.getEvent(), "VideoAnalyze")) {
				title = "intelligent analysis";
			}
		}
		Toast.makeText(this,title,Toast.LENGTH_LONG).show();
		Intent newIntent = new Intent(this, ActivityGuideDeviceAlarmResult.class);
		newIntent.putExtra("FUN_DEVICE_ID", funDev.getId());
		newIntent.putExtra("FUN_DEVICE_SN", funDev.getDevSn());
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 
				funDev.getId(), newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo_app)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(funDev.getDevSn())
                .setContentIntent(pendingIntent)
                .setNumber(1);
        Notification notification = builder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
		mNotifyManager.notify(funDev.getId(), notification);
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_CHECK_ALARM_STATUS:
				{
					checkMps();
				}
				break;
			}
		}
		
	};
	
	@Override
	public void onLoginSuccess() {
		// 用户登录成功, 启动报警推送消息检测
		resetCheckInterval();
	}

	@Override
	public void onLoginFailed(Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout() {
		// 用户登出后,立刻停止报警推送消息检测
		stopMps();
	}
	
	@Override
	public void onDeviceAlarmReceived(FunDevice funDevice,AlarmInfo alarmInfo) {
		// 收到设备报警
		notifyDeviceAlarm(funDevice,alarmInfo);
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
		//notifyDeviceAlarm(funDevice);
	}
	
}
