package com.lib.funsdk.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TimeTextView extends TextView {
	public static final int PERIOD = 1000;
	private Timer mTimer;
	private byte[] mLock = new byte[1];
	private ScheduledExecutorService mService;
	private long mDevSysTime;

	public TimeTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TimeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public long getDevSysTime() {
		return mDevSysTime;
	}

	public void setDevSysTime(long devSysTime) {
		synchronized (mLock) {
			this.mDevSysTime = devSysTime;
		}
	}

	public void onStartTimer() {
		synchronized (mLock) {
			if (mTimer != null && mService != null) {
				mService.shutdown();
				mService = null;
				mTimer = null;
			}
			mTimer = new Timer();
			mService = Executors.newScheduledThreadPool(1);
			mService.scheduleAtFixedRate(mTimer, PERIOD, PERIOD,
					TimeUnit.MILLISECONDS);
		}
	}


	public void onStopTimer() {
		synchronized (mLock) {
			if (mTimer != null && mService != null) {
				mService.shutdown();
				mService = null;
				mTimer = null;
			}
		}
	}

	class Timer implements Runnable {

		@Override
		public void run() {
			synchronized (mLock) {
				mDevSysTime += PERIOD;
				TimeTextView.this.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(new Date(mDevSysTime)));
					}
				});
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		onStopTimer();
		super.onDetachedFromWindow();
	}
}
