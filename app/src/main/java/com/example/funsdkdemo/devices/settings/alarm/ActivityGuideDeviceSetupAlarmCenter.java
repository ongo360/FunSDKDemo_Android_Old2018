package com.example.funsdkdemo.devices.settings.alarm;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.NetWorkAlarmServer;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.utils.MyAdapter;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.util.ArrayList;
import java.util.List;

public class ActivityGuideDeviceSetupAlarmCenter extends ActivityDemo implements
		OnClickListener, OnFunDeviceOptListener, OnItemSelectedListener {

	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	// 协议类型
	private Spinner mSpAgreementType=null;
	// 启用
	private CheckBox mCbDeviceUse=null;
	// 警报上传
	private CheckBox mCbDeviceWarnUp=null;
	// 日志上传
	private CheckBox mCbDeviceLogUp=null;
	// 服务器地址
	private EditText mEditAddress = null;
	// 端口
	private EditText mEditPort=null;
	
	private FunDevice mFunDevice = null;
	
	private ImageButton mBtnSave = null;
	
	private ArrayList<String> mData = null;
	
	private BaseAdapter myAdadpter = null;
	
	/**
	 * 本界面需要获取到的设备配置信息
	 */
	private final String[] DEV_CONFIGS = {

			NetWorkAlarmServer.CONFIG_NAME,
	};
	
	// 设置配置信息的时候,由于有多个,通过下面的列表来判断是否所有的配置都设置完成了
	private List<String> mSettingConfigs = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_setup_alarm_center);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_setup_alarm_center);
		
		mEditAddress = (EditText)findViewById(R.id.et_address);
		mEditAddress.setOnClickListener(this);
		mEditPort = (EditText)findViewById(R.id.et_port);
		mEditPort.setOnClickListener(this);
		mCbDeviceLogUp=(CheckBox)findViewById(R.id.cb_device_log_up);
		mCbDeviceLogUp.setOnClickListener(this);
		mCbDeviceWarnUp=(CheckBox)findViewById(R.id.cb_device_warn_up);
		mCbDeviceWarnUp.setOnClickListener(this);
		mCbDeviceUse=(CheckBox)findViewById(R.id.cb_device_use);
		mCbDeviceUse.setOnClickListener(this);
		mSpAgreementType=(Spinner)findViewById(R.id.sp_agreement_type);
		mBtnSave = (ImageButton)setNavagateRightButton(R.layout.imagebutton_save);
		mBtnSave.setOnClickListener(this);
		
		mData = new ArrayList<String>();
		
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
		tryGetAlarmCentreConfig();
		
	}
	
	private void tryGetAlarmCentreConfig() {
		if (null != mFunDevice) {

			showWaitDialog();

			for (String configName : DEV_CONFIGS) {
				// 删除老的配置信息
				mFunDevice.invalidConfig(configName);
				// 重新搜索新的配置信息
				FunSupport.getInstance().requestDeviceConfig(mFunDevice,
						configName);
			}
		}
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
		case R.id.cb_device_log_up:// 日志上报
			{
				
			}
			break;
		case R.id.cb_device_warn_up:// 警报上报
			{
				
			}
		break;
		case R.id.cb_device_use:// 设置是否启用
			{
				tryDeviceUseSet();
			}
		break;
		case R.id.et_address:// 设置服务器地址
			{
			
			}
		break;
		case R.id.et_port:// 设置端口号
			{
				
			}
		break;
		case R.id.btnSave:	// 保存
			{
				trySaveAlarmConfig();
			}
		break;
		}
		
	}

	private void trySaveAlarmConfig() {
		NetWorkAlarmServer alarmServerInfo = (NetWorkAlarmServer)mFunDevice.getConfig(NetWorkAlarmServer.CONFIG_NAME);
		if(null!=alarmServerInfo){
			
			String address=null;
			address=mEditAddress.getText().toString();
			alarmServerInfo.setName(address);
			
			int i=-1;
			i=Integer.parseInt(mEditPort.getText().toString());
			alarmServerInfo.setPort(i);
			
			if(false==mCbDeviceWarnUp.isChecked()){
				alarmServerInfo.setAlarm(false);
			}else{
				alarmServerInfo.setAlarm(true);
			}
			
			if(false==mCbDeviceLogUp.isChecked()){
				alarmServerInfo.setLog(false);
			}else{
				alarmServerInfo.setLog(true);
			}
			
			showWaitDialog();
			
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, alarmServerInfo);
		}
		
	}

	private void tryDeviceUseSet() {
		NetWorkAlarmServer alarmServerInfo = (NetWorkAlarmServer)mFunDevice.getConfig(NetWorkAlarmServer.CONFIG_NAME);
		if(null!=alarmServerInfo){
			if(true==alarmServerInfo.getEnable()){
				alarmServerInfo.setEnable(false);
			}else{
				alarmServerInfo.setEnable(true);
			}
			showWaitDialog();
			
			FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, alarmServerInfo);
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
	
	private boolean isCurrentUsefulConfig(String configName) {
		for ( int i = 0; i < DEV_CONFIGS.length; i ++ ) {
			if ( DEV_CONFIGS[i].equals(configName) ) {
				return true;
			}
		}
		return false;
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
			
			refreshAlarmCentreConfig();
			tryGetProtocol();
		}
	}
	
	private void refreshAlarmCentreConfig() {
		NetWorkAlarmServer alarmCentreInfo = (NetWorkAlarmServer)mFunDevice.getConfig(NetWorkAlarmServer.CONFIG_NAME);
		if(null!=alarmCentreInfo){

			mCbDeviceLogUp.setChecked(alarmCentreInfo.getLog());
			mCbDeviceWarnUp.setChecked(alarmCentreInfo.getAlarm());
			mCbDeviceUse.setChecked(alarmCentreInfo.getEnable());
			mEditAddress.setText(alarmCentreInfo.getName().toString());
			String str = String.valueOf(alarmCentreInfo.getPort());
			mEditPort.setText(str);	

		}
		if(false==alarmCentreInfo.getEnable()){
			mEditAddress.setEnabled(false);
			mEditPort.setEnabled(false);
			mCbDeviceLogUp.setClickable(false);	
			mCbDeviceWarnUp.setClickable(false);
		}else{
			mEditAddress.setEnabled(true);
			mEditPort.setEnabled(true);
			mCbDeviceLogUp.setClickable(true);	
			mCbDeviceWarnUp.setClickable(true);
		}
	}
	
	private void tryGetProtocol(){
		NetWorkAlarmServer alarmCentreInfo = (NetWorkAlarmServer)mFunDevice.getConfig(NetWorkAlarmServer.CONFIG_NAME);
		if(null!=alarmCentreInfo){ 
		mData.add(alarmCentreInfo.getProtocol());
		myAdadpter = new MyAdapter<String>(mData,R.layout.alarm_center_protocol) {
	     @Override
	     public void bindView(ViewHolder holder, String obj) {
	            holder.setText(R.id.txt_name, obj);
	            }
	        };
	        mSpAgreementType.setAdapter(myAdadpter);
	        mSpAgreementType.setOnItemSelectedListener(this);
		}
	}
	
	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		if ( null != mFunDevice 
				&& funDevice.getId() == mFunDevice.getId() ){
			if ( NetWorkAlarmServer.CONFIG_NAME.equals(configName) ) {
				
				hideWaitDialog();
				refreshAlarmCentreConfig();

			}
		}	
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
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
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
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
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}


}
