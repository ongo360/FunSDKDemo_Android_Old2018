package com.zbar.lib;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.funsdkdemo.R;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;
import com.zbar.lib.decode.ZBarID;

public class CaptureActivity extends Activity implements Callback,
		OnClickListener {

	private final int MESSAGE_OPEN_CAMERA_FAILED = 0x100;
	
	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private ImageView mImgTopMask = null;
	private ImageButton mBtnBack = null;

	private View mResultView;
	private Button mBtnOK;
	private Button mBtnRetry;
	private TextView mDeviceSnText;
	private ImageView barcodeImageView;
	
	private ImageView mQrLineView;
	private TextView mErrorNoCamera;
	
	private View mMaskLeft = null;
	private View mMaskRight = null;
	private View mMaskTop = null;
	private View mMaskBottom = null;

	private String mDeviceSN = null;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MESSAGE_OPEN_CAMERA_FAILED:
				{
//					ToastSNS.show(CaptureActivity.this, 
//							getResources().getString(R.string.msg_camera_framework_bug));
					if ( null != mQrLineView ) {
						if ( null != mQrLineView.getAnimation() ) {
							mQrLineView.getAnimation().cancel();
						}
						mQrLineView.setVisibility(View.GONE);
					}
					if ( null != mErrorNoCamera ) {
						mErrorNoCamera.setVisibility(View.VISIBLE);
					}
				}
				break;
			}
		}
		
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_qr_scan);
		
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		mImgTopMask = (ImageView)findViewById(R.id.top_mask);
		RelativeLayout.LayoutParams cropLp = (RelativeLayout.LayoutParams)mCropLayout.getLayoutParams();
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int cropSize = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels)*3/5;
		cropLp.width = cropSize;
		cropLp.height = cropSize;
		mCropLayout.setLayoutParams(cropLp);
		RelativeLayout.LayoutParams topMaskLp = (RelativeLayout.LayoutParams)mImgTopMask.getLayoutParams();
		topMaskLp.height = cropSize / 3;
		mImgTopMask.setLayoutParams(topMaskLp);

		mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
				0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
		mErrorNoCamera = (TextView) findViewById(R.id.capture_error_nocamera);

		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		((TextView)findViewById(R.id.textViewInTopLayout)).setText(R.string.scan_qr_code);
		
		mResultView = findViewById(R.id.result_view);

		mBtnOK = (Button) findViewById(R.id.result_button_ok);
		mBtnOK.setOnClickListener(this);

		mBtnRetry = (Button) findViewById(R.id.result_button_retry);
		mBtnRetry.setOnClickListener(this);

		mDeviceSnText = (TextView) findViewById(R.id.DeviceName);

		barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);

		mMaskTop = findViewById(R.id.top_mask);
		mMaskBottom = findViewById(R.id.bottom_mask);
		mMaskLeft = findViewById(R.id.left_mask);
		mMaskRight = findViewById(R.id.right_mask);
		
		showMask();
	}
	
	private void showMask() {
		mCropLayout.setVisibility(View.VISIBLE);
		mMaskTop.setVisibility(View.VISIBLE);
		mMaskBottom.setVisibility(View.VISIBLE);
		mMaskLeft.setVisibility(View.VISIBLE);
		mMaskRight.setVisibility(View.VISIBLE);
		mResultView.setVisibility(View.INVISIBLE);
	}
	
	private void hideMask() {
		mCropLayout.setVisibility(View.INVISIBLE);
		mMaskTop.setVisibility(View.INVISIBLE);
		mMaskBottom.setVisibility(View.INVISIBLE);
		mMaskLeft.setVisibility(View.INVISIBLE);
		mMaskRight.setVisibility(View.INVISIBLE);
		mResultView.setVisibility(View.VISIBLE);
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		startScan();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		stopScan();
		super.onDestroy();
	}
	
	private void startScan() {
		showMask();
		
		CameraManager.init(getApplication());
		inactivityTimer = new InactivityTimer(this);
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if ( hasSurface ) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}
	
	private void stopScan() {
		try {
			hideMask();
			
			if ( null != inactivityTimer ) {
				inactivityTimer.shutdown();
				inactivityTimer = null;
			}
			if (handler != null) {
				handler.quitSynchronously();
				handler = null;
			}
			
			if ( null != CameraManager.get() ) {
				CameraManager.get().closeDriver();
			}
		} catch ( Exception e) {
			
		}
	}

	public void handleDecode(String result, Bitmap barcode) {
		playBeepSoundAndVibrate();

		if (barcode != null) {
			barcodeImageView.setImageBitmap(barcode);
		}
		
		stopScan();

		mDeviceSN = result;
		
		if (result != null) {
			mDeviceSnText.setText(result);
		} else {
			mDeviceSnText.setText("");
			
			// 二维码不符合情况下,隐藏确认按钮
			mBtnOK.setVisibility(View.INVISIBLE);
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			if ( null == CameraManager.get() ) {
				if ( null != mHandler ) {
					mHandler.sendEmptyMessage(MESSAGE_OPEN_CAMERA_FAILED);
				}
			}
			
			if ( !CameraManager.get().openDriver(surfaceHolder) ) {
				if ( null != mHandler ) {
					mHandler.sendEmptyMessage(MESSAGE_OPEN_CAMERA_FAILED);
				}
				
				return;
			}

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
		} catch (Exception e) {
			if ( null != mHandler ) {
				mHandler.sendEmptyMessage(MESSAGE_OPEN_CAMERA_FAILED);
			}
			return;
		}
		
		try {
			if (handler == null) {
				handler = new CaptureActivityHandler(CaptureActivity.this);
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler = null;
			if ( null != mHandler ) {
				mHandler.sendEmptyMessage(MESSAGE_OPEN_CAMERA_FAILED);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == mBtnBack.getId()) {
			finish();
		} else if (v == mBtnOK) {
			
			if ( null != mDeviceSN ) {
				Intent resIntent = new Intent();
				resIntent.putExtra("SN", mDeviceSN);
				setResult(1, resIntent);
			}
			
			finish();
		} else if (v == mBtnRetry) {
			restartPreviewAfterDelay(0L);
		}
	}
	

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(ZBarID.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		mDeviceSnText.setText("");
		startScan();
	}

}