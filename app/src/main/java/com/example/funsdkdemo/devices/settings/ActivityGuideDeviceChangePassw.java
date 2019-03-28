package com.example.funsdkdemo.devices.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.funsdkdemo.ActivityDemo;
import com.example.funsdkdemo.R;
import com.lib.FunSDK;
import com.lib.funsdk.support.FunDevicePassword;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.ModifyPassword;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

/**
 * Demo: 修改设备密码
 */
public class ActivityGuideDeviceChangePassw extends ActivityDemo implements View.OnClickListener, OnFunDeviceOptListener {

    private TextView mTextTitle = null;
    private ImageButton mBtnBack = null;

    private EditText mEditOldPassw = null;
    private EditText mEditNewPassw = null;
    private EditText mEditNewPasswConfirm = null;
    private Button mBtnChangePassw = null;

    private FunDevice mFunDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_change_password);

        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
		FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
		if ( null == funDevice ) {
			finish();
			return;
		}
		
		mFunDevice = funDevice;
		
        mTextTitle = (TextView)findViewById(R.id.textViewInTopLayout);

        mBtnBack = (ImageButton)findViewById(R.id.backBtnInTopLayout);
        mBtnBack.setOnClickListener(this);

        mEditOldPassw = (EditText) findViewById(R.id.oldPasswd);
        mEditNewPassw = (EditText) findViewById(R.id.newPasswd);
        mEditNewPasswConfirm = (EditText) findViewById(R.id.newPasswdConfirm);
        mBtnChangePassw = (Button) findViewById(R.id.userChangePasswBtn);

        mTextTitle.setText(R.string.device_setup_change_password);
        mBtnChangePassw.setOnClickListener(this);
        
        // 注册设备操作类的监听
        FunSupport.getInstance().registerOnFunDeviceOptListener(this);
    }

    @Override
    protected void onDestroy() {

        // 注销监听

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                finish();
                break;
            case R.id.userChangePasswBtn:
                tryToChangePassw();
                break;
            default:
                break;
        }
    }



    private void tryToChangePassw() {

        String oldPassw = mEditOldPassw.getText().toString();
        String newPassw = mEditNewPassw.getText().toString();
        String newPasswConfirm = mEditNewPasswConfirm.getText().toString();

        if (!newPassw.equals(newPasswConfirm)) {
            showToast(R.string.user_change_password_error_passwnotequal);
            return;
        }
        
        showWaitDialog();

        // 修改密码,设置ModifyPassword参数
        // 注意,如果是直接调用FunSDK.DevSetConfigByJson()接口,需要对密码做MD5加密,参考ModifyPassword.java的处理
        ModifyPassword modifyPasswd = new ModifyPassword();
        modifyPasswd.PassWord = oldPassw;
        modifyPasswd.NewPassWord = newPasswConfirm;
        
        FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, modifyPasswd);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
		if ( ModifyPassword.CONFIG_NAME.equals(configName) ) {
			// 修改密码成功,保存新密码,下次登录使用
			if ( null != mFunDevice && null != mEditNewPassw ) {
				FunDevicePassword.getInstance().saveDevicePassword(
						mFunDevice.getDevSn(), 
						mEditNewPassw.getText().toString());
			}
			// 库函数方式本地保存密码
			if (FunSupport.getInstance().getSaveNativePassword()) {
				FunSDK.DevSetLocalPwd(mFunDevice.getDevSn(), "admin", mEditNewPassw.getText().toString());
				// 如果设置了使用本地保存密码，则将密码保存到本地文件
			}
			// 隐藏等待框
			hideWaitDialog();
			showToast(R.string.user_forget_pwd_reset_passw_success);
		}
	}

	@Override
	public void onDeviceSetConfigFailed(FunDevice funDevice, String configName,
			Integer errCode) {
		hideWaitDialog();
		showToast(FunError.getErrorStr(errCode));
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
