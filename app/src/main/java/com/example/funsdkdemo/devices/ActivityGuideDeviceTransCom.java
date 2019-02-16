package com.example.funsdkdemo.devices;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;


/**
 * Demo:串口命令透传
 * 1. 注册监听
 * 2. 登入设备  - 设备操作都需要先登录设备
 * 3. 打开串口  - 在设备登录成功之后才能打开串口,收到onDeviceLoginSuccess()回调
 * 4. 测试串口命令  - 成功打开串口之后才能测试写串口命令
 * 5. 关闭串口
 * 6. 注销监听
 * 7. 登出设备并退出
 * 说明: 关于串口类型的选择，根据实际需求，具体的智能设备选择不同的串口类型，
 *     在FunSupport.java中,对应串口操作的几个函数：
 *     transportSerialOpen()/transportSerialClose()/transportSerialWrite()
 *     可选
 *     SDK_CommTypes.SDK_COMM_TYPES_RS485/SDK_CommTypes.SDK_COMM_TYPES_RS232
 * 
 *
 */
public class ActivityGuideDeviceTransCom extends ActivityDemo implements OnClickListener, OnFunDeviceOptListener, OnFunDeviceSerialListener {

	
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private Button mBtnSend = null;
	private EditText mEditInput = null;
	
	private ListView mListCommands = null;
	private ListAdapterDeviceComComands mAdapter = null;
	
	private Button mBtnTest1 = null;
	private Button mBtnTest2 = null;
	private Button mBtnTest3 = null;
	
	private FunDevice mFunDevice = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_device_trans_com);
		
		mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);
		
		mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mTextTitle.setText(R.string.device_opt_transcom);
		
		mBtnSend = (Button)findViewById(R.id.btnSend);
		mBtnSend.setOnClickListener(this);
		
		mEditInput = (EditText)findViewById(R.id.editInput);
		
		mListCommands = (ListView)findViewById(R.id.listCommands);
		mAdapter = new ListAdapterDeviceComComands(this);
		mListCommands.setAdapter(mAdapter);
		
		mBtnTest1 = (Button)findViewById(R.id.btnTest1);
		mBtnTest2 = (Button)findViewById(R.id.btnTest2);
		mBtnTest3 = (Button)findViewById(R.id.btnTest3);
		mBtnTest1.setOnClickListener(this);
		mBtnTest2.setOnClickListener(this);
		mBtnTest3.setOnClickListener(this);
		
		int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mFunDevice = funDevice;
		
		// 1. 注册监听
		// 注册设备操作类监听(设备登录时需要)
		FunSupport.getInstance().registerOnFunDeviceOptListener(this);
		// 注册串口操作监听(串口打开/写数据需要)
		FunSupport.getInstance().registerOnFunDeviceSerialListener(this);
		
		// 2. 登入设备
		loginDevice();
	}
	

	@Override
	protected void onDestroy() {
		
		// 6. 注销监听
		FunSupport.getInstance().removeOnFunDeviceSerialListener(this);
		
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
		case R.id.btnSend:
			{
				// 4. 测试串口命令  - 成功打开串口之后才能测试写串口命令
				sendComData();
			}
			break;
		case R.id.btnTest1:
			{
				// 4. 测试串口命令  - 成功打开串口之后才能测试写串口命令
				sendTestQS1();
			}
			break;
		case R.id.btnTest2:
			{
				// 4. 测试串口命令  - 成功打开串口之后才能测试写串口命令
				sendTestQS2();
			}
			break;
		case R.id.btnTest3:
			{
				// 4. 测试串口命令  - 成功打开串口之后才能测试写串口命令
				sendComTest3();
			}
			break;
		}
	}
	
	void loginDevice() {
		showWaitDialog();
		
		FunSupport.getInstance().requestDeviceLogin(mFunDevice);
	}
	
	private void sendComData() {
		String dataStr = mEditInput.getText().toString();
		
		if ( dataStr.length() > 0 ) {
			showWaitDialog();
			
			FunSupport.getInstance().transportSerialWrite(mFunDevice, dataStr);
			
			putCommand(true, dataStr);
		} else {
			showToast(R.string.device_opt_com_data_empty);
		}
	}
	
	private void sendComData(byte[] sendData) {
		showWaitDialog();
		
		FunSupport.getInstance().transportSerialWrite(mFunDevice, sendData);
		
		putCommand(true, sendData);
	}
	
	private void sendComTest3() {
		byte[] td = new byte[1];
		td[0] = 0x56;
		
		sendComData(td);
	}
	
	private void sendTestQS1() {
		
		byte[] cmd = {
				2, 0, 0, 15, 20, 0, 0, 0, 0, 0, 0, 54, -21, 16, 3, 0, 0, 0, 0, 0, 0, 0, 0, 16, 4, 2, -52, 51, 3
		};
		
		sendComData(cmd);
	}
	
	private void sendTestQS2() {
		
		byte[] cmd = {
				2, 0, 0, 15, 19, 0, 0, 0, 0, 0, 0, 99, -30, 16, 3, 0, 0, 0, 0, 0, 0, 0, 0, 16, 4, 2, 0, 0, 3
		};
		
		sendComData(cmd);
	}
	
	private void putCommand(boolean send, byte[] data) {
		String str = "";
		for ( int i = 0; i < data.length; i ++ ) {
			str += String.format("%02x ", data[i]&0xff);
		}
		
		putCommand(send, str);
	}
	
	private void putCommand(boolean send, String content) {
		FunLog.d("test", (send?"SEND: " : "RECV: ") + content);
		
		mAdapter.updateCommand(new ComCommand(send, content));
		mListCommands.post(new Runnable() {
			
			@Override
			public void run() {
				mListCommands.setSelection(mAdapter.getCount()-1);
			}
		});
	}

	@Override
	public void onDeviceLoginSuccess(FunDevice funDevice) {
		// 3. 打开串口  - 在设备登录成功之后才能打开串口
		hideWaitDialog();
		FunSupport.getInstance().transportSerialOpen(mFunDevice);
	}


	@Override
	public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
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
	public void onDeviceSetConfigSuccess(final FunDevice funDevice,
			final String configName) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDeviceSetConfigFailed(final FunDevice funDevice, 
			final String configName, final Integer errCode) {
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
	public void onDeviceSerialOpenSuccess(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDeviceSerialOpenFailed(FunDevice funDevice, Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceSerialWriteSuccess(FunDevice funDevice) {
		hideWaitDialog();
	}


	@Override
	public void onDeviceSerialWriteFailed(FunDevice funDevice, Integer errCode) {
		showToast(FunError.getErrorStr(errCode));
	}


	@Override
	public void onDeviceSerialTransmitData(FunDevice funDevice, byte[] pData) {
		putCommand(true, pData);
	}


	@Override
	public void onDeviceFileListGetFailed(FunDevice funDevice) {
		// TODO Auto-generated method stub
		
	}

}
