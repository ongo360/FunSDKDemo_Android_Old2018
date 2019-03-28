package com.example.funsdkdemo.devices.settings;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.common.FileManagerActivity;
import com.example.common.UIFactory;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.ECONFIG;
import com.lib.EDEV_JSON_ID;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.JsonConfig;
import com.lib.funsdk.support.config.OPTimeQuery;
import com.lib.funsdk.support.config.OPTimeSetting;
import com.lib.funsdk.support.config.StatusNetInfo;
import com.lib.funsdk.support.config.SystemInfo;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.widget.TimeTextView;
import com.lib.sdk.bean.DefaultConfigBean;
import com.lib.sdk.bean.HandleConfigData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Demo: 设备信息/设备连接方式/设备升级
 * 前置: 设备已登录,此Demo不需要再重复登入设备
 * 1. 注册录像文件搜索结果监听 - 在搜索完成后以回调的方式返回
 * 2. 按照时间(日期)获取远程设备的录像列表  - onSearchFile()
 * 3. 在回调中处理录像列表结果 - onRequestRecordListSuccess()
 * 4. 播放录像文件  - playRecordVideo()
 * 5. 退出注销监听  - onDestroy()
 * @author Administrator
 */
public class ActivityGuideDeviceSystemInfo extends ActivityDemo 
	implements OnClickListener, OnFunDeviceOptListener, IFunSDKResult{

	private final String TAG = "ActivityGuideDeviceSystemInfo";
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private TextView mTextDevSn = null;
	private TextView mTextDevModel = null;
	private TextView mTextDevHWVer = null;
	private TextView mTextDevSWVer = null;
	private TextView mTextDevPubDate = null;
	private TextView mTextDevPubTime = null;
	private TextView mTextDevRunTime = null;
	private TextView mTextDevNatCode = null;
	private TextView mTextDevNatStatus = null;
	private ImageView mImgDevSNCode = null;
	private TextView mTextDevUpdate = null;
	private Button mBtnDefaltConfig = null;
	
	private FunDevice mFunDevice = null;
	private DefaultConfigBean mdefault = null;
	private Bitmap mQrCodeBmp = null;
	private int mHandler;
	private int checkUpgrade = -1;
	private boolean isUpdating = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_system_info);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_system_info);
		
		mTextDevSn = (TextView)findViewById(R.id.textDeviceSN);
		mTextDevModel = (TextView)findViewById(R.id.textDeviceModel);
		mTextDevHWVer = (TextView)findViewById(R.id.textDeviceHWVer);
		mTextDevSWVer = (TextView)findViewById(R.id.textDeviceSWVer);
		mTextDevPubDate = (TextView)findViewById(R.id.textDevicePubDate);
		mTextDevPubTime=(TextView)findViewById(R.id.textDevicePubTime);
		mTextDevPubTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mTextDevPubTime.getPaint().setAntiAlias(true);
		mTextDevPubTime.setClickable(true);
		mTextDevRunTime = (TextView)findViewById(R.id.textDeviceRunTime);
		mTextDevNatCode = (TextView)findViewById(R.id.textDeviceNatCode);
		mTextDevNatStatus = (TextView)findViewById(R.id.textDeviceNatStatus);
		mImgDevSNCode = (ImageView)findViewById(R.id.imgDeviceQRCode);
		mTextDevUpdate = (TextView) findViewById(R.id.textDeviceUpgrade);
        mBtnDefaltConfig = (Button) findViewById(R.id.defealtconfig);
        mBtnDefaltConfig.setOnClickListener(this);

		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == mFunDevice ) {
			finish();
			return;
		}
        mdefault = new DefaultConfigBean();

		
		mHandler = FunSDK.RegUser(this);
		
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		requestSystemInfo();
		
		FunSDK.DevCheckUpgrade(mHandler, mFunDevice.devSn, 0);
	}
	
//	public synchronized int GetId() {
//		_msgId = FunSDK.GetId(_msgId, (IFunSDKResult) this);
//		return _msgId;
//	}

	@Override
	protected void onDestroy() {
		
		// 注销监听
		FunSupport.getInstance().removeOnFunDeviceOptListener(this);
		
		if ( null != mQrCodeBmp ) {
			mQrCodeBmp.recycle();
			mQrCodeBmp = null;
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
            case R.id.defealtconfig:
            {
                showWaitDialog();
                DeviceDefaltConfig();
            }
            break;
            default:
                break;
		}
	}
	
	// 0: p2p 1:转发 2IP直连
	private int getConnectTypeStringId(int netConnectType) {
		if ( netConnectType == 0 ) {
			return R.string.device_net_connect_type_p2p;
		} else if ( netConnectType == 1 ) {
			return R.string.device_net_connect_type_transmit;
		} else if ( netConnectType == 2 ) {
			return R.string.device_net_connect_type_ip;
		} else if ( netConnectType == 5) {
			return R.string.device_net_connect_type_rps;
		}
		
		return R.string.device_net_connect_type_unknown;
	}
	
	private void refreshSystemInfo() {
		if ( null != mFunDevice ) {
			SystemInfo systemInfo = (SystemInfo)mFunDevice.getConfig(SystemInfo.CONFIG_NAME);
			if ( null != systemInfo ) {
				// 序列号
				mTextDevSn.setText(systemInfo.getSerialNo());
				
				// 设备型号
				mTextDevModel.setText(systemInfo.getHardware());
				
				// 硬件版本号
				mTextDevHWVer.setText(systemInfo.getHardwareVersion());
				
				// 软件版本号
				mTextDevSWVer.setText(systemInfo.getSoftwareVersion());
				
				// 发布时间
				mTextDevPubDate.setText(systemInfo.getBuildTime());
				
				// 设备运行时间
				mTextDevRunTime.setText(systemInfo.getDeviceRunTimeWithFormat());
				
				// 设备连接方式
				mTextDevNatCode.setText(getConnectTypeStringId(mFunDevice.getNetConnectType()));
				
				// 生成二维码
				Bitmap qrCodeBmp = UIFactory.createCode(
						systemInfo.getSerialNo(), 600, 0xff202020);
				if ( null != qrCodeBmp ) {
					if ( null != mQrCodeBmp ) {
						mQrCodeBmp.recycle();
					}
					mQrCodeBmp = qrCodeBmp;
					mImgDevSNCode.setImageBitmap(qrCodeBmp);
				}
			}
			
			StatusNetInfo netInfo = (StatusNetInfo)mFunDevice.getConfig(StatusNetInfo.CONFIG_NAME);
			if ( null != netInfo ) {
				mTextDevNatStatus.setText(netInfo.getNatStatus());
			}
			
			OPTimeQuery showDevtimeQuery = (OPTimeQuery) mFunDevice
					.getConfig(OPTimeQuery.CONFIG_NAME);		
			if (null != showDevtimeQuery) {
				String mOPTimeQuery = showDevtimeQuery.getOPTimeQuery();
				mTextDevPubTime.setText(mOPTimeQuery);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date;
				try {
					date = sdf.parse(mOPTimeQuery);
					((TimeTextView) mTextDevPubTime).setDevSysTime(date.getTime());
					((TimeTextView) mTextDevPubTime).onStartTimer();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 请求获取系统信息:SystemInfo/Status.NatInfo
	 */
	private void requestSystemInfo() {
		showWaitDialog();
		
		// 获取系统信息
		FunSupport.getInstance().requestDeviceConfig(
				mFunDevice, SystemInfo.CONFIG_NAME);
		
		// 获取NAT状态
		FunSupport.getInstance().requestDeviceConfig(
				mFunDevice, StatusNetInfo.CONFIG_NAME);
		
		// 获取时间
		FunSupport.getInstance().requestDeviceCmdGeneral(
				mFunDevice, new OPTimeQuery());
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
				&& ( SystemInfo.CONFIG_NAME.equals(configName) 
						|| StatusNetInfo.CONFIG_NAME.equals(configName)
						|| OPTimeQuery.CONFIG_NAME.equals(configName)) ) {
			hideWaitDialog();
			refreshSystemInfo();
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
		if ( OPTimeSetting.CONFIG_NAME.equals(configName) ) {
			hideWaitDialog();
			
			// 重新获取时间
			FunSupport.getInstance().requestDeviceCmdGeneral(
					mFunDevice, new OPTimeQuery());
		}
	}


	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
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
    
	public void syncTime(View v){
		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle(R.string.device_system_info_time_sync);
		builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
			
			@Override
            public void onClick(DialogInterface dialog, int which) {
				showWaitDialog();
				Calendar cal = Calendar.getInstance(Locale.getDefault());
				String sysTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault()).format(cal.getTime());
				syncDevZone(cal);
				syncDevTime(sysTime);
			}
		});
		
		builder.setNeutralButton(R.string.common_cancel, new DialogInterface.OnClickListener() {		
			@Override
            public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog ad = builder.create();
		ad.show();
	}

	//同步设备时间（这个时间同步 设备端如果开启了NTP服务器同步的话，
	// 这个设置是不起作通的，因为设备会到服务器那边同步时间，
	// 所以这个时候只需要同步时区就可以了）
	private void syncDevTime(String sysTime) {
		OPTimeSetting devtimeInfo = (OPTimeSetting)mFunDevice.checkConfig(OPTimeSetting.CONFIG_NAME);
		devtimeInfo.setmSysTime(sysTime);

		FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, devtimeInfo);
	}

	//同步设备时区
	private void syncDevZone(Calendar calendar) {
		if (calendar == null) {
			return;
		}
		float zoneOffset = (float) calendar.get(java.util.Calendar.ZONE_OFFSET);
		float zone = (float) (zoneOffset / 60.0 / 60.0 / 1000.0);// 时区，东时区数字为正，西时区为负
		FunSupport.getInstance().requestSyncDevZone(mFunDevice, (int) (-zone * 60));
	}

    private void DeviceDefaltConfig(){

        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle(R.string.device_system_info_defealtconfig);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

				mdefault.setAllConfig(1);
                FunSDK.DevSetConfigByJson(mHandler, mFunDevice.devSn, JsonConfig.OPERATION_DEFAULT_CONFIG, HandleConfigData.getSendData(JsonConfig.OPERATION_DEFAULT_CONFIG, "0x1", mdefault), -1 , 20000, mFunDevice.getId());
            }
        });

		builder.setNeutralButton(R.string.common_cancel, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
    }


    public void onUpdate(View v) {

        if (!isUpdating) {
            if (checkUpgrade == 0) {
                startActivityForResult(new Intent(ActivityGuideDeviceSystemInfo.this, FileManagerActivity.class), 0);
            }else if (checkUpgrade > 0){
                FunSDK.DevStartUpgrade(mHandler, mFunDevice.devSn,
                        checkUpgrade, 0);
            }
        }else {
            FunSDK.DevStopUpgrade(mHandler, mFunDevice.devSn, 0);
        }
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String path = data.getStringExtra("path");
            FunSDK.DevStartUpgradeByFile(mHandler, mFunDevice.devSn, path, 0);
        }
    }

    @Override
	public int OnFunSDKResult(Message msg, MsgContent msgContent) {
		// TODO Auto-generated method stub
        FunLog.d(TAG, "msg.what : " + msg.what);
        FunLog.d(TAG, "msg.arg1 : " + msg.arg1 + " [" + FunError.getErrorStr(msg.arg1) + "]");
        FunLog.d(TAG, "msg.arg2 : " + msg.arg2);
        if (null != msgContent) {
            FunLog.d(TAG, "msgContent.sender : " + msgContent.sender);
            FunLog.d(TAG, "msgContent.seq : " + msgContent.seq);
            FunLog.d(TAG, "msgContent.str : " + msgContent.str);
            FunLog.d(TAG, "msgContent.arg3 : " + msgContent.arg3);
            FunLog.d(TAG, "msgContent.pData : " + msgContent.pData);
        }

        hideWaitDialog();
		switch (msg.what) {
		 	case EUIMSG.DEV_CHECK_UPGRADE:
				if (msg.arg1 < 0) {
					mTextDevUpdate.setText(R.string.device_setup_devcheckupdate_failed);
					mTextDevUpdate.setEnabled(false);
					break;
				}
				checkUpgrade = msg.arg1;
				if (checkUpgrade == 0) {
					mTextDevUpdate.setText(R.string.device_setup_devcheckupdate_versionlast);
				} else {
					mTextDevUpdate.setText(R.string.device_setup_devcheckupdate_clickupdate);
					findViewById(R.id.device_update).setClickable(true);
				}
				/* 检查设备升级 */
				break;
				
			case EUIMSG.DEV_START_UPGRADE:
				
				if (msg.arg1 < 0) {
					Toast.makeText(this, "Failed to Update!!", Toast.LENGTH_LONG).show();
				}
				isUpdating = true;
				break;
			case EUIMSG.DEV_STOP_UPGRADE:
				if (msg.arg1 < 0) {
					Log.e("STOP_UPDATE", "Error");
				}
				isUpdating = false;
				break;
				
			case EUIMSG.DEV_ON_UPGRADE_PROGRESS:
				System.out.println("DEV_ON_UPGRADE_PROGRESS1:" + msg.arg1 + "fff:"
						+ msg.arg2);
				switch (msg.arg1) {
				case ECONFIG.EUPGRADE_STEP_DOWN:
						if (msg.arg2 < 0 || msg.arg2 > 100) {
							Log.e("devicedewnload", "Error");
						} else {
							mTextDevUpdate.setText("Downloading...." + Integer.toString(msg.arg2) + "%");
						}
					/* 正在下载升级包 */
					break;
				case ECONFIG.EUPGRADE_STEP_UP:
					if (msg.arg2 < 0 || msg.arg2 > 100){
						Log.e("deviceupdoad", "Error");
					}else {
                        mTextDevUpdate.setText("UpLoading...." + Integer.toString(msg.arg2) + "%");
                    }
					break;
				case ECONFIG.EUPGRADE_STEP_UPGRADE:{
						if (msg.arg2 < 0 || msg.arg2 > 100) {
							Log.e("deviceupdate", "Error");
						} else {
							mTextDevUpdate.setText("Updating...." + Integer.toString(msg.arg2) + "%");
						}
					/* 正在升级 */
					break;}
				case ECONFIG.EUPGRADE_STEP_COMPELETE:
					System.out.println("complete");
					if (msg.arg2 < 0) {
						System.out.println("complete1");
						Log.e("deviceupdatecomplete", "Error");
						break;
					}
					mTextDevUpdate.setText(R.string.device_setup_devcheckupdate_versionlast);
					checkUpgrade = 0;
					// getString(FunSDK.TS("complete)
					/* 升级完成 */
			 		break;
			 	default:
			 		break;
			 	}
				break;
            case EUIMSG.DEV_SET_JSON:
            {
                if (msg.arg1 < 0) {
                    showToast(FunError.getErrorStr(msg.arg1));
                }else {
                    if (msgContent.str.equals(JsonConfig.OPERATION_DEFAULT_CONFIG)) {
                        JSONObject object = new JSONObject();
						object.put("Action", "Reboot");
						FunSDK.DevCmdGeneral(mHandler, mFunDevice.devSn, EDEV_JSON_ID.OPMACHINE, JsonConfig.OPERATION_MACHINE, 1024, 5000,
								HandleConfigData.getSendData(JsonConfig.OPERATION_MACHINE, "0x1", object).getBytes(), -1, 0);
                        showToast(R.string.device_system_info_defaultconfigsucc);
                    }
                }
            }
                break;
			default:
				break;
		}

		return 0;
	}

	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}
	
	
}
