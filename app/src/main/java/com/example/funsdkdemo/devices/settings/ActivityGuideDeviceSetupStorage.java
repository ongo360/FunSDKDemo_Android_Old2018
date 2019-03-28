package com.example.funsdkdemo.devices.settings;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.SDKCONST.SDK_FileSystemDriverTypes;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.GeneralGeneral;
import com.lib.funsdk.support.config.GeneralGeneral.OverWriteType;
import com.lib.funsdk.support.config.OPStorageManager;
import com.lib.funsdk.support.config.StorageInfo;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.FileUtils;
import com.lib.funsdk.support.utils.MyUtils;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;



/**
 * Demo: 图像配置
 *
 */
public class ActivityGuideDeviceSetupStorage extends ActivityDemo implements OnClickListener, OnFunDeviceOptListener, OnItemSelectedListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	// 存储容量
	private TextView mTextStorageCapacity = null;
	// 录像分区
	private TextView mTextStorageRecord = null;
	// 图片分区
	private TextView mTextStorageSnapshot = null;
	// 剩余容量
	private TextView mTextStorageRemain = null;
	// 停止播放
	private RadioButton mRbRecordStop=null;
	// 循环播放
	private RadioButton mRbRecordCycle=null;
	// 格式化
	private Button mBtnFormat = null;
	
	private FunDevice mFunDevice = null;
	
	private OPStorageManager mOPStorageManager;
	
	/**
	 * 本界面需要获取到的设备配置信息
	 */
	private final String[] DEV_CONFIGS = {
			// SD卡存储容量信息
			StorageInfo.CONFIG_NAME,
			
			// 录像满时停止录像或循环录像
			GeneralGeneral.CONFIG_NAME
	};
	
	// 设置配置信息的时候,由于有多个,通过下面的列表来判断是否所有的配置都设置完成了
	private List<String> mSettingConfigs = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_setup_storage);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_setup_storage);
		
		mTextStorageCapacity = (TextView)findViewById(R.id.textStorageCapacity);
		mTextStorageRecord = (TextView)findViewById(R.id.textStorageRecord);
		mTextStorageSnapshot = (TextView)findViewById(R.id.textStorageSnapshot);
		mTextStorageRemain = (TextView)findViewById(R.id.textStorageRemain);
		mRbRecordStop=(RadioButton)findViewById(R.id.rbRecordStop);
		mRbRecordStop.setOnClickListener(this);
		mRbRecordCycle=(RadioButton)findViewById(R.id.rbRecordCycle);
		mRbRecordCycle.setOnClickListener(this);
		mBtnFormat = (Button)findViewById(R.id.btnStorageFormat);
		mBtnFormat.setOnClickListener(this);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mFunDevice = funDevice;
		
		// 注册设备操作监听
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		
		// 获取报警配置信息
		tryGetStorageConfig();
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
		case R.id.rbRecordStop:
			{
				trySetOverWrite(false);
			}
			break;
		case R.id.rbRecordCycle:
			{
				trySetOverWrite(true);
			}
		break;
		case R.id.btnStorageFormat:		// 格式化
			{
				showFormatPartitionDialog();
			}
			break;
		}
	}
	
	
	private boolean isCurrentUsefulConfig(String configName) {
		for ( int i = 0; i < DEV_CONFIGS.length; i ++ ) {
			if ( DEV_CONFIGS[i].equals(configName) ) {
				return true;
			}
		}
		return false;
	}
	
	
	private void tryGetStorageConfig() {
		if ( null != mFunDevice ) {
			
			showWaitDialog();
			
			for ( String configName : DEV_CONFIGS ) {
				
				// 删除老的配置信息
				mFunDevice.invalidConfig(configName);
				
				// 重新搜索新的配置信息
				FunSupport.getInstance().requestDeviceConfig(
						mFunDevice, configName);
			}
		}
	}
	
	private void trySetOverWrite(boolean overWrite) {
		GeneralGeneral generalInfo = (GeneralGeneral)mFunDevice.getConfig(GeneralGeneral.CONFIG_NAME);
		if ( null != generalInfo ) {
			if ( overWrite ) {
				//录像满时，循环录像
				generalInfo.setOverWrite(OverWriteType.OverWrite);
			} else {
				//录像满时，停止录像
				generalInfo.setOverWrite(OverWriteType.StopRecord);
			}
			
			showWaitDialog();
			
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, generalInfo);
		}
	}
	
	private void refreshStorageConfig() {
		StorageInfo storageInfo = (StorageInfo)mFunDevice.getConfig(StorageInfo.CONFIG_NAME);
		if ( null != storageInfo ) {
			int totalSpace = 0;
			int remainSpace = 0;
			List<StorageInfo.Partition> partitions = storageInfo.getPartitions();
			for ( StorageInfo.Partition partition : partitions ) {
				if ( partition.IsCurrent ) {
					// 获取当前分区的大小
					int partTotalSpace = MyUtils.getIntFromHex(partition.TotalSpace);
					int partRemainSpace = MyUtils.getIntFromHex(partition.RemainSpace);
					
					if ( partition.DirverType == SDK_FileSystemDriverTypes.SDK_DRIVER_SNAPSHOT ) {
						// 快照驱动器
						mTextStorageSnapshot.setText(FileUtils.FormetFileSize(partTotalSpace, 2));
					} else if ( partition.DirverType == SDK_FileSystemDriverTypes.SDK_DRIVER_READ_WRITE) {
						// 关键录像驱动器
						mTextStorageRecord.setText(FileUtils.FormetFileSize(partTotalSpace, 2));
					}
					
					// 累加总大小
					totalSpace += partTotalSpace;
					remainSpace += partRemainSpace;
				}
			}
			
			mTextStorageCapacity.setText(FileUtils.FormetFileSize(totalSpace, 2));
			mTextStorageRemain.setText(FileUtils.FormetFileSize(remainSpace, 2));
		}
		
		GeneralGeneral generalInfo = (GeneralGeneral)mFunDevice.getConfig(GeneralGeneral.CONFIG_NAME);
		if ( null != generalInfo ) {
			if( generalInfo.getOverWrite() == OverWriteType.OverWrite ) {
				mRbRecordCycle.setChecked(true);
			}
			else{
				mRbRecordStop.setChecked(true);
			}
		}
	}
	
	/**
	 * 判断是否所有需要的配置都获取到了
	 * @return
	 */
	private boolean isAllConfigGetted() {
		for ( String configName : DEV_CONFIGS ) {
			if ( null == mFunDevice.getConfig(configName) ) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 请求格式化指定的分区
	 * @param iPartition
	 * @return
	 */
	private boolean requestFormatPartition(int iPartition) {
		StorageInfo storageInfo = (StorageInfo)mFunDevice.getConfig(StorageInfo.CONFIG_NAME);
		if ( null != storageInfo && iPartition < storageInfo.PartNumber ) {
			if ( null == mOPStorageManager ) {
				mOPStorageManager = new OPStorageManager();
				mOPStorageManager.setAction("Clear");
				mOPStorageManager.setSerialNo(0);
				mOPStorageManager.setType("Data");
			}
			
			mOPStorageManager.setPartNo(iPartition);
			
			return FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, mOPStorageManager);
		}
		
		return false;
	}

	private void showFormatPartitionDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.device_setup_storage_format_tip) 
        .setIcon(android.R.drawable.ic_dialog_info) 
        .setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() { 
     
            @Override 
            public void onClick(DialogInterface dialog, int which) {
            	if ( requestFormatPartition(0) ) {
            		showWaitDialog();
            	}
            }
        }) 
        .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() { 
     
            @Override 
            public void onClick(DialogInterface dialog, int which) {
            } 
        }).show(); 
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
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
				&& isCurrentUsefulConfig(configName) ) {
			if ( isAllConfigGetted() ) {
				hideWaitDialog();
			}
			
			refreshStorageConfig();
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
		if ( null != mFunDevice 
				&& funDevice.getId() == mFunDevice.getId() ) {
			
			if ( OPStorageManager.CONFIG_NAME.equals(configName) 
					&& null != mOPStorageManager ) {
				// 请求格式化下一个分区
				if ( !requestFormatPartition(mOPStorageManager.getPartNo() + 1) ) {
					
					// 所有分区格式化完成之后,重新获取设备磁盘信息
					tryGetStorageConfig();
				}
			} else if ( GeneralGeneral.CONFIG_NAME.equals(configName) ) {
				// 设置录像满时，选择停止录像或循环录像成功
				hideWaitDialog();
				refreshStorageConfig();
			}
		}
	}
	
	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
		hideWaitDialog();
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
