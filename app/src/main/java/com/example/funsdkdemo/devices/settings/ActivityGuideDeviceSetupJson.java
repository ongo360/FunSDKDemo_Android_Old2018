package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.basic.G;
import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.EUIMSG;
import com.lib.FunSDK;
import com.lib.IFunSDKResult;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;

/**
 * @author Administrator
 */
public class ActivityGuideDeviceSetupJson extends ActivityDemo implements OnCheckedChangeListener, OnClickListener, IFunSDKResult {
	
	private EditText mEditTextJson;
	private EditText mEditTextConfigname;
	private EditText mEditTextChannel;
	private EditText mEditTextID;
	private Button mButtonGetConfig;
	private Button mButtonSetConfig;
	private RadioGroup mConfigOptMode;
	private TextView mTextTitle = null;
	private ImageButton mBtnBack = null;
	
	private boolean byJson = true;
	private int mHandler;
	private static final String TAG = "ActivityGuideDeviceSetupJson";
	
	FunDevice mFunDevice = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_setup_json);
		
		int devID = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		mFunDevice = FunSupport.getInstance().findDeviceById(devID);
		if (null == mFunDevice) {
			finish();
			return;
		}
		

		mTextTitle = (TextView) findViewById(R.id.textViewInTopLayout);
		mTextTitle.setText(R.string.device_setup_jsonanddevcmd);
		mBtnBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
		mBtnBack.setOnClickListener(this);
		
		mEditTextJson = (EditText) findViewById(R.id.edit_json);
		mEditTextConfigname = (EditText) findViewById(R.id.edit_sn);
		mEditTextChannel = (EditText) findViewById(R.id.edit_channel);
		mEditTextChannel.setText("-1");
		mEditTextID = (EditText) findViewById(R.id.edit_optid);
		mEditTextID.setText("-1");
		
		mConfigOptMode = (RadioGroup) findViewById(R.id.radioConfigMode);
		mConfigOptMode.check(R.id.radioBtnbyjson);
		mConfigOptMode.setOnCheckedChangeListener(this);
		
		mButtonGetConfig = (Button) findViewById(R.id.btn_getConfig);
		mButtonSetConfig = (Button) findViewById(R.id.btn_setConfig);
		mButtonGetConfig.setOnClickListener(this);
		mButtonSetConfig.setOnClickListener(this);
		
		mHandler = FunSDK.RegUser(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FunSDK.UnRegUser(mHandler);
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radioBtnbyjson:
			
			byJson = true;
			findViewById(R.id.linearlayout_seq).setVisibility(View.GONE);
			
			break;
		case R.id.radioBtnbydevcmd:
			
			byJson = false;
			findViewById(R.id.linearlayout_seq).setVisibility(View.VISIBLE);
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.backBtnInTopLayout:
			
			finish();
			
			break;
		case R.id.btn_getConfig:
			
				getConfig();
				
			break;
			
		case R.id.btn_setConfig:
			
				setConfig();
				
			break;

		default:
			break;
		}
		
	}
	
	private void getConfig(){
		
		String StrJson = mEditTextJson.getText().toString().trim();
		String StrConfigName = mEditTextConfigname.getText().toString().trim();
		String StrChannelNo = mEditTextChannel.getText().toString().trim();
		String StrDevCmdID = mEditTextID.getText().toString().trim();
		
		int mChannel = Integer.parseInt(StrChannelNo);
		int mDevCmdID = Integer.parseInt(StrDevCmdID);
		if (byJson) {
			if (StrConfigName == null || StrChannelNo == null) {
				showToast(R.string.device_setup_nullconfigname);
				return;
			}
			showWaitDialog();
			FunSDK.DevGetConfigByJson(mHandler, mFunDevice.getDevSn(), StrConfigName, 4096, mChannel, 10000, mFunDevice.getId());
		}else {
			if (StrConfigName == null || StrDevCmdID == null || StrChannelNo == null) {
				showToast(R.string.device_setup_nullconfigname);
				return;
			}
			showWaitDialog();
			FunSDK.DevCmdGeneral(mHandler, mFunDevice.devSn, mDevCmdID, StrConfigName, 1024, 10000, StrJson.equals("") ? null : StrJson.getBytes(), -1, mFunDevice.getId());
		}
	}
	
	private void setConfig(){
		
		String StrJson = mEditTextJson.getText().toString().trim();
		String StrConfigName = mEditTextConfigname.getText().toString().trim();
		String StrChannelNo = mEditTextChannel.getText().toString().trim();
		String StrDevCmdID = mEditTextID.getText().toString().trim();
		
		int mChannel = Integer.parseInt(StrChannelNo);
		int mDevCmdID = Integer.parseInt(StrDevCmdID);
		if (byJson) {
			if (StrConfigName == null || StrJson == null) {
				showToast(R.string.device_setup_nullconfigname);
				return;
			}
			showWaitDialog();
			FunSDK.DevSetConfigByJson(mHandler, mFunDevice.getDevSn(), StrConfigName, StrJson, mChannel, 60000, mFunDevice.getId());
		}
	}
	
	private void getconfigsuccess(String pData){
		hideWaitDialog();
		
		mEditTextJson.setText(FormatStr(pData));
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("ConfigData");
//		builder.setMessage(pData);
//		builder.setPositiveButton("OK", null);
//		builder.show();
	}
	
	private String FormatStr(String str){
		String fStr = null;
//		fStr = str.replace("{", "{\n");
//		fStr = fStr.replace("}", "}\n");
//		fStr = fStr.replace("[", "[\n");
//		fStr = fStr.replace("]", "]\n");
		fStr = str.replace(",", ",\n");
		return fStr;
	}
	
	private void getconfigfailed(int ErrCode){
		hideWaitDialog();
		showToast(FunError.getErrorStr(ErrCode));
	}
	
	private void setconfigsuccess(){
		hideWaitDialog();
		showToast(R.string.device_setup_setconfigsuccess);
	}
	
	private void setconfigfailed(int errCode){
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
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
		
		switch (msg.what) {
		
		case EUIMSG.DEV_CMD_EN:
		case EUIMSG.DEV_GET_JSON:
			FunLog.i(TAG, "EUIMSG.DEV_GET_JSON");
			FunLog.i(TAG, "JSON__" + G.ToString(msgContent.pData));

			if (msgContent.seq == mFunDevice.getId()) {
				
				if (msg.arg1 < 0) {
					getconfigfailed(msg.arg1);
				}else if (msgContent.pData != null) {
					getconfigsuccess(G.ToString(msgContent.pData));
				}
			}
			break;
			
		case EUIMSG.DEV_SET_JSON:
			
			FunLog.i(TAG, "EUIMSG>DEV_SET_JSON");
			
			if (msgContent.seq == mFunDevice.getId()) {
				
				if (msg.arg1 < 0) {
					setconfigfailed(msg.arg1);
				}else {
					setconfigsuccess();
				}
			}
			break;

		default:
			break;
		}
		
		return 0;
	}
	
	

}
